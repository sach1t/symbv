package main.java.diffparser.parser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.stmt.BlockStmt;
import javafx.util.Pair;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class CodeMethod {
    String completeName;

    List<Pair<String, String>> parameterTypes;
    String returnType;

    EnumSet<Modifier> modifiers;
    Optional<BlockStmt> block;

    String originalName;

    String packageName;
    String className;
    String methodName;


    public CodeMethod(String completeName, List<Pair<String, String>> parameterTypes, String returnType,
                      EnumSet<Modifier> modifiers, Optional<BlockStmt> block) throws Exception {

        this.completeName = completeName;
        this.originalName = null;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.modifiers = modifiers;
        this.block = block;

        String[] parts = this.splitCompleteName(completeName);
        this.packageName = parts[0];
        this.className = parts[1];
        this.methodName = parts[2];
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

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public List<Pair<String, String>> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Pair<String, String>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public EnumSet<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(EnumSet<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public Optional<BlockStmt> getBlock() {
        return block;
    }

    public void setBlock(Optional<BlockStmt> block) {
        this.block = block;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

        public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
