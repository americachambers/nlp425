package edu.pugetsound.mathcs.nlp.features;

import java.util.ArrayList;
import java.util.List;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.kb.PrologStructure;
import edu.pugetsound.mathcs.nlp.lang.*;
import edu.stanford.nlp.trees.CollinsHeadFinder;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
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
 * @author alchambers
 */
public class SemanticAnalyzer {
	protected static final String ISA = "isA";
	protected static final String AGENT = "agent";
	protected static final String THEME = "theme";
	protected static final String EVENT = "event";
	protected static final String ENTITY = "entity";
	protected static final String POSSESSION = "possessedBy";
	
	
	/**
	 * The utterance that is currently being processed by the analyzer
	 */
	protected Utterance current;

	/**
	 * Contains methods for analyzing the String representation of a constituency parse tree
	 */
	protected ParseTreeAnalyzer analyzer;

	/**
	 * Increments with each variable made
	 */
	protected int counter;

	/**
	 * Finds the head of phrase 
	 */
	protected CollinsHeadFinder headFinder;

	/**
	 * A factory for constructing parse trees
	 */
	protected LabeledScoredTreeFactory factory;

	/**
	 * Knowledge base to query for anaphora resolution
	 */
	protected KBController kb;


	/**
	 * Constructs a new semantic analyzer
	 */
	public SemanticAnalyzer(KBController kb){
		this.counter = 0;
		this.kb = kb;
		this.current = null;			
		headFinder = new CollinsHeadFinder();
		analyzer = new ParseTreeAnalyzer();
		factory = new LabeledScoredTreeFactory();
	}

	/**
	 * Converts an utterance into a (neo-Davidsonian, event reified) first order
	 * expression
	 * 
	 * @param utt The utterance to be translated
	 * @param convo The entire conversation
	 * @return A List of Prolog predicate representing the content of the utterance
	 */
	public void analyze(Utterance utt, Conversation convo){
		counter = 0;
		current = utt;		

		if(utt.rootConstituency.equals("SQ") || utt.daTag == DialogueActTag.QUESTION_YES_NO){
			utt.firstOrderRep = processYesNoQuestion();
		}
		else if(utt.rootConstituency.equals("SBARQ")){
			// TODO: Fill in
		}
		else if(utt.punct == Punctuation.QUEST_MARK){
			// TODO: Fill in
		}
		else if(utt.rootConstituency.equals("S")){
			utt.firstOrderRep = processStatement();			
		}	
	}



	/*----------------------------------------------------------
	 * 				AUXILIARY METHODS
	 *----------------------------------------------------------*/

	/**
	 * Converts a yes-no question into a first-order logic expression
	 * @return The first-order logical representation
	 */
	private List<PrologStructure> processYesNoQuestion(){
		String utt = current.utterance;		
		if(utt.startsWith("Does") ||  utt.startsWith("Do") || utt.startsWith("Did")){			
			current.constituencyParse.getChild(0).removeChild(0);
			current.constituencyParse.getChild(0).setValue("S");
			return processStatement();
		}
		else if(utt.startsWith("Am") || utt.startsWith("Is") || utt.startsWith("Are") ||
				utt.startsWith("Was") || utt.startsWith("Were")){

			/*
			 * Identifies predicate nominatives and predicate adjectives, e.g.
			 * "Is Fluffy a cat?"
			 * "Is Fluffy white?"
			 */
			if(label(current.constituencyParse.getChild(0).getChild(1)).equals("NP") &&
					(label(current.constituencyParse.getChild(0).getChild(2)).equals("NP") ||
							label(current.constituencyParse.getChild(0).getChild(2)).equals("ADJP"))){
				Tree copula = current.constituencyParse.getChild(0).removeChild(0);				

				// After removing the copula, the predicate nominative phrase is child 1
				Tree nounPhrase = current.constituencyParse.getChild(0).removeChild(1);
				List<Tree> children = new ArrayList<Tree>();
				children.add(copula);
				children.add(nounPhrase);

				// Creates a verbphrase
				Tree verbPhrase = factory.newTreeNode(current.constituencyParse.label(), children);
				verbPhrase.setValue("VP");

				current.constituencyParse.getChild(0).addChild(1, verbPhrase);				
				current.constituencyParse.getChild(0).setValue("S");

				return processStatement();
			}			
		}
		return new ArrayList<PrologStructure>();
	}

