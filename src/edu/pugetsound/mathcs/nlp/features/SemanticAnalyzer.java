package edu.pugetsound.mathcs.nlp.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;

/**
 * This class translates an utterance into a first-order logical expression
 * 
 * It is assumed that the constituent parse tree of the utterance uses tags
 * from the Penn Treebank Tag Set whose tagging guidelines are given in,
 * 
 * Part-of-Speech Tagging Guidelines for the Penn Treebank Project (3rd Revision)
 * Beatrice Santorini
 * University of Pennsylvania
 * 7/1/1990
 * 
 * According to the guidelines, the following words are tagged as a determiner
 * (with some qualifications):
 * 			a, an, every, no, the, another, any, some, each, either, neither, 
 * 			that, these, this, those, all, both,
 * 
 * @author alchambers
 *
 */
public class SemanticAnalyzer {

	/**
	 * The utterance that is currently being processed by the analyzer
	 */
	private Utterance current;

	/**
	 * Contains methods for analyzing a constituency parse tree
	 */
	private ParseTreeAnalyzer analyzer;
	
	/**
	 * Quantified variables
	 */
	private String variables;
	
	/**
	 * 
	 */
	private int counter;
	
	
	/**
	 * Constructs a new semantic analyzer
	 */
	public SemanticAnalyzer(){
		counter = 0;
		current = null;		
		variables = "";		
		analyzer = new ParseTreeAnalyzer();
	}

	/**
	 * Converts an utterance into a (neo-Davidsonian, event reified) first order
	 * expression
	 * 
	 * @param utt
	 * @param convo
	 * @return A List of Prolog predicate representing the content of the utterance
	 */
	public List<PrologStructure> analyze(Utterance utt, Conversation convo){
		current = utt;

		switch(utt.rootConstituency){
			case "S":
				//processStatement(utt);
				break;
			case "SQ":
				break;
			case "SBARQ":
				break;
			default:
				break;
		}
		
		// This return value is just temporary so it compiles
		List<PrologStructure> predicates = new ArrayList<PrologStructure>();
		return predicates;
	}

	/*----------------------------------------------------------
	 * 				AUXILIARY METHODS
	 *----------------------------------------------------------*/

	/**
	 * Converts a statement into a first-order logic expression
	 * @param utt The utterance
	 * @param fol The first-order logical representation
	 */
	private void processStatement(Utterance utt){	
		System.out.println(depthFirstSearch(utt.constituencyParse.getChild(0)));
	}


	private List<PrologStructure> depthFirstSearch(Tree node){
		String nodeLabel = node.label().value();
		
		// Proper noun -- e.g., "John" or "New" (as in "New York")
		if(analyzer.isProperNoun(nodeLabel)){
			assert hasSingleLeafChild(node);
			String properNoun = node.getChild(0).label().value();
			return makeTerm(properNoun);
		}

		// Personal pronoun -- e.g., "I", "you", "he"
		else if(analyzer.isPersonalPronoun(nodeLabel)){
			assert hasSingleLeafChild(node);
			String pronoun = node.getChild(0).label().value();
			return makeTerm(pronoun);
		}

		// Possessive pronoun -- e.g., "my", "your" , "his"
		else if(analyzer.isPossessivePronoun(nodeLabel)){
			assert hasSingleLeafChild(node);
			String possessor = node.getChild(0).label().value();

			// PossessedBy(null, pronoun) <--- need to replace pronoun with Prolog entity!!!
			List<PrologStructure> predicate = new ArrayList<PrologStructure>();
			predicate.add(new PrologStructure(2));
			predicate.get(0).setName("PossessedBy");
			predicate.get(0).addArgument(possessor, 1);
			return predicate;
		}		

		// A noun (non-proper) -- e.g., "cats", "fish", "love", "deer"
		else if(analyzer.isNoun(nodeLabel)){
			assert hasSingleLeafChild(node);
			String noun = node.getChild(0).label().value();		

			List<PrologStructure> predicate = new ArrayList<PrologStructure>();
			predicate.add(new PrologStructure(2));
			predicate.get(0).setName("IsA");
			predicate.get(0).addArgument(noun, 1);
			predicate.get(0).addArgument("X" + counter, 0);
			counter++;
			// Waiting for a quantifier for the variable X
			return predicate;
		}

		// A preposition -- e.g., "with", "on", "in", "by", "between"
		else if(analyzer.isPreposition(nodeLabel)){
			assert hasSingleLeafChild(node);
			String prep = node.getChild(0).label().value();

			List<PrologStructure> predicate = new ArrayList<PrologStructure>();
			predicate.add(new PrologStructure(2));
			predicate.get(0).setName(prep);
			// Argument 0: the object being modified (the verb event or a noun) 
			// Argument 1: object of the preposition			
			return predicate;										
		}

		// A verb
		else if(analyzer.isVerb(nodeLabel)){
			assert hasSingleLeafChild(node);
			String verb = node.getChild(0).label().value();

			List<PrologStructure> predicate = new ArrayList<PrologStructure>();
			predicate.add(new PrologStructure(2));
			predicate.get(0).setName("IsA");
			String var = "X" + counter;
			counter++;
			predicate.get(0).addArgument(var, 0);
			predicate.get(0).addArgument(verb+"Event", 1);
			
			variables += "var ";
			return predicate;
		}

		// Determinant -- e.g., "a", "the", "some", "every", "none"
		else if(analyzer.isDeterminer(nodeLabel)){
			assert hasSingleLeafChild(node);
			String det = node.getChild(0).label().value();			
			return makeTerm(semanticAttachmentDeterminant(det));			
		}


		Tree[] children = node.children();		
		if(children.length == 1){
			return depthFirstSearch(children[0]);
		}

		// NP ---> Det NN
		if(pattern1(node)){

			// QUANTIFIER
			PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0);

			// IsA(null, noun)
			PrologStructure child1 = depthFirstSearch(node.getChild(1)).get(0);

			// QUANTIFIER X IsA(X, noun)
			String var = "X" + counter;
			counter++;
			
			variables += " " + child1.getArgument(1) + " " + var;
			PrologStructure nounPhrase = new PrologStructure(2);
			nounPhrase.setName("IsA");
			nounPhrase.addArgument(child1.getArgument(1), 1);
			nounPhrase.addArgument(var, 0);
			List<PrologStructure> temp = new ArrayList<PrologStructure>();
			temp.add(nounPhrase);			
			return temp;
		}

