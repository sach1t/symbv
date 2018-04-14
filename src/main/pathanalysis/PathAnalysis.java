package pathanalysis;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import soot.*;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class PathAnalysis {
    private static final String CLASSPATH = "build/examples";
    private static final String RTDOTJAR = "/lib/jvm/java-8-jdk/jre/lib/rt.jar";

    public HashSet<Integer> run(String clazz, String originalMethodName, String modifiedMethodName) {
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_keep_line_number(true);
        Options.v().set_keep_offset(true);
        Scene s = Scene.v();
        s.setSootClassPath(CLASSPATH + ":" + RTDOTJAR);
        SootClass c = s.loadClassAndSupport(clazz);
        s.loadNecessaryClasses();
        c.setApplicationClass();

        SootMethod modifiedMethod = c.getMethodByName(modifiedMethodName);
        Body modifiedBody = modifiedMethod.retrieveActiveBody();
        SootMethod originalMethod = c.getMethodByName(originalMethodName);
        Body originalBody = originalMethod.retrieveActiveBody();

        Patch patch = compare(originalBody, modifiedBody);

        // --------------- debugging
        System.out.println("--------ORIGINAL-----------");
        Iterator<Unit> ui = originalBody.getUnits().iterator();
        int i = 0;
        while (ui.hasNext()) {
            System.out.println(i + ": " + ui.next());
            i+=1;
        }
        System.out.println("----------MODIFIED---------");
        ui = modifiedBody.getUnits().iterator();
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
        patch.getDeltas().forEach(d -> processDelta(d, originalBody, modifiedBody));
        System.out.println("--------@#@!#@#@!#@!#-----------");

        modifiedBody.getUnits().forEach(u -> {
            System.out.println(u.getJavaSourceStartLineNumber() + "\t" + u + ":" + u.getTags());
        });
        System.out.println("--------!@#!@#!@#!@#-----------");


        UnitGraph modifiedGraph = new BriefUnitGraph(modifiedBody);
        GoodPathsFlow analysis = new GoodPathsFlow(modifiedGraph);
        for (Unit u: modifiedBody.getUnits()) {
            System.out.println(u + "\t\t[" + analysis.getFlowBefore(u) + ", " + analysis.getFlowAfter(u) + "]");
        }
        PropogateInfection analysi2 = new PropogateInfection(modifiedGraph);
        for (Unit u: modifiedBody.getUnits()) {
            System.out.println(u + "\t\t[" + analysi2.getFlowBefore(u) + ", " + analysi2.getFlowAfter(u) + "]");
        }

        HashSet<Integer> prune = new HashSet<>();
        modifiedGraph.forEach(u ->{
            if (u.hasTag("change") || analysis.getFlowAfter(u).contains(1) || analysi2.getFlowAfter(u).contains(1)) {
                // infected
            } else {
                prune.add(u.getJavaSourceStartLineNumber());
            }
        });
        System.out.println("--------!@#!@#!@#!@#-----------");

        prune.forEach(System.out::println);
        System.out.println("--------!@#!@#!@#!@#-----------");


        return prune;
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
}
