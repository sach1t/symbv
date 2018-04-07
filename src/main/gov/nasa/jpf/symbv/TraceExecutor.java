package gov.nasa.jpf.symbv;

/*
Issues encountered with implementation:
    - injecting values onto heap for each run
    - getting path results
*/
public class TraceExecutor {
    Executor exA, exB;

    public TraceExecutor(Executor exA, Executor exB) {
        this.exA = exA;
        this.exB = exB;
    }

    public ExecutionResult run() {
        exA.getSconf().setMaxAltDepth(0);
        exB.getSconf().setMaxAltDepth(0);

        ExecutionResult erB = exB.run().get(0);
        ExecutionResult erA = exA.run().get(0);

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

        // Example:
        // (((('pedalCmd' + 1) + 1) == 2) && (('bSwitch' == 0) && ('pedalPos' <= 0)))
        // (((('pedalCmd' + 1) + 1) == 2) && (('bSwitch' == 0) && ('pedalPos' == 0)))

        return null;
    }
}
