package genGraph.symbv;

import genGraph.Graph;

public class genGraph___Graph____findNodeWithHeight {
    public static void main(String args[]) {
        genGraph___Graph____findNodeWithHeight t = new genGraph___Graph____findNodeWithHeight();
        t.run(0,
            0);
    }
    public void run(int current, int i) {
        Graph patched = Graph.symbv();
        Graph original = Graph.symbv();

        int patchedResult = patched._findNodeWithHeight(current,
            i);
        int originalResult = original._findNodeWithHeight___original(current,
            i);

        if (originalResult == patchedResult) {
            throw new Error();
        }
    }
}
