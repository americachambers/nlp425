package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;
import java.lang.ProcessBuilder;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import edu.pugetsound.mathcs.nlp.processactions.srt.*;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.pugetsound.mathcs.nlp.features.*;

/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * @author Thomas Gagne
 */
public class ActionProcessor {

    private static final HashMap<ResponseTag, SemanticResponseTemplate> responseTagToSRT =
        new HashMap<ResponseTag, SemanticResponseTemplate>() {{
            // Instantiate HashMap's values
            
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


    /*
     * Verify that the conversation given to us has all past utterances classified with a DA tag and AMR
     * Never trust other people with your own data validation!!! :)
     */
    private static void verifyConversation(Conversation convo) {
        DAClassifier classifier = new DAClassifier();
        for(Utterance utt: convo.getConversation()) {
            if (utt.daTag == null) 
                utt.daTag = classifier.classify(utt, convo);
            if (utt.amr == null) 
                utt.amr = AMR.convertTextToAMR(utt.utterance)[0];
        }
    }

    
    /**
     * Wrapper function that converts an utterance to a conversation
     * For backwards compatability only; use the one that takes a conversation preferably!
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Utterance utterance, DialogueActTag responseDATag) {
        Conversation convo = new Conversation();
        convo.addUtterance(utterance);
        return generateResponse(convo, responseDATag);
    }


    /**
     * Takes in a conversation and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public static String generateResponse(Conversation convo, DialogueActTag responseDATag) {
        verifyConversation(convo);
        ResponseTag responseTag = ResponseTag.getResponseTag(responseDATag);
        SemanticResponseTemplate responseGenerator = responseTagToSRT.get(responseTag);        
        if(responseGenerator != null) {
            // Use the given daTag to determine what type of response to generate
            return responseGenerator.constructResponseFromTemplate(convo);
        }

        // Should probably throw an excetion here
        return "Error: Response could not be generated, bad extendedDA tag";
    }

    public static void main(String[] args){
        for (String a: args)
            System.out.println(generateResponse(new Utterance(a), DialogueActTag.WELCOME));
    }

}
