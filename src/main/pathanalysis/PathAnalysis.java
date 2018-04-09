package pathanalysis;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import soot.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class PathAnalysis {
    private static final String CLASSPATH = "/home/sachit/jpf/symbv/src/examples";
    private static final String RTDOTJAR = "/lib/jvm/java-8-jdk/jre/lib/rt.jar";

    public void run(String clazz, String originalMethodName, String modifiedMethodName) {
        Scene s = Scene.v();
        s.setSootClassPath(CLASSPATH + ":" + RTDOTJAR);
        SootClass c = s.loadClassAndSupport(clazz);
        s.loadNecessaryClasses();
        c.setApplicationClass();

        SootMethod modifiedMethod = c.getMethodByName(modifiedMethodName);
        Body modified = modifiedMethod.retrieveActiveBody();

        SootMethod originalMethod = c.getMethodByName(originalMethodName);
        Body original = originalMethod.retrieveActiveBody();

        Patch patch = compare(original, modified);

        // --------------- debugging
        System.out.println("--------ORIGINAL-----------");
        Iterator<Unit> ui = original.getUnits().iterator();
        int i = 0;
        while (ui.hasNext()) {
            System.out.println(i + ": " + ui.next());
            i+=1;
        }
        System.out.println("----------MODIFIED---------");
        ui = modified.getUnits().iterator();
        i = 0;
        while (ui.hasNext()) {
            System.out.println(i + ": " + ui.next());
            i+=1;
        }
        System.out.println("--------DIFFERENCES-----------");
        patch.getDeltas().forEach(d -> {
            System.out.println(d.getType());
            System.out.println(d.getOriginal());
            System.out.println(d.getRevised());
        });
        // --------------- end debugging

        // update the body tags
        patch.getDeltas().forEach(d -> processDelta(d, original, modified));

        System.out.println("--------MODIFIED CFG-----------");
        UnitGraph modifiedGraph = new BriefUnitGraph(modified);
        System.out.println(modifiedGraph);

        System.out.println("--------MODIFIED CODE-----------");
        for (Unit u : modifiedGraph) {
            System.out.println(u.getTags());
        }

        System.out.println("--------MODIFIED CFG: GOOD PATHS-----------");
        GoodPathsFlow analysis = new GoodPathsFlow(modifiedGraph);
        for (Unit u: modifiedGraph) {
            System.out.println(u + "\t\t[" + analysis.getFlowBefore(u) + ", " + analysis.getFlowBefore(u) + "]");
        }

//        UnitGraph originalGraph = new BriefUnitGraph(original);
//        System.out.println(originalGraph);
//        for (Unit u : originalGraph) {
//            System.out.println(u.getTags());
//        }
    }

    public void processDelta(Delta d, Body original, Body modified) {
        switch (d.getType()) {
            case CHANGE:
                List<UnitWrapper> changedUnits = (List<UnitWrapper>) d.getRevised().getLines();
                changedUnits.forEach(u -> {
                    u.unwrap().addTag(new ChangeTag("changed"));
                });
                break;
            case INSERT:
                List<UnitWrapper> insertedUnits = (List<UnitWrapper>) d.getRevised().getLines();
                insertedUnits.forEach(u -> {
                    u.unwrap().addTag(new ChangeTag("inserted"));
                });
                break;
            case DELETE:
                List<UnitWrapper> deletedUnits = (List<UnitWrapper>) d.getOriginal().getLines();
                deletedUnits.forEach(u -> {
                    u.unwrap().addTag(new ChangeTag("deleted"));
                });
                break;
        }
    }

    public Patch compare(Body original, Body modified) {
        List<UnitWrapper> uModified = modified.getUnits().stream()
                .map(UnitWrapper::new)
                .collect(Collectors.toList());

        List<UnitWrapper> uOriginal = original.getUnits().stream()
                .map(UnitWrapper::new)
                .collect(Collectors.toList());

        Patch p = DiffUtils.diff(uOriginal, uModified);
        p.getDeltas().forEach(d -> {
            System.out.println(d.getType());
            System.out.println(d.getOriginal());
            System.out.println(d.getRevised());
        });
        return p;
    }

    private void compareExample() {
        List<String> a = new ArrayList<>();
        a.add("A");
        a.add("B");
        a.add("C");
        a.add("D");
        List<String> b = new ArrayList<>();
        b.add("A");
        b.add("E");
        b.add("C");
        b.add("D");
        b.add("E");
        Patch p = DiffUtils.diff(a, b);
        p.getDeltas().forEach(d -> {
            System.out.println(d.getType());
            System.out.println(d.getOriginal());
            System.out.println(d.getRevised());
        });
    }
}
