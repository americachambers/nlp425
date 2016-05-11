package edu.pugetsound.mathcs.nlp.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.pugetsound.mathcs.nlp.util.PathFormat;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


/**
 * This is the main class responsible for constructing Utterance objects.
 * @author alchambers
 */
public class TextAnalyzer {
	/**
	 * Certain annotators require other annotators to be loaded first.
	 * So the order of the annotators in this list is actually important.
	 */
	protected static final String ANNOTATORS =
			"tokenize, ssplit, pos, lemma, parse, natlog, ner, dcoref";

	/**
	 * Runs all annotators on a piece of text
	 */
	protected StanfordCoreNLP pipeline;

	/**
	 * Maps slang to standardized English forms
	 */
	protected HashMap<String, String> standardizedForms;

	/**
	 * Maps greetings and closing expressions to the respective
	 * dialogue act tags
	 */
	protected HashMap<String, DialogueActTag> greetClose;

	/**
	 * A semantic analyzer to translate from utterances to first-order representation
	 */
	protected SemanticAnalyzer semAnalyzer;

	/**
	 * An anaphora analyzer
	 */
	protected AnaphoraAnalyzer anaphoraAnalyzer;

	/**
	 * Classifies utterance by dialogue act
	 */
	protected DAClassifier dialogueClassifier;


	/**
	 * Creates a new TextAnalyzer
	 */
	public TextAnalyzer(KBController kb){
		Properties props = new Properties();
		props.setProperty("annotators", ANNOTATORS);
		pipeline = new StanfordCoreNLP(props);

		standardizedForms = new HashMap<String, String>();
		greetClose = new HashMap<String, DialogueActTag>();
		HashReader reader = new HashReader();
		reader.populateGreeting();
		reader.populateStandardForms();

		semAnalyzer = new SemanticAnalyzer(kb);
		anaphoraAnalyzer = new AnaphoraAnalyzer();
		dialogueClassifier = new DAClassifier(DAClassifier.Mode.DUMB_NAIVE_BAYES);
	}

	/**
	 * Computes syntactic, semantic, and pragmatic features of a piece of text
	 *
	 * @param input a piece of text
	 * @return an Utterance object that encapsulates all syntactic,
	 * 		   semantic, and pragmatic features of the input
	 *
	 * <br>
	 * <b>Preconditions:</b>
	 * <ul>
	 * 	<li>The input contains a single sentence.</li>
	 * </ul>
	 */
	public Utterance analyze(String input, Conversation conversation) throws IllegalArgumentException {
		if(input == null || conversation == null){
			throw new IllegalArgumentException();
		}

		/*
		 * The order in which the analysis is done is extremely important! Certain analyzers
		 * require/use the output of other analyzers
		 */

		// Strip ending punctuation
		String stripped = input.replaceAll("\\p{Punct}*$", "");

		// Checks for a standardized form
		if(standardizedForms.containsKey(stripped)){
			input = standardizedForms.get(stripped);
			stripped = input.replaceAll("\\p{Punct}*$", "");
		}

		// Create the utterance
		Utterance h = new Utterance(input);
		storePunctuation(h, input);

		// Annotate document with all tools registered with the pipeline
		Annotation document = new Annotation(input);
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		if(sentences.size() == 0){
			return h;
		}
		CoreMap sentence = sentences.get(0);

		// Compute basic syntactic features
		storeTokens(h, sentence);

		if(greetClose.containsKey(stripped)){
			h.daTag = greetClose.get(stripped);
			return h;
		}

		// Certain dialogue acts do not need deep semantic and anaphora analysis
		h.daTag = dialogueClassifier.classify(h, conversation);
		if(canShortCircuit(h)){
			return h;
		}

//		AMR[] temp = AMR.convertTextToAMR(input);
//		if (temp != null && temp.length > 0){
//			h.amr = temp[0];
//		}

		// Compute parse tree features
		storeParseTrees(h, sentence);
		storeParseFeatures(h);

		anaphoraAnalyzer.analyze(h, conversation, pipeline);
		try {
			semAnalyzer.analyze(h, conversation);
		} catch (java.lang.IndexOutOfBoundsException e) {
			System.out.println("Error with semantic analysis");
			System.out.println(e);
		}

		return h;
	}

	/*------------------------------------------------------------------
	 * 						Private Auxiliary Methods
	 *------------------------------------------------------------------*/

	/**
	 * Determine if dialogue act tag is simple enough that further processing (e.g. semantic
	 * and anaphoric) is not necessary
	 */
	private boolean canShortCircuit(Utterance h){
		return h.daTag == DialogueActTag.BACKCHANNEL ||
				h.daTag == DialogueActTag.SIGNAL_NON_UNDERSTANDING ||
				h.daTag == DialogueActTag.AGREEMENTS ||
				h.daTag == DialogueActTag.COMMENT ||
				h.daTag == DialogueActTag.COLLABORATIVE_COMPLETION ||
				h.daTag == DialogueActTag.THANKS ||
				h.daTag == DialogueActTag.WELCOME ||
				h.daTag == DialogueActTag.APOLOGY;
	}

	/**
	 * Tokenizes the input. Tokens are delimited by space
	 * @param h Utterance to store tokens
	 * @param sentence The sentence
	 */
	private void storeTokens(Utterance h, CoreMap sentence){
		List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
		for(CoreLabel token : tokens){
			if(!isSymbol(token.word())){
				Token t = new Token(token.word());
				t.beginPosition = token.beginPosition();
				t.endPosition = token.endPosition();
				t.pos = token.get(PartOfSpeechAnnotation.class);
				t.entityTag = token.getString(NamedEntityTagAnnotation.class);
				h.tokens.add(t);
			}
		}
	}


