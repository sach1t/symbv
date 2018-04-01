package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;

import java.util.*;
import java.util.stream.Stream;

public class SymbvConfig {
    private String METHOD_CONFIG = "fun";
    private String CONSTRAINTS_CONFIG = METHOD_CONFIG + "_constraints";
    private String ALL_FIELDS_SYMBOLIC_CONFIG = METHOD_CONFIG + "_all_fields_symbolic";

    private String CONCOLIC_PREFIX = "concolic.method";
    private String ANALYSIS_PREFIX = "jdart.configs";

    private Config config;
    private JPFLogger logger = JPF.getLogger("symbv");
    private Collection<String> methodConfigs;

    public SymbvConfig(Config jpfConfig) {
        this.config = SymbvConfig.copyConfig(jpfConfig);
        setDefaults();
        methodConfigs = new HashSet<>();
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

    public void setConcolicMethod(String FQClassName, String methodSpec) {
        config.setProperty("target", FQClassName);
        config.setProperty(CONCOLIC_PREFIX + "." + METHOD_CONFIG, FQClassName + "." + methodSpec);
        config.setProperty(CONCOLIC_PREFIX, METHOD_CONFIG);
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
}
