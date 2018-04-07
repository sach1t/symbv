package main.java.diffparser.parser;

import main.java.diffparser.io.FileManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiffApplierTest {
    @Mock
    static FileManager fileManager;

    DiffApplier diffApplier = new DiffApplier(fileManager);

    static String oneChangePatch = "From b32e991d7e3d177d22bfc376a54d411abaa37723 Mon Sep 17 00:00:00 2001\n" +
            "From: albf <albf.unicamp@gmail.com>\n" +
            "Date: Fri, 9 Mar 2018 00:30:39 -0500\n" +
            "Subject: [PATCH] yyy\n" +
            "\n" +
            "---\n" +
            " src/test/x | 2 +-\n" +
            " 1 file changed, 1 insertion(+), 1 deletion(-)\n" +
            "\n" +
            "diff --git a/src/test/x b/src/test/x\n" +
            "index 019fb23..abe4786 100644\n" +
            "--- a/src/test/x\n" +
            "+++ b/src/test/x\n" +
            "@@ -1 +1 @@\n" +
            "-ddddddd\n" +
            "+ddddd\n" +
            "-- \n" +
            "2.11.0\n" +
            "\n";

    static String oneChangePatchExpected = "@@ -1 +1 @@\n" +
            "-ddddddd\n" +
            "+ddddd\n";

    static String oneChangeFilepath = "src/test/x";
    static String oneChangeOriginal = "ddddddd";
    static String oneChangeModified = "ddddd";

    static String twoChangesPatch = "From 397ff5d8cc1091852dfec56d42b1b472f15416db Mon Sep 17 00:00:00 2001\n" +
            "From: albf <albf.unicamp@gmail.com>\n" +
            "Date: Sat, 10 Mar 2018 00:30:05 -0500\n" +
            "Subject: [PATCH] two files modification\n" +
            "\n" +
            "---\n" +
            " src/main/java/diffparser/parser/x | 3 +--\n" +
            " src/main/java/diffparser/parser/y | 2 +-\n" +
            " 2 files changed, 2 insertions(+), 3 deletions(-)\n" +
            "\n" +
            "diff --git a/src/main/java/diffparser/parser/x b/src/main/java/diffparser/parser/x\n" +
            "index 9405325..50d4abb 100644\n" +
            "--- a/src/main/java/diffparser/parser/x\n" +
            "+++ b/src/main/java/diffparser/parser/x\n" +
            "@@ -1,5 +1,4 @@\n" +
            " a\n" +
            "-b\n" +
            "+k\n" +
            " c\n" +
            "-d\n" +
            " e\n" +
            "diff --git a/src/main/java/diffparser/parser/y b/src/main/java/diffparser/parser/y\n" +
            "index 8baef1b..24c5735 100644\n" +
            "--- a/src/main/java/diffparser/parser/y\n" +
            "+++ b/src/main/java/diffparser/parser/y\n" +
            "@@ -1 +1 @@\n" +
            "-abc\n" +
            "+def\n" +
            "-- \n" +
            "2.11.0\n" +
            "\n";

    static String[] twoChangesPatchExpected = {
            "@@ -1,5 +1,4 @@\n" +
                    " a\n" +
                    "-b\n" +
                    "+k\n" +
                    " c\n" +
                    "-d\n" +
                    " e\n",

            "@@ -1 +1 @@\n" +
                    "-abc\n" +
                    "+def\n"
    };

    static String[] twoChangesFilepath = {
            "src/main/java/diffparser/parser/x",
            "src/main/java/diffparser/parser/y"
    };

    static String[] twoChangesOriginal = {
            "a\nb\nc\nd\ne",
            "abc"
    };

    static String[] twoChangesModified = {
            "a\nk\nc\ne",
            "def"
    };

    static String abcPatch = "From 311bec457560e0083aa077bc38d574b4e3e323de Mon Sep 17 00:00:00 2001\n" +
            "From: albf <albf.unicamp@gmail.com>\n" +
            "Date: Sat, 10 Mar 2018 19:27:54 -0500\n" +
            "Subject: [PATCH] DependableRocks\n" +
            "\n" +
            "---\n" +
            " src/main/java/diffparser/parser/abc | 4 ++--\n" +
            " 1 file changed, 2 insertions(+), 2 deletions(-)\n" +
            "\n" +
            "diff --git a/src/main/java/diffparser/parser/abc b/src/main/java/diffparser/parser/abc\n" +
            "index 0edb856..82632d2 100644\n" +
            "--- a/src/main/java/diffparser/parser/abc\n" +
            "+++ b/src/main/java/diffparser/parser/abc\n" +
            "@@ -1,7 +1,7 @@\n" +
            " a\n" +
            " b\n" +
            " c\n" +
            "-d\n" +
            "+dependable\n" +
            " e\n" +
            " f\n" +
            " g\n" +
            "@@ -15,7 +15,7 @@ n\n" +
            " o\n" +
            " p\n" +
            " q\n" +
            "-r\n" +
            "+rocks\n" +
            " s\n" +
            " t\n" +
            " u\n" +
            "-- \n" +
            "2.11.0\n" +
            "\n";

    static String abcPatchExpected = "@@ -1,7 +1,7 @@\n" +
            " a\n" +
            " b\n" +
            " c\n" +
            "-d\n" +
            "+dependable\n" +
            " e\n" +
            " f\n" +
            " g\n" +
            "@@ -15,7 +15,7 @@ n\n" +
            " o\n" +
            " p\n" +
            " q\n" +
            "-r\n" +
            "+rocks\n" +
            " s\n" +
            " t\n" +
            " u\n";

    static String abcFilepath = "src/main/java/diffparser/parser/abc";
    static String abcOriginal = "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nr\ns\nt\nu\nv\nw\nx\ny\nz\n";
    static String abcModified = "a\nb\nc\ndependable\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\no\np\nq\nrocks\ns\nt\nu\nv\nw\nx\ny\nz\n";

    @BeforeClass
    public static void setupMock() {
        fileManager = mock(FileManager.class);
        try {
            when(fileManager.readFile(oneChangeFilepath)).thenReturn(oneChangeOriginal);
            when(fileManager.readFile(twoChangesFilepath[0])).thenReturn(twoChangesOriginal[0]);
            when(fileManager.readFile(twoChangesFilepath[1])).thenReturn(twoChangesOriginal[1]);
            when(fileManager.readFile(abcFilepath)).thenReturn(abcOriginal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldSplitGitDiffWithOneChange() {
        Map<String, String> patches = diffApplier.splitGitPatch(oneChangePatch);

        Assert.assertEquals(1, patches.size());
        Assert.assertEquals(true, patches.containsKey(oneChangeFilepath));
        Assert.assertEquals(oneChangePatchExpected, patches.get(oneChangeFilepath));
    }

    @Test
    public void shouldIgnoreNewFile() {
        String newFilePatch = oneChangePatch.replaceFirst("--- a/src/test/x", "--- /dev/null");
        Map<String, String> patches = diffApplier.splitGitPatch(newFilePatch);

        Assert.assertEquals(0, patches.size());
    }

    @Test
    public void shouldSplitGitDiffWithTwoChanges() {
        Map<String, String> patches = diffApplier.splitGitPatch(twoChangesPatch);

        Assert.assertEquals(2, patches.size());

        Assert.assertEquals(true, patches.containsKey(twoChangesFilepath[0]));
        Assert.assertEquals(twoChangesPatchExpected[0], patches.get(twoChangesFilepath[0]));

        Assert.assertEquals(true, patches.containsKey(twoChangesFilepath[1]));
        Assert.assertEquals(twoChangesPatchExpected[1], patches.get(twoChangesFilepath[1]));
    }

    @Test
    public void shouldApplyPatchWithOneChangeCorrectly() {
        DiffResult diffResult = diffApplier.applyPatch(oneChangeFilepath, oneChangePatchExpected);

        Assert.assertEquals(oneChangeOriginal, diffResult.getOriginal());
        Assert.assertEquals(oneChangeModified, diffResult.getModified());

        Assert.assertEquals(1, diffResult.getModifiedLines().size());
        Assert.assertEquals(new Integer(1), diffResult.getModifiedLines().get(0));
    }

    @Test
    public void shouldApplyPatchWithMultipleLinesChangedCorrectly() {
        DiffResult diffResult = diffApplier.applyPatch(twoChangesFilepath[0], twoChangesPatchExpected[0]);

        Assert.assertEquals(twoChangesOriginal[0], diffResult.getOriginal());
        Assert.assertEquals(twoChangesModified[0], diffResult.getModified());

        Assert.assertEquals(2, diffResult.getModifiedLines().size());
        Assert.assertEquals(new Integer(2), diffResult.getModifiedLines().get(0));
        Assert.assertEquals(new Integer(4), diffResult.getModifiedLines().get(1));
    }

    @Test
    public void shouldSplitABCGitPatchCorrectly() {
        Map<String, String> patches = diffApplier.splitGitPatch(abcPatch);

        Assert.assertEquals(1, patches.size());

        Assert.assertEquals(true, patches.containsKey(abcFilepath));
        Assert.assertEquals(abcPatchExpected, patches.get(abcFilepath));
    }

    @Test
    public void shouldApplyABCPatchCorrectly() {
        DiffResult diffResult = diffApplier.applyPatch(abcFilepath, abcPatchExpected);

        Assert.assertEquals(abcOriginal, diffResult.getOriginal());
        Assert.assertEquals(abcModified, diffResult.getModified());

        Assert.assertEquals(2, diffResult.getModifiedLines().size());
        Assert.assertEquals(new Integer(4), diffResult.getModifiedLines().get(0));
        Assert.assertEquals(new Integer(18), diffResult.getModifiedLines().get(1));
    }
}
