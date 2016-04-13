package edu.pugetsound.mathcs.nlp.processactions;

import java.util.HashMap;
import java.lang.ProcessBuilder;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
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
        ExtendedDialogueActTag xdaTag = ExtendedDialogueActTag.getXDATag(responseDATag);
        SemanticResponseTemplate responseGenerator = xdaTagToSRT.get(xdaTag);        
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
