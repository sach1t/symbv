package main.java.diffparser.generator;

import main.java.diffparser.io.FileManager;
import main.java.diffparser.parser.CodeExplorer;
import main.java.diffparser.parser.CodeMethod;
import main.java.diffparser.parser.DiffApplier;
import main.java.diffparser.parser.DiffResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Patcher {
    DiffApplier diffApplier;

    public Patcher(FileManager fileManager) {
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
        Map<String, Boolean> methodsWithTest = new HashMap<>();

        // Explore each diff result
        diffResults.forEach(diffResult -> {
            CodeExplorer originalCodeExplorer = new CodeExplorer(diffResult.getOriginal());
            CodeExplorer modifiedCodeExplorer = new CodeExplorer(diffResult.getModified());

            // Check which methods were modified. A method can be modified
            // by more than one change, don't generate repeated tests.
            List<String> modifiedMethods = modifiedCodeExplorer.findModifiedMethods(diffResult.getModifiedLines());
            modifiedMethods.forEach(method -> {
                if (!methodsWithTest.containsKey(method)) {
                    methodsWithTest.put(method, true);
                    testInfos.add(new TestInfo(originalCodeExplorer, modifiedCodeExplorer, method));
                }
            });
        });

        // Finally create the tests
        testInfos.forEach(ti -> {
            this.createTest(ti);
        });
    }

    private void createTest(TestInfo t) {
        try {
            // TODO: Check for signature differences
            CodeMethod modifiedCodeMethod = t.modifiedCodeExplorer.findCodeMethod(t.method);
            CodeMethod originalCodeMethod = t.originalCodeExplorer.findCodeMethod(t.method);

            if (!modifiedCodeMethod.hasSameParameters(originalCodeMethod)) {
                throw new Error("Signature has changed");
            }

            TestGenerator testGenerator = new TestGenerator(modifiedCodeMethod, false);
            String test = testGenerator.generate();
            System.out.println(test);
        } catch (Exception e) {
            System.out.println("Can't find generate test for function " + t.method + ", stack: ");
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
