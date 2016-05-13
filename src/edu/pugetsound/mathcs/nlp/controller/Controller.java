package edu.pugetsound.mathcs.nlp.controller;

import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.mdp.Action;
import edu.pugetsound.mathcs.nlp.mdp.HyperVariables;
import edu.pugetsound.mathcs.nlp.mdp.QLearner;
import edu.pugetsound.mathcs.nlp.processactions.ActionProcessor;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * This class contains the main input/output loop. 
 * @author alchambers, kstern
 */
public class Controller {
	protected static final String INITIAL_GREETING = "Hello.";  
	
	/**
	 * TODO: In the future, have a team detect the focus of the conversation. There should be
	 * a module between the process actions team and the kb team that logs statistics about 
	 * how many hits/misses we've gotten from a particular knowledge base, controls which kbs
	 * to query, and shifts the central kb based on focus
	 */
	protected static final String KNOWLEDGE_BASE_PATH = "knowledge/cats.pl";
	
	protected static Conversation conversation;		
	protected static Scanner input;
	protected static TextAnalyzer analyzer;
	protected static QLearner mdp;
	protected static HyperVariables hyperVariables;
	protected static KBController kb;
	
	/**
	 * The discounted value for the Markov Decision Process
	 */
	protected static final double GAMMA = 0.1;

	/**
	 * Related to the duration and likelihood of exploring vs. exploiting for the MDP 
	 * A higher value corresponds to a longer exploration phase 
	 */
	protected static final int EXPLORE = 1000;

	/**
	 * The stream where all output is sent. Refactored to make it easier
	 * to test input/output functionality
	 */
	protected static PrintStream out;
		
	/**
	 * Sets up the necessary tools for the conversational agent
	 */
	protected static void setup(InputStream in, PrintStream outStream){
		out = outStream;
		kb = new KBController(KNOWLEDGE_BASE_PATH);
		conversation = new Conversation();	
		analyzer = new TextAnalyzer(kb);
		input = new Scanner(in);
		hyperVariables = new HyperVariables(GAMMA, EXPLORE);
		mdp = new QLearner(hyperVariables,true);		
	}

	/**
	 * @param utt The response to the user
	 */
	protected static void respondToUser(Utterance utt){		
		out.println();
		out.println();
		out.println("Agent: " + utt.utterance);
	}

	/**
	 * Prints an initial greeting to the user. This initial greeting
	 * is the first utterance in the conversation
	 */
	protected static void initiateGreeting(){
		out.println();
		Action action = mdp.train(conversation);
		String response = ActionProcessor.generateResponse(conversation, action.getDATag(), kb);
		Utterance agentUtt = analyzer.analyze(response, conversation);
		conversation.addUtterance(agentUtt);
		respondToUser(agentUtt);
	}

	
	/**
	 * Runs a single interaction with the human
	 */
	private static boolean run(){
		// Read the human's typed input
		out.print("> ");
		String line = input.nextLine();

		// Process the typed input
		Utterance utt = analyzer.analyze(line, conversation);			
		conversation.addUtterance(utt);

		// Get an action from the Markov Decision Process
		Action action = mdp.train(conversation);

		// Process the action and produce a response for the user
		String response = ActionProcessor.generateResponse(conversation, action.getDATag(), kb);
		Utterance agentUtt = analyzer.analyze(response, conversation);
		conversation.addUtterance(agentUtt);
		respondToUser(agentUtt);
		
        ResponseTag eDAT = action.getDATag();
		if(eDAT.equals(ResponseTag.CONVENTIONAL_CLOSING)){
			return false;
		}
        return true;		
	}

	/**
	 * Saves the state from the conversation 
	 */
	private static void saveState(){
		mdp.saveToFile();
	}
	
	/**
	 * Main controller for the conversational agent. 
	 * TODO: Add description of any command line arguments
	 */
	public static void main(String[] args){		
		setup(System.in, System.out);		
		initiateGreeting();
		boolean typing = true;
		while(typing){
			typing = run();
		}
		saveState();
	}	
}
