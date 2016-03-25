package edu.pugetsound.mathcs.nlp.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


/**
 * This class aggregates all features computed from a single utterance
 * (uttered by the human or agent). This includes all syntactic, semantic, and
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
	 * The root of the dependency parse tree (or null)
	 * Since the nodes of a dependency parse tree are the words in the
	 * utterance, the root is a word.
	 */
	public String rootDependency;
	
	/**
	 * The root of the constituency parse tree (or null)
	 * Since the nodes of a constituency parse tree are syntax tags (e.g. NP),
	 * the root is a Penn Treebank tag. 
	 */
	public String rootConstituency;
	
	/**
	 * A mapping from a phrase in the utterance to the Prolog entity ID  
	 * For example, if the user types "My cat's name is Fluffy" then we would store
	 * 
	 * "my" ---> entityHuman
	 * "my cat" ---> cat001
	 * 
	 */
	public HashMap<String, String> resolutions = new HashMap<String, String>();
	
	
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
		str += "Root Dep: " + rootDependency + "\n";
		str += "Root Con: " + rootConstituency + "\n";
		str += "Passive:" 	+ isPassive  + "\n";
		return str;
	}	
}
