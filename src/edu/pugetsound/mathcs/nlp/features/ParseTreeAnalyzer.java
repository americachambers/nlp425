package edu.pugetsound.mathcs.nlp.features;

/**
 * A class with methods for analyzing the String structure of the constituency parse tree
 * @author alchambers
 *
 */
public class ParseTreeAnalyzer {
	private final int NUM_TAGS = 70;

	/**
	 * Unfortunately, there is no way to get the words at the leaves of a node's subtree in the
	 * parse tree. For example, a "NP" node in the tree might correspond to the phrase "the cat".
	 * Given the "NP" node, there is no way to get this phrase. Thus, I'm taking a really ugly 
	 * String replace approach...
	 */
	private String[] patterns = new String[NUM_TAGS];

	/**
	 * Constructs a new analyzer for constituency parse trees
	 */
	public ParseTreeAnalyzer(){
		populatePatterns();
	}

	/**
	 * Takes the string representation of a node in the parse tree and strips away all Penn
	 * Treebank tags to extract just the words in the sentence 
	 * @param str A string representation of a node in a constituency parse tree
	 * @return The words in the sentence covered by this node
	 */
	public String stripParserTags(String str) {				
		for(int i = 0; i < patterns.length; ++i){
			str = str.replace(patterns[i],"");
		}
		str = str.trim();
		return(str);
	}	

	/**
	 * Determines if the string is a form of the verb "to be"
	 * @param str
	 * @return
	 */
	public boolean isCopula(String str){
		return str.equals("am") || str.equals("are") || str.equals("is") || str.equals("was") ||
				str.equals("were");
	}
	
	/**
	 * Determines if the label is a word-level preposition tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level preposition tag, false otherwise
	 */
	public boolean isPreposition(String label){
		return label.equals("IN") || label.equals("TO");
	}

	/**
	 * Determines if the label is a word-level noun tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level noun tag, false otherwise
	 */
	public boolean isNoun(String label){
		return label.equals("NN") || label.equals("NNS") || label.equals("NNP")
				|| label.equals("NNPS");
	}

	/**
	 * Determines if the label is a word-level proper noun tag 
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level proper noun tag, false otherwise
	 */
	public boolean isProperNoun(String label){
		return label.equals("NNP") || label.equals("NNPS");
	}

	/**
	 * Determines if the label is a word-level personal pronoun tag 
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level personal pronoun tag, false otherwise
	 */
	public boolean isPersonalPronoun(String label){
		return label.equals("PRP");
	}

	/**
	 * Determines if the label is a word-level verb tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level verb tag, false otherwise
	 */	
	public boolean isVerb(String label){
		return label.equals("VB") || label.equals("VBD") || label.equals("VBG")
				|| label.equals("VBN") || label.equals("VBP")
				|| label.equals("VBZ");
	}

	/**
	 * Determines if the label is a word-level adjective tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level adjective tag, false otherwise
	 */	
	public boolean isAdjective(String label){
		return label.equals("JJ") || label.equals("JJR") || label.equals("JJS");
	}

	/**
	 * Determines if the label is a word-level adverb tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level adverb tag, false otherwise
	 */	
	public boolean isAdverb(String label){
		return label.equals("RB") || label.equals("RBR") || label.equals("RBS")
				|| label.equals("WRB");
	}


	/**
	 * Determines if the label is a word-level wh- adverb, adjective, or noun phrase
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level  wh- adverb, adjective, or noun phrase
	 */	
	public boolean isWh(String label){
		return label.equals("WDT") || label.equals("WP") || label.equals("WP$");				
	}

	/**
	 * Determines if the tag is a word-level pronoun tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level pronoun tag, false otherwise
	 */	
	public boolean isPronoun(String label){
		return isPossessivePronoun(label) || isPersonalPronoun(label);
	}

	/**
	 * Determines if the tag is a word-level possessive pronoun tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level possessive pronoun tag, false otherwise
	 */	
	public boolean isPossessivePronoun(String label){
		return label.equals("PRP$");
	}

	/**
	 * Determines if the tag is a word-level determiner tag
	 * @param label The label of the node in the parse tree
	 * @return true if the label is a word-level determiner tag, false otherwise
	 */
	public boolean isDeterminer(String label){
		return label.equals("DT");
	}

	/*----------------------------------------------------
	 *				AUXILIARY METHODS 
	 *----------------------------------------------------*/

	/**
	 * Ugh...yes, I know. It's ugly... 
	 */
	private void populatePatterns(){		
		patterns[0] = ")";
		patterns[1] = "(S ";
		patterns[2] = "(SBARQ";
		patterns[3] = "(SBAR";
		patterns[4] = "(SINV";
		patterns[5] = "(SQ";
		patterns[6] = "(ADJP";
		patterns[7] = "(ADVP";
		patterns[8] = "(CONJP";
		patterns[9] = "(FRAG";
		patterns[10] = "(INTJ";
		patterns[11] = "(LST";
		patterns[12] = "(NAC";
		patterns[13] = "(NP";
		patterns[14] = "(NX";
		patterns[15] = "(PP";
		patterns[16] = "(PRN";
		patterns[17] = "(PRT";
		patterns[18] = "(QP";
		patterns[19] = "(RRC";
		patterns[20] = "(UCP";
		patterns[21] = "(VP";
		patterns[22] = "(WHADJP";
		patterns[23] = "(WHAVP";
		patterns[24] = "(WHNP";
		patterns[25] = "(WHPP";
		patterns[26] = "(CC";
		patterns[27] = "(CD";
		patterns[28] = "(DT";
		patterns[29] = "(EX";
		patterns[30] = "(FW";
		patterns[31] = "(IN";
		patterns[32] = "(JJR";
		patterns[33] = "(JJS";
		patterns[34] = "(JJ";
		patterns[35] = "(LS";
		patterns[36] = "(MD";
		patterns[37] = "(NNPS";
		patterns[38] = "(NNS";
		patterns[39] = "(NNP";
		patterns[40] = "(NN";
		patterns[41] = "(PDT";
		patterns[42] = "(POS";
		patterns[43] = "(PRP$";
		patterns[44] = "(PRP";		
		patterns[45] = "(RBS";
		patterns[46] = "(RBR";
		patterns[47] = "(RP";
		patterns[48] = "(SYM";
		patterns[49] = "(TO";
		patterns[50] = "(UH";
		patterns[51] = "(VBD";
		patterns[52] = "(VBG";
		patterns[53] = "(VBN";
		patterns[54] = "(VBP";
		patterns[55] = "(VBZ";
		patterns[56] = "(VB";
		patterns[57] = "(WDT";
		patterns[58] = "(WP";
		patterns[59] = "(WP$";
		patterns[60] ="(WRP";
		patterns[61] = "(.";
		patterns[62] = "(!";
		patterns[63] = "(,";
		patterns[64] ="(?";		
		patterns[65] = "(WHADVP";
		patterns[66] = "(WRB";
		patterns[67] = "(ROOT";
		patterns[68] = "(RB";
		patterns[69] = "(:";
	}
}
