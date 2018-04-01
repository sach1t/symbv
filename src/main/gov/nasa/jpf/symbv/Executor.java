package gov.nasa.jpf.symbv;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.jdart.JDart;
import gov.nasa.jpf.jdart.constraints.Path;
import gov.nasa.jpf.util.JPFLogger;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

public class Executor {
    SymbvConfig sconf;
    private JPFLogger logger = JPF.getLogger("symbv");

    public Executor(SymbvConfig sconf) {
        this.sconf = sconf;
    }

    public ExecutionResult run() {
        int iterations = 5;
        IntStream.range(0, iterations).forEach(i -> {
            JDart jd = new JDart(sconf.getJPFConfig(), false);
            ExecutionResult er = new ExecutionResult(jd.run());
            Collection<Path> paths = er.getOkayPaths();
            for (Path p: paths) {
                for (Variable v: p.getValuation().getVariables()) {
                    sconf.addMethodConstraints(v.getName() + "!=" + p.getValuation().getValue(v));
                }
            }
            logger.warning("CONSTRAINTSCONSTRAINTS");
            logger.warning(Arrays.asList(sconf.getMethodConstraints()));
        });
        logger.warning(Arrays.asList(sconf.getMethodConstraints()));
        return null;
    }

}
