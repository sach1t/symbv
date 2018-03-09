package main.java.diffparser.parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;


public class CodeExplorerTest {
    String codeSimple = "class X {" +
            "\n  int field;" +
            "\n  void x (){ " +
            "\n    field = 0;" +
            "}}";
    CodeExplorer ceSimple = new CodeExplorer(codeSimple);

    String codeMultiple = "class X {" +
        "\n  int field;" +
        "\n  void x (){ " +
        "\n    field = 0;" +
        "\n}" +
        "\n  void y (){ " +
        "\n    field = 0;" +
        "\n}" +
        "\n  void z (){ " +
        "\n    field = 0;" +
        "\n}}";
    CodeExplorer ceMultiple = new CodeExplorer(codeMultiple);

    List<Integer> modifiedLines;

    @Before
    public void init() {
        System.out.println("Init");
        modifiedLines = new ArrayList<>();
    }

    @Test
    public void shouldReturnEmptyIfClassNameChanged() {
        modifiedLines.add(1);

        List<String> modified = ceSimple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(0, modified.size());
    }

    @Test
    public void shouldFindOneModifiedFunction() {
        modifiedLines.add(4);

        List<String> modified = ceSimple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(1, modified.size());
        Assert.assertEquals(true, modified.contains("x"));
    }

    @Test
    public void shouldFindModifiedFunctionName() {
        modifiedLines.add(3);

        List<String> modified = ceSimple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(1, modified.size());
        Assert.assertEquals(true, modified.contains("x"));
    }

    @Test
    public void shouldFindMultipleModifiedFunctions() {
        modifiedLines.add(4);
        modifiedLines.add(7);
        modifiedLines.add(10);

        List<String> modified = ceMultiple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(3, modified.size());
        Assert.assertEquals(true, modified.contains("x"));
        Assert.assertEquals(true, modified.contains("y"));
        Assert.assertEquals(true, modified.contains("z"));
    }
}
