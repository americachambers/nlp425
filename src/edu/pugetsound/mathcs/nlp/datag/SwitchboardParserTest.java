package edu.pugetsound.mathcs.nlp.datag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the SwitchboardParser class
 * @author Creavesjohnson
 * @version 05/13/2016
 */
public class SwitchboardParserTest {

	private File switchboardFile = null;

	@Before
	/**
	 * Grab the test file from the test/ directory
	 * @throws Exception if the test file does not exist
	 */
	public void setUp() throws Exception {
		switchboardFile = new File("test/SwitchboardParserTest.utt");
	}

	@Test
	/**
	 * Tests the SwitchboardParser for proper tag counts, tag correctness, and token correctness
	 * @throws FileNotFoundException if the test .utt file does not exist
	 */
	public void test() throws FileNotFoundException {
		SwitchboardParser parser = new SwitchboardParser(switchboardFile);

		assertEquals("Improper number of tags parsed.", parser.getActs().size(), 7);

		assertEquals("Improper act subset size.",
				parser.getActs(DialogueActTag.DECLARATIVE_QUESTION).size(), 5);
		assertEquals("Improper act subset size.", parser.getActs(DialogueActTag.OTHER).size(), 1);
		assertEquals("Improper act subset size.",
				parser.getActs(DialogueActTag.QUESTION_WH).size(), 1);

		assertEquals("Improper subset tag.", parser.getActs(DialogueActTag.DECLARATIVE_QUESTION)
				.get(0).getTag(), DialogueActTag.DECLARATIVE_QUESTION);
		assertEquals("Improper subset tag.", parser.getActs(DialogueActTag.OTHER).get(0).getTag(),
				DialogueActTag.OTHER);
		assertEquals("Improper subset tag.", parser.getActs(DialogueActTag.QUESTION_WH).get(0)
				.getTag(), DialogueActTag.QUESTION_WH);

		assertNotNull("Null TokenIndexMap.", parser.getTokenIndexMap());
		assertEquals("Improper number of tokens in index map.", parser.getTokenIndexMap().size(), 9);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("wow"), 0);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("look"), 1);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("at"), 2);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("this"), 3);
		assertEquals("Improper token index.", parser.getTokenIndexMap()
				.indexForToken("declarative"), 4);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("question"),
				5);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("so"), 6);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("i"), 7);
		assertEquals("Improper token index.", parser.getTokenIndexMap().indexForToken("guess"), 8);

	}

}
