package edu.pugetsound.mathcs.nlp.datag;

import static org.junit.Assert.*;

import org.junit.Test;

public class DialogueActTagTest {

	@Test
	public void test() {
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("qw"), DialogueActTag.QUESTION_WH);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("na"), DialogueActTag.DESCRIPTIVE_AFFIRMATIVE_ANSWER);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("^d"), DialogueActTag.DECLARATIVE_QUESTION);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("^g"), DialogueActTag.TAG_QUESTION);
		assertEquals("Returned incorrect enum item.", DialogueActTag.fromLabel("*"), DialogueActTag.COMMENT);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notFound() {
		DialogueActTag.fromLabel("AYYY");
	}

}
