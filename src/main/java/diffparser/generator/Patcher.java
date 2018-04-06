package main.java.diffparser.generator;

import main.java.diffparser.io.FileManager;
import main.java.diffparser.parser.CodeExplorer;
import main.java.diffparser.parser.CodeMethod;
import main.java.diffparser.parser.DiffApplier;
import main.java.diffparser.parser.DiffResult;

import java.util.*;

public class Patcher {
    DiffApplier diffApplier;
    FileManager fileManager;

    public Patcher(FileManager fileManager) {
        this.fileManager = fileManager;
        this.diffApplier = new DiffApplier(fileManager);
    }

    // Apply patch and generate tests.
    public void apply(String patchFilename) {
        Map<String, String> patches = this.diffApplier.splitGitPatchFile(patchFilename);
        List<DiffResult> diffResults = new ArrayList<>();

        // Process all patches on the git patch
        patches.forEach((k, v) -> {
            diffResults.add(this.diffApplier.applyPatch(k, v));
        });

        // Only create tests after checking every change, to avoid conflicts.
        List<TestInfo> testInfos = new ArrayList<>();

        // Explore each diff result
        diffResults.forEach(diffResult -> {
            this.createClassTests(diffResult);
        });
    }

    private void createClassTests(DiffResult diffResult) {
        try {
            CodeExplorer originalCodeExplorer = new CodeExplorer(diffResult.getOriginal());
            CodeExplorer modifiedCodeExplorer = new CodeExplorer(diffResult.getModified());

            // Check which methods were modified. A method can be modified
            // by more than one change, don't generate repeated tests.
            List<String> modifiedMethods = modifiedCodeExplorer.findModifiedMethods(diffResult.getModifiedLines());

            Map<String, Boolean> methodsWithTest = new HashMap<>();
            for (Iterator<String> iter = modifiedMethods.iterator(); iter.hasNext();) {
                String method = iter.next();
                if (!methodsWithTest.containsKey(method)) {
                    methodsWithTest.put(method, true);

                    CodeMethod modifiedCodeMethod = modifiedCodeExplorer.findCodeMethod(method);
                    CodeMethod originalCodeMethod = originalCodeExplorer.findCodeMethod(method);
                    if (!modifiedCodeMethod.hasSameParameters(originalCodeMethod)) {
                        throw new Exception("Signature has changed");
                    }

                    // Add the original method to the code.
                    modifiedCodeExplorer.includeMethod(modifiedCodeMethod.getCompleteOriginalName(), originalCodeMethod);

                    // TODO: Check for symbv constructor
                    TestGenerator testGenerator = new TestGenerator(modifiedCodeMethod, false);
                    String test = testGenerator.generate();
                    this.fileManager.writeFile(testGenerator.getTestFilename(), test);
                }
            }

            // Finally, save the new class, with patch and original functions added
            this.fileManager.writeFile(diffResult.getFilepath(), modifiedCodeExplorer.currentCode());

        } catch (Exception e) {
            System.out.println("Can't find generate test for " + diffResult.getFilepath() + ", stack: ");
            e.printStackTrace();
            return;
        }
    }

    private class TestInfo {
        CodeExplorer originalCodeExplorer;
        CodeExplorer modifiedCodeExplorer;
        String method;

        public TestInfo(CodeExplorer originalCodeExplorer, CodeExplorer modifiedCodeExplorer, String method) {
            this.originalCodeExplorer = originalCodeExplorer;
            this.modifiedCodeExplorer = modifiedCodeExplorer;
            this.method = method;
        }
    }
}
