package gov.nasa.jpf.symbv;

import diffparser.generator.Patcher;
import diffparser.io.FileManager;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.LogManager;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

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
                    return;
                }

                FileManager fileManager = new FileManager(args[2]);
                Patcher patcher = new Patcher(fileManager);
                patcher.setBasePackage(fileManager.getParentName(args[2]));
                patcher.setJpfFile(fileManager.getFileName(args[0]));
                patcher.apply(fileManager.getFileName(args[2]));
                logger.info("Generation completed");
                break;

            case "exec":
                List<SymbvConfig> sConfs = null;
                try {
                    sConfs = TestConfigGenerator.parseConfig(config);
                } catch (InvalidPropertiesFormatException e) {
                    e.printStackTrace();
                    return;
                }
                sConfs.forEach(s ->{
                    s.setInstanceFieldsSymbolic();
                    run(s);
                });
                break;

            default:
                this.logger.warning("Unknown step " + args[1] + " argument passed (should use gen or exec)");
        }
    }

    public void run(SymbvConfig sconf) {
        Executor ex = new Executor(sconf);
        List<ExecutionResult> results = ex.run();
        System.out.println("\nBEHAVIORAL CHANGES");
        System.out.println("==================");
        ex.printValuations(results);
    }
}
