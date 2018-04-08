package genContains;

public class Contains {
    public boolean contains(String text, String query) {
        // Count the size of a split. Dumb approach.
        return text.split(query, -1).length > 2;
    }

    public boolean containsAny(String text, String query1, String query2) {
        // Another dumb mistake. Any should be "OR", but it's "AND" here.
        return contains(text, query1) &&
                contains(text, query2);
    }
}
