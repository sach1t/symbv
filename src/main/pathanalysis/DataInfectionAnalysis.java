package pathanalysis;

import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class DataInfectionAnalysis extends ForwardFlowAnalysis<Unit, FlowSet<Value>> {
    public DataInfectionAnalysis(DirectedGraph<Unit> graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(FlowSet<Value> in, Unit unit, FlowSet<Value> out) {
        FlowSet<Value> updatedValues = new ArraySparseSet<>();
        if (unit.hasTag("symbvTag")) {
            if (unit instanceof AssignStmt) {
                for (ValueBox vb: unit.getDefBoxes()) {
                    updatedValues.add(vb.getValue());
                }
            }
        } else {
            for (ValueBox vb: unit.getUseBoxes()) {
                Value v = vb.getValue();
                if (in.contains(v)) {
                    unit.addTag(new ChangeTag("dataDep"));
                }
            }
        }
        in.union(updatedValues, out);
    }

    @Override
    protected FlowSet<Value> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected void merge(FlowSet<Value> in1, FlowSet<Value> in2, FlowSet<Value> out) {
        in1.union(in2, out);
    }


    @Override
    protected void copy(FlowSet<Value> source, FlowSet<Value> dest) {
        source.copy(dest);
    }
}
