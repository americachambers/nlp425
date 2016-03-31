package edu.pugetsound.mathcs.nlp.controller;

import java.util.Scanner;

import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.mdp.QLearner;
import edu.pugetsound.mathcs.nlp.mdp.Action;
import edu.pugetsound.mathcs.nlp.processactions.ActionProcessor;

/**
 * This class contains the main input/output loop. 
 * @author alchambers
 *
 */
public class Controller {
	private static Scanner input;
	private static TextAnalyzer analyzer;
	private static Conversation conversation;		
	private static String initialGreeting = "Hello.";  
	private static QLearner mdp;
	
	/**
	 * Setups the necessary tools for the conversational agent
	 */
	private static void setup(){
		conversation = new Conversation();	
		analyzer = new TextAnalyzer();
		input = new Scanner(System.in);
		mdp = new QLearner();
	}

	
	/**
	 * TODO: Needs to be replaced by the Stage 7: Text Generation code
	 * @param u The response to the user
	 */
	private static void respondToUser(Utterance utt){		
		System.out.println();
		System.out.println();
		System.out.println("Agent: " + utt.utterance);
	}


	/**
	 * Prints an initial greeting to the user. This initial greeting
	 * is the first utterance in the conversation
	 */
	private static void initiateGreeting(){
		System.out.println();
		Utterance utt = analyzer.analyze(initialGreeting, conversation);
		conversation.addUtterance(utt);
		respondToUser(utt);
	}

	/**
	 * Reads a line of text from the user, computes features,
	 * and prints the features for inspection
	 * TODO: Add description of any command line arguments
	 */
	public static void main(String[] args){
		setup();				
		initiateGreeting();		
		
		while(true){
			// Read the human's typed input
			System.out.print("> ");
			String line = input.nextLine();
			
			// Process the typed input
			Utterance utt = analyzer.analyze(line, conversation);			
			conversation.addUtterance(utt);
			
			// Get an action from the Markov Decision Process
			Action action = mdp.train(conversation);
			
			// Process the action and produce a response for the user
			String response = ActionProcessor.generateResponse(utt, action.getDATag());			
			Utterance agentUtt = analyzer.analyze(response,  conversation);
			conversation.addUtterance(agentUtt);
			respondToUser(agentUtt);
		}
	}	
}
