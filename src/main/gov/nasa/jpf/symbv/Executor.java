package gov.nasa.jpf.symbv;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.jdart.JDart;
import gov.nasa.jpf.jdart.constraints.Path;
import gov.nasa.jpf.util.JPFLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Executor {
    SymbvConfig sconf;
    private JPFLogger logger = JPF.getLogger("symbv");
    private int maxIterations = 25;

    public Executor(SymbvConfig sconf) {
        this.sconf = sconf;
        this.maxIterations = sconf.getIterations();
    }

    public List<ExecutionResult> run() {
        List<ExecutionResult> results = new ArrayList<>();
        int i = 0;

        while (true) {
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

            // conditions to break out
            i += 1;
            if (i == maxIterations || er.getOkayPaths().size() == 0) {
                break;
            }
        }
        logger.info(Arrays.asList(sconf.getMethodConstraints()));
        logger.info("TOTAL ITERATIONS = " + i);

        return results;
    }

    public SymbvConfig getSconf() {
        return sconf;
    }
}
