package gov.nasa.jpf.symbv;

import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.jdart.ConcolicExplorer;
import gov.nasa.jpf.jdart.constraints.ConstraintsTree;
import gov.nasa.jpf.jdart.constraints.Path;

import java.util.*;
import java.util.function.Function;

public class ExecutionResult {

    ConcolicExplorer ce;

    public ExecutionResult(ConcolicExplorer ce) {
        this.ce = ce;
    }

    public List<Path> getPaths(Function<ConstraintsTree, Collection<Path>> pathType) {
        List<Path> paths = new ArrayList<>();

        ce.getCompletedAnalyses().forEach((methodName, completedAnalyses) ->
                completedAnalyses.forEach(completedAnalysis -> {
                    if (completedAnalysis.getConstraintsTree() != null) {
                        paths.addAll(pathType.apply(completedAnalysis.getConstraintsTree()));
                    }
                }));

        return paths;
    }

    public List<Path> getOkayPaths() {
        return getPaths(ConstraintsTree::getCoveredPaths);
    }

    public List<Path> getErrorPaths() {
        return getPaths(ConstraintsTree::getErrorPaths);
    }

    public String toString () {
        return pathsToString(getOkayPaths());
    }

    public static String pathsToString(List<Path> paths) {
        Set<String> out = new HashSet<>();
        paths.forEach(path -> {
            Valuation valuation = path.getValuation();
            List<String> assignments = new ArrayList<>();
            valuation.getVariables().forEach(var -> {
                assignments.add(var.getName() + " = " + valuation.getValue(var));
            });
            out.add(String.join(", ", assignments));
        });
        return String.join("\n", out);
    }
}
