package gov.nasa.jpf.symbv;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.jdart.JDart;
import gov.nasa.jpf.jdart.constraints.Path;
import gov.nasa.jpf.util.JPFLogger;

import java.util.*;
import java.util.stream.IntStream;

public class Executor {
    SymbvConfig sconf;
    private JPFLogger logger = JPF.getLogger("symbv");
    private int iterations;

    public Executor(SymbvConfig sconf) {
        this(sconf, 5);
    }

    public Executor(SymbvConfig sconf, int iterations) {
        this.sconf = sconf;
        this.iterations = iterations;
    }

    public List<ExecutionResult> run() {
        List<ExecutionResult> results = new ArrayList<>();

        IntStream.range(0, iterations).forEach(i -> {
            JDart jd = new JDart(sconf.getJPFConfig(), false);
            ExecutionResult er = new ExecutionResult(jd.run());
            results.add(er);
            Collection<Path> paths = er.getOkayPaths();
            for (Path p: paths) {
                for (Variable v: p.getValuation().getVariables()) {
                    sconf.addMethodConstraints(v.getName() + "!=" + p.getValuation().getValue(v));
                }
            }
            logger.warning("CONSTRAINTS");
            logger.warning("-----------");
            logger.warning(Arrays.asList(sconf.getMethodConstraints()));
        });
        logger.warning(Arrays.asList(sconf.getMethodConstraints()));

        return results;
    }

    public static void printValuations(List<ExecutionResult> results) {
        results.forEach(er -> {
            er.getOkayPaths().forEach(path -> {
                Valuation valuation = path.getValuation();
                List<String> out = new ArrayList<>();
                valuation.getVariables().forEach(var -> {
                    out.add(var.getName() + " = " + valuation.getValue(var));
                });
                System.out.println(String.join(", ", out));
            });
        });
    }

    public SymbvConfig getSconf() {
        return sconf;
    }
}
