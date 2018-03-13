package main.java.diffparser.parser;

import main.java.diffparser.io.FileManager;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiffApplier {
    FileManager fileManager;

    public DiffApplier(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    protected Map<String, String> splitGitPatch(String gitPatchText) {
        final Map<String, String> patches = new HashMap<>();

        // Remove initial e-mail or other junk
        gitPatchText = gitPatchText.substring(gitPatchText.indexOf("diff --git"), gitPatchText.length()-1);

        boolean reading = false;
        String currentPatch = "";

        String oldFilename = "";
        String newFilename = "";

        if (gitPatchText.isEmpty()) {
            return patches;
        }

        Scanner scanner = new Scanner(gitPatchText);
        for (String line = scanner.nextLine(); scanner.hasNextLine(); line = scanner.nextLine()) {
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

        return patches;
    }

    private String appendLine(String current, String lineSeparator, String newLine) {
        if (current.isEmpty()) {
            return new String(newLine);
        }
        return current + lineSeparator + newLine;
    }

    private void appendDifferentLineNumber(List<Integer> list, int lineNumber) {
        if (list.size() == 0 || (list.get(list.size() - 1) != lineNumber)) {
            list.add(lineNumber);
        }
    }

    protected DiffResult applyPatch(String filepath, String patch) {
        String original;

        try {
            original = fileManager.readFile(filepath);
        } catch (IOException e) {
            System.out.println("Can't open file: " + filepath);
            return null;
        }

        String lineSeparator = original.contains("\r\n") ? "\r\n" : "\n";
        String[] originalLines = original.split("\\r?\\n");
        Scanner scanner = new Scanner(patch);
        String modifiedFile = "";
        List<Integer> modifiedLines = new ArrayList<>();

        // Caution, header might contain the first line/garbage
        Pattern patchHeader = Pattern.compile("^@@ -(\\d+),?(\\d*) \\+(\\d+),?(\\d*) @@ ?(.*)$");
        int originalCurrentLine = 1;
        int modifiedCurrentLine = 1;

        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            switch (next.charAt(0)) {
                case '@':
                    Matcher h = patchHeader.matcher(next);
                    if (!h.matches()) {
                        System.out.println("Bad header line: " + next);
                        continue;
                    }
                    int originalHunkLine = Integer.parseInt(h.group(1));

                    // Copy all previous lines
                    if ((originalHunkLine-1) > (originalCurrentLine-1)) {
                        for (int i = originalCurrentLine-1; i < (originalHunkLine-1); i++) {
                            modifiedFile = appendLine(modifiedFile, lineSeparator, originalLines[i]);
                        }
                        modifiedCurrentLine = modifiedCurrentLine + originalHunkLine - originalCurrentLine;
                        originalCurrentLine = originalHunkLine;
                    }
                    break;
                case ' ':
                    // Line remains the same
                    modifiedFile = appendLine(modifiedFile, lineSeparator, originalLines[originalCurrentLine-1]);
                    originalCurrentLine++;
                    modifiedCurrentLine++;
                    break;
                case '+':
                    // New Line was added
                    modifiedFile = appendLine(modifiedFile, lineSeparator, next.substring(1));
                    appendDifferentLineNumber(modifiedLines, modifiedCurrentLine);
                    modifiedCurrentLine++;
                    break;
                case '-':
                    // Line was removed, just move to the next
                    originalCurrentLine++;
                    appendDifferentLineNumber(modifiedLines, modifiedCurrentLine);
                    break;
                default:
                    System.out.println("Unknown patch line: " + next);
                    break;
            }
        }

        // Copy the rest of the file, if there is anything remaining
        if ((originalCurrentLine-1) < originalLines.length) {
            for(int i = (originalCurrentLine-1); i < originalLines.length; i++) {
                modifiedFile = appendLine(modifiedFile, lineSeparator, originalLines[i]);
            }

            // last \n is problematic
            if (original.charAt(original.length()-1) == lineSeparator.charAt(lineSeparator.length()-1)) {
                modifiedFile += lineSeparator;
            }
        }

        return new DiffResultBuilder()
                .setOriginal(original)
                .setModified(modifiedFile)
                .setModifiedLines(modifiedLines)
                .createDiffResult();
    }
}
