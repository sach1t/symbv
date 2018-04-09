package pathanalysis;

import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;


// TODO: forward flow analysis to taint affected statements/branches for removed statements
// https://github.com/brucespang/taint-analysis/blob/master/src/Taint.java

public class GoodPathsFlow extends BackwardFlowAnalysis<Unit, FlowSet<String>> {
    FlowSet<String> emptySet = new ArraySparseSet<>();

    public GoodPathsFlow(DirectedGraph graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(FlowSet<String> in, Unit unit, FlowSet<String> out) {
        if (unit.hasTag("symbvTag")) {
            in.add(new String(unit.getTag("symbvTag").getValue()), out);
        } else {
            out.union(in);
        }
    }

    @Override
    protected FlowSet<String> newInitialFlow() {
        return emptySet.clone();
    }

    @Override
    protected void merge(FlowSet<String> in1, FlowSet<String> in2, FlowSet<String> out) {
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<String> source, FlowSet<String> target) {
        source.copy(target);
    }
}