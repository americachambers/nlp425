package edu.pugetsound.mathcs.nlp.processactions.srt;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

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

/**
 * The abstract class every response template should extend
 * All templates need to be able to take in an utterance and return a String response.
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 */
public abstract class SemanticResponseTemplate {

    // A list of all possible default outputs
    private HashMap<AMR, String[]> outputs =
        SemanticResponseTemplate.responses.get(this.getClass().getSimpleName());

    public static HashMap<String, HashMap<AMR, String[]>> responses = new HashMap<String, HashMap<AMR, String[]>>() {{
        JSONParser parser = new JSONParser();
        try {
            String filePath = PathFormat.absolutePathFromRoot("src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json");
            JSONObject responsesJson = ((JSONObject)
                    parser.parse(
                        new FileReader(filePath)));
            Set<String> responsesTemplates = responsesJson.keySet();
            for (String responseTemplate: responsesTemplates) {
                HashMap<AMR, String[]> responsesMapping = new HashMap<AMR, String[]>();
                JSONObject responseTemplateJson = ((JSONObject) responsesJson.get(responseTemplate));
                for (Object o: responseTemplateJson.keySet() ) {
                    String keyStr = (String) o;
                    AMR key = AMRParser.parseAMRString(keyStr);
                    JSONArray valueJson = (JSONArray) responseTemplateJson.get(o);
                    String[] value = new String[valueJson.size()];
                    for (int i=0; i<value.length; i++)
                        value[i] = valueJson.get(i).toString();
                    responsesMapping.put(key, value);
                }
                put(responseTemplate, responsesMapping);
            }
        }
        catch (ParseException pe) {
            System.out.println("Parse error with responses.json file");
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
        catch (IOException ie) {
            System.out.println("Read error with responses.json file");
            System.out.println(ie);
        }
    }};

    /**
     * @author Thomas Gagne & Jon Sims
     * Returns a response to a user's utterance in string form.
     * The dumb response generator chooses from a list of preconstructed responses.
     *
     * @param convo The conversation thus far.
     * @return A string response.
     */
    public String constructDumbResponse(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        AMR amr = (AMR) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
        return AMRParser.convertAMRToText(amr, outputs.get(amr));
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
    public String constructSmartResponse(Conversation convo) {
        return constructDumbResponse(convo);
    }

}
