package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;
import java.lang.ProcessBuilder;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.processactions.ExtendedDialogueActTag;
import edu.pugetsound.mathcs.nlp.processactions.srt.*;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.pugetsound.mathcs.nlp.features.*;

/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * @author Thomas Gagne
 */
public class ActionProcessor {

    private static final HashMap<ExtendedDialogueActTag, SemanticResponseTemplate> xdaTagToSRT =
        new HashMap<ExtendedDialogueActTag, SemanticResponseTemplate>() {{
            // Instantiate HashMap's values
            
            put(ExtendedDialogueActTag.STATEMENT, new StatementTemplate());
            put(ExtendedDialogueActTag.NARRATIVE_DESCRIPTIVE, new StatementNonOpinionTemplate());
            put(ExtendedDialogueActTag.VIEWPOINT, new StatementOpinionTemplate());
            put(ExtendedDialogueActTag.BACKCHANNEL, new BackchannelTemplate());
            put(ExtendedDialogueActTag.AGREEMENT_DISAGREEMENT, new AgreementDisagreementTemplate());
            put(ExtendedDialogueActTag.DECLARATIVE_QUESTION, new DeclarativeQuestionTemplate());
            put(ExtendedDialogueActTag.TAG_QUESTION, new TagQuestionTemplate());
            put(ExtendedDialogueActTag.ACTION_DIRECTIVE, new ActionDirectiveTemplate());
            put(ExtendedDialogueActTag.ACCEPT_REJECT_MAYBE, new AcceptRejectMaybeTemplate());
            put(ExtendedDialogueActTag.REPEAT_PHRASE, new RepeatPhraseTemplate());
            put(ExtendedDialogueActTag.ASSESSMENT_APPRECIATION, new AssessmentAppreciationTemplate());
            put(ExtendedDialogueActTag.DOWNPLAYING_SYMPATHY, new DownplaySympathyTemplate());
            put(ExtendedDialogueActTag.REFORMULATE_SUMMARIZE, new ReformulateTemplate());
            put(ExtendedDialogueActTag.RHETORICAL_QUESTION_CONTINUER, new RhetoricalQuestionContinuer());
            put(ExtendedDialogueActTag.ACKNOWLEDGE_ANSWER, new AcknowledgeAnswerTemplate());
            put(ExtendedDialogueActTag.SIGNAL_NON_UNDERSTANDING, new NonUnderstandingTemplate());
            put(ExtendedDialogueActTag.SIGNAL_NON_UNDERSTANDING_MIMIC, new NonUnderstandingMimicTemplate());
            put(ExtendedDialogueActTag.SYMPATHETIC_COMMENT, new SympathyTemplate());
            put(ExtendedDialogueActTag.COMMIT, new CommitTemplate());
            put(ExtendedDialogueActTag.APOLOGY, new ApologyTemplate());
            put(ExtendedDialogueActTag.CONVENTIONAL_CLOSING, new ConventionalClosingTemplate());
            put(ExtendedDialogueActTag.EXCLAMATION, new ExclamationTemplate());
            put(ExtendedDialogueActTag.CONVENTIONAL_OPENING, new ConventionalOpeningTemplate());
            put(ExtendedDialogueActTag.THANKS, new ThanksTemplate());
            put(ExtendedDialogueActTag.WELCOME, new WelcomeTemplate());
            put(ExtendedDialogueActTag.OPEN_OPTION, new OpenOptionTemplate());
            put(ExtendedDialogueActTag.YES_NO_ANSWER, new YesNoAnswerTemplate());
            put(ExtendedDialogueActTag.QUESTION, new QuestionTemplate());
            put(ExtendedDialogueActTag.QUESTION_YES_NO, new YesNoQuestionTemplate());
            put(ExtendedDialogueActTag.QUESTION_WH, new WhQuestionTemplate());
            put(ExtendedDialogueActTag.GREETING, new GreetingTemplate());
        }};
    
    /**
     * Takes in an Utterance and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Utterance utterance, ExtendedDialogueActTag xdaTag) {
        // Use the given daTag to determine what type of response to generate

        SemanticResponseTemplate responseGenerator = xdaTagToSRT.get(xdaTag);
        

        if(responseGenerator != null) {
            return responseGenerator.constructResponseFromTemplate(utterance);
        }

        // Should probably throw an excetion here
        return "Error: Response could not be generated, bad extendedDA tag";
    }

}
