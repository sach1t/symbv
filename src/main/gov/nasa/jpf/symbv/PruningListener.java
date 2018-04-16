package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jdart.ConcolicMethodExplorer;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import pathanalysis.CFGWrapper;
import pathanalysis.PathData;


public class PruningListener extends gov.nasa.jpf.ListenerAdapter {
    CFGWrapper cfg;
    private JPFLogger logger = JPF.getLogger("symbv");
    String patchedMethod;

    public PruningListener(Config conf) {
        cfg = PathData.getInstance().getAnalysis(
                conf.getProperty(SymbvConfig.testOriginalTargetProperty));
        patchedMethod = conf.getProperty(SymbvConfig.testPatchedMethodProperty);
    }

    @Override
    public void instructionExecuted(VM vm, ThreadInfo current, Instruction nextIns, Instruction execIns) {
        String methodName = current.getPC().getMethodInfo().getName();

        if (execIns instanceof IfInstruction) {
            if (methodName.equals(patchedMethod)) {
                ConcolicMethodExplorer cm = ConcolicMethodExplorer.getCurrentAnalysis(current);
                int nextLineNumber = nextIns.getLineNumber();
                if (cfg != null && !cfg.promisingPath(nextLineNumber)) {
                    logger.info("PRUNED Line " + nextLineNumber);
                    cm.getInternalConstraintsTree().failCurrentTarget();
                    current.breakTransition(true);
                }
            }
        }
    }
}
