package edu.pugetsound.mathcs.nlp.processactions.srt;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.util.Logger;

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

    // A list of all possible dumb responses
    // Read in from SemanticResponseTemplate's allDumbResponses for each extending class
    private String[] dumbOutputs =
        SemanticResponseTemplate.allDumbResponses.get(this.getClass().getSimpleName());

    // Read all the responses from dumbResponses.json and load them into a hashmap
    // The hashmap maps template name to an array of responses
    // The file is read once into SemanticResponseTemplate, each extending class takes the
    // String[] is needs when it's constructed.
    private static HashMap<String, String[]> allDumbResponses = new HashMap<String, String[]>() {{
            JSONParser parser = new JSONParser();
            try {
                String filePath = PathFormat.absolutePathFromRoot("src/edu/pugetsound/mathcs/nlp/" +
                                                                  "processactions/srt/dumbResponses.json");
                JSONObject responsesJSON = (JSONObject) parser.parse(new FileReader(filePath));

                // For each Template in dumbResponses.json, do put(template, template's responses array)
                for(Object responseTemplate : responsesJSON.keySet()) {
                    // Get responses array
                    Object[] objRes = ((JSONArray)responsesJSON.get(responseTemplate)).toArray();
                    // Convert to string array
                    String[] responses = Arrays.copyOf(objRes, objRes.length, String[].class);
                    put((String)responseTemplate, responses);
                }

            } catch(ParseException pe) {
                if(Logger.debug()) {
                    System.out.println("Parse error with responses.json file");
                    System.out.println("position: " + pe.getPosition());
                    pe.printStackTrace();
                }
            } catch(IOException ie) {
                if(Logger.debug()) {
                    System.out.println("Read error with responses.json file");
                    ie.printStackTrace();
                }
            }
        }};

    public static HashMap<String, HashMap<AMR, String[]>> responses = new HashMap<String, HashMap<AMR, String[]>>() {{
        JSONParser parser = new JSONParser();
        try {
            String filePath = PathFormat.absolutePathFromRoot("src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json");
            JSONObject responsesJson = ((JSONObject) parser.parse(new FileReader(filePath)));
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
    public String constructSmartResponse(Conversation convo) {
        return constructDumbResponse(convo);
    }

}
