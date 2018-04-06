package main.java.diffparser.parser;

import java.util.List;

public class DiffResultBuilder {
    private String filepath;
    private String original;
    private String modified;
    private List<Integer> modifiedLines;

    public DiffResultBuilder setFilepath(String filepath) {
        this.filepath = filepath;
        return this;
    }

    public DiffResultBuilder setOriginal(String original) {
        this.original = original;
        return this;
    }

    public DiffResultBuilder setModified(String modified) {
        this.modified = modified;
        return this;
    }

    public DiffResultBuilder setModifiedLines(List<Integer> modifiedLines) {
        this.modifiedLines = modifiedLines;
        return this;
    }

    public DiffResult createDiffResult() {
        return new DiffResult(filepath, original, modified, modifiedLines);
    }
}