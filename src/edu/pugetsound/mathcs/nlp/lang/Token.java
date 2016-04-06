package edu.pugetsound.mathcs.nlp.lang;

/**
 * Stores information about a single word token in an utterance. Tokens are 
 * assumed to be delimited by a space although this is not enforced.
 * 
 * Using a space as a delimiter, the utterance "Hi, how are you doing?" 
 * contains 5 tokens:
 * 
 * - Hi  (begins at char position 0 and ends at char position 1)
 * - how (begins at char position 4 and ends at char position 6)
 * - are (begins at char position 8 and ends at char position 10)
 * - you (begins at char position 12 and ends at char position 14)
 * - doing (begins at char position 16 and ends at char position 20)
 * 
 * Punctuation symbols are not considered tokens
 * 
 * Note: This class is a plain old data structure 
 * 
 * @author alchambers
 */
public class Token {
	
	/**
	 * The part-of-speech (or null)
	 */
	public String pos;
	
	/**
	 * The token itself
	 */
	public String token;	
	
	/**
	 * The beginning character position of the token within the utterance
	 */
	public int beginPosition;
	
	/**
	 * The end character position of the token within the utterance
	 */
	public int endPosition;
	
	/**
	 * Named entity tag (or null)
	 */
	public String entityTag;
	
	
	/**
	 * Constructs a new token
	 * @param word a string representing the token
	 */
	public Token(String word){
		token = word;
	}
	
	/**
	 * Returns a string representation of the token
	 * @return a string representing the token 
	 */
	public String toString(){
		return "[token=" + token + ", pos=" + pos + ", begin=" + beginPosition 
				+ ", end=" + endPosition + ", entity=" + entityTag + "]";
	}	
}
