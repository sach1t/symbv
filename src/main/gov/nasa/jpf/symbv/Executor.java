package gov.nasa.jpf.symbv;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.jdart.JDart;
import gov.nasa.jpf.jdart.constraints.Path;
import gov.nasa.jpf.util.JPFLogger;
import pathanalysis.PathAnalysis;
import pathanalysis.PathData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Executor {
    SymbvConfig sconf;
    private JPFLogger logger = JPF.getLogger("symbv");
    private int maxIterations;

    public Executor(SymbvConfig sconf) {
        this.sconf = sconf;
        this.maxIterations = sconf.getIterations();
        runControlFlowAnalysis();
    }

    public void runControlFlowAnalysis() {
        String originalTarget = sconf.getJPFConfig().getProperty("originalTarget");
        String originalMethod = sconf.getJPFConfig().getProperty("originalMethod");
        String patchedMethod = sconf.getJPFConfig().getProperty("patchedMethod");
        HashSet<Integer> analysisData = PathAnalysis.run(originalTarget, originalMethod, patchedMethod);
        PathData.getInstance().addAnalysis(originalTarget, analysisData);
    }

    public List<ExecutionResult> run() {
        List<ExecutionResult> results = new ArrayList<>();
        for (int i = 0; i < maxIterations; i++) {
            JDart jd = new JDart(sconf.getJPFConfig(), false);
            ExecutionResult er = new ExecutionResult(jd.run());
            results.add(er);

            for (Path p : er.getOkayPaths()) {
                for (Variable v : p.getValuation().getVariables()) {
                    sconf.addMethodConstraints(v.getName() + "!=" + p.getValuation().getValue(v));
                }
            }
            for (Path p: er.getErrorPaths()) {
                for (Variable v: p.getValuation().getVariables()) {
                    sconf.addMethodConstraints(v.getName() + "!=" + p.getValuation().getValue(v));
                }
            }

            logger.info("CONSTRAINTS");
            logger.info("-----------");
            logger.info(Arrays.asList(sconf.getMethodConstraints()));
        }
        logger.info(Arrays.asList(sconf.getMethodConstraints()));

        return results;
    }

    public SymbvConfig getSconf() {
        return sconf;
    }
}
