package gov.nasa.jpf.symbv;

import diffparser.generator.Patcher;
import diffparser.io.FileManager;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.LogManager;


public class Symbv implements JPFShell {
    private Config config;
    private JPFLogger logger;

    public Symbv(Config conf) {
        this.config = conf;
        LogManager.init(conf);
        logger = JPF.getLogger("symbv");
    }

    @Override
    public void start(String[] args) {
        if (args.length < 2) {
            this.logger.warning("Step parameter required (gen or exec)");
            return;
        }

        switch (args[1]) {
            case "gen":
                if (args.length < 3) {
                    this.logger.warning("For generation, patch file should be specified.");
                    Patcher patcher = new Patcher(new FileManager());
                    patcher.apply(args[2]);
                    return;
                }
                break;

            case "exec":
                // this information can be gotten using JavaParser
                String target = "simple2.Runner"; // fully qualified class name
                String methodSpec = "run(i: int)"; // run method

                SymbvConfig sConf = new SymbvConfig(config);
                sConf.setConcolicMethod(target, methodSpec);
                sConf.setInstanceFieldsSymbolic();
                run(sConf);
                break;

            default:
                this.logger.warning("Unknown step " + args[1] + " argument passed (should use gen or exec)");
                return;
        }
    }

    public void run(SymbvConfig sconf) {
        Executor ex = new Executor(sconf);
        ExecutionResult er = ex.run();
    }
}
