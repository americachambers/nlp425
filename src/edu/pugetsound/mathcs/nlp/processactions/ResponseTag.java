package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/**
 * A list of extended DA tags, based off Chili's DA tags
 * These are more general, and we've collapsed tags such as YES and NO into a single tag
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 */
public enum ResponseTag {
    STATEMENT("STATEMENT"),
    BACKCHANNEL("BACKCHANNEL"),
    REPEAT_PHRASE("REPEAT_PHRASE"),
    SIGNAL_NON_UNDERSTANDING("SIGNAL_NON_UNDERSTANDING"),
    SYMPATHETIC_COMMENT("SYMPATHETIC_COMMENT"),
    APOLOGY("APOLOGY"),
    CONVENTIONAL_CLOSING("CONVENTIONAL_CLOSING"),
    CONVENTIONAL_OPENING("CONVENTIONAL_OPENING"),
    THANKS("THANKS"),
    WELCOME("WELCOME"),
    YES_NO_ANSWER("YES_NO_ANSWER"),
    QUESTION_YES_NO("QUESTION_YES_NO"),
    QUESTION_WH("QUESTION_WH");

    private String name;

    /**
     * Constructor for ResponseTag
     * @param name The string name of this reponse tag
     */
    ResponseTag(String name) {
        this.name = name;
    }
}
