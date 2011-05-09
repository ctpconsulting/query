package com.ctp.cdi.query.test.util;

import java.io.InputStream;
import java.util.logging.LogManager;

public class Logging {

	public static void reconfigure() {
		try {
			InputStream loggingProperties = Logging.class.getClassLoader().getResourceAsStream("logging.properties");
			LogManager.getLogManager().readConfiguration(loggingProperties);
		} catch (Exception e) {
			throw new RuntimeException("Failed to reconfigure Java Logging.", e);
		}
	}
}
