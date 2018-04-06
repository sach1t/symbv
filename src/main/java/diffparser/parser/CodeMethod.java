package main.java.diffparser.parser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class CodeMethod {
    String completeName;
    List<String> parameterTypes;

    EnumSet<Modifier> modifiers;
    Optional<BlockStmt> block;

    public CodeMethod(String completeName, List<String> parameterTypes, EnumSet<Modifier> modifiers, Optional<BlockStmt> block) {
        this.completeName = completeName;
        this.parameterTypes = parameterTypes;
        this.modifiers = modifiers;
        this.block = block;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
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
}
