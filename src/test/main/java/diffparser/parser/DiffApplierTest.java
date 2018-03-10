package main.java.diffparser.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DiffApplierTest {
    DiffApplier diffApplier = new DiffApplier();

    String oneChangePatch = "From b32e991d7e3d177d22bfc376a54d411abaa37723 Mon Sep 17 00:00:00 2001\n" +
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

    String oneChangePatchExpected = "\n@@ -1 +1 @@\n" +
            "-ddddddd\n" +
            "+ddddd";

    String twoChangesPatch = "From 397ff5d8cc1091852dfec56d42b1b472f15416db Mon Sep 17 00:00:00 2001\n" +
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

    String[] twoChangesPatchExpected = {
            "\n@@ -1,5 +1,4 @@\n" +
                    " a\n" +
                    "-b\n" +
                    "+k\n" +
                    " c\n" +
                    "-d\n" +
                    " e",

            "\n@@ -1 +1 @@\n" +
                    "-abc\n" +
                    "+def"
    };

    @Test
    public void shouldSplitGitDiffWithOneChange() {
        Map<String, String> patches = diffApplier.splitGitPatch(oneChangePatch);

        Assert.assertEquals(1, patches.size());
        Assert.assertEquals(true, patches.containsKey("src/test/x"));
        Assert.assertEquals(oneChangePatchExpected, patches.get("src/test/x"));
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

        Assert.assertEquals(true, patches.containsKey("src/main/java/diffparser/parser/x"));
        Assert.assertEquals(twoChangesPatchExpected[0], patches.get("src/main/java/diffparser/parser/x"));

        Assert.assertEquals(true, patches.containsKey("src/main/java/diffparser/parser/y"));
        Assert.assertEquals(twoChangesPatchExpected[1], patches.get("src/main/java/diffparser/parser/y"));
    }
}
