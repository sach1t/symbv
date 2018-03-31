package gov.nasa.jpf.symbv;

import gov.nasa.jpf.jdart.JDart;

public class Executor {
    SymbvConfig sconf;
    JDart jd;

    public Executor(SymbvConfig sconf) {
        this.sconf = sconf;
        jd = new JDart(sconf.getJPFConfig(), false);
    }

    public ExecutionResult run() {
        return new ExecutionResult(jd.run());
    }

}
