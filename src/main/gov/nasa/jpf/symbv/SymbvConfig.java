package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;

import java.util.*;
import java.util.stream.Stream;

public class SymbvConfig {
    private static String METHOD_CONFIG = "fun";
    private static String CONSTRAINTS_CONFIG = METHOD_CONFIG + "_constraints";
    private static String ALL_FIELDS_SYMBOLIC_CONFIG = METHOD_CONFIG + "_all_fields_symbolic";
    private static String MAX_ALT_DEPTH_CONFIG = METHOD_CONFIG + "_alt_depth";

    static String CONCOLIC_PREFIX = "concolic.method";
    static String ANALYSIS_PREFIX = "jdart.configs";
    static String TEST_PREFIX = "symbv.test";

    public int testNumber;
    public int iterations;

    private Config config;
    private JPFLogger logger = JPF.getLogger("symbv");
    private Collection<String> methodConfigs;

    public SymbvConfig(Config jpfConfig) {
        this.config = SymbvConfig.copyConfig(jpfConfig);
        setDefaults();
        methodConfigs = new HashSet<>();
        iterations = getIterations();
    }

    public int getIterations() {
        return config.getInt("symbv.iterations", 25);
    }

    public static Config copyConfig(Config conf) {
        Config newConf = new Config("");
        newConf.putAll(conf);
        newConf.setClassLoader(conf.getClassLoader());
        return newConf;
    }

    private void setDefaults() {
        config.setProperty("symbolic.dp", "z3");
        config.setProperty("symbolic.dp.z3.bitvectors", "true");
    }

    public void setConcolicMethod(String FQClassName, String methodSpec, int testNumber) {
        this.testNumber = testNumber;
        setConcolicMethod(FQClassName, methodSpec);
    }

    public void setConcolicMethod(String FQClassName, String methodSpec) {
        // validate settings
        int startParam = methodSpec.indexOf('(');
        int endParam = methodSpec.indexOf(')');
        if ((startParam + 1) != endParam) { // i.e. there are some parameters
            String parameters = methodSpec.substring(startParam + 1, endParam);
            if (!(parameters.contains(":"))) {
                throw new SymbvConfig.ConfigurationError("Parameter list is not formatted correctly.");
            }
        }

        config.setProperty("target", FQClassName);
        config.setProperty(CONCOLIC_PREFIX + "." + METHOD_CONFIG, FQClassName + "." + methodSpec);
        config.setProperty(CONCOLIC_PREFIX, METHOD_CONFIG);
    }

    public void setMaxAltDepth(int depth) {
        config.setProperty(ANALYSIS_PREFIX + "." + MAX_ALT_DEPTH_CONFIG + ".max_alt_depth", Integer.toString(depth));
        methodConfigs.add(MAX_ALT_DEPTH_CONFIG);
        updateMethodConfigProperty();
    }

    public void setInstanceFieldsSymbolic() {
        config.setProperty(ANALYSIS_PREFIX + "." + ALL_FIELDS_SYMBOLIC_CONFIG + ".symbolic.include",  "this.*");
        methodConfigs.add(ALL_FIELDS_SYMBOLIC_CONFIG);
        updateMethodConfigProperty();
    }

    private void updateMethodConfigProperty() {
        String configs = String.join(";", methodConfigs);
        config.setProperty(CONCOLIC_PREFIX + "." + METHOD_CONFIG + ".config", configs);
    }

    public String[] getMethodConstraints() {
        String constraints = config.getProperty(ANALYSIS_PREFIX + "." + CONSTRAINTS_CONFIG  + ".constraints");
        if (constraints == null) {
            return new String[]{};
        }
        return constraints.split("\\s*;\\s*");
    }

    public void setMethodConstraints(String...constraints) {
        String property = ANALYSIS_PREFIX + "." + CONSTRAINTS_CONFIG  + ".constraints";
        if (constraints.length == 0) {
            config.remove(property);
            methodConfigs.remove(CONSTRAINTS_CONFIG);
        } else {
            Set<String> uniqueConstraints = new HashSet<>(Arrays.asList(constraints));
            config.setProperty(property, String.join(";", uniqueConstraints));
            methodConfigs.add(CONSTRAINTS_CONFIG);
        }
        updateMethodConfigProperty();
    }

    public void addMethodConstraints(String...constraints) {
        String[] allConstraints = Stream.concat(Arrays.stream(getMethodConstraints()), Arrays.stream(constraints))
                .toArray(String[]::new);
        setMethodConstraints(allConstraints);
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

    public static class ConfigurationError extends Error {
        public ConfigurationError(String errorMsg) {
            super(errorMsg);
        }
    }

}
