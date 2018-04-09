package genGraph;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    Map<Integer, Node> nodes;
    int root;

    public Graph(Map<Integer, Node> nodes, int root) {
        this.nodes = nodes;
        this.root = root;
    }

    public static Graph symbv() {
        Map<Integer, Node> nodes = new HashMap<>();

        nodes.put(0, new Node(1, 40));
        nodes.put(1, new Node(2, 3));
        nodes.put(2, new Node(null, null));
        nodes.put(3, new Node(null, null));
        nodes.put(40, new Node(41, 50));
        nodes.put(41, new Node(42, 43));
        nodes.put(42, new Node(null, null));
        nodes.put(43, new Node(null, null));
        nodes.put(50, new Node(null, null));

        return new Graph(nodes, 0);
    }

    public int findNodeWithHeigh(int i) {
        if (i < 0) {
            return -1;
        }

        return _findNodeWithHeight(0, i);
    }

    private int _findNodeWithHeight(int current, int i) {
        if (i <= 0) {
            return current;
        }

        Node cn = nodes.get(current);
        if (cn == null) {
            return -1;
        }

        if (cn.left != null) {
            int leftResult = this._findNodeWithHeight(cn.left, i-1);
            if (leftResult >= 0) {
                return leftResult;
            }
        }

        if (cn.right != null) {
            int rightResult = this._findNodeWithHeight(cn.right, i-1);
            if (rightResult >= 0) {
                return rightResult;
            }
        }

        return -1;
    }
}

class Node {
    public Integer left;
    public Integer right;

    public Node(Integer left, Integer right) {
        this.left = left;
        this.right = right;
    }
}
