package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
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
import java.util.Set;

/**
 * @author Thomas Gagne
 * The interface for all response templates.
 * All templates need to be able to take in an utterance and return a String response.
 */
public interface SemanticResponseTemplate {

    /**
     * @author Thomas Gagne
     * Returns a response to a user's utterance in string form.
     * Since interfaces in Java can't have static methods, this method must be called on an object.
     * For inline expressions, new MyTemplate().constructResponseFromTemplate(utt); works well
     *
     * @param utterance The utteranec corresponding to the user's input.
     * @return A string response. In early versions, this might be an AMR response.
     */
    public String constructResponseFromTemplate(Utterance utterance);
        

    public static HashMap<String, AMR> getResponses(String responseTemplate) {
        JSONParser parser = new JSONParser();
        HashMap<String, AMR> responses = new HashMap<String, AMR>();
        try {
            JSONObject jObject = (JSONObject) ((JSONObject) parser.parse(new FileReader("edu/pugetsound/mathcs/nlp/processactions/srt/responses.json"))).get(responseTemplate);
            Set<String> keys = jObject.keySet();
            for(String o: keys) {
                String key = (String) o;
                AMR value = AMR.parseAMRString((String) jObject.get(key));
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

