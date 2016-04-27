package edu.pugetsound.mathcs.nlp.processactions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.pugetsound.mathcs.nlp.processactions.srt.*;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.processactions.ActionProcessor;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;

public class ActionProcessorTest {

    private TextAnalyzer analyzer;
    private Conversation conversation;
    private String statement;
    private String question;
    private String empty;
    private Utterance utt;
    private DAClassifier daClass;


    @Before
    public void setUp() throws Exception {
        analyzer = new TextAnalyzer();
        conversation = new Conversation();
        statement = "The cat ate the fish";
        question = "What are you doing?";
        empty = "";
        utt = analyzer.analyze(statement, conversation);
        daClass = new DAClassifier(DAClassifier.Mode.DECISION_TREE);
        daClass.classify(utt, conversation);
    }

    @Test
    public void testResponseTagMapping() {
        conversation = new Conversation();
        conversation.addUtterance(utt);
        for(ResponseTag datag : ResponseTag.values()) {
            String response = ActionProcessor.generateResponse(conversation, datag);
            assertNotNull("should not be null", response);
            assertFalse("failure - response should not be \"Error: Response could not be generated, bad extendedDaTag\"",
                        response.equals("Error: Response could not be generated, bad extendedDA tag"));
        }

    }

	// @Test
	// public void testEmpty() {
	// 	Utterance utt = analyzer.analyze(empty, conversation);
	// 	assertEquals(empty, utt.utterance);
	// 	assertFalse(utt.isPassive);
	// 	assertEquals(0, utt.tokens.size());
	// 	assertEquals(Punctuation.UNKNOWN, utt.punct);
	// 	assertNull(utt.constituencyParse);
	// 	assertNull(utt.dependencyParse);
	// 	assertEquals(0, utt.subjects.size());
	// 	assertEquals(0, utt.directObjects.size());
	// 	assertNull(utt.rootConstituency);
	// 	assertNull(utt.rootDependency);
	// }

	// @Test
	// public void testQuestion() {
	// 	Utterance utt = analyzer.analyze(question, conversation);
	// 	assertEquals(question, utt.utterance);
	// 	assertFalse(utt.isPassive);
	// 	assertEquals(4, utt.tokens.size());
	// 	assertEquals(Punctuation.QUEST_MARK, utt.punct);
	// 	assertEquals(
	// 		"(ROOT (SBARQ (WHNP (WP What)) (SQ (VBP are) (NP (PRP you)) (VP (VBG doing))) (. ?)))",
	// 		utt.constituencyParse.toString());
	// 	assertEquals(1, utt.subjects.size());
	// 	assertEquals(1, utt.directObjects.size());
	// 	assertEquals("you", utt.subjects.get(0));
	// 	assertEquals("What", utt.directObjects.get(0));
	// 	assertEquals("SBARQ", utt.rootConstituency);
	// 	assertEquals("doing", utt.rootDependency);
	// }

	// @Test(expected=IllegalArgumentException.class)
	// public void testUtteranceNull(){
	// 	analyzer.analyze(null, conversation);
	// }

	// @Test(expected=IllegalArgumentException.class)
	// public void testConversationNull(){
	// 	analyzer.analyze("",null);
	// }
}
