package gov.nasa.jpf.symbv;

import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.jdart.ConcolicExplorer;
import gov.nasa.jpf.jdart.constraints.Path;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResult {

    ConcolicExplorer ce;

    public ExecutionResult(ConcolicExplorer ce) {
        this.ce = ce;
    }

    public List<Path> getOkayPaths() {
        List<Path> paths = new ArrayList<>();

        ce.getCompletedAnalyses().forEach((methodName, completedAnalyses) ->
                completedAnalyses.forEach(completedAnalysis -> {
                    if (completedAnalysis.getConstraintsTree() != null) {
                        paths.addAll(completedAnalysis.getConstraintsTree().getCoveredPaths());
                    }
                }));

        return paths;
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
