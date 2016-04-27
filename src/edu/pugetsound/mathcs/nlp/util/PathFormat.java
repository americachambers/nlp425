package edu.pugetsound.mathcs.nlp.util;

/**
 * Utility class for path conversions to allow the agent to be run from any subdirectory of nlp425
 * @author Chili Johnson
 *
 */
public class PathFormat {
	
	public static final char SEPARATOR = '/';
	
	private static final String BASE_DIR = "nlp425";
	private static final String USER_DIR = System.getProperty("user.dir");
	private static final String ROOT = USER_DIR.substring(0, USER_DIR.indexOf(BASE_DIR) + BASE_DIR.length());
	
	/**
	 * Constructs an absolute path based on a path relative to the nlp425 directory.
	 * @param relativePath A '/' delimited path relative to the nlp425 directory
	 * @return The absolute path
	 */
	public static String absolutePathFromRoot(String relativePath) {
		if(relativePath.charAt(0) != SEPARATOR)
			relativePath = SEPARATOR + relativePath;
		
		return ROOT + relativePath;
	}
	
}
