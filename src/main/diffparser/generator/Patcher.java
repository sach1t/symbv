package diffparser.generator;

import diffparser.io.FileManager;
import diffparser.parser.CodeExplorer;
import diffparser.parser.CodeMethod;
import diffparser.parser.DiffApplier;
import diffparser.parser.DiffResult;

import java.util.*;

public class Patcher {
    DiffApplier diffApplier;
    FileManager fileManager;
    String basePackage;

    String jpfFile;
    int numTest;

    public Patcher(FileManager fileManager) {
        this.fileManager = fileManager;
        this.diffApplier = new DiffApplier(fileManager);
        this.basePackage = "";
        this.jpfFile = "simple.jpf";
        this.numTest = 0;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getJpfFile() {
        return jpfFile;
    }

    public void setJpfFile(String jpfFile) {
        this.jpfFile = jpfFile;
    }

    // Apply patch and generate tests.
    public void apply(String patchFilename) {
        Map<String, String> patches = this.diffApplier.splitGitPatchFile(patchFilename);
        List<DiffResult> diffResults = new ArrayList<>();

        // If can't split patch for some reason
        if (patches == null) {
            System.out.println("Can't split patch file provided");
            return;
        }

        // Process all patches on the git patch
        patches.forEach((k, v) -> {
            diffResults.add(this.diffApplier.applyPatch(k, v));
        });

        // Create symbv directory if it doesn't exist
        this.fileManager.createDirectory(TestGenerator.PACKAGE_NAME);

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

                    // Save test file
                    boolean hasSymbvConstructor = modifiedCodeExplorer.hasMethod(
                            modifiedCodeMethod.getPackageName(),
                            modifiedCodeMethod.getClassName(),
                            TestGenerator.SYMBV_CONSTUCTOR,
                            "static",
                            modifiedCodeMethod.getClassName());
                    TestGenerator testGenerator = new TestGenerator(modifiedCodeMethod, hasSymbvConstructor, this.basePackage);
                    String test = testGenerator.generate();
                    this.fileManager.writeFile(TestGenerator.PACKAGE_NAME + "/" + testGenerator.getTestFilename(), test);

                    // Append information to jpf file
                    String extraPackage = this.basePackage.length() == 0 ? testGenerator.getTestClassName()
                            : this.basePackage + "." + "symbv." + testGenerator.getTestClassName();
                    List<String> arguments = new ArrayList<>();
                    modifiedCodeMethod.getParameterTypes().forEach(pp -> {
                        arguments.add(pp.getValue() + ": " + pp.getKey());
                    });

                    String jpfInfo = "\nsymbv.test" + Integer.toString(this.numTest) + ".target = " + extraPackage;
                    jpfInfo += "\nsymbv.test" + Integer.toString(this.numTest) + ".method = run(" + String.join(",", arguments) + ")";

                    // TODO: Have a way to specify the jpf file?
                    this.fileManager.appendToFile(this.jpfFile, jpfInfo);
                    this.numTest++;
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
}
