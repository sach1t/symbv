package main.java.diffparser.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;

public class CodeExplorer {
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

    public void replaceClassNames() {
        this.cu.getTypes().forEach(type -> {
            SimpleName simpleName = new SimpleName(type.getNameAsString() + "-modified");
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
