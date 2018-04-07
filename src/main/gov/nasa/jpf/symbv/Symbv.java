package gov.nasa.jpf.symbv;

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
        // this information can be gotten using JavaParser
        String target = "simple2.Runner"; // fully qualified class name
        String methodSpec = "run(i: int)"; // run method

        SymbvConfig sConf = new SymbvConfig(config);
        sConf.setConcolicMethod(target, methodSpec);
        sConf.setInstanceFieldsSymbolic();
        run(sConf);
    }

    public void run(SymbvConfig sconf) {
        Executor ex = new Executor(sconf);
        ExecutionResult er = ex.run();
    }
}
