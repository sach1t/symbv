package main.java.diffparser.parser;

import org.junit.Assert;
import org.junit.Test;

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
}
