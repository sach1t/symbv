package diffparser.parser;

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CodeMethodTest {
    @Test
    public void shouldIdentifyShortNamesCorrectly() {
        String[] parts = null;

        try {
            parts = CodeMethod.splitCompleteName("aaa.b.c");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(parts[0], "aaa");
        Assert.assertEquals(parts[1], "b");
        Assert.assertEquals(parts[2], "c");
    }


    @Test
    public void shouldIdentifyEqualSignatures() throws Exception {
        CodeMethod codeMethod1 = new CodeMethod("aaa.b.c",
                Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3")),
                "int", null, null);

        CodeMethod codeMethod2 = new CodeMethod("aaa.b.c2",
                Arrays.asList(new Pair("int", "x"), new Pair("boolean", "y"), new Pair("otherClass", "z")),
                "int", null, null);

        Assert.assertEquals(true, codeMethod1.hasSameParameters(codeMethod2));
    }

    @Test
    public void shouldIdentifyDifferentSignatures() throws Exception {
        CodeMethod codeMethod1 = new CodeMethod("aaa.b.c",
                Arrays.asList(new Pair("int", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3")),
                "int", null, null);

        CodeMethod codeMethod2 = new CodeMethod("aaa.b.c",
                Arrays.asList(new Pair("float", "arg1"), new Pair("boolean", "arg2"), new Pair("otherClass", "arg3")),
                "int", null, null);

        Assert.assertEquals(false, codeMethod1.hasSameParameters(codeMethod2));
    }

}
