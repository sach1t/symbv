package gov.nasa.jpf.symbv;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.jdart.JDart;
import gov.nasa.jpf.jdart.constraints.Path;
import gov.nasa.jpf.util.JPFLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Executor {
    SymbvConfig sconf;
    private JPFLogger logger = JPF.getLogger("symbv");
    private int iterations;

    public Executor(SymbvConfig sconf) {
        this.sconf = sconf;
        this.iterations = sconf.getIterations();
    }

    public List<ExecutionResult> run() {
        List<ExecutionResult> results = new ArrayList<>();
        IntStream.range(0, iterations).forEach(i -> {
            JDart jd = new JDart(sconf.getJPFConfig(), false);
            ExecutionResult er = new ExecutionResult(jd.run());
            results.add(er);
            for (Path p : er.getOkayPaths()) {
                for (Variable v : p.getValuation().getVariables()) {
                    sconf.addMethodConstraints(v.getName() + "!=" + p.getValuation().getValue(v));
                }
            }
            logger.info("CONSTRAINTS");
            logger.info("-----------");
            logger.info(Arrays.asList(sconf.getMethodConstraints()));
        });
        logger.info(Arrays.asList(sconf.getMethodConstraints()));

        return results;
    }

    public SymbvConfig getSconf() {
        return sconf;
    }
}
