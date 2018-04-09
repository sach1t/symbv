package gov.nasa.jpf.symbv;

import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.jdart.ConcolicExplorer;
import gov.nasa.jpf.jdart.constraints.ConstraintsTree;
import gov.nasa.jpf.jdart.constraints.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        StringBuilder out = new StringBuilder();
        List<Path> paths = this.getOkayPaths();
        paths.forEach(path -> {
            Valuation valuation = path.getValuation();

            List<String> assignments = new ArrayList<>();
            valuation.getVariables().forEach(var -> {
                assignments.add(var.getName() + " = " + valuation.getValue(var));
            });

            out.append(String.join(", ", assignments)).append("\n");
        });
        return out.toString();
    }
}
