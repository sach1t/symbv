package pathanalysis;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.JPFLogger;
import soot.*;
import soot.options.Options;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PathAnalysis {
    private static final String CLASSPATH = "build/examples";
    private static final String RTDOTJAR = "/lib/jvm/java-8-jdk/jre/lib/rt.jar";
    private JPFLogger logger = JPF.getLogger("symbv");

    public CFGWrapper run(String clazz, String originalMethodName, String modifiedMethodName) {
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_keep_line_number(true);
        Options.v().set_keep_offset(true);
        Scene s = Scene.v();
        s.setSootClassPath(CLASSPATH + ":" + RTDOTJAR);
        SootClass c = s.loadClassAndSupport(clazz);
        s.loadNecessaryClasses();
        c.setApplicationClass();

        // get methods out of class
        SootMethod modifiedMethod = c.getMethodByName(modifiedMethodName);
        Body modifiedBody = modifiedMethod.retrieveActiveBody();
        SootMethod originalMethod = c.getMethodByName(originalMethodName);
        Body originalBody = originalMethod.retrieveActiveBody();

        // compare original and modified methods on IR representation
        Patch patch = compare(originalBody, modifiedBody);
        logger.finest("Code Changes:");
        patch.getDeltas().forEach(d -> {
            logger.finest(d.getType());
            logger.finest(d.getOriginal());
            logger.finest(d.getRevised());
        });

        // mark changes in the original and modified code
        patch.getDeltas().forEach(d -> processDelta(d, originalBody, modifiedBody));

        // create mapping for unchanged lines from original to modified code
        Map<Unit, Unit> originalToModifiedMap = calculateUnitMap(modifiedBody, originalBody);
        logger.finest("Original To Modified Code Map:");
        originalToModifiedMap.forEach((k,v) -> {
            logger.finest(k + "\t ===== \t" + v);
        });

        logger.finest("Modified code before CF analysis:");
        modifiedBody.getUnits().forEach(u -> {
            logger.finest(u.getJavaSourceStartLineNumber() + "\t" + u + ":" + u.getTags());
        });

        // perform data and control dependency analysis on modified code, for changed and added lines
        UnitGraph modifiedGraph = new BriefUnitGraph(modifiedBody);
        DataInfectionAnalysis mda = new DataInfectionAnalysis(modifiedGraph);
        BlockGraph modifiedBlockGraph = new BriefBlockGraph(modifiedBody);
        ControlInfectionAnalysis mca = new ControlInfectionAnalysis(modifiedBlockGraph);
        logger.finest("Modified code after CF analysis:");
        for (Unit u: modifiedBody.getUnits()) {
            logger.finest(Integer.toString(u.getJavaSourceStartLineNumber()) + ":" + u + "\t\t[" +  u.getTags() + "]");
        }

        // perform data and control dependency analysis on original code, for deleted lines
        UnitGraph originalGraph = new BriefUnitGraph(originalBody);
        DataInfectionAnalysis oda = new DataInfectionAnalysis(originalGraph);
        BlockGraph originalBlockGraph = new BriefBlockGraph(originalBody);
        ControlInfectionAnalysis oca = new ControlInfectionAnalysis(originalBlockGraph);
        logger.finest("Original code after CF analysis:");
        for (Unit u: originalBody.getUnits()) {
            logger.finest(Integer.toString(u.getJavaSourceStartLineNumber()) + ":" + u + "\t\t[" +  u.getTags() + "]");
        }

        // bring over affected lines from control flow analysis on original code to the modified code
        for (Unit u: originalBody.getUnits()) {
            if (u.hasTag("symbvTag") && originalToModifiedMap.containsKey(u)) {
                originalToModifiedMap.get(u).addTag(new ChangeTag("deleteEffect"));
            }
        }
        logger.finest("Modified code after importing changes:");
        for (Unit u: modifiedBody.getUnits()) {
            logger.finest(Integer.toString(u.getJavaSourceStartLineNumber()) + ":" + u + "\t\t[" +  u.getTags() + "]");
        }

        return new CFGWrapper(modifiedGraph);
    }


    private Map<Unit, Unit> calculateUnitMap(Body modified, Body original) {
        Map<Unit, Unit> unitMap = new HashMap<>();

        Iterator<Unit> modIter = modified.getUnits().iterator();
        Iterator<Unit> oriIter = original.getUnits().iterator();

        Unit mod = modIter.next();
        Unit ori = oriIter.next();

        while (modIter.hasNext() && oriIter.hasNext()) {
            boolean skipped = false;

            if (mod.hasTag("symbvTag")) {
                String value = new String(mod.getTag("symbvTag").getValue());
                if (value.equals("inserted")) {
                    mod = modIter.next();
                    skipped = true;
                }
            }

            if (ori.hasTag("symbvTag")) {
                ChangeTag ct = (ChangeTag) ori.getTag("symbvTag");
                if (ct.type.equals("deleted")) {
                    ori = oriIter.next();
                    skipped = true;
                }
            }

            if (!skipped) {
                unitMap.put(ori, mod);
                ori = oriIter.next();
                mod = modIter.next();
            }
        }

        return unitMap;
    }

    private void processDelta(Delta d, Body original, Body modified) {
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

    private Patch compare(Body original, Body modified) {
        List<UnitWrapper> uModified = modified.getUnits().stream()
                .map(UnitWrapper::new)
                .collect(Collectors.toList());

        List<UnitWrapper> uOriginal = original.getUnits().stream()
                .map(UnitWrapper::new)
                .collect(Collectors.toList());

        return DiffUtils.diff(uOriginal, uModified);
    }
}
