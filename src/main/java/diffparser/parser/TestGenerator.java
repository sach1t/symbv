package main.java.diffparser.parser;

import java.util.*;

public class TestGenerator {
    public static final Map<String, String> basicTypesValues = new HashMap<>();
    static {
        basicTypesValues.put("byte", "0");
        basicTypesValues.put("short", "0");
        basicTypesValues.put("int", "0");
        basicTypesValues.put("long", "0");
        basicTypesValues.put("float", "0");
        basicTypesValues.put("double", "0");
        basicTypesValues.put("boolean", "true");
        basicTypesValues.put("char", "'\\u0000'");
    }

    CodeMethod codeMethod;
    String testClassName;

    static final String PACKAGE_SEPARATOR = "_";
    static final String CLASS_SEPARATOR = "___";
    static final String IDENTATION = "    ";

    // If has a static construct named symbv that should be used instead of the default one.
    boolean symbvConstructor;

    public TestGenerator(CodeMethod codeMethod, boolean symbvConstructor) throws Exception {
        this.codeMethod = codeMethod;
        this.testClassName = codeMethod.getPackageName().replace(".", this.PACKAGE_SEPARATOR) + this.CLASS_SEPARATOR
                + codeMethod.getClassName() + this.CLASS_SEPARATOR + codeMethod.getMethodName();

        this.symbvConstructor = symbvConstructor;
    }

    private String genPackages() {
        String pkgs = "package symbv;\n\n";
        pkgs += "import " + this.codeMethod.getPackageName() + ";\n";
        return pkgs;
    }

    private String indented(int tabs, String txt) {
        return tabSpaces(tabs) + txt + "\n";
    }

    private String tabSpaces(int tabs) {
        return String.join("", Collections.nCopies(tabs, this.IDENTATION));
    }

    String genRunner() {
        String runner = "public Class " + testClassName + " {";
        runner += this.genMainFunction();

        // TODO: Generate test function
        return runner;
    }

    String runnerCallArguments() {
        List<String> parameters = new ArrayList<>(this.codeMethod.parameterTypes.size());
        this.codeMethod.getParameterTypes().forEach(arg -> {
            String type = arg.getKey();
            if (TestGenerator.basicTypesValues.containsKey(type)) {
                parameters.add(TestGenerator.basicTypesValues.get(type));
            } else {
                // If don't know how to initialize it, just construct an object.
                parameters.add("new " + type + "()");
            }
        });
        return String.join(",\n" + tabSpaces(3), parameters);
    }

    String genMainFunction() {
        String mainFunction = "";
        mainFunction += this.indented(1, "public static void main(String args[]) {");
        mainFunction += this.indented(2, this.testClassName + " t = new " + this.testClassName + "();");
        mainFunction += this.indented(2, "t.run(" + this.runnerCallArguments() + ");");    // Call with right parameters
        mainFunction += this.indented(1, "}");
        return mainFunction;
    }

    String runnerSignatureArguments() {
        List<String> parameters = new ArrayList<>(this.codeMethod.parameterTypes.size());
        this.codeMethod.getParameterTypes().forEach(arg -> {
            parameters.add(arg.getKey() + " " + arg.getKey());
        });
        return String.join(", ", parameters);
    }

    String getConstructor() {
        if (this.symbvConstructor) {
            return this.testClassName + ".symbv()";
        } else {
            return "new " + this.testClassName + "()";
        }
    }

    String genRunFunction() {
        String runFunction = "";
        runFunction += this.indented(1, "public void run(" + this.runnerSignatureArguments() + ") {");
        runFunction += this.indented(2, this.testClassName + " original = " + this.getConstructor() + ";");
        runFunction += this.indented(2, this.testClassName + " patched = " + this.getConstructor() + ";");
        runFunction += "\n";
        runFunction += this.indented(2, this.codeMethod.getReturnType() + " originalResult = original." + this.codeMethod.getOriginalName());
        //runFunction += this.indented(2, this.codeMethod.getReturnType() + " patchedResult = patched." + this.);

        return  runFunction;
    }
}
