package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A map from token strings to a unique index
 * The index associated with the token string is used to maintain
 * correct input vector construction between training and classification
 * @author Creavesjohnson
 *
 */
class TokenIndexMap {
	
	private static final String ENCODING = "UTF-8";

	private final Map<String,Integer> tokenToIndex;
	private final int numTokens;
	
	/**
	 * Constructs a TokenIndexMap from an existing Map<String,Integer>
	 * @param tokenToIndex
	 */
	public TokenIndexMap(Map<String,Integer> tokenToIndex) {
		this.tokenToIndex = tokenToIndex;
		this.numTokens = this.tokenToIndex.size();
	}
	
	/**
	 * Reads a TokenIndexMap from a file.
	 * @param tokenFile The file path to the saved TokenIndexMap file
	 * @throws FileNotFoundException if the file cannot be opened
	 */
	public TokenIndexMap(File tokenFile) throws FileNotFoundException {
		this.tokenToIndex = new HashMap<String,Integer>();
		
		Scanner input = new Scanner(tokenFile);
		
		int index = 0;
		while(input.hasNextLine())
			this.tokenToIndex.put(input.nextLine(), index++);
		
		this.numTokens = index;
		
		input.close();
		
	}
	
	/**
	 * Get the index associated with a given token String
	 * @param token A token String
	 * @return The index associated with the token, -1 if the token has not been given an index
	 */
	public int indexForToken(String token) {
		return this.tokenToIndex.containsKey(token) ? this.tokenToIndex.get(token) : -1;
	}
	
	/**
	 * Get the number of stored token-index pairs
	 * @return The number of stored token-index pairs
	 */
	public int size() {
		return this.numTokens;
	}
	
	/**
	 * Saves this object to a file to be read in later
	 * @param filename The file in which to save this object
	 * @throws FileNotFoundException if the file provided cannot be opened.
	 */
	public void saveToFile(String filename) throws FileNotFoundException {
		try {
			PrintWriter output = new PrintWriter(filename, ENCODING);
			for(String token : tokenToIndex.keySet())
				output.println(token);
			output.close();
			
		} catch(UnsupportedEncodingException e) {
			System.err.println("[DATAG: TokenIndexMap] Encoding \"" + ENCODING + "\" not supported.");
		}
	}
	
}
