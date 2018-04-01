package gov.nasa.jpf.symbv;

import gov.nasa.jpf.jdart.ConcolicExplorer;
import gov.nasa.jpf.jdart.constraints.Path;

import java.util.ArrayList;
import java.util.Collection;

public class ExecutionResult {

    ConcolicExplorer ce;

    public ExecutionResult(ConcolicExplorer ce) {
        this.ce = ce;
    }

    public Collection<Path> getOkayPaths() {
        Collection<Path> paths = new ArrayList<>();

        ce.getCompletedAnalyses().forEach((methodName, completedAnalyses) ->
                completedAnalyses.forEach(completedAnalysis -> {
                    if (completedAnalysis.getConstraintsTree() != null) {
                        paths.addAll(completedAnalysis.getConstraintsTree().getCoveredPaths());
                    }
                }));

        return paths;
    }
}
