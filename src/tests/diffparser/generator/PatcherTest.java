package diffparser.generator;

import diffparser.io.FileManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatcherTest {
    @Mock
    FileManager fileManager;

    String dummyClass = "package main.java.diffparser.generator;\n"+
            "\n"+
            "public class dummy {\n"+
            "    public int dummy() {\n"+
            "        return 1;\n"+
            "    }\n"+
            "}";

    String dummyPatchFilepath = "dummyPatch";
    String dummyPatch =  "From da546b70c1348324d614c46a85f5ba27a8112be3 Mon Sep 17 00:00:00 2001\n" +
            "From: albf <albf.unicamp@gmail.com>\n" +
            "Date: Fri, 6 Apr 2018 20:25:33 -0400\n" +
            "Subject: [PATCH] mod\n" +
            "\n" +
            "---\n" +
            " src/main/java/diffparser/generator/dummy.java | 2 +-\n" +
            " 1 file changed, 1 insertion(+), 1 deletion(-)\n" +
            "\n" +
            "diff --git a/src/main/java/diffparser/generator/dummy.java b/src/main/java/diffparser/generator/dummy.java\n" +
            "index 1a11316..f60bab7 100644\n" +
            "--- a/src/main/java/diffparser/generator/dummy.java\n" +
            "+++ b/src/main/java/diffparser/generator/dummy.java\n" +
            "@@ -2,6 +2,6 @@ package main.java.diffparser.generator;\n" +
            " \n" +
            " public class dummy {\n" +
            "     public int dummy() {\n" +
            "-        return 1;\n" +
            "+        return 2;\n" +
            "     }\n" +
            " }\n" +
            "-- \n" +
            "2.11.0\n" +
            "\n";

    String dummyWithSymbvClass = "package main.java.diffparser.generator;\n"+
        "\n"+
        "public class dummyWithSymbv {\n"+
        "    public int dummy() {\n"+
        "        return 1;\n"+
        "    }\n" +
        "    public static dummyWithSymbv symbv() {\n"+
        "        return new dummy();\n" +
        "    }\n" +
        "}";

    String dummyWithSymbvPatchFilepath = "dummyWithSymbvPatch";
    String dummyWithSymbvPatch = "From 051158a3add1aa0e7053a39a3e1fa1240dfe3d0a Mon Sep 17 00:00:00 2001\n" +
            "From: albf <albf.unicamp@gmail.com>\n" +
            "Date: Sun, 8 Apr 2018 10:00:14 -0400\n" +
            "Subject: [PATCH] finally patch\n" +
            "\n" +
            "---\n" +
            " src/main/java/diffparser/generator/dummyWithSymbv.java | 2 +-\n" +
            " 1 file changed, 1 insertion(+), 1 deletion(-)\n" +
            "\n" +
            "diff --git a/src/main/java/diffparser/generator/dummyWithSymbv.java b/src/main/java/diffparser/generator/dummyWithSymbv.java\n" +
            "index a18a5ff..ccdc215 100644\n" +
            "--- a/src/main/java/diffparser/generator/dummyWithSymbv.java\n" +
            "+++ b/src/main/java/diffparser/generator/dummyWithSymbv.java\n" +
            "@@ -2,7 +2,7 @@ package main.java.diffparser.generator;\n" +
            " \n" +
            " public class dummyWithSymbv {\n" +
            "     public int dummy() {\n" +
            "-        return 1;\n" +
            "+        return 2;\n" +
            "     }\n" +
            "     public static dummyWithSymbv symbv() {\n" +
            "         return new dummy();\n" +
            "-- \n" +
            "2.11.0\n" +
            "\n";

    ArgumentCaptor<String> saveFilepathCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> saveContentCaptor = ArgumentCaptor.forClass(String.class);


    @Before
    public void setupMock() {
        fileManager = mock(FileManager.class);
        try {
            when(fileManager.readFile(dummyPatchFilepath)).thenReturn(dummyPatch);
            when(fileManager.readFile(dummyWithSymbvPatchFilepath)).thenReturn(dummyWithSymbvPatch);
            when(fileManager.readFile("src/main/java/diffparser/generator/dummy.java")).thenReturn(dummyClass);
            when(fileManager.readFile("src/main/java/diffparser/generator/dummyWithSymbv.java")).thenReturn(dummyWithSymbvClass);

            doNothing().when(fileManager).writeFile(any(String.class), any(String.class));
            doNothing().when(fileManager).appendToFile(any(String.class), any(String.class));
            doNothing().when(fileManager).createDirectory(any(String.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGenerateDummyTestCorrectly() throws IOException {

        Patcher patcher = new Patcher(fileManager);

        patcher.apply(dummyPatchFilepath);

        // Should save test and modified dummy.java
        verify(fileManager, times(2))
                .writeFile(saveFilepathCaptor.capture(), saveContentCaptor.capture());

        verify(fileManager, times(1)).createDirectory(any(String.class));
        verify(fileManager, times(1)).appendToFile(any(String.class), any(String.class));

        Assert.assertEquals("symbv/main_java_diffparser_generator___dummy___dummy.java", this.saveFilepathCaptor.getAllValues().get(0));
        Assert.assertEquals("src/main/java/diffparser/generator/dummy.java", this.saveFilepathCaptor.getAllValues().get(1));

        // Modified/resulting file, should have BOTH dummy and dummy___original
        Assert.assertEquals(false, this.saveContentCaptor.getAllValues().get(0).contains("dummy.symbv();"));
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy()"));
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy___original()"));
    }

    @Test
    public void shouldGenerateDummyWithSymbvTestCorrectly() throws IOException {
        Patcher patcher = new Patcher(fileManager);

        patcher.apply(dummyWithSymbvPatchFilepath);

        // Should save test and modified dummy.java
        verify(fileManager, times(2))
                .writeFile(saveFilepathCaptor.capture(), saveContentCaptor.capture());

        verify(fileManager, times(1)).createDirectory(any(String.class));
        verify(fileManager, times(1)).appendToFile(any(String.class), any(String.class));

        Assert.assertEquals("symbv/main_java_diffparser_generator___dummyWithSymbv___dummy.java", this.saveFilepathCaptor.getAllValues().get(0));
        Assert.assertEquals("src/main/java/diffparser/generator/dummyWithSymbv.java", this.saveFilepathCaptor.getAllValues().get(1));

        // Modified/resulting file, should have BOTH dummy and dummy___original
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(0).contains("dummyWithSymbv.symbv();"));
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy()"));
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy___original()"));
    }
}
