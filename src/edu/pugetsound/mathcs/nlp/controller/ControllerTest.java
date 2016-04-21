package edu.pugetsound.mathcs.nlp.controller;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class ControllerTest {
	/**
	 * Allows us to unit test input/output functionality
	 * This is the input stream which is set to a string
	 */
	private ByteArrayInputStream in;
	private ByteArrayOutputStream outputArray;
	private PrintStream outputStream;


	@Before
	public void setUp() throws Exception {
		String input = "Is Fluffy a cat?";
		in = new ByteArrayInputStream(input.getBytes());
		outputArray = new ByteArrayOutputStream();
		outputStream = new PrintStream(outputArray);
		Controller.setup(in, outputStream);
	}

	/**
	 * TODO: Use reflection to replace this with a loop over the
	 * instance variables (whatever they may be)
	 */
	@Test
	public void testSetup() {
		assertNotNull(Controller.path);
		assertNotNull(Controller.conversation);	
		assertNotNull(Controller.input);
		assertNotNull(Controller.analyzer);
		assertNotNull(Controller.mdp);
		assertNotNull(Controller.hyperVariables);
	}

	@Test
	public void testGetBasePath(){
		String delimiter = "/";

		String path1 = "nlp425";			// should be nlp425/
		String path2 = "nlp425/";			// should be nlp425/
		String path3 = "nlp425/build";		// should be nlp425/build/../
		String path4 = "nlp425/build/";		// should be nlp425/build/../

		String result1 = "";
		String result2 = "";
		String result3 = "";
		String result4 = "";

		try{
			result1 = Controller.getBasePath(path1, delimiter);
			result2 = Controller.getBasePath(path2, delimiter);
			result3 = Controller.getBasePath(path3, delimiter);
			result4 = Controller.getBasePath(path4, delimiter);
		}
		catch(IOException e){
			fail();
		}

		assertEquals(path1 + delimiter , result1);
		assertEquals(path2, result2);
		assertEquals(path3 + delimiter + ".." + delimiter, result3);
		assertEquals(path4 + ".." + delimiter , result4);				
	}

	@Test(expected=IOException.class)
	public void testGetBasePathException() throws IOException {
		String path = "gibberish";	
		Controller.getBasePath(path, "/");
	}



	@Test
	public void testRespondToUser() {
		Utterance utt = new Utterance("hello");
		Controller.respondToUser(utt);		
		String printedResponse = outputArray.toString().trim();
		assertEquals("Agent: hello", printedResponse);
	}

	@Test
	public void testInitiateGreeting() {
		outputArray.reset();
		Controller.initiateGreeting();

		String printedResponse = outputArray.toString().trim();
		assertEquals("Agent: " + Controller.INITIAL_GREETING, printedResponse);

		List<Utterance> convo = Controller.conversation.getConversation();			
		assertEquals(1, convo.size());		
	}

}
