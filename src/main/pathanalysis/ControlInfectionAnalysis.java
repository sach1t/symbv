package pathanalysis;

import soot.Unit;
import soot.toolkits.graph.*;
import soot.toolkits.graph.pdg.MHGDominatorTree;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.List;

// should do control + data dependence
public class ControlInfectionAnalysis extends ForwardFlowAnalysis<Block, FlowSet<Block>> {
    DominatorsFinder postdoms;
    DominanceFrontier domfront;
    DominatorTree domTree;
    FlowSet<Block> day0Affected = new ArraySparseSet<>();

    public ControlInfectionAnalysis(DirectedGraph<Block> graph) {
        super(graph);
        postdoms = new MHGPostDominatorsFinder(graph);
        domTree = new MHGDominatorTree<>(postdoms);
        domfront = new CytronDominanceFrontier(domTree);
        for (Block b : graph) {
            for (Unit u: b) {
                if (u.hasTag("symbvTag")) {
                    day0Affected.add(b);
                    break;
                }
            }
        }
        doAnalysis();
    }

    private boolean isDependent(Block s, Block t) {
        List<DominatorNode> domNodes = domfront.getDominanceFrontierOf(domTree.getDode(s));
        DominatorNode tDomNode = domTree.getDode(t);
        return domNodes.contains(tDomNode);
    }

    private boolean isDependentOnAny(Block s, Iterable<Block> ts) {
        for (Block t: ts) {
            if (isDependent(s, t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void flowThrough(FlowSet<Block> in, Block b, FlowSet<Block> out) {
        if (isDependentOnAny(b, in)) {
            in.add(b, out);
            for (Unit u: b) {
                u.addTag(new ChangeTag("ControlDep"));
            }
        } else {
            out.union(in);
        }
    }

    @Override
    protected FlowSet<Block> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected void merge(FlowSet<Block> in1, FlowSet<Block> in2, FlowSet<Block> out) {
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<Block> source, FlowSet<Block> dest) {
        source.copy(dest);
    }

    @Override
    protected FlowSet<Block> entryInitialFlow() {
        return day0Affected;
    }
}
