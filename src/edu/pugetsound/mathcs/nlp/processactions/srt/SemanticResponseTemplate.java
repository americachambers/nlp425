package edu.pugetsound.mathcs.nlp.processactions.srt;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.util.Logger;

import edu.pugetsound.mathcs.nlp.kb.KBController;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.AMRParser;
import edu.pugetsound.mathcs.nlp.util.PathFormat;


//Requires Simple Json: https://code.google.com/archive/p/json-simple/
//https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar
import org.json.simple.*;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.Arrays;

/**
 * The abstract class every response template should extend
 * All templates need to be able to take in an utterance and return a String response.
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 */
public abstract class SemanticResponseTemplate {

    // A mapping from template names to a each template's list of dumb responses
    // This mapping is read once by SemanticResponseTemplate and is shared by every
    // extending template
    private static HashMap<String, String[]> allDumbResponses = ResponseLoader.loadDumbResponses();

    // A list of all possible dumb responses for this template
    private String[] dumbOutputs =
        SemanticResponseTemplate.allDumbResponses.get(this.getClass().getSimpleName());

    /**
     * @author Thomas Gagne & Jon Sims
     * Returns a response to a user's utterance in string form.
     * The dumb response generator chooses from a list of preconstructed responses.
     *
     * @param convo The conversation thus far.
     * @return A string response.
     */
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Random rand = new Random();
        //Utterance utterance = convo.getLastUtterance();
        //AMR amr = (AMR) dumbOutputs.keySet().toArray()[rand.nextInt(dumbOutputs.size())];
        //return AMRParser.convertAMRToText(amr, dumbOutputs.get(amr));
        if(dumbOutputs.length > 0) {
            return dumbOutputs[rand.nextInt(dumbOutputs.length)];
        } else {
            if(Logger.debug()) {
                System.out.println("Error: tried to construct a dumb response with an empty " +
                                   "response list! Did you not override constructDumbResponse()?");
            }
            return null;
        }
    }

    /**
     * @author Thomas Gagne & Jon Sims
     * Returns a response to a user's utterance in string form.
     * The smart response generator will attempt to use information from the what the user said
     * in order to create a unique response.
     * Unless this method is overridden, it will return a response from the dumb generator.
     * Most response templates are simple and therefore won't override this method.
     *
     * @param convo The conversation thus far.
     * @return A string response.
     */
    public String constructSmartResponse(Conversation convo, KBController kb) {
        return constructDumbResponse(convo, kb);
    }

}
