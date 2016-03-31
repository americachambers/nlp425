package edu.pugetsound.mathcs.nlp.processactions;

import edu.pugetsound.mathcs.nlp.processactions.srt.*;
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
            
            put(DialogueActTag.QUESTION, new QuestionTemplate());
            put(DialogueActTag.STATEMENT, new StatementNonOpinionTemplate());
            put(DialogueActTag.BACKCHANNEL, new BackchannelTemplate());
            put(DialogueActTag.FORWARD_LOOKING, new ArbitraryTemplate());
            put(DialogueActTag.AGREEMENTS, new AgreementTemplate());
            put(DialogueActTag.INDETERMINATE, new ArbitraryTemplate());
            put(DialogueActTag.COMMENT, new ArbitraryTemplate());
            put(DialogueActTag.CONTINUED_FROM_PREVIOUS, new ArbitraryTemplate());
            put(DialogueActTag.COLLABORATIVE_COMPLETION, new ArbitraryTemplate());
            put(DialogueActTag.ABOUT_COMMUNICATION, new ArbitraryTemplate());
            put(DialogueActTag.DECLARATIVE_QUESTION, new DeclarativeQuestionTemplate());
            put(DialogueActTag.ELABORATED_REPLY_Y_N_QUESTION, new ArbitraryTemplate());
            put(DialogueActTag.TAG_QUESTION, new TagQuestionTemplate());
            put(DialogueActTag.HOLD, new ArbitraryTemplate());
            put(DialogueActTag.MIMIC_OTHER, new ArbitraryTemplate());
            put(DialogueActTag.QUOTATION, new ArbitraryTemplate());
            put(DialogueActTag.REPEAT_SELF, new ArbitraryTemplate());
            put(DialogueActTag.ABOUT_TASK, new ArbitraryTemplate());
            put(DialogueActTag.ACCEPT_PART, new AcceptPartTemplate());
            put(DialogueActTag.ACTION_DIRECTIVE, new ActionDirectiveTemplate());
            put(DialogueActTag.ACCEPT, new AcceptTemplate());
            put(DialogueActTag.MAYBE, new MaybeTemplate());
            put(DialogueActTag.REJECT, new RejectTemplate());
            put(DialogueActTag.REJECT_PART, new RejectPartTemplate());
            put(DialogueActTag.CONTINUER, new BackchannelTemplate());
            put(DialogueActTag.REPEAT_PHRASE, new RepeatPhraseTemplate());
            put(DialogueActTag.ASSESSMENT_APPRECIATION, new AssessmentAppreciationTemplate());
            put(DialogueActTag.CORRECT_MISSPEAKING, new ArbitraryTemplate());
            put(DialogueActTag.DOWNPLAYING_SYMPATHY, new DownplaySympathyTemplate());
            put(DialogueActTag.REFORMULATE_SUMMARIZE, new ReformulateTemplate());
            put(DialogueActTag.RHETORICAL_QUESTION_CONTINUER, new RhetoricalQuestionContinuer());
            put(DialogueActTag.ACKNOWLEDGE_ANSWER, new AcknowledgeAnswerTemplate());
            put(DialogueActTag.SIGNAL_NON_UNDERSTANDING, new NonUnderstandingTemplate());
            put(DialogueActTag.SIGNAL_NON_UNDERSTANDING_MIMIC, new NonUnderstandingMimicTemplate());
            put(DialogueActTag.NON_UNDERSTANDING_MEDIUM, new NonUnderstandingTemplate());
            put(DialogueActTag.SYMPATHETIC_COMMENT, new SympathyTemplate());
            put(DialogueActTag.COMMIT, new CommitTemplate());
            put(DialogueActTag.OFFER, new ArbitraryTemplate());
            put(DialogueActTag.APOLOGY, new ApologyTemplate());
            put(DialogueActTag.CONVENTIONAL_CLOSING, new ConventionalClosingTemplate());
            put(DialogueActTag.EXCLAMATION, new ExclamationTemplate());
            put(DialogueActTag.OTHER_FORWARD_FUNCTION, new ArbitraryTemplate());
            put(DialogueActTag.CONVENTIONAL_OPENING, new ConventionalOpeningTemplate());
            put(DialogueActTag.THANKS, new ThanksTemplate());
            put(DialogueActTag.WELCOME, new WelcomeTemplate());
            put(DialogueActTag.EXPLICIT_PERFORMATIVE, new ArbitraryTemplate());
            put(DialogueActTag.DESCRIPTIVE_AFFIRMATIVE_ANSWER, new YesTemplate());
            put(DialogueActTag.ANSWER_DISPREFERRED, new AnswerDispreferredTemplate());
            put(DialogueActTag.DESCRIPTIVE_NEGATIVE_ANSWER, new NoTemplate());
            put(DialogueActTag.NO, new NoTemplate());
            put(DialogueActTag.INDETERMINATE_RESPONSE, new IndeterminateResponseTemplate());
            put(DialogueActTag.YES, new YesTemplate());
            put(DialogueActTag.OTHER, new ArbitraryTemplate());
            put(DialogueActTag.OPEN_OPTION, new OpenOptionTemplate());
            put(DialogueActTag.QUESTION_RHETORICAL, new QuestionTemplate());
            put(DialogueActTag.QUESTION_OPEN_ENDED, new QuestionTemplate());
            put(DialogueActTag.QUESTION_ALTERNATIVE, new QuestionTemplate());
            put(DialogueActTag.QUESTION_YES_NO_OR, new QuestionTemplate());
            put(DialogueActTag.QUESTION_WH, new WhQuestionTemplate());
            put(DialogueActTag.QUESTION_YES_NO, new YesNoQuestionTemplate());
            put(DialogueActTag.NARRATIVE_DESCRIPTIVE, new StatementNonOpinionTemplate());
            put(DialogueActTag.VIEWPOINT, new StatementOpinionTemplate());
            put(DialogueActTag.TALK_SELF, new ArbitraryTemplate());
            put(DialogueActTag.TALK_THIRD_PARTY, new ArbitraryTemplate());
            put(DialogueActTag.NON_SPEECH, new ArbitraryTemplate());
        }};
    
    /**
     * Takes in an Utterance and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Utterance utterance, DialogueActTag daTag) {
        // Use the given daTag to determine what type of response to generate

        // I'm going on the assumption that the MDP team will hand us one of the daTags described
        // in Chili's DialogueActTag
        
        SemanticResponseTemplate responseGenerator = daTagToSRT.get(daTag);
        
        if(responseGenerator != null) {
            return responseGenerator.constructResponseFromTemplate(utterance);
        }

        // Should probably throw an excetion here
        return "Error: Response could not be generated, bad DA tag";
    }


}
