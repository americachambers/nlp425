package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.AMRParser;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.QuestionTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a declarative question to the user.
 * The form and content of the question is dependent on the user's input, and this typically
 * returns something similar to a rhetorical or incredulous response.
 * An example is: INPUT: "I ate the whole cat." OUTPUT: "You ate the whole cat?"
 */
public class DeclarativeQuestionTemplate extends SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Utterance utterance = convo.getLastUtterance();
        return new QuestionTemplate().constructResponseFromTemplate(convo);
    }

}
