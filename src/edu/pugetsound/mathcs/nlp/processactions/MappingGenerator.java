package edu.pugetsound.mathcs.nlp.processactions;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;


import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


import edu.pugetsound.mathcs.nlp.lang.Token;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;

//Requires Simple Json: https://code.google.com/archive/p/json-simple/
//https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar
import org.json.simple.*;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 * Maps from DATag label to a list of adherences for each ResponseTag (as a fraction of how many reses of the restag adhere to it) 
 * @author Jon Sims
 */
public class MappingGenerator {

    protected static HashMap<String, String[]> getResponses(File f) throws FileNotFoundException,ParseException,IOException {
        
        JSONParser parser = new JSONParser();
        JSONObject dict = (JSONObject) parser.parse(
            new BufferedReader(
                new FileReader(f)));

        HashMap<String, String[]> responses = new HashMap<String, String[]>();

        ArrayList<String> temp;
        String[] temp2;
        for (Object resTagAMRsObj: dict.entrySet()) {
            Map.Entry<String,JSONObject>  resTagAMRs = (Map.Entry<String,JSONObject>) resTagAMRsObj;
            temp = new ArrayList<String>();
            for (Object amrsReses: resTagAMRs.getValue().values()){
                JSONArray amrsResesArr = ((JSONArray)amrsReses);
                temp2 = new String[amrsResesArr.size()];
                for (int i=0; i<temp2.length; i++)
                    temp2[i] = amrsResesArr.get(i).toString();
                temp.add(Token.detokenize(temp2));
            }
            String[] arr = new String[temp.size()];
            responses.put(resTagAMRs.getKey(), temp.toArray(arr));
        }
        return responses;
    }


    /*
     * Call this if you want a mapping of daTags to response tags by adherence
     *
     */
    public static HashMap<String, HashMap<String, Double>> populateMappingMultinomial(String fName) {
        try {

            HashMap<String, String[]> responses = getResponses(
                new File("../src/edu/pugetsound/mathcs/nlp/processactions/srt/" + fName));
            HashMap<String, HashMap<String, Double>> mapping = new HashMap<String, HashMap<String, Double>>();
            
            for(DialogueActTag datag : DialogueActTag.values())
                mapping.put(datag.getLabel(), new HashMap<String, Double>());
            DAClassifier dac = new DAClassifier();
            HashMap<String, Double> curDATagReses;

            for (Map.Entry<String,String[]> resTagReses: responses.entrySet()) {
                double weight = Math.pow(resTagReses.getValue().length, -1);
                for (String text: resTagReses.getValue()) {
                    curDATagReses = mapping.get(dac.classify(new Utterance(text), new Conversation()).getLabel());
                    if (! curDATagReses.containsKey(resTagReses.getKey()))
                        curDATagReses.put(resTagReses.getKey(), new Double(weight));
                    else
                        curDATagReses.put(resTagReses.getKey(), curDATagReses.get(resTagReses.getKey())+weight);
                }
            }
            return mapping;

        } catch(FileNotFoundException ex) {
            System.out.println("Error: Unable to open file");    
            ex.printStackTrace();            
        } catch (ParseException e) {
            System.out.println("Error: ParseException");
            e.printStackTrace();
        }  catch (IOException e) {
            System.out.println("Error: IOException");
            e.printStackTrace();
        } 
        return null;
    }

    public static HashMap<String, String> populateMapping(HashMap<String, HashMap<String, Double>> multinomMap) {
        HashMap<String, String> likelyhoodMap = new HashMap<String, String>();
        HashMap<String, Double> resTags;
        double max;
        for (String daTag: multinomMap.keySet()) {
            max = -1;
            resTags = multinomMap.get(daTag);
            for (String resTag: resTags.keySet())
                if (resTags.get(resTag) > max) {
                    max = resTags.get(resTag);
                    likelyhoodMap.put(daTag, resTag);
                }
        }
        return likelyhoodMap;
    }

    public static HashMap<String, String> populateMapping(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        return populateMapping(multinomMap);
    }

    public static HashMap<String, String> populateMapping() {
        return populateMapping("responses.json");
    }

    public static HashMap<DialogueActTag, String> populateMappingDATags(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        HashMap<String, String> likelyhoodMap = populateMapping(multinomMap);
        if (likelyhoodMap == null)
            return null;
        HashMap<DialogueActTag, String> daMap = new HashMap<DialogueActTag, String>();
        for (String daLabel: likelyhoodMap.keySet())
            try {
                daMap.put(DialogueActTag.fromLabel(daLabel), likelyhoodMap.get(daLabel));
            } catch (IllegalArgumentException e) {
                System.out.println("Error with datag label: "+daLabel);
                System.out.println(e);
            }
        return daMap;
    }

    public static HashMap<DialogueActTag, String> populateMappingDATags() {
        return populateMappingDATags("responses.json");
    }


    public static void main(String a[]) {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(a));
        if (args.contains("-h") || args.contains("--help")) {
            System.out.println( "Help message goes here" );
        } 
        else if (args.size() > 1){
            System.out.println("Error with arguments: need one or two. You provided "+args.size());
        } 
        else {
            if (args.size() < 1)
                args.add("responses.json");
            HashMap<String, HashMap<String, Double>> mapping = populateMappingMultinomial(args.get(0));
            
            try (FileWriter file = new FileWriter("../src/edu/pugetsound/mathcs/nlp/processactions/DATagToSRT_probabilities.json")) {
                file.write((new JSONObject(mapping)).toJSONString());
            } catch (IOException e) {
                System.out.println("Error: IOException");
                e.printStackTrace();
            }

            try (FileWriter file = new FileWriter("../src/edu/pugetsound/mathcs/nlp/processactions/DATagToSRT.json")) {
                file.write((new JSONObject(populateMapping(mapping))).toJSONString());
            } catch (IOException e) {
                System.out.println("Error: IOException");
                e.printStackTrace();
            }
        }
    }

}
