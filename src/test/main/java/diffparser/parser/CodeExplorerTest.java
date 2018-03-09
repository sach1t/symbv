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
            "\n}}";
    CodeExplorer ceSimple = new CodeExplorer(codeSimple);

    String codeMultiple = "package dream.was.so.big;" +
        "\nclass X {" +
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
        Assert.assertEquals(true, modified.contains("X.x"));
    }

    @Test
    public void shouldFindModifiedFunctionName() {
        modifiedLines.add(3);

        List<String> modified = ceSimple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(1, modified.size());
        Assert.assertEquals(true, modified.contains("X.x"));
    }

    @Test
    public void shouldFindModifiedFunctionLastLine() {
        modifiedLines.add(5);

        List<String> modified = ceSimple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(1, modified.size());
        Assert.assertEquals(true, modified.contains("X.x"));
    }

    @Test
    public void shouldFindMultipleModifiedFunctionsWithPackage() {
        modifiedLines.add(5);
        modifiedLines.add(8);
        modifiedLines.add(11);

        List<String> modified = ceMultiple.findModifiedMethods(modifiedLines);
        Assert.assertEquals(3, modified.size());
        Assert.assertEquals(true, modified.contains("dream.was.so.big.X.x"));
        Assert.assertEquals(true, modified.contains("dream.was.so.big.X.y"));
        Assert.assertEquals(true, modified.contains("dream.was.so.big.X.z"));
    }
}
