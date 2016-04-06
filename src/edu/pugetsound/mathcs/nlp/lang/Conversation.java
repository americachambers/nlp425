package edu.pugetsound.mathcs.nlp.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all utterances from the conversation
 * @author alchambers
 *
 */
public class Conversation {
	
	/**
	 * A conversation is an array of Utterances
	 */
	private ArrayList<Utterance> conversation;
	
	/**
	 * A string representation of the conversation where each 
	 * utterance is separated by a newline.
	 */
	private String stringRep;
		
	
	/**
	 * Creates a new conversation
	 */
	public Conversation(){
		conversation = new ArrayList<Utterance>();
		stringRep = "";
	}
	
	/**
	 * Adds an utterance to the conversation
	 * @param u the new utterance to be added
	 */
	public void addUtterance(Utterance u){
		conversation.add(u);
		stringRep += u.utterance + "\n";
	}
	
	/**
	 * Returns a string representation of the entire conversation
	 * @return the conversation
	 */
	public String getStringRepresentation(){
		return stringRep;
	}
	
	/**
	 * Returns a list representation of the conversation
	 * @return a list containing all utterances from the conversation so far 
	 */
	public List<Utterance> getConversation(){
		return conversation;
	}
}
