package edu.pugetsound.mathcs.nlp.processactions;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * @author Thomas Gagne
 *
 */
public class ResponseGenerator {
    
    /**
     * Takes in an Utterance and a DA tag for what type of statement to respond from the MDP
     * Returns a string corresponding to the generated response
     * @return A string representation of the response. In early versions, this might be an AMR
     */
    public String generateResponse(Utterance utterance, DialogueActTag daTag) {
        // Use the given daTag to determine what type of response to generate
    }

}
