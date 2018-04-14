package pathanalysis;

import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;


// TODO: forward flow analysis to taint affected statements/branches for removed statements
// https://github.com/brucespang/taint-analysis/blob/master/src/Taint.java

public class GoodPathsFlow extends BackwardFlowAnalysis<Unit, FlowSet<Integer>> {
    FlowSet<Integer> emptySet = new ArraySparseSet<>();

    public GoodPathsFlow(DirectedGraph graph) {
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
        return emptySet.clone();
    }

    @Override
    protected void merge(FlowSet<Integer> in1, FlowSet<Integer> in2, FlowSet<Integer> out) {
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<Integer> source, FlowSet<Integer> target) {
        source.copy(target);
    }
}