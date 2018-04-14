package pathanalysis;

import java.util.HashMap;
import java.util.HashSet;

public class PathData {
    private static PathData instance;

    HashMap<String, HashSet<Integer>> data;
    protected PathData() {
        data = new HashMap<>();
    }

    public static PathData getInstance() {
        if (instance == null) {
            instance = new PathData();
        }
        return instance;
    }

    public void addAnalysis(String clazz, HashSet<Integer> pathInfo) {
        instance.data.put(clazz, pathInfo);
    }

    public HashSet<Integer> getAnalysis(String clazz) {
        return data.get(clazz);
    }
}
