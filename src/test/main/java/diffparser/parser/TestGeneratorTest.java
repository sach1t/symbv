package main.java.diffparser.parser;

import org.junit.Assert;
import org.junit.Test;

public class TestGeneratorTest {
    @Test
    public void shouldIdentifyShortNamesCorrectly() {
        TestGenerator testGenerator = null;

        try {
            testGenerator = new TestGenerator("aaa.b.c");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(testGenerator.packageName, "aaa");
        Assert.assertEquals(testGenerator.className, "b");
        Assert.assertEquals(testGenerator.methodName, "c");
    }
}
