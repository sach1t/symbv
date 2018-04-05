package main.java.diffparser.parser;

public class TestGenerator {
    String packageName;
    String className;
    String methodName;

    public TestGenerator(String completeName) throws Exception {
        String[] parts = this.splitCompleteClassName(completeName);

        this.packageName = parts[0];
        this.className = parts[1];
        this.methodName = parts[2];
    }

    private String[] splitCompleteClassName(String completeName) throws Exception {
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
}
