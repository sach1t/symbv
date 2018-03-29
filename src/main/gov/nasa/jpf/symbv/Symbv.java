package gov.nasa.jpf.symbv;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFShell;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.LogManager;


public class Symbv implements JPFShell {
	private Config config;
	private JPFLogger logger;

	public Symbv(Config conf) {
		this.config = conf;
		LogManager.init(conf);
		logger = JPF.getLogger("symbv");
	}

	@Override
	public void start(String[] args) {
		run();
	}

	public void run() {
		System.out.println("It Works!");
	}
}
