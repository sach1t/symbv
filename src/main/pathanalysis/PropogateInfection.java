package pathanalysis;

import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

// should do control + data dependence
public class PropogateInfection extends ForwardFlowAnalysis<Unit, FlowSet<Integer>> {
    FlowSet<Integer> emptySet = new ArraySparseSet<>();

    public PropogateInfection(DirectedGraph graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(FlowSet<Integer> in, Unit unit, FlowSet<Integer> out) {
        if (unit.hasTag("symbvTag")) {
            in.add(1, out);
        } else {
            out.union(in);
        }
    }

    @Override
    protected FlowSet<Integer> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected void merge(FlowSet<Integer> in1, FlowSet<Integer> in2, FlowSet<Integer> out) {
        in1.union(in2, out);
    }


    @Override
    protected void copy(FlowSet<Integer> source, FlowSet<Integer> dest) {
        source.copy(dest);
    }
}
