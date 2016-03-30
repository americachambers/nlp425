package edu.pugetsound.mathcs.nlp.features;

import java.util.ArrayList;
import java.util.List;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * Resolves all anaphoras in the utterance mapping them to Prolog entities
 * @author alchambers
 *
 */
public class AnaphoraAnalyzer {	
	/**
	 * Holds a list of candidate anaphoras
	 * Cleared every time the analyze method is called
	 */
	private List<String> anaphoras;
	
	/**
	 * The parse tree corresponding to the utterance being analyzed
	 */
	private Tree parseTree;
	
	/**
	 * Contains various convenience methods for analyzing the String
	 * structure of the parse tree
	 */
	private ParseTreeAnalyzer analyzer;
	
	/**
	 * Constructs a new anaphora analyzer to analyze the anaphoric structure
	 * of utterances made
	 */
	public AnaphoraAnalyzer(){
		parseTree = null;
		analyzer = new ParseTreeAnalyzer();
		anaphoras = new ArrayList<String>();
	}
	
	/**
	 * Resolves anaphoras in a given utterance within the context of a conversation
	 * @param utt The utterance
	 * @param conversation The conversation
	 * @param pipeline A pipeline used to run the Stanford Coreference Resolver
	 */
	public void analyze(Utterance utt, Conversation conversation, StanfordCoreNLP pipeline){
		if(utt.constituencyParse == null){
			return;
		}		
		anaphoras.clear();
		parseTree = utt.constituencyParse;		
		findCandidateAnaphoras(parseTree);					
	}
	
	/**
	 * Traverses the parse tree looking for candidate anaphoras
	 * @param node A node in the parse tree
	 */
	private void findCandidateAnaphoras(Tree node){
		if(node.isLeaf()){
			return;
		}		
		if(node.label().value().equals("NP")){	
			anaphoras.add(analyzer.stripParserTags(node.toString()));
		}		
		Tree[] children = node.children();
		assert(children.length > 0);
		for(Tree child : children){
			findCandidateAnaphoras(child);
		}		
	}
}
