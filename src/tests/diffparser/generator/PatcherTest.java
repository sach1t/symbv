package diffparser.generator;

import diffparser.io.FileManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatcherTest {
    @Mock
    static FileManager fileManager;

    static String dummyClass = "package main.java.diffparser.generator;\n"+
            "\n"+
            "public class dummy {\n"+
            "    public int dummy() {\n"+
            "        return 1;\n"+
            "    }\n"+
            "}";

    static String dummyPatchFilepath = "dummyPatch";
    static String dummyPatch =  "From da546b70c1348324d614c46a85f5ba27a8112be3 Mon Sep 17 00:00:00 2001\n" +
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

    static ArgumentCaptor<String> saveFilepathCaptor = ArgumentCaptor.forClass(String.class);
    static ArgumentCaptor<String> saveContentCaptor = ArgumentCaptor.forClass(String.class);


    @BeforeClass
    public static void setupMock() {
        fileManager = mock(FileManager.class);
        try {
            when(fileManager.readFile(dummyPatchFilepath)).thenReturn(dummyPatch);
            when(fileManager.readFile("src/main/java/diffparser/generator/dummy.java")).thenReturn(dummyClass);

            doNothing().when(fileManager).writeFile(any(String.class), any(String.class));
            doNothing().when(fileManager).createDirectory(any(String.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGenerateDummyTestCorrectly() throws FileNotFoundException {

        Patcher patcher = new Patcher(fileManager);

        patcher.apply(dummyPatchFilepath);

        // Should save test and modified dummy.java
        verify(fileManager, times(2))
                .writeFile(PatcherTest.saveFilepathCaptor.capture(), PatcherTest.saveContentCaptor.capture());

        verify(fileManager, times(1)).createDirectory(any(String.class));

        Assert.assertEquals("symbv/main_java_diffparser_generator___dummy___dummy.java", this.saveFilepathCaptor.getAllValues().get(0));
        Assert.assertEquals("src/main/java/diffparser/generator/dummy.java", this.saveFilepathCaptor.getAllValues().get(1));

        // Modified/resulting file, should have BOTH dummy and dummy___original
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy()"));
        Assert.assertEquals(true, this.saveContentCaptor.getAllValues().get(1).contains("public int dummy___original()"));
    }
}
