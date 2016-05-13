package edu.pugetsound.mathcs.nlp.util;

/**
 * A simple class to hold the logging debug state. Make sure only to print debug messages
 * if Logger.debug() == true
 * @author Creavesjohnson
 *
 */
public class Logger {
	
	private static final boolean DEBUG = false;
	
	/**
	 * Get the debug state for printing
	 * If TRUE, print debug messages
	 * @return Whether or not to print debug messages
	 */
	public static boolean debug() {
		return DEBUG;
	}
	
}
