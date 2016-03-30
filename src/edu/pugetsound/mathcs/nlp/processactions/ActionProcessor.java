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
            put(DialogueActTag.FORWARD_LOOKING, ArbitraryTemplate);
            put(DialogueActTag.AGREEMENTS, AgreementTemplate);
            put(DialogueActTag.INDETERMINATE, ArbitraryTemplate);
            put(DialogueActTag.COMMENT, CommentTemplate);
            put(DialogueActTag.CONTINUED_FROM_PREVIOUS, ArbitraryTemplate);
            put(DialogueActTag.COLLABORATIVE_COMPLETION, ArbitraryTemplate);
            put(DialogueActTag.ABOUT_COMMUNICATION, ArbitraryTemplate);
            put(DialogueActTag.DECLARATIVE_QUESTION, DeclarativeQuestionTemplate);
            put(DialogueActTag.ELABORATED_REPLY_Y_N_QUESTION, ArbitraryTemplate);
            put(DialogueActTag.TAG_QUESTION, ArbitraryTemplate);
            put(DialogueActTag.HOLD, ArbitraryTemplate);
            put(DialogueActTag.MIMIC_OTHER, ArbitraryTemplate);
            put(DialogueActTag.QUOTATION, ArbitraryTemplate);
            put(DialogueActTag.REPEAT_SELF, ArbitraryTemplate);
            put(DialogueActTag.ABOUT_TASK, ArbitraryTemplate);
            put(DialogueActTag.ACCEPT_PART, ArbitraryTemplate);
            put(DialogueActTag.ACTION_DIRECTIVE, ArbitraryTemplate);
            put(DialogueActTag.ACCEPT, AcceptTemplate);
            put(DialogueActTag.MAYBE, MaybeTemplate);
            put(DialogueActTag.REJECT, RejectTemplate);
            put(DialogueActTag.REJECT_PART, ArbitraryTemplate);
            put(DialogueActTag.CONTINUER, ArbitraryTemplate);
            put(DialogueActTag.REPEAT_PHRASE, ArbitraryTemplate);
            put(DialogueActTag.ASSESSMENT_APPRECIATION, ArbitraryTemplate);
            put(DialogueActTag.CORRECT_MISSPEAKING, ArbitraryTemplate);
            put(DialogueActTag.DOWNPLAYING_SYMPATHY, DownplaySympathyTemplate);
            put(DialogueActTag.REFORMULATE_SUMMARIZE, ReformulateTemplate);
            put(DialogueActTag.RHETORICAL_QUESTION_CONTINUER, ArbitraryTemplate);
            put(DialogueActTag.ACKNOWLEDGE_ANSWER, ArbitraryTemplate);
            put(DialogueActTag.SIGNAL_NON_UNDERSTANDING, NonUnderstandingTemplate);
            put(DialogueActTag.SIGNAL_NON_UNDERSTANDING_MIMIC, NonUnderstandingTemplate);
            put(DialogueActTag.NON_UNDERSTANDING_MEDIUM, NonUnderstandingTemplate);
            put(DialogueActTag.SYMPATHETIC_COMMENT, SympathyTemplate);
            put(DialogueActTag.COMMIT, ArbitraryTemplate);
            put(DialogueActTag.OFFER, ArbitraryTemplate);
            put(DialogueActTag.APOLOGY, ApologyTemplate);
            put(DialogueActTag.CONVENTIONAL_CLOSING, ConventionalClosingTemplate);
            put(DialogueActTag.EXCLAMATION, ExclamationTemplate);
            put(DialogueActTag.OTHER_FORWARD_FUNCTION, ArbitraryTemplate);
            put(DialogueActTag.CONVENTIONAL_OPENING, ConventionalOpeningTemplate);
            put(DialogueActTag.THANKS, ThanksTemplate);
            put(DialogueActTag.WELCOME, WelcomeTemplate);
            put(DialogueActTag.EXPLICIT_PERFORMATIVE, ArbitraryTemplate);
            put(DialogueActTag.DESCRIPTIVE_AFFIRMITIVE, ArbitraryTemplate);
            put(DialogueActTag.ANSWER_DISPREFERRED, AnswerDispreferredTemplate);
            put(DialogueActTag.DESCRIPTIVE_NEGATIVE_ANSWER, NoTemplate);
            put(DialogueActTag.NO, NoTemplate);
            put(DialogueActTag.INDETERMINATE_RESPONSE, ArbitraryTemplate);
            put(DialogueActTag.YES, YesTemplate);
            put(DialogueActTag.OTHER, ArbitraryTemplate);
            put(DialogueActTag.OPEN_OPTION, ArbitraryTemplate);
            put(DialogueActTag.QUESTION_RHETORICAL, QuestionTemplate);
            put(DialogueActTag.QUESTION_OPEN_ENDED, QuestionTemplate);
            put(DialogueActTag.QUESTION_ALTERNATIVE, QuestionTemplate);
            put(DialogueActTag.QUESTION_YES_NO_OR, QuestionTemplate);
            put(DialogueActTag.QUESTION_WH, WhQuestionTemplate);
            put(DialogueActTag.QUESTION_YES_NO, YesNoQuestionTemplate);
            put(DialogueActTag.NARRATIVE_DESCRIPTIVE, ArbitraryTemplate);
            put(DialogueActTag.VIEWPOINT, ArbitraryTemplate);
            put(DialogueActTag.TALK_SELF, ArbitraryTemplate);
            put(DialogueActTag.TALK_THIRD_PARTY, ArbitraryTemplate);
            put(DialogueActTag.NON_SPEECH, ArbitraryTemplate);
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
        
        SemanticResponseTemplate responseGenerator = DaTagToSRT.get(daTag);
        
        if(responseGenerator != null) {
            return responseGenerator.constructResponseFromTemplate(utterance);
        }

        // Should probably throw an excetion here
        return "Error: Response could not be generated, bad DA tag";
    }


}
