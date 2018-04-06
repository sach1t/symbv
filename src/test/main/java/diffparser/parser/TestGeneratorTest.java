package main.java.diffparser.parser;

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class TestGeneratorTest {
    private String noSpacings(String code) {
        return code
                .replace("\n", "")
                .replace(" ", "");
    }

    @Test
    public void shouldIdentifyShortNamesCorrectly() {
        String[] parts = null;

        try {
            parts = TestGenerator.splitCompleteName("aaa.b.c");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(parts[0], "aaa");
        Assert.assertEquals(parts[1], "b");
        Assert.assertEquals(parts[2], "c");
    }

    @Test
    public void shouldGenerateTestFunctionCorrectly() throws Exception {
        List<Pair<String, String>> args = Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3"));
        CodeMethod codeMethod = new CodeMethod("aaa.b.c", args, null, null);
        TestGenerator testGenerator = new TestGenerator(codeMethod, false);

        String type = "aaa" + TestGenerator.CLASS_SEPARATOR + "b" + TestGenerator.CLASS_SEPARATOR + "c";
        String expected = "public static void main(String args[]) {\n " + type + " t = new " + type + " ();\n"
                + "t.test(0, true, new otherClass()); }";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.genMainFunction()));
    }
}
