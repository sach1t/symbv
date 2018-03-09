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

    @Test
    public void shouldSplitGitDiffWithOneChange() {
        Map<String, String> patches = diffApplier.splitGitPatch(oneChangePatch);

        Assert.assertEquals(1, patches.size());
        Assert.assertEquals(true, patches.containsKey("/src/test/x"));
        Assert.assertEquals(oneChangePatchExpected, patches.get("/src/test/x"));
    }

    @Test
    public void shouldIgnoreNewFile() {
        String newFilePatch = oneChangePatch.replaceFirst("--- a/src/test/x", "--- /dev/null");
        Map<String, String> patches = diffApplier.splitGitPatch(newFilePatch);

        Assert.assertEquals(0, patches.size());
    }
}
