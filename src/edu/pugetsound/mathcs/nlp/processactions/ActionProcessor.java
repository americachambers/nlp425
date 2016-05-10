package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;
import java.io.IOException;
import java.lang.ProcessBuilder;

import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import edu.pugetsound.mathcs.nlp.processactions.srt.*;
import edu.pugetsound.mathcs.nlp.controller.Controller;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.pugetsound.mathcs.nlp.features.*;

import edu.pugetsound.mathcs.nlp.util.Logger;

/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * Every method of this class is static
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 */
public class ActionProcessor {

    private static final boolean DUMB_RESPONSES = true;

    private static final HashMap<ResponseTag, SemanticResponseTemplate> RESPONSE_TAG_TO_SRT =
        new HashMap<ResponseTag, SemanticResponseTemplate>() {{
            // Instantiate the HashMap's values

            put(ResponseTag.STATEMENT, new StatementTemplate());
            put(ResponseTag.NARRATIVE_DESCRIPTIVE, new StatementNonOpinionTemplate());
            put(ResponseTag.VIEWPOINT, new StatementOpinionTemplate());
            put(ResponseTag.BACKCHANNEL, new BackchannelTemplate());
            put(ResponseTag.AGREEMENT_DISAGREEMENT, new AgreementDisagreementTemplate());
            put(ResponseTag.DECLARATIVE_QUESTION, new DeclarativeQuestionTemplate());
            put(ResponseTag.TAG_QUESTION, new TagQuestionTemplate());
            put(ResponseTag.ACTION_DIRECTIVE, new ActionDirectiveTemplate());
            put(ResponseTag.ACCEPT_REJECT_MAYBE, new AcceptRejectMaybeTemplate());
            put(ResponseTag.REPEAT_PHRASE, new RepeatPhraseTemplate());
            put(ResponseTag.ASSESSMENT_APPRECIATION, new AssessmentAppreciationTemplate());
            put(ResponseTag.DOWNPLAYING_SYMPATHY, new DownplaySympathyTemplate());
            put(ResponseTag.REFORMULATE_SUMMARIZE, new ReformulateTemplate());
            put(ResponseTag.RHETORICAL_QUESTION_CONTINUER, new RhetoricalQuestionContinuer());
            put(ResponseTag.ACKNOWLEDGE_ANSWER, new AcknowledgeAnswerTemplate());
            put(ResponseTag.SIGNAL_NON_UNDERSTANDING, new NonUnderstandingTemplate());
            put(ResponseTag.SIGNAL_NON_UNDERSTANDING_MIMIC, new NonUnderstandingMimicTemplate());
            put(ResponseTag.SYMPATHETIC_COMMENT, new SympathyTemplate());
            put(ResponseTag.COMMIT, new CommitTemplate());
            put(ResponseTag.APOLOGY, new ApologyTemplate());
            put(ResponseTag.CONVENTIONAL_CLOSING, new ConventionalClosingTemplate());
            put(ResponseTag.EXCLAMATION, new ExclamationTemplate());
            put(ResponseTag.CONVENTIONAL_OPENING, new ConventionalOpeningTemplate());
            put(ResponseTag.THANKS, new ThanksTemplate());
            put(ResponseTag.WELCOME, new WelcomeTemplate());
            put(ResponseTag.OPEN_OPTION, new OpenOptionTemplate());
            put(ResponseTag.YES_NO_ANSWER, new YesNoAnswerTemplate());
            put(ResponseTag.QUESTION, new QuestionTemplate());
            put(ResponseTag.QUESTION_YES_NO, new YesNoQuestionTemplate());
            put(ResponseTag.QUESTION_WH, new WhQuestionTemplate());
            put(ResponseTag.GREETING, new GreetingTemplate());
        }};

    /**
     * Wrapper function that converts an utterance to a conversation
     * For backwards compatability only; use the one that takes a conversation preferably!
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Utterance utterance, ResponseTag responseDATag) {
        Conversation convo = new Conversation();
        convo.addUtterance(utterance);
        return generateResponse(convo, responseDATag);
    }

    /**
     * Takes in a conversation and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @param convo The conversation thus far, so we can use local info to generate the response
     * @param responseTag The type of response we should respond with. Ex: YesNoAnswer
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Conversation convo, ResponseTag responseTag) {
        SemanticResponseTemplate responseGenerator = RESPONSE_TAG_TO_SRT.get(responseTag);
        //SemanticResponseTemplate responseGenerator = new QuestionTemplate();
        if(responseGenerator != null) {
            // Use the given daTag to determine what type of response to generate
            try {
                String response = null;
                if(DUMB_RESPONSES) {
                    response = responseGenerator.constructDumbResponse(convo);
                } else {
                    response = responseGenerator.constructSmartResponse(convo);
                }

                if(response == null) {
                    return "Not goooood";
                } else {
                    System.out.println("Response: " + response);
                    return response;
                }
            } catch (Exception e) {
                if(Logger.debug()) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
            return "Sorry, I didn't understand that.";
        }

        // Should probably throw an exception here
        return "Error: Response could not be generated, bad extendedDA tag";
    }

    /**
     * Generates a list of responses to an inputted conversation.
     * @param args A list of Strings should be given, each being 1 line of user input in the convo
     */
    public static void main(String[] args) throws IOException {
        TextAnalyzer ta = new TextAnalyzer();
        Conversation convo = new Conversation();
        for (String a: args) {
            convo.addUtterance(ta.analyze(a,convo));
        }
        for (Utterance utt: convo.getConversation()){
            System.out.println(generateResponse(utt, ResponseTag.GREETING));
        }
    }

}