	/**
	 * Sets the ending punctuation mark
	 * @param h The utterance
	 * @param sentence The sentence
	 */
	private void storePunctuation(Utterance h, String sentence){
		if(sentence.endsWith(".")){
			h.punct = Punctuation.PERIOD;
		}
		else if(sentence.endsWith("...")){
			h.punct = Punctuation.ELLIPSIS;
		}
		else if(sentence.endsWith("!")){
			h.punct = Punctuation.EXCLAMATION;
		}
		else if(sentence.endsWith("?")){
			h.punct =  Punctuation.QUEST_MARK;
		}else{
			h.punct = Punctuation.UNKNOWN;
		}
	}

	/**
	 * Stores the constituency and dependency parse trees
	 *
	 */
	private void storeParseTrees(Utterance h, CoreMap sentence){
		// Get the constituency parse tree and its root
		h.constituencyParse = sentence.get(TreeAnnotation.class);
		h.rootConstituency = h.constituencyParse.firstChild().label().value();

		// Get the dependency parse tree and its root
		SemanticGraph tree =
				sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		h.dependencyParse = tree;
		h.rootDependency = tree.getFirstRoot().word();
	}


	/**
	 * The wrapper method for extracting grammatical relations from
	 * the dependency parse tree
	 */
	private void storeParseFeatures(Utterance h){
		SemanticGraph tree = h.dependencyParse;
		IndexedWord root = tree.getFirstRoot();
		extractGrammaticalRelations(tree, root, h);
	}


	/**
	 * A recursive method that traverses the dependency parse tree searching
	 * for certain grammatical relations
	 * @param tree A dependency parse tree
	 * @param node A node in the parse tree
	 * @param h The utterance itself
	 */
	private void extractGrammaticalRelations(SemanticGraph tree, IndexedWord node, Utterance h){
		if(!tree.hasChildren(node)){
			return;
		}
		Set<GrammaticalRelation> reltns = tree.childRelns(node);
		for(GrammaticalRelation rel : reltns){
			Set<IndexedWord> childrenWithReltn = tree.getChildrenWithReln(node, rel);

			// Capture the subjects of the sentence
			if(rel.getShortName().equals("nsubj")){
				for(IndexedWord w : childrenWithReltn){
					h.subjects.add(w.word());
				}
			}

			// Capture the direct objects of the sentence
			else if(rel.getShortName().equals("dobj")){
				for(IndexedWord w : childrenWithReltn){
					h.directObjects.add(w.word());
				}
			}

			else if(rel.getShortName().equals("nsubjpass")){
				h.isPassive = true;
				for(IndexedWord w : childrenWithReltn){
					h.subjects.add(w.word());
				}
			}

			// Recurse regardless
			for(IndexedWord w : childrenWithReltn){
				extractGrammaticalRelations(tree, w, h);
			}
		}
	}

	/**
	 * Matches any of the following 32 symbol characters:
	 *
	 * 			!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
	 *
	 * @param token a string
	 * @return true if string is a symbol and false otherwise
	 */
	private boolean isSymbol(String token){
		return Pattern.matches("\\p{Punct}", token);
	}

	/**
	 * A nested class that reads in from file and populates the respective hashes
	 * @author alchambers
	 *
	 */
	class HashReader{
		/**
		 * Populates the hash of standardized forms
		 */
		public void populateStandardForms(){
			try{
				BufferedReader input = new BufferedReader(new FileReader(PathFormat.absolutePathFromRoot("models/phrases/slang.txt")));
				String line = input.readLine();
				while(line != null){
					int sep = line.indexOf("\t");
					if(sep != -1){
						standardizedForms.put(line.substring(0, sep), line.substring(sep+1));
					}
					line = input.readLine();
				}
				input.close();
			}
			catch(IOException e){
				System.out.println(e);
			}
		}
		/**
		 * Populates the greeting and closing hash
		 */
		public void populateGreeting(){
			try{
				readTextFile(new BufferedReader(new FileReader(PathFormat.absolutePathFromRoot("models/phrases/closing.txt"))), DialogueActTag.CONVENTIONAL_CLOSING);
				readTextFile(new BufferedReader(new FileReader(PathFormat.absolutePathFromRoot("models/phrases/greeting.txt"))), DialogueActTag.CONVENTIONAL_OPENING);
			}
			catch(IOException e){
				System.out.println(e);
			}
		}

		/**
		 * Reads lines from a text file and adds them to the greetClose hash with the
		 * corresponding dialogue act tag
		 * @param input A text file
		 * @param tag Corresponding dialogue act tag
		 */
		private void readTextFile(BufferedReader input, DialogueActTag tag){
			try{
				String line = input.readLine();
				while(line != null){
					greetClose.put(line, tag);
					line = input.readLine();
				}
				input.close();
			}
			catch(IOException e){
				System.out.println(e);
			}

		}
	}



	/**
	 * Run this method to inspect the features computed for a given typed
	 * piece of text.
	 * @param args
	 */
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		TextAnalyzer analyzer = new TextAnalyzer(null);
		Conversation convo = new Conversation();
		while(true){
			System.out.print("Enter a line of text: ");
			String line = scan.nextLine();
			Utterance utt = analyzer.analyze(line, convo);
			convo.addUtterance(utt);
			System.out.println(utt);
		}
	}

}
