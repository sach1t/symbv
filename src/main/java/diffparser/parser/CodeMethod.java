package main.java.diffparser.parser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.EnumSet;
import java.util.Optional;

public class CodeMethod {
    EnumSet<Modifier> modifiers;
    Optional<BlockStmt> block;

    public CodeMethod(EnumSet<Modifier> modifiers, Optional<BlockStmt> block) {
        this.modifiers = modifiers;
        this.block = block;
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
