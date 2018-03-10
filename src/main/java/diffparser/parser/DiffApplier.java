package main.java.diffparser.parser;

import main.java.diffparser.io.FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DiffApplier {
    FileManager fileManager;
    diff_match_patch diffMatchPatch;

    public DiffApplier(FileManager fileManager) {
        this.fileManager = fileManager;
        this.diffMatchPatch = new diff_match_patch();
    }

    protected Map<String, String> splitGitPatch(String patchText) {
        final Map<String, String> patches = new HashMap<>();

        // Remove initial e-mail or other junk
        patchText = patchText.substring(patchText.indexOf("diff --git"), patchText.length()-1);

        BufferedReader reader = new BufferedReader(new StringReader(patchText));

        boolean reading = false;
        String currentPatch = "";

        String oldFilename = "";
        String newFilename = "";

        // Catch should never happened, it's using a String
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                // Found the end of a patch and is currently reading.
                if ((line.startsWith("diff --git") || line.startsWith("--")) && reading) {
                    patches.put(oldFilename, currentPatch);
                    currentPatch = "";
                    reading = false;
                }

                if (line.startsWith("diff --git")) {
                    // Starting a new diff, get new and old filename, remove initial "a" or "b"
                    String[] tokens = line.split(" ");
                    newFilename = tokens[tokens.length-1];
                    newFilename = newFilename.substring(2, newFilename.length());
                } else if (line.startsWith("---")) {
                    String[] tokens = line.split(" ");
                    oldFilename = tokens[tokens.length-1];

                    // If a new file, there is no "a"
                    if (oldFilename.startsWith("a")) {
                        oldFilename = oldFilename.substring(2, oldFilename.length());
                    }
                }

                if (reading) {
                    currentPatch = currentPatch + line + "\n";
                }

                // Starting a new patch, should start reading if the filename is the same
                if (line.startsWith("+++") && oldFilename.equals(newFilename)) {
                    reading = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return patches;
    }

    protected DiffResult applyPatch(String filepath, String patch) {
        String original;

        try {
            original = fileManager.readFile(filepath);
        } catch (IOException e) {
            System.out.println("Can't open file: " + filepath);
            return null;
        }

        LinkedList<diff_match_patch.Patch> diffPatchs = diffMatchPatch.patch_fromText(patch);
        String modified = new String(original);

        modified = (String) diffMatchPatch.patch_apply(diffPatchs, modified)[0];
        return new DiffResult(original, modified);
    }
}
