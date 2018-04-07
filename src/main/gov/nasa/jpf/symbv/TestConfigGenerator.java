package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestConfigGenerator {

    public static List<SymbvConfig> parseConfig(Config config) throws InvalidPropertiesFormatException {
        List<SymbvConfig> sconfs = new ArrayList<>();

        // get test numbers from config
        Set<Integer> testNums = new HashSet<>();
        Pattern p = Pattern.compile("symbv[.]test(\\d*)[.](.*)");
        String[] testKeys = config.getKeysStartingWith(SymbvConfig.TEST_PREFIX);
        for (String testKey: testKeys) {
            Matcher m = p.matcher(testKey);
            if (m.matches()) {
                int number = Integer.parseInt(m.group(1));
                String suffix = m.group(2);
                testNums.add(number);
            } else {
                throw new InvalidPropertiesFormatException("Could not match: " + testKey);
            }
        }

        // get test properties
        for (int testNum: testNums) {
            String target = config.getProperty(SymbvConfig.TEST_PREFIX + Integer.toString(testNum) + ".target");
            String method = config.getProperty(SymbvConfig.TEST_PREFIX + Integer.toString(testNum) + ".method");
            if (target == null || method == null) {
                throw new InvalidPropertiesFormatException("Invalid test config with number " + Integer.toString(testNum));
            }
            SymbvConfig sconf = new SymbvConfig(config);
            sconf.setConcolicMethod(target, method, testNum);
            sconfs.add(sconf);
        }
        return sconfs;
    }
}
