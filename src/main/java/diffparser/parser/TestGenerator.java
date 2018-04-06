package main.java.diffparser.parser;

import java.util.*;

public class TestGenerator {
    public static final Map<String, String> initialValues = new HashMap<>();
    static {
        initialValues.put("byte", "0");
        initialValues.put("short", "0");
        initialValues.put("int", "0");
        initialValues.put("long", "0");
        initialValues.put("float", "0");
        initialValues.put("double", "0");
        initialValues.put("boolean", "true");
        initialValues.put("char", "'\\u0000'");
    }

    CodeMethod codeMethod;

    String packageName;
    String testClassName;

    static final String PACKAGE_SEPARATOR = "_";
    static final String CLASS_SEPARATOR = "___";
    static final String IDENTATION = "    ";

    // If has a static construct named symbv that should be used instead of the default one.
    boolean symbvConstructor;

    public TestGenerator(CodeMethod codeMethod, boolean symbvConstructor) throws Exception {
        this.codeMethod = codeMethod;
        String[] parts = this.splitCompleteName(codeMethod.getCompleteName());

        this.packageName = parts[0];
        String className = parts[1];
        String methodName = parts[2];
        this.testClassName = this.packageName.replace(".", this.PACKAGE_SEPARATOR) + this.CLASS_SEPARATOR
                + className + this.CLASS_SEPARATOR + methodName;

        this.symbvConstructor = symbvConstructor;
    }

    static String[] splitCompleteName(String completeName) throws Exception {
        String[] parts = new String[3];

        int indexMethod = completeName.lastIndexOf(".");
        int indexClass = completeName.lastIndexOf(".", indexMethod-1);
        int length = completeName.length();

        // Minimum requirements for having something for each field.
        if (indexMethod < 3 || indexMethod > length - 2 ||
                indexClass < 1 || indexClass > length -4) {
            throw new Error("Should receive at least package, class and method names");
        }

        parts[0] = completeName.substring(0, indexClass);
        parts[1] = completeName.substring(indexClass + 1, indexMethod);
        parts[2] = completeName.substring(indexMethod + 1, length);

        return parts;
    }

    private String genPackages() {
        String pkgs = "package symbv;\n\n";
        pkgs += "import " + this.packageName + ";\n";
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

    String runnerArguments() {
        List<String> parameters = new ArrayList<>(this.codeMethod.parameterTypes.size());
        this.codeMethod.getParameterTypes().forEach(arg -> {
            String type = arg.getKey();
            if (TestGenerator.initialValues.containsKey(type)) {
                parameters.add(TestGenerator.initialValues.get(type));
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
        mainFunction += this.indented(2, "t.test(" + this.runnerArguments() + ");");    // Call with right parameters
        mainFunction += this.indented(1, "}");
        return mainFunction;
    }
}
