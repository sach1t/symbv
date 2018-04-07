package diffparser.generator;

import javafx.util.Pair;
import diffparser.parser.CodeMethod;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


public class TestGeneratorTest {
    CodeMethod codeMethod = new CodeMethod("aaa.b.c",
            Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3")),
            "int", null, null);

    public TestGeneratorTest() throws Exception {
    }

    private String noSpacings(String code) {
        return code
                .replace("\n", "")
                .replace(" ", "");
    }

    @Test
    public void shouldGenerateMainFunctionCorrectly() throws Exception {
        TestGenerator testGenerator = new TestGenerator(this.codeMethod, false, "");

        String type = "aaa" + TestGenerator.CLASS_SEPARATOR + "b" + TestGenerator.CLASS_SEPARATOR + "c";
        String expected = "public static void main(String args[]) {\n " + type + " t = new " + type + " ();\n"
                + "t.run(0, true, new otherClass()); }";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.genMainFunction()));
    }

    @Test
    public void shouldGenerateRunFunctionCorrectly() throws Exception {
        codeMethod.setOriginalName("c___original");
        TestGenerator testGenerator = new TestGenerator(this.codeMethod, false, "");

        String expected = "public void run(int arg1, boolean arg2, otherClass arg3) {\n "
                + "b patched = new b();"
                + "b original = new b();"

                + "int patchedResult = patched.c(arg1, arg2, arg3);"
                + "int originalResult = original.c___original(arg1, arg2, arg3);"

                + "if (originalResult == patchedResult) {"
                + "  throw new Error();"
                + "}}";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.genRunFunction()));
    }

    @Test
    public void shouldGenerateTestCorrectly() throws Exception {
        codeMethod.setOriginalName("c___original");
        TestGenerator testGenerator = new TestGenerator(this.codeMethod, false, "");
        this.assertCompleteTest(testGenerator, "");
    }

    @Test
    public void shouldGenerateTestCorrectlyWithBasePackage() throws Exception {
        codeMethod.setOriginalName("c___original");
        TestGenerator testGenerator = new TestGenerator(this.codeMethod, false, "abc");
        this.assertCompleteTest(testGenerator, "abc.");
    }

    private void assertCompleteTest(TestGenerator testGenerator, String basePackage) {
        String expected = "package " + basePackage + "symbv;\n" +
                "import " + "aaa.b;\n" +
                "public class aaa___b___c {" +
                testGenerator.genMainFunction() +
                testGenerator.genRunFunction() +
                "}";

        Assert.assertEquals(noSpacings(expected), noSpacings(testGenerator.generate()));
    }
}
