package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * @author Thomas Gagne
 *
 */
public class ActionProcessor {

    private static final HashMap<DialogueActTag, SemanticResponseTemplate> daTagToSRT =
        new HashMap<DialogueActTag, SemanticResponseTemplate>() {{
            // Instantiate HashMap's values
            // Any DA tag which we don't know how to generate for now
            // will simply go to the ArbitraryTemplate
            
            put(DialogueActTag.QUESTION, QuestionTemplate);
            put(DialogueActTag.STATEMENT, StatementTemplate);
            put(DialogueActTag.BACKCHANNEL, BackchannelTemplate);
            put(DialogueActTag.ForwardLooking, ArbitraryTemplate);
            put(DialogueActTag.AGREEMENTS, AgreementTemplate);
            put(DialogueActTag.INDETERMINATE, ArbitraryTemplate);
            put(DialogueActTag.COMMENT, CommentTemplate);
            put(DialogueActTag.CONTINUED_FROM_PREVIOUS, ArbitraryTemplate);
            put(DialogueActTag.COLLABORATIVE_COMPLETION, ArbitraryTemplate);
            put(DialogueActTag.ABOUT_COMMUNICATION, ArbitraryTemplate);
            put(DialogueActTag.DECLARATIVE_QUESTION, DeclarativeQuestionTemplate);
            put(DialogueActTag.ELABORATED_RELPLY_Y_N_QUESTION, ArbitraryTemplate);
            put(DialogueActTag.TAG_QUESTION, ArbitraryTemplate);
            put(DialogueActTag.HOLD, ArbitraryTemplate);
            put(DialogueActTag.MIMIC_OTHER, ArbitraryTemplate);
            put(DialogueActTag.QUOTATION, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            put(DialogueActTag.STATEMENT, ArbitraryTemplate);
            
            
        }}
    
    /**
     * Takes in an Utterance and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Utterance utterance, DialogueActTag daTag) {
        // Use the given daTag to determine what type of response to generate

        // I'm going on the assumption that the MDP team will hand us one of the daTags described
        // in Chili's DialogueActTag
        
        SemanticResponseTemplate responseGenerator = null;
        
        switch(daTag) {
        }

        if(responseGenerator != null) {
            return responseGenerator.constructResponseFromTemplate(utterance);
        }

        // Should probably throw an excetion here
        return "Error: Response could not be generated";
    }


}
