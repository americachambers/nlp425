package edu.pugetsound.mathcs.nlp.util;

/**
 * A simple class to hold the logging debug state. Make sure only to print debug messages
 * if Logger.debug() == true
 * @author Creavesjohnson
 *
 */
public class Logger {
	
	private static final boolean DEBUG = true;
	
	public static boolean debug() {
		return DEBUG;
	}
	
}
