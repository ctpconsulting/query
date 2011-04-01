package com.ctp.cdi.query.test.util;

import java.io.IOException;
import java.util.logging.LogManager;

public class Logging {

    public static void reconfigure() {
	try {
	    LogManager.getLogManager().readConfiguration(
		    Logging.class.getClassLoader().getResourceAsStream("logging.properties"));
	} catch (IOException e) {
	    e.printStackTrace();
	    System.err.println("Failed to reconfigure Java Logging.");
	}
    }
}
