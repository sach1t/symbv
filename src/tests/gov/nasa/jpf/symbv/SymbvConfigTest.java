package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SymbvConfigTest {
    private SymbvConfig sconf;
    private Config conf;
    private String target = "simple.Runner";
    private String methodSpec = "run(i: int)";

    @Before
    public void setUp() {
        conf = new Config("");
        sconf = new SymbvConfig(conf);
        sconf.setConcolicMethod(target, methodSpec);
    }

    @Test
    public void shouldInitializeDefaults() {
        Assert.assertEquals(sconf.getJPFConfig().getProperty("symbolic.dp"), "z3");
        Assert.assertEquals(sconf.getJPFConfig().getProperty("symbolic.dp.z3.bitvectors"), "true");
    }

    @Test
    public void shouldMakeCopyOfConfig() {
        Assert.assertNotSame(sconf.getJPFConfig(), conf);
    }

    @Test
    public void shouldSetConcolicMethod() {
        conf = sconf.getJPFConfig();

        Assert.assertEquals(conf.getProperty("target"), target);
        Assert.assertTrue(conf.hasValue("concolic.method"));
        String methodConfig = conf.getProperty("concolic.method");
        Assert.assertEquals(conf.getProperty("concolic.method." + methodConfig), target + "." + methodSpec);
    }

    @Test
    public void shouldEnableSymbolicFields() {
        sconf.setInstanceFieldsSymbolic();
        conf = sconf.getJPFConfig();

        String methodConfig = conf.getProperty("concolic.method");
        String configsProperty = "concolic.method." + methodConfig + ".config";
        Assert.assertTrue(conf.hasValue(configsProperty));
        List<String> activeConfigs = Arrays.asList(conf.getProperty(configsProperty).split("\\s*;\\s*"));
        String symbolicConfigName = methodConfig + "_all_fields_symbolic";
        Assert.assertTrue(activeConfigs.contains(symbolicConfigName));
        Assert.assertEquals(conf.getProperty("jdart.configs." + symbolicConfigName + ".symbolic.include"), "this.*");
    }

    @Test
    public void shouldAddConstraints() {
        sconf.addMethodConstraints("i < 2");
        conf = sconf.getJPFConfig();

        // check config set up correctly
        String methodConfig = conf.getProperty("concolic.method");
        String configsProperty = "concolic.method." + methodConfig + ".config";
        Assert.assertTrue(conf.hasValue(configsProperty));
        List<String> activeConfigs = Arrays.asList(conf.getProperty(configsProperty).split("\\s*;\\s*"));
        String constraintConfigName = methodConfig + "_constraints";
        Assert.assertTrue(activeConfigs.contains(constraintConfigName));
        String constraintProperty = "jdart.configs." + constraintConfigName + ".constraints";

        conf = sconf.getJPFConfig();
        String[] constraints = conf.getProperty(constraintProperty).split("\\s*;\\s*");
        Assert.assertEquals(constraints.length, 1);
        Assert.assertTrue(Arrays.asList(constraints).contains("i < 2"));

        sconf.addMethodConstraints("i > 10");
        conf = sconf.getJPFConfig();
        constraints = conf.getProperty(constraintProperty).split("\\s*;\\s*");
        Assert.assertEquals(constraints.length, 2);
        Assert.assertTrue(Arrays.asList(constraints).contains("i > 10"));
        Assert.assertTrue(Arrays.asList(constraints).contains("i < 2"));
    }

    @Test
    public void shouldSetConstraintsFirstTime() {
        sconf.setMethodConstraints("i < 2");
        conf = sconf.getJPFConfig();

        // check config set up correctly
        String methodConfig = conf.getProperty("concolic.method");
        String configsProperty = "concolic.method." + methodConfig + ".config";
        Assert.assertTrue(conf.hasValue(configsProperty));
        List<String> activeConfigs = Arrays.asList(conf.getProperty(configsProperty).split("\\s*;\\s*"));
        String constraintConfigName = methodConfig + "_constraints";
        Assert.assertTrue(activeConfigs.contains(constraintConfigName));
        String constraintProperty = "jdart.configs." + constraintConfigName + ".constraints";

        conf = sconf.getJPFConfig();
        String[] constraints = conf.getProperty(constraintProperty).split("\\s*;\\s*");
        Assert.assertEquals(constraints.length, 1);
        Assert.assertTrue(Arrays.asList(constraints).contains("i < 2"));
    }

    @Test
    public void shouldOverwriteConstraintsWhenSetting() {
        sconf.setMethodConstraints("i < 2");
        conf = sconf.getJPFConfig();

        // check config set up correctly
        String methodConfig = conf.getProperty("concolic.method");
        String configsProperty = "concolic.method." + methodConfig + ".config";
        Assert.assertTrue(conf.hasValue(configsProperty));
        List<String> activeConfigs = Arrays.asList(conf.getProperty(configsProperty).split("\\s*;\\s*"));
        String constraintConfigName = methodConfig + "_constraints";
        Assert.assertTrue(activeConfigs.contains(constraintConfigName));
        String constraintProperty = "jdart.configs." + constraintConfigName + ".constraints";

        sconf.setMethodConstraints("i > 10");
        conf = sconf.getJPFConfig();
        String[] constraints = conf.getProperty(constraintProperty).split("\\s*;\\s*");
        Assert.assertEquals(constraints.length, 1);
        Assert.assertTrue(Arrays.asList(constraints).contains("i > 10"));
    }

    @Test
    public void shouldEnableBothConfigs() {
        sconf.setMethodConstraints("i < 2");
        sconf.setInstanceFieldsSymbolic();
        conf = sconf.getJPFConfig();

        // check config set up correctly
        String methodConfig = conf.getProperty("concolic.method");
        String configsProperty = "concolic.method." + methodConfig + ".config";
        Assert.assertTrue(conf.hasValue(configsProperty));
        List<String> activeConfigs = Arrays.asList(conf.getProperty(configsProperty).split("\\s*;\\s*"));
        String constraintConfigName = methodConfig + "_constraints";
        String symbolicConfigName = methodConfig + "_all_fields_symbolic";

        Assert.assertEquals(activeConfigs.size(), 2);
        Assert.assertTrue(activeConfigs.contains(constraintConfigName));
        Assert.assertTrue(activeConfigs.contains(symbolicConfigName));
    }
}

