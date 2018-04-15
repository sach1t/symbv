package pathanalysis;

import soot.Unit;
import soot.toolkits.graph.UnitGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CFGWrapper {
    UnitGraph g;
    private Map<Integer, Boolean> cache;

    public CFGWrapper(UnitGraph g) {
        this.g = g;
        cache = new HashMap<>();
    }

    public boolean promisingPath(int lineNum) {
        if (cache.containsKey(lineNum)) {
            return cache.get(lineNum);
        }

        Unit start = null;
        for (Unit u: g.getBody().getUnits()) {
            if (u.getJavaSourceStartLineNumber() == lineNum) {
                start = u;
            }
        }

        if (start == null) {
            return true;
        }

        Set<Unit> seen = new HashSet<>();
        boolean out = dfs(start, seen);
        cache.put(lineNum, out);
        return out;
    }

    private boolean dfs(Unit s, Set<Unit> visited) {
        visited.add(s);
        if (s.hasTag("symbvTag")) {
            return true;
        }
        for (Unit u: g.getSuccsOf(s)) {
            if (!visited.contains(u)) {
                if (dfs(u, visited)) {
                    return true;
                }
            }
        }
        return false;
    }



}
