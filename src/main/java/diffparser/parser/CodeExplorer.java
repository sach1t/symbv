package main.java.diffparser.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeExplorer {
    String code;

    public CodeExplorer(String code) {
        this.code = code;
    }

    private Map<String, LineInterval> findAllMethods() {
        HashMap<String, LineInterval> hashMap = new HashMap<>();

        CompilationUnit cu = JavaParser.parse(code);
        cu.getTypes().forEach(type ->
            type.getMethods().forEach(method ->
                hashMap.put(method.getName().asString(), LineInterval.convert(method))));

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
}
