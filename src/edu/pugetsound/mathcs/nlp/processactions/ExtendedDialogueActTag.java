package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/**
 * @author Thomas Gagne
 * A list of extended DA tags, based off Chili's DA tags
 * These are more general, and we've collapsed tags such as YES and NO into a single tag
 */
public enum ExtendedDialogueActTag {
    STATEMENT("STATEMENT"),
    NARRATIVE_DESCRIPTIVE("NARRATIVE_DESCRIPTIVE"),
    VIEWPOINT("VIEWPOINT"),
    BACKCHANNEL("BACKCHANNEL"),
    AGREEMENT_DISAGREEMENT("AGREEMENT_DISAGREEMENT"),
    DECLARATIVE_QUESTION("DECLARATIVE_QUESTION"),
    TAG_QUESTION("TAG_QUESTION"),
    ACTION_DIRECTIVE("ACTION_DIRECTIVE"),
    ACCEPT_REJECT_MAYBE("ACCEPT_REJECT_MAYBE"),
    REPEAT_PHRASE("REPEAT_PHRASE"),
    ASSESSMENT_APPRECIATION("ASSESSMENT_APPRECIATION"),
    DOWNPLAYING_SYMPATHY("DOWNPLAYING_SYMPATHY"),
    REFORMULATE_SUMMARIZE("REFORMULATE_SUMMARIZE"),
    RHETORICAL_QUESTION_CONTINUER("RHETORICAL_QESTION_CONTINUER"),
    ACKNOWLEDGE_ANSWER("ACKNOWLEDGE_ANSWER"),
    SIGNAL_NON_UNDERSTANDING("SIGNAL_NON_UNDERSTANDING"),
    SIGNAL_NON_UNDERSTANDING_MIMIC("SIGNAL_NON_UNDERSTANDING_MIMIC"),
    SYMPATHETIC_COMMENT("SYMPATHETIC_COMMENT"),
    COMMIT("COMMIT"),
    APOLOGY("APOLOGY"),
    CONVENTIONAL_CLOSING("CONVENTIONAL_CLOSING"),
    EXCLAMATION("EXCLAMATION"),
    CONVENTIONAL_OPENING("CONVENTIONAL_OPENING"),
    THANKS("THANKS"),
    WELCOME("WELCOME"),
    OPEN_OPTION("OPEN_OPTION"),
    YES_NO_ANSWER("YES_NO_ANSWER"),
    QUESTION("QUESTION"),
    QUESTION_YES_NO("QUESTION_YES_NO"),
    QUESTION_WH("QUESTION_WH"),
    GREETING("GREETING");

    private String name;

    ExtendedDialogueActTag(String name) {
        this.name = name;
    }


    private static final HashMap<DialogueActTag, ExtendedDialogueActTag> daToXDA = new HashMap<DialogueActTag, ExtendedDialogueActTag>() {{
        // put( someDATag, itsCorrospondingXDATag);
    }};

    public static ExtendedDialogueActTag getXDATag(DialogueActTag daTag) {
        if (daToXDA.containsKey(daTag))
            return daToXDA.get(daTag);
        return GREETING;
    }
        

}