		// VP --> Verb DirectObject
		else if(pattern2(node)){
			
			//IsA(E, verbEvent)
			PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0);

			// IsA(X,noun) -- OR -- ProperNoun -- OR -- Pronoun 
			PrologStructure child1 = depthFirstSearch(node.getChild(1)).get(0);

			PrologStructure theme = new PrologStructure(2);
			theme.setName("Theme");
			theme.addArgument(child0.getArgument(0), 0);
			
			ArrayList<PrologStructure> verbPhrase = new ArrayList<PrologStructure>();
			verbPhrase.add(child0);


			// A term: either a ProperNoun or a Pronoun
			if(child1.getArity() == 0){
				theme.addArgument(child1.getName(), 1);
			}
			else if(child1.getArity() == 2){
				theme.addArgument(child1.getArgument(0), 1);
				verbPhrase.add(child1);
			}
			verbPhrase.add(theme);
			
			return verbPhrase;
		}

		// S --> Subject VP
		else if(pattern3(node)){
			
			//IsA(X,noun) -- OR -- Term
			PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0);

			//  IsA(E, verbEvent)...
			List<PrologStructure> child1 = depthFirstSearch(node.getChild(1));

			// Agent(E, X)
			PrologStructure agent = new PrologStructure(2);
			agent.setName("Agent");
			
			assert child1.size() > 0;
			agent.addArgument(child1.get(0).getArgument(0), 0);
			

			// A term: either a ProperNoun or a Pronoun
			if(child0.getArity() == 0){
				agent.addArgument(child0.getName(), 1);
			}
			else if(child0.getArity() == 2){
				agent.addArgument(child1.get(0).getArgument(0), 1);
				child1.add(child0);
			}
			
			child1.add(agent);
			return child1;
		}
		
		System.out.println("No rule found: " + node.toString());
		for(Tree child : children){
			depthFirstSearch(child);
		}
		

		return new ArrayList<PrologStructure>();

	}

	// S --> Subject VP
	private boolean pattern3(Tree node){
		String nodeLabel = node.label().value();
		if(!nodeLabel.equals("S") || node.numChildren() < 2){
			return false;
		}				
		String childLabel0 = node.getChild(0).label().value();
		String childLabel1 = node.getChild(1).label().value();				
		if(!childLabel0.equals("NP") || !childLabel1.equals("VP")){
			return false;
		}		
		String noun = analyzer.stripParserTags(node.getChild(0).toString());
		return current.subjects.size() == 1 && noun.equals(current.subjects.get(0));		
	}
	
	// VP --> Verb DirectObject
	private boolean pattern2(Tree node){
		
		String nodeLabel = node.label().value();		

		if(!nodeLabel.equals("VP") || node.numChildren() != 2){
			return false;
		}

		String childLabel0 = node.getChild(0).label().value();
		String childLabel1 = node.getChild(1).label().value();		
		if(!analyzer.isVerb(childLabel0) || !childLabel1.equals("NP")){
			return false;
		}

		String noun = analyzer.stripParserTags(node.getChild(1).toString());
		return current.directObjects.size() == 1 && noun.equals(current.directObjects.get(0));
	}

	
	// 	NP --> Det NN
	private boolean pattern1(Tree node){
		String nodeLabel = node.label().value();
		if(!nodeLabel.equals("NP") || node.numChildren() == 2){
			return false;
		}
		String childLabel0 = node.getChild(0).label().value();
		String childLabel1 = node.getChild(1).label().value();
		return analyzer.isDeterminer(childLabel0) && analyzer.isNoun(childLabel1);
	}

	
	
	
	private List<PrologStructure> makeTerm(String name){
		List<PrologStructure> term = new ArrayList<PrologStructure>();
		term.add(new PrologStructure(0));
		term.get(0).setName(name);
		return term;
	}

	/**
	 * The semantic attachment for the syntax rule:
	 * 			DT ---> [word]
	 * @param word
	 * @return
	 */
	private String semanticAttachmentDeterminant(String word){
		if(word.equalsIgnoreCase("a") || word.equalsIgnoreCase("an") ||
				word.equalsIgnoreCase("the") || word.equalsIgnoreCase("that") ||
				word.equalsIgnoreCase("this") || word.equalsIgnoreCase("those") ||
				word.equalsIgnoreCase("some")){
			return "EXISTS";
		}
		else if(word.equalsIgnoreCase("every") || word.equalsIgnoreCase("all")){
			return "FORALL";
		}
		else{
			return "???";
		}
	}

	/**
	 * Checks that node has a single child and that child is a leaf
	 * @param node A node in a parse tree
	 * @return true if node has single leaf child, false otherwise
	 */
	private boolean hasSingleLeafChild(Tree node){
		return node.numChildren()==1 && node.getChild(0).isLeaf();
	}
}
