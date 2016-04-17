package edu.pugetsound.mathcs.nlp.processactions.srt;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;


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

/**
 * @author Thomas Gagne & Jon Sims
 * The interface for all response templates.
 * All templates need to be able to take in an utterance and return a String response.
 */
public interface SemanticResponseTemplate {

    /**
     * @author Thomas Gagne & Jon Sims
     * Returns a response to a user's utterance in string form.
     * Since interfaces in Java can't have static methods, this method must be called on an object.
     * For inline expressions, new MyTemplate().constructResponseFromTemplate(utt); works well
     *
     * @param utterance The utteranec corresponding to the user's input.
     * @return A string response. In early versions, this might be an AMR response.
     */
    public String constructResponseFromTemplate(Conversation convo);
    

    public static HashMap<AMR, String[]> getResponses(String responseTemplate) {
        JSONParser parser = new JSONParser();
        HashMap<AMR, String[]> responses = new HashMap<AMR, String[]>();
        try {
            JSONObject jObject = (JSONObject) ((JSONObject) parser.parse(new FileReader("edu/pugetsound/mathcs/nlp/processactions/srt/responses.json"))).get(responseTemplate);
            Set<String> keys = jObject.keySet();
            for(String o: keys) {
                String keyStr = o;
                AMR key = AMR.parseAMRString(keyStr);
                JSONArray valueJson = (JSONArray) jObject.get(keyStr);
                String[] value = new String[valueJson.size()];
                for (int i=0; i<value.length; i++)
                    value[i] = valueJson.get(i).toString();
                responses.put(key, value);
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
        return responses;
    }
}

