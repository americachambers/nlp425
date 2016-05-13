package edu.pugetsound.mathcs.nlp.processactions.srt;


import edu.pugetsound.mathcs.nlp.util.Logger;
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
import java.util.Arrays;

/**
 * This class provides static utilities for reading in and loading response JSON files
 * @author Thomas Gagne & Jon Sims
 * @version 05/09/16
 */
public class ResponseLoader {

    private static final String DUMB_RESPONSES_JSON_FILE =
        "src/edu/pugetsound/mathcs/nlp/processactions/srt/dumbResponses.json";

    private static final String RESPONSES_JSON_FILE =
        "src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json";

    /**
     * Read in all the responses from dumbResponses.json and load them into a hashmap.
     * The hashmap maps Template names to an array of responses.
     * @author Thomas Gagne & Jon Sims
     * @version 05/09/16
     */
    public static HashMap<String, String[]> loadDumbResponses() {
        HashMap<String, String[]> templateToResponses = new HashMap<String, String[]>();
        JSONParser parser = new JSONParser();

        try {
            String filePath = PathFormat.absolutePathFromRoot(DUMB_RESPONSES_JSON_FILE);
            JSONObject responsesJSON = (JSONObject) parser.parse(new FileReader(filePath));

            // For each Template in dumbResponses.json, do:
            // put(template, template's responses array)
            for(Object responseTemplate : responsesJSON.keySet()) {
                // Get responses array
                Object[] objRes = ((JSONArray)responsesJSON.get(responseTemplate)).toArray();
                // Convert to string array
                String[] responses = Arrays.copyOf(objRes, objRes.length, String[].class);
                templateToResponses.put((String)responseTemplate, responses);
            }

            return templateToResponses;

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

        return null;
    }

    /**
     * Returns a hashmap which maps a Template name to a hashmap which maps AMRs to their
     * string response form, represented as a string array of tokens in the response.
     * The entries are loaded from responses.json
     * responses.json is intended to be used for storing structures used for constructing
     * smart responses.
     * @author Thomas Gagne & Jon Sims
     * @version 05/09/16
     */
    public static HashMap<String, HashMap<AMR, String[]>> loadTemplateToAMRAndResponse() {
        HashMap<String, HashMap<AMR, String[]>> templateToAMRAndResponse =
            new HashMap<String, HashMap<AMR, String[]>>();

        JSONParser parser = new JSONParser();
        try {
            String filePath = PathFormat.absolutePathFromRoot(RESPONSES_JSON_FILE);
            JSONObject responsesJson = ((JSONObject) parser.parse(new FileReader(filePath)));
            Set<String> responsesTemplates = responsesJson.keySet();
            for (String responseTemplate: responsesTemplates) {
                HashMap<AMR, String[]> responsesMapping = new HashMap<AMR, String[]>();
                JSONObject responseTemplateJson = ((JSONObject)
                                                   responsesJson.get(responseTemplate));
                for (Object o: responseTemplateJson.keySet() ) {
                    String keyStr = (String) o;
                    AMR key = AMRParser.parseAMRString(keyStr);
                    JSONArray valueJson = (JSONArray) responseTemplateJson.get(o);
                    String[] value = new String[valueJson.size()];
                    for (int i=0; i<value.length; i++)
                        value[i] = valueJson.get(i).toString();
                    responsesMapping.put(key, value);
                }
                templateToAMRAndResponse.put(responseTemplate, responsesMapping);
            }

            return templateToAMRAndResponse;
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

        return null;
    }
}
