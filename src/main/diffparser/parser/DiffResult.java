package diffparser.parser;

import java.util.List;

public class DiffResult {
    private String original;
    private String modified;
    private List<Integer> modifiedLines;

    public DiffResult(String original, String modified, List<Integer> modifiedLines) {
        this.original = original;
        this.modified = modified;
        this.modifiedLines = modifiedLines;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<Integer> getModifiedLines() {
        return modifiedLines;
    }

    public void setModifiedLines(List<Integer> modifiedLines) {
        this.modifiedLines = modifiedLines;
    }
}
