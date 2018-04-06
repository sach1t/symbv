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
    public void shouldGenerateMainFunctionCorrectly() throws Exception {
        List<Pair<String, String>> args = Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3"));
        CodeMethod codeMethod = new CodeMethod("aaa.b.c", args, "NOT_NEEDED", null, null);
        TestGenerator testGenerator = new TestGenerator(codeMethod, false);

        String type = "aaa" + TestGenerator.CLASS_SEPARATOR + "b" + TestGenerator.CLASS_SEPARATOR + "c";
        String expected = "public static void main(String args[]) {\n " + type + " t = new " + type + " ();\n"
                + "t.run(0, true, new otherClass()); }";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.genMainFunction()));
    }

    @Test
    public void shouldGenerateRunFunctionCorrectly() throws Exception {
        List<Pair<String, String>> args = Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3"));
        CodeMethod codeMethod = new CodeMethod("aaa.b.c", args, "int", null, null);
        codeMethod.setOriginalName("c___original");
        TestGenerator testGenerator = new TestGenerator(codeMethod, false);

        String expected = "public void run(int arg1, boolean arg2, otherClass arg3) {\n "
                + "b original = new b();"
                + "b patched = new b();"

                + "int originalResult = original.c___original(arg1, arg2, arg3);"
                + "int patchedResult = patched.c(arg1, arg2, arg3);"

                + "if (originalResult == patchedResult) {"
                + "  throw new Error();"
                + "}}";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.genRunFunction()));
    }
}
