package pathanalysis;

import java.util.HashMap;

public class PathData {
    private static PathData instance;

    HashMap<String, CFGWrapper> data;
    protected PathData() {
        data = new HashMap<>();
    }

    public static PathData getInstance() {
        if (instance == null) {
            instance = new PathData();
        }
        return instance;
    }

    public void addAnalysis(String clazz, CFGWrapper cfg) {
        instance.data.put(clazz, cfg);
    }

    public CFGWrapper getAnalysis(String clazz) {
        return data.get(clazz);
    }
}
