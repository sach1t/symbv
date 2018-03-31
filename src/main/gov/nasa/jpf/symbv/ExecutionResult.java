package gov.nasa.jpf.symbv;

import gov.nasa.jpf.jdart.ConcolicExplorer;
import gov.nasa.jpf.jdart.constraints.ConstraintsTree;
import gov.nasa.jpf.jdart.constraints.Path;

import java.util.Collection;
import java.util.function.Function;

public class ExecutionResult {

    ConcolicExplorer ce;

    public ExecutionResult(ConcolicExplorer ce) {
        this.ce = ce;
    }

    private void printPaths(Function<ConstraintsTree, Collection<Path>> pathType) {
        ce.getCompletedAnalyses().forEach((methodName, completedAnalyses) ->
                completedAnalyses.forEach(completedAnalysis -> {
                    if (completedAnalysis.getConstraintsTree() != null) {
                        pathType.apply(completedAnalysis.getConstraintsTree()).forEach(path ->
                                System.out.println(path.getValuation()));
                    }
                }));
    }

    public void printOkayPaths() {
        printPaths(ConstraintsTree::getCoveredPaths);
    }

    public void printErrorPaths() {
        printPaths(ConstraintsTree::getErrorPaths);
    }

    public void printDontKnowPaths() {
        printPaths(ConstraintsTree::getDontKnowPaths);
    }
}