	/**
	 * Converts a statement into a first-order logic expression
	 * @return The first-order logical representation
	 */
	private List<PrologStructure> processStatement(){	
		return depthFirstSearch(current.constituencyParse.getChild(0));
	}

	/**
	 * Performs a depth-first traversal of the parse tree. For each syntactic rule, it applies
	 * a corresponding semantic rule to incrementally build the first-order logical representation
	 * of the utterance
	 * @param node A node in the parse tree
	 * @return A list of Prolog terms and/or predicates
	 */
	private List<PrologStructure> depthFirstSearch(Tree node){
		String nodeLabel = label(node);

		// Rewrite rules of the form A --> B
		if(analyzer.isProperNoun(nodeLabel) ){				
			return properNoun(node);			
		}
		else if(analyzer.isPersonalPronoun(nodeLabel)){
			return personalPronoun(node);					
		}
		else if(analyzer.isDeterminer(nodeLabel)){
			return determinant(node);
		}
		else if(analyzer.isPossessivePronoun(nodeLabel)){
			return possessivePronoun(node);
		}		
		else if(analyzer.isNoun(nodeLabel)){
			return noun(node);
		}
		else if(analyzer.isPreposition(nodeLabel)){
			return preposition(node);
		}
		else if(analyzer.isVerb(nodeLabel)){
			return verb(node);
		}
		else if(node.numChildren() == 1){
			return depthFirstSearch(node.getChild(0));
		}


		// Rewrite rules of the form A --> B1 B2 ... BN
		
		/*
		 * NP --> Det NN
		 * 
		 * TODO: Does this work if NN is a proper noun?
		 * TODO: Right now, I'm ignoring the quantifier. By default, all variables are existentially
		 * quantified. How do we want to deal with the universal quantifier? 
		 */
		if(pattern1(node)){
			//PrologStructure quantifier = depthFirstSearch(node.getChild(0)).get(0);
			List<PrologStructure> child1 = depthFirstSearch(node.getChild(1)); // IsA(X, noun)								
			return child1;
		}

		/*
		 * VP --> Verb NP(directObject)
		 */
		else if(pattern2(node)){		
			List<PrologStructure> child0 = depthFirstSearch(node.getChild(0)); //IsA(E, verbEvent)
			PrologStructure child1 = depthFirstSearch(node.getChild(1)).get(0); // IsA(X,noun) or Term

			String recipient = child1.getArity() == 0 ? child1.getName() : child1.getArgument(0);			
			PrologStructure vp = makeBinaryPredicate(THEME, child0.get(0).getArgument(0), recipient);			

			child0.add(vp);			
			if(child1.getArity() != 0){ 
				child0.add(child1);
			}
			return child0;
		}


		/*
		 * S --> NP(subject) VP(non-copula)
		 */
		else if(pattern3(node) && !analyzer.isCopula(label(node.getChild(1).headTerminal(headFinder)))){
			PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0); // IsA(X,noun) or Term
			List<PrologStructure> child1 = depthFirstSearch(node.getChild(1)); // IsA(E, verbEvent)
			//assert child1.size() > 0;

			// Agent(E, X)
			// TODO: Can't guarantee that the first predicate in the VP clause has E as the first term
			String actor = child0.getArity() == 0 ? child0.getName() : child0.getArgument(0);			
			PrologStructure agent = makeBinaryPredicate(AGENT, child1.get(0).getArgument(0), actor);

			child1.add(agent);
			if(child0.getArity() != 0){
				child1.add(child0);
			}			
			return child1;
		}


		/*
		 * S --> NP(subject) VP(copula)
		 */
		else if(pattern3(node) && analyzer.isCopula(label(node.getChild(1).headTerminal(headFinder)))){		
			PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0); // IsA(X,noun1) or Term
			List<PrologStructure> child1 = depthFirstSearch(node.getChild(1)); // IsA(Y,noun2)

