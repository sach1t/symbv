package gov.nasa.jpf.symbv;

public class TraceExecutor {
    Executor exA, exB;

    public TraceExecutor(Executor exA, Executor exB) {
        this.exA = exA;
        this.exB = exB;
    }

    public ExecutionResult run() {

        exB.getSconf().setMaxAltDepth(0);
        ExecutionResult erB = exB.run();

        exA.getSconf().setMaxAltDepth(0);
        ExecutionResult erA = exA.run();

        erA.ce.getCompletedAnalyses().forEach((method,completedAnalyses) -> {
            completedAnalyses.forEach(ca -> {
                ca.getConstraintsTree().getCoveredPaths().forEach(path -> {
                    System.out.println(path.getPathCondition());
                });
            });
        });

        erB.ce.getCompletedAnalyses().forEach((method,completedAnalyses) -> {
            completedAnalyses.forEach(ca -> {
                ca.getConstraintsTree().getCoveredPaths().forEach(path -> {
                    System.out.println(path.getPathCondition());
                });
            });
        });

        // PROBLEM: injecting values onto VM stack for each run.

        // (((('pedalCmd' + 1) + 1) == 2) && (('bSwitch' == 0) && ('pedalPos' <= 0)))
        // (((('pedalCmd' + 1) + 1) == 2) && (('bSwitch' == 0) && ('pedalPos' == 0)))

        // tree requires backwards transversal
        // (('pedalPos' < 160) && (('pedalPos' < 40) && (('pedalPos' < 20) && (('pedalPos' < 10) && ('pedalPos' <= 0)))))
        //        if (pedalPos <= 0) {
        //            if (pedalPos < 10) {
        //                if (pedalPos < 20) {
        //                    if (pedalPos < 40) {
        //                        if (pedalPos < 160) {
        //                            bSwitch = 2;
        //                        }
        //                    }
        //                }
        //            }
        //        }

        return null;
    }
}
