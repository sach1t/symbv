package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.jdart.ConcolicMethodExplorer;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import pathanalysis.PathAnalysis;

import java.util.Set;

// To get data in here, we will just do analysis in here
public class CustomListener extends gov.nasa.jpf.ListenerAdapter {
    Set<Integer> prune;

    public CustomListener(Config conf) {
        PathAnalysis pa = new PathAnalysis();
        prune = pa.run(conf.getProperty("originalTarget"),
                conf.getProperty("originalMethod"),
                conf.getProperty("patchedMethod"));
    }

    @Override
    public void instructionExecuted(VM vm, ThreadInfo current, Instruction nextIns, Instruction execIns) {
        Integer lineNum = current.getPC().getLineNumber();
        String methodName = current.getPC().getMethodInfo().getName();

        if (execIns instanceof IfInstruction) {
            if (methodName.equals("update")) {
                System.out.println("INSTRUCTION EXECUTED: " + methodName + ":" + lineNum);
                ConcolicMethodExplorer cm = ConcolicMethodExplorer.getCurrentAnalysis(current);
                int nextLineNumber = nextIns.getLineNumber();
                if (prune.contains(nextLineNumber)) {
                    System.out.println("PRUNED PRUNED PRUNED");
                    cm.getInternalConstraintsTree().failCurrentTarget();
                    current.breakTransition(true);
                }
            }
        }
    }
}
