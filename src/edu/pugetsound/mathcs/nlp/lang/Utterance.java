package edu.pugetsound.mathcs.nlp.lang;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


/**
 * This class aggregates all features computed from a single utterance
 * (by the human or agent). This includes all syntactic, semantic, and
 * pragmatic features.
 * 
 * Note: This class is a plain old data structure 
 * 
 * @author alchambers
 *
 */
public class Utterance {	
	
	/**
	 * String representing the actual utterance itself (or null)
	 */
	public String utterance;
	
	/**
	 * A list of tokens in the utterance. Tokens are delimited by spaces.
	 */
	public List<Token> tokens = new ArrayList<Token>();
		
	/**
	 * The ending punctuation mark (or null)
	 */
	public Punctuation punct;

	/**
	 * The constituency parse tree (or null)
	 */
	public Tree constituencyParse;
	
	/**
	 * The dependency parse tree (or null) 
	 */
	public SemanticGraph dependencyParse;

	/**
	 * A list of the grammatical subjects in the utterance
	 */
	public List<String> subjects = new ArrayList<String>();
	
	/**
	 * A list of the grammatical direct objects in the utterance 
	 */
	public List<String> directObjects = new ArrayList<String>();	
	
	/**
	 * The root of the dependency parase tree (or null)
	 */
	public String rootDependency;
	
	/**
	 * Indicates whether the sentence is passive (true) or active (false)
	 */
	public boolean isPassive;

	/**
	 * Constructs a new utterance 
	 * @param utterance the typed input
	 */
	public Utterance(String utt){
		utterance = utt;							
	}
	
	/**
	 * Returns a string representation of all features
	 * @return string representation of the utterance 
	 */
	public String toString(){
		String str = "";
		str += "Utterance: " + utterance + "\n";
		str += "Tokens: " 	+ tokens + "\n";
		str += "Punct: " 	+ punct + "\n";
		str += "Parse: " 	+ constituencyParse + "\n";
		str += "Parse: " 	+ dependencyParse + "\n";
		str += "Subj: " 	+ subjects + "\n";
		str += "Dobj: " 	+ directObjects + "\n";
		str += "Root: " 	+ rootDependency + "\n";		
		str += "Passive:" 	+ isPassive  + "\n";
		return str;
	}	
}
