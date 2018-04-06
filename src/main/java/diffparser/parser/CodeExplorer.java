package main.java.diffparser.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;

public class CodeExplorer {
    final static String CLASS_MODIFIER = "___modified";
    CompilationUnit cu;

    public CodeExplorer(String code) {
        this.cu = JavaParser.parse(code);
    }

    private Map<String, LineInterval> findAllMethods() {
        HashMap<String, LineInterval> hashMap = new HashMap<>();

        String packageName = this.cu.getPackageDeclaration().isPresent() ?
                this.cu.getPackageDeclaration().get().getNameAsString() : null;

        this.cu.getTypes().forEach(type -> {
            String functionName = type.getNameAsString();

            type.getMethods().forEach(method -> {
                String prefix = functionName;
                if (packageName != null) {
                    prefix = packageName + "." + prefix;
                }
                hashMap.put(prefix + "." + method.getName().asString(), LineInterval.convert(method));
            });
        });

        return hashMap;
    }

    public List<String> findModifiedMethods(List<Integer> modifiedLines) {
        List<String> modifiedMethods = new ArrayList<>();
        Map<String, LineInterval> allMethods = findAllMethods();

        modifiedLines.forEach(line ->
                allMethods.entrySet().forEach(entry -> {
                    LineInterval interval = entry.getValue();
                    if (line >= interval.begin && line <= interval.end) {
                        modifiedMethods.add(entry.getKey());
                    }
                }));

        return modifiedMethods;
    }

    private TypeDeclaration findClass(String className) throws Exception {
        for (int i = 0; i < this.cu.getTypes().size(); i++) {
            TypeDeclaration type = this.cu.getTypes().get(i);
            if (type.getNameAsString().equals(className)) {
                return type;
            }
        }

        throw new Exception("Can't find class: " + className);
    }

    private String[] splitCompleteName(String completeName) throws Exception {
        String[] parts = completeName.split("\\.");
        if (parts.length < 2) {
            throw new Error("Should receive at least class and method names");
        }
        return parts;
    }

    public void includeMethod(String completeName, CodeMethod codeMethod) throws Exception {
        String[] parts = this.splitCompleteName(completeName);

        String className = parts[parts.length-2];
        String methodName = parts[parts.length-1];

        TypeDeclaration type = this.findClass(className);
        EnumSet<Modifier> modifiers = codeMethod.getModifiers();
        MethodDeclaration methodDeclaration = type.addMethod(methodName, modifiers.toArray(new Modifier[modifiers.size()]));

        if (codeMethod.getBlock().isPresent()) {
            methodDeclaration.setBody(codeMethod.getBlock().get());
        }
    }

    public CodeMethod findCodeMethod(String completeName) throws Exception {
        String [] parts = this.splitCompleteName(completeName);

        String className = parts[parts.length-2];
        String methodName = parts[parts.length-1];

        TypeDeclaration type = this.findClass(className);

        List<MethodDeclaration> declarations = type.getMethodsByName(methodName);
        if (declarations.size() == 0) {
            throw new Error("Can't find method: " + methodName + " on class: " + className);
        }

        // TODO: Deal with overloads
        MethodDeclaration methodDeclaration = declarations.get(0);
        List<String> parameterTypes = new ArrayList<>();

        methodDeclaration.getParameters().forEach(parameter -> {
            parameterTypes.add(parameter.getType().asString());
        });

        return new CodeMethod(completeName, parameterTypes, methodDeclaration.getModifiers(), methodDeclaration.getBody());
    }

    public void replaceClassNames() {
        this.cu.getTypes().forEach(type -> {
            SimpleName simpleName = new SimpleName(type.getNameAsString() + this.CLASS_MODIFIER);
            type.setName(simpleName);
        });
    }

    public void makeFieldsPublic() {
        this.cu.getTypes().forEach(type -> {
            type.getFields().forEach(field -> {
                Optional<FieldDeclaration> fieldDeclaration = field.toFieldDeclaration();
                if (fieldDeclaration.isPresent()) {
                    FieldDeclaration declaration = fieldDeclaration.get();
                    declaration.setPrivate(false);
                    declaration.setProtected(false);
                    declaration.setPublic(true);
                }
            });
        });
    }

    public String currentCode() {
        return cu.toString();
    }
}
