package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymbvConfig {
    String METHOD_CONFIG_NAME = "fun";
    String CONCOLIC_PROPERTY = "concolic.method." + METHOD_CONFIG_NAME;
    String ANALYSIS_PROPERTY = "jdart.configs." + METHOD_CONFIG_NAME;
    String TARGET_PROPERTY = "target";
    Config config;
    private JPFLogger logger = JPF.getLogger("symbv");


    public SymbvConfig(Config jpfConfig) {
        this.config = copyConfig(jpfConfig);
        configureDefaults();
    }

    private Config copyConfig(Config conf) {
        Config newConf = new Config("");
        newConf.putAll(conf);
        newConf.setClassLoader(conf.getClassLoader());
        return newConf;
    }

    public void configureDefaults() {
        config.setProperty("symbolic.dp", "z3");
        config.setProperty("symbolic.dp.z3.bitvectors", "true");
        config.setProperty("jdart.configs.all_fields_symbolic.symbolic.include", "this.*");
    }

    /**
     * Add configuration options for running a method concolically. To run this configuration
     * use setActiveMethodConfig function.
     * <p>
     * See https://github.com/psycopaths/jdart/wiki/Concolic-Execution-Configuration
     *
     * @param FQClassName Fully qualified main class, e.g. simple.Runner
     * @param methodSpec  method name with signature, e.g. run(a:int, b:boolean)
     */
    public void setConcolic(String FQClassName, String methodSpec) {
        config.setProperty(TARGET_PROPERTY, FQClassName);
        config.setProperty(CONCOLIC_PROPERTY, FQClassName + "." + methodSpec);
        config.setProperty("concolic.method", METHOD_CONFIG_NAME);
    }

    private boolean hasMethodConfig() {
        return config.hasValue(CONCOLIC_PROPERTY + ".config");
    }

    private String getMethodConfig() {
        return config.getProperty(CONCOLIC_PROPERTY + ".config");
    }

    public void setMethodInstanceFieldsSymbolic() {
        if (hasMethodConfig()) {
            logger.warning("Cannot have two active method configurations, old configuration " +
                    getMethodConfig() + " disabled.");
        }
        config.setProperty(CONCOLIC_PROPERTY + ".config", "all_fields_symbolic");
    }

    public void setMethodConstraints(String constraints) {
        if (hasMethodConfig()) {
            logger.warning("Cannot have two active method configurations, old configuration " +
                    getMethodConfig() + " disabled.");
        }
        config.setProperty(ANALYSIS_PROPERTY + ".constraints", constraints);
        config.setProperty(CONCOLIC_PROPERTY + ".config", METHOD_CONFIG_NAME);
    }

    public Config getJPFConfig() {
        return config;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        config.forEach((k, v) -> {
            sb.append(k).append(" = ").append(v).append("\n");
        });

        sb.append("======== JDART SPECIFIC =========\n");
        List<String[]> setKeys = new ArrayList<>();
        setKeys.add(config.getKeysStartingWith("target"));
        setKeys.add(config.getKeysStartingWith("jdart"));
        setKeys.add(config.getKeysStartingWith("concolic"));

        setKeys.forEach(keys -> {
            Arrays.asList(keys).forEach(k -> {
                sb.append(k).append(" = ").append(config.getProperty(k)).append("\n");
            });
        });

        return sb.toString();
    }
}