			if(child0.getArity() == 0){
				child1.get(0).addArgument(child0.getName(), 0);
			}
			else{
				String newVar = getVariable();
				child0.addArgument(newVar, 0);
				child1.get(0).addArgument(newVar, 0);
				child1.add(child0);				
			}
			return child1;
		}


		/*
		 * VP --> Copula NP
		 * TODO: Right now, the copula is being ignored. Later, if we add tense (e.g. past, present)
		 * we'll need to examine the copula
		 */
		else if(pattern4(node)){
			// PrologStructure child0 = depthFirstSearch(node.getChild(0)).get(0); // copula
			List<PrologStructure> child1 = depthFirstSearch(node.getChild(1)); // IsA(X,noun) or Term

			if(child1.get(0).getArity()==0){
				return makeBinaryPredicateList("isa", getVariable(), child1.get(0).getName());
			}
			else{
				return child1;
			}
		}

		/*
		 * VP --> Copula ADJP/ADVP
		 */
		else if(pattern5(node)){
			PrologStructure child1 = depthFirstSearch(node.getChild(1)).get(0); // Property
			assert(child1.getArity() == 0);			
			return makeBinaryPredicateList("property", getVariable(), child1.getName());
		}

		/*
		 * This will return back and cause a runtime exception... 
		 */
		Tree[] children = node.children();
		for(Tree child : children){
			depthFirstSearch(child);
		}
		return new ArrayList<PrologStructure>();
	}

	/**
	 * Return the label of a node in the parse tree
	 * @param node A node in the parse tree
	 * @return The label of the node
	 */
	private String label(Tree node){
		return node.value();
	}

	/**
	 * Creates a new variable
	 * @return the name of an unused variable
	 */
	private String getVariable(){
		String var = "X" + counter;
		counter++;
		return var;
	}

	/**
	 * Makes a unary Prolog predicate name(arg0)
	 * @param name The name of the predicate
	 * @param arg0 The first argument of the predicate
	 */
	private List<PrologStructure> makeUnaryPredicate(String name, String arg0){
		List<PrologStructure> list = new ArrayList<PrologStructure>();
		PrologStructure predicate = new PrologStructure(1);
		predicate.setName(name);
		predicate.addArgument(arg0, 0);
		list.add(predicate);
		return list;
	}
	
	/**
	 * Makes a binary Prolog predicate name(arg0, arg1)
	 * @param name The name of the predicate
	 * @param arg0 The first argument of the predicate
	 * @param arg1 The second argument of the predicate
	 * @return A binary Prolog predicate
	 */
	private PrologStructure makeBinaryPredicate(String name, String arg0, String arg1){
		PrologStructure predicate = new PrologStructure(2);
		predicate.setName(name);
		predicate.addArgument(arg0, 0);
		predicate.addArgument(arg1, 1);
		return predicate;
	}


	/**
	 * Constructs a list with a single (binary) Prolog predicate
	 * @param name The name of the predicate
	 * @param arg0 The first argument of the predicate
	 * @param arg1 The second argumetn of the predicate
	 * @return A list with one Prolog predicate
	 */
	private List<PrologStructure> makeBinaryPredicateList(String name, String arg0, String arg1){
		ArrayList<PrologStructure> struct = new ArrayList<PrologStructure>();
		struct.add(makeBinaryPredicate(name, arg0, arg1));
		return struct;
	}

	/**
	 * Makes a Prolog term
	 * @param name The name of the term
	 * @return A Prolog term
	 */
	private List<PrologStructure> makeTerm(String name){
		ArrayList<PrologStructure> struct = new ArrayList<PrologStructure>();
		PrologStructure term = new PrologStructure(0);
		term.setName(name);
		struct.add(term);
		return struct;
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


	/*----------------------------------------------------------------------------------------
	 * 		THESE METHODS IDENTIFY RULES OF THE FORM: NonTerminal --> Terminal
	 *----------------------------------------------------------------------------------------*/
 
	// Determinant -- e.g., "a", "the", "some", "every", "none"
	private List<PrologStructure> determinant(Tree node){
		assert hasSingleLeafChild(node);			
		return makeTerm(semanticAttachmentDeterminant(label(node.getChild(0))));			
	}

	// Proper noun -- e.g., "John" or "London"
	private List<PrologStructure> properNoun(Tree node){
		assert hasSingleLeafChild(node);
		String childLabel = label(node.getChild(0));

		// Doing anaphora resolution for this proper noun
		// If entity(ProperNoun) doesn't exist in KB, we add it
		assert current.resolutions.containsKey(childLabel);			
		List<PrologStructure> entity = makeUnaryPredicate(ENTITY, childLabel.toLowerCase());
		if(kb != null && !kb.yesNo(entity)){
			kb.assertNew(entity);
		}			
		return makeTerm(current.resolutions.get(childLabel));				
	}

	// Personal pronoun -- e.g., "I", "you", "he"
	private List<PrologStructure> personalPronoun(Tree node){
		assert hasSingleLeafChild(node);
		return makeTerm(label(node.getChild(0)));	
	}

	// Possessive pronoun -- e.g., "my", "your" , "his"
	private List<PrologStructure> possessivePronoun(Tree node){
		assert hasSingleLeafChild(node);
		String possessor = label(node.getChild(0));
		return makeBinaryPredicateList(POSSESSION, getVariable(), possessor);
	}

	// A noun (non-proper) -- e.g., "cats", "fish", "love", "deer"
	private List<PrologStructure> noun(Tree node){
		assert hasSingleLeafChild(node);
		String noun = label(node.getChild(0));		
		return makeBinaryPredicateList(ISA, getVariable(), noun);
	}

	// A preposition -- e.g., "with", "on", "in", "by", "between"
	private List<PrologStructure> preposition(Tree node){
		assert hasSingleLeafChild(node);
		String prep = label(node.getChild(0));
		return makeBinaryPredicateList(prep, getVariable(), getVariable());
	}

	// A verb -- "hit", "love", "eat"
	private List<PrologStructure> verb(Tree node){
		assert hasSingleLeafChild(node);
		String verb = label(node.getChild(0));						
		if(analyzer.isCopula(verb)){
			return makeTerm(verb);	// currently this return value is never used
		}
		else{
			String var = getVariable();			
			return makeBinaryPredicateList(ISA, var, verb+"Event"); 			
		}
	}


	/*----------------------------------------------------------------------------------------
	 * 		THE "PATTERN" METHODS IDENTIFY DIFFERENT PRODUCTION RULES IN THE GRAMMAR
	 *----------------------------------------------------------------------------------------*/

	/*
	 * NP --> Det NN
	 */
	private boolean pattern1(Tree node){
		String nodeLabel = label(node);
		if(!nodeLabel.equals("NP") || node.numChildren() != 2){
			return false;
		}		
		String childLabel0 = label(node.getChild(0));
		String childLabel1 = label(node.getChild(1));
		return analyzer.isDeterminer(childLabel0) && analyzer.isNoun(childLabel1);
	}


	/*
	 * VP --> Verb DirectObject
	 */
	private boolean pattern2(Tree node){
		String nodeLabel = label(node);		
		if(!nodeLabel.equals("VP") || node.numChildren() != 2){
			return false;
		}
		String childLabel0 = label(node.getChild(0));
		String childLabel1 = label(node.getChild(1));		
		if(!analyzer.isVerb(childLabel0) || !childLabel1.equals("NP")){
			return false;
		}		
		String headNoun = label(node.getChild(1).headTerminal(headFinder));
		return current.directObjects.size() == 1 && headNoun.equals(current.directObjects.get(0));
	}	


	/*
	 * S --> Subject VP
	 */
	private boolean pattern3(Tree node){
		String nodeLabel = label(node);
		if(!nodeLabel.equals("S") || node.numChildren() < 2){
			return false;
		}				
		String childLabel0 = label(node.getChild(0));
		String childLabel1 = label(node.getChild(1));				
		if(!childLabel0.equals("NP") || !childLabel1.equals("VP")){
			return false;
		}		
		String headNoun = label(node.getChild(0).headTerminal(headFinder));
		return current.subjects.size() == 1 && headNoun.equals(current.subjects.get(0));		
	}


	/*
	 * VP --> Copula NP
	 */
	private boolean pattern4(Tree node){
		String nodeLabel = label(node);		
		if(!nodeLabel.equals("VP") || node.numChildren() != 2){
			return false;
		}
		if(!label(node.getChild(1)).equals("NP")){
			return false;
		}
		String headVerb = label(node.getChild(0).headTerminal(headFinder));
		return analyzer.isCopula(headVerb);
	}


	/*
	 * VP --> Copula ADJP/ADVP 
	 */
	public boolean pattern5(Tree node){
		String nodeLabel = label(node);		
		if(!nodeLabel.equals("VP") || node.numChildren() != 2){
			return false;
		}
		String childLabel = label(node.getChild(1));
		if(!childLabel.equals("ADJP") && !childLabel.equals("ADVP")){
			return false;
		}		
		String headVerb = label(node.getChild(0).headTerminal(headFinder));
		return analyzer.isCopula(headVerb);
	}
}
