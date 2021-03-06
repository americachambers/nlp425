package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.util.Logger;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import edu.pugetsound.mathcs.nlp.processactions.srt.*;

import java.io.IOException;

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
            put(ResponseTag.BACKCHANNEL, new BackchannelTemplate());
            put(ResponseTag.REPEAT_PHRASE, new RepeatPhraseTemplate());
            put(ResponseTag.SIGNAL_NON_UNDERSTANDING, new NonUnderstandingTemplate());
            put(ResponseTag.SYMPATHETIC_COMMENT, new SympathyTemplate());
            put(ResponseTag.APOLOGY, new ApologyTemplate());
            put(ResponseTag.CONVENTIONAL_CLOSING, new ConventionalClosingTemplate());
            put(ResponseTag.CONVENTIONAL_OPENING, new ConventionalOpeningTemplate());
            put(ResponseTag.THANKS, new ThanksTemplate());
            put(ResponseTag.WELCOME, new WelcomeTemplate());
            put(ResponseTag.YES_NO_ANSWER, new YesNoAnswerTemplate());
            put(ResponseTag.QUESTION_YES_NO, new YesNoQuestionTemplate());
            put(ResponseTag.QUESTION_WH, new WhQuestionTemplate());
        }};

    /**
     * Takes in a conversation and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @param convo The conversation thus far, so we can use local info to generate the response
     * @param responseTag The type of response we should respond with. Ex: YesNoAnswer
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Conversation convo, ResponseTag responseTag,
                                          KBController kb) {
        SemanticResponseTemplate responseGenerator = RESPONSE_TAG_TO_SRT.get(responseTag);
        //SemanticResponseTemplate responseGenerator = new YesNoAnswerTemplate();
        if(responseGenerator != null) {
            try {
                String response = null;
                if(DUMB_RESPONSES) {
                    response = responseGenerator.constructDumbResponse(convo, kb);
                } else {
                    response = responseGenerator.constructSmartResponse(convo, kb);
                }

                if(response == null) {
                    if(Logger.debug()) {
                        System.out.println("Fatal error in ActionProcessor: null response!");
                    }
                    return null;
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
        } else {
            if(Logger.debug()) {
                System.out.println("Error: no response template found for: " + responseTag);
            }

            return null;
        }
    }

    /**
     * Generates a list of responses to an inputted conversation.
     * @param args A list of Strings should be given, each being 1 line of user input in the convo
     */
    public static void main(String[] args) throws IOException {
        TextAnalyzer ta = new TextAnalyzer(null);
        Conversation convo = new Conversation();
        for (String a: args) {
            convo.addUtterance(ta.analyze(a,convo));
        }
        for (Utterance utt: convo.getConversation()){
            // System.out.println(generateResponse(utt, ResponseTag.GREETING));
        }
    }

}
