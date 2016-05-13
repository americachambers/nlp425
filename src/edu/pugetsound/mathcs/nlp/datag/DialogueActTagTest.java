package edu.pugetsound.mathcs.nlp.datag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for the DialogueActTag enum
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
public class DialogueActTagTest {

	@Test
	/**
	 * Tests the fromLabel() method of DialogueActTag
	 */
	public void test() {
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("qw"),
				DialogueActTag.QUESTION_WH);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("na"),
				DialogueActTag.DESCRIPTIVE_AFFIRMATIVE_ANSWER);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("^d"),
				DialogueActTag.DECLARATIVE_QUESTION);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("^g"),
				DialogueActTag.TAG_QUESTION);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("*"),
				DialogueActTag.COMMENT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notFound() {
		DialogueActTag.fromLabel("AYYY");
	}

}
