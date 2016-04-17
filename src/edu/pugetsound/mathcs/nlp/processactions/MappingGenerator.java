package edu.pugetsound.mathcs.nlp.processactions;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.lang.Double;

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
 * MappingGenerator solves the problem of "given an utterance with a DATag, 
 *   determine which type of response the utterance should be considered"
 *
 * Three types of maps are constructed in this file:
 *   populateMappingMultinomial: a mapping of DATags to probabilities for how adherent each response template is to each DATag
 *   populateMapping: a mapping of DATags to the highest probability Response template
 *   populateMappingUnique: an injective mapping of DATags to the highest probability Response template
 *
 * Run the main method to save your mapping to a file in json
 *
 * @author Jon Sims
 */
public class MappingGenerator {


    /**
     * A Hardcoded list of DATags that definitely go to certain response templates
     */
    protected static HashMap<DialogueActTag, String> hardcodedMap = 
        new HashMap<DialogueActTag, String>() {{
            // Instantiate HashMap's values
            put(DialogueActTag.ACKNOWLEDGE_ANSWER , "AcknowledgeAnswerTemplate");
            put(DialogueActTag.ACCEPT_PART , "AcceptPartTemplate");
            put(DialogueActTag.ACCEPT , "AcceptTemplate");
            put(DialogueActTag.ACTION_DIRECTIVE , "ActionDirectiveTemplate"); 
            put(DialogueActTag.AGREEMENTS , "AgreementTemplate");
            put(DialogueActTag.ANSWER_DISPREFERRED , "AnswerDispreferredTemplate");
            put(DialogueActTag.APOLOGY , "ApologyTemplate");
            put(DialogueActTag.BACKCHANNEL , "BackchannelTemplate");
            put(DialogueActTag.COMMIT , "CommitTemplate");
            put(DialogueActTag.CONVENTIONAL_CLOSING , "ConventionalClosingTemplate");
            put(DialogueActTag.CONVENTIONAL_OPENING , "ConventionalOpeningTemplate");
            put(DialogueActTag.DOWNPLAYING_SYMPATHY , "DownplaySympathyTemplate");
            put(DialogueActTag.EXCLAMATION , "ExclamationTemplate");
            put(DialogueActTag.MAYBE , "MaybeTemplate");
            put(DialogueActTag.OPEN_OPTION , "OpenOptionTemplate");
            put(DialogueActTag.OFFER , "QuestionTemplate");
            put(DialogueActTag.QUESTION , "QuestionTemplate");
            put(DialogueActTag.REJECT , "RejectTemplate");
            put(DialogueActTag.REJECT_PART , "RejectPartTemplate");
            put(DialogueActTag.SIGNAL_NON_UNDERSTANDING , "NonUnderstandingTemplate");
            put(DialogueActTag.SYMPATHETIC_COMMENT , "SympathyTemplate");
            put(DialogueActTag.THANKS , "ThanksTemplate");
            put(DialogueActTag.WELCOME , "WelcomeTemplate");
            put(DialogueActTag.INDETERMINATE_RESPONSE , "IndeterminateResponseTemplate");
            put(DialogueActTag.ASSESSMENT_APPRECIATION , "AssessmentAppreciationTemplate");
            put(DialogueActTag.DECLARATIVE_QUESTION , "DeclarativeQuestionTemplate");
            put(DialogueActTag.CONTINUER , "AcknowledgeAnswerTemplate");
            put(DialogueActTag.HOLD , "AcknowledgeAnswerTemplate");
            put(DialogueActTag.MIMIC_OTHER , "NonUnderstandingMimicTemplate");
            put(DialogueActTag.QUOTATION , "RhetoricalQuestionContinuer");
            put(DialogueActTag.QUESTION_YES_NO , "YesNoQuestionTemplate");
            put(DialogueActTag.YES , "YesTemplate");
            put(DialogueActTag.NO , "NoTemplate");
            put(DialogueActTag.QUESTION_WH , "WhQuestionTemplate");
            put(DialogueActTag.QUESTION_OPEN_ENDED , "RhetoricalQuestionContinuer");
            put(DialogueActTag.QUESTION_RHETORICAL , "RhetoricalQuestionContinuer");
            put(DialogueActTag.VIEWPOINT , "StatementOpinionTemplate");
            put(DialogueActTag.TAG_QUESTION , "TagQuestionTemplate");
            put(DialogueActTag.REFORMULATE_SUMMARIZE , "ReformulateTemplate");
            put(DialogueActTag.QUESTION_ALTERNATIVE , "QuestionTemplate");
            put(DialogueActTag.NARRATIVE_DESCRIPTIVE , "StatementNonOpinionTemplate");
            put(DialogueActTag.ABOUT_COMMUNICATION , "RepeatPhraseTemplate");
          
          }};

    /**
     * Helper method to get responses out of the responses.json file
     * Optionally can use another responses file name
     * @param the file from which to draw responses from
     * @return A mapping of ResponseTemplate names with a list of detokenized response sentences
     */
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


    /**
     * Maps from DATag label to a list of adherences for each ResponseTag (as a fraction of how many reses of the restag adhere to it) 
     * Call this if you want a mapping of daTags to response tags by adherence
     * @param the file name from which to read responses from
     * @return a mapping from DATag labels to a mapping of Response Template names to how adherent the response template is to that DA tag
     */
    public static HashMap<String, HashMap<String, Double>> populateMappingMultinomial(String fName) {
        try {

            HashMap<String, String[]> responses = getResponses(
                new File("../src/edu/pugetsound/mathcs/nlp/processactions/srt/" + fName));
            HashMap<String, HashMap<String, Double>> mapping = new HashMap<String, HashMap<String, Double>>();
            
            for(DialogueActTag datag : DialogueActTag.values()) {
                mapping.put(datag.getLabel(), new HashMap<String, Double>());
                if (hardcodedMap.containsKey(datag))
                    mapping.get(datag.getLabel()).put(hardcodedMap.get(datag), Double.POSITIVE_INFINITY);
            }
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


    /**
     * Maps from DATag label to the most probably-associated ResponseTag 
     * Call this if you want a general, non-injective mapping of daTags to response tags
     * @param the multinomial mapping from above
     * @return a mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMapping(HashMap<String, HashMap<String, Double>> multinomMap) {
        HashMap<String, String> likelyhoodMap = new HashMap<String, String>();
        HashMap<String, Double> resTags;
        for (String daTag: multinomMap.keySet()) {
            resTags = multinomMap.get(daTag);
            if (resTags.size() > 0)
                likelyhoodMap.put(daTag, Collections.max(
                    resTags.keySet(), 
                    new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            return Double.compare(multinomMap.get(daTag).get(o1), multinomMap.get(daTag).get(o2));
                        }
                    }));
        }
        return likelyhoodMap;
    }

    /**
     * Helper method
     * Call this if you want the above, only given a responses filename, and the multinomial mapping will be generated for you
     * @param the responses file name
     * @return a mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMapping(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        return populateMapping(multinomMap);
    }

    /**
     * Helper method
     * Call this if you want the above, only given the default responses filename 'responses.json', and the multinomial mapping will be generated for you
     * @return a mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMapping() {
        return populateMapping("responses.json");
    }


    /**
     * Maps injectively from DATag label to the most probably-associated ResponseTag 
     * Call this if you want a less-general, injective mapping of daTags to response tags
     * @param the multinomial mapping from above
     * @return an injective mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMappingUnique(HashMap<String, HashMap<String, Double>> multinomMap) {
        ArrayList<String> templates = new ArrayList<String>();
        for ( HashMap<String, Double> map: multinomMap.values())
            for (String key: map.keySet())
                if (! templates.contains(key))
                    templates.add(key);
        DialogueActTag[] daTags = DialogueActTag.values();
        Arrays.sort(daTags, new Comparator<DialogueActTag>() {
            public int compare(DialogueActTag o1, DialogueActTag o2) {
                return Double.compare(multinomMap.get(o1.getLabel()).size(), multinomMap.get(o2.getLabel()).size());
            }
        });
        HashMap<String, String> likelyhoodMap = new HashMap<String, String>();
        HashMap<String, Double> resTags;
        String[] resTemp;        
        for (DialogueActTag daTag: daTags) {
            resTags = multinomMap.get(daTag.getLabel());
            resTemp = new String[resTags.size()];
            for (String resTag: resTags.keySet().toArray(resTemp))
                if (!templates.contains(resTag))
                    resTags.remove(resTag);
            if (resTags.size() > 0) {
                likelyhoodMap.put(daTag.getLabel(), Collections.max(
                    resTags.keySet(), 
                    new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            return Double.compare(multinomMap.get(daTag.getLabel()).get(o1), multinomMap.get(daTag.getLabel()).get(o2));
                        }
                    }));
                templates.remove(likelyhoodMap.get(daTag.getLabel()));
            }
        }
        return likelyhoodMap;
    }

    /**
     * Helper method
     * Call this if you want the above, only given a responses filename, and the multinomial mapping will be generated for you
     * @param the responses file name
     * @return an injective mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMappingUnique(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        return populateMappingUnique(multinomMap);
    }

    /**
     * Helper method
     * Call this if you want the above, only given the default responses filename 'responses.json', and the multinomial mapping will be generated for you
     * @return an injective mapping from DATag labels to a mapping of Response Template names
     */
    public static HashMap<String, String> populateMappingUnique() {
        return populateMappingUnique("responses.json");
    }


    /**
     * Maps from DATag object to the most probably-associated ResponseTag 
     * Call this if you want a general, non-injective mapping of daTags to response tags
     * @param any mapping from DATag label to Response Template String
     * @return a mapping from DATag Objects to a mapping of Response Template names
     */
    public static HashMap<DialogueActTag, String> populateMappingDATags(HashMap<String, String> likelyhoodMap) {
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

    /**
     * Helper method
     * Call this if you want the above, only given a responses filename, 
     *   and a noninjective mapping from DATag label to Response Template String will be generated for you
     * @param the responses file name
     * @return a mapping from DATag Objects to a mapping of Response Template names
     */
    public static HashMap<DialogueActTag, String> populateMappingDATags(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        HashMap<String, String> likelyhoodMap = populateMapping(multinomMap);
        if (likelyhoodMap == null)
            return null;
        return populateMappingDATags(likelyhoodMap);
    }

    /**
     * Helper method
     * Call this if you want the above, only given the default responses filename 'responses.json', and the multinomial mapping will be generated for you
     * @return a mapping from DATag Objects to a mapping of Response Template names
     */
    public static HashMap<DialogueActTag, String> populateMappingDATags() {
        return populateMappingDATags("responses.json");
    }



    /**
     * Maps injectively from DATag Object to the most probably-associated ResponseTag 
     * Call this if you want the above, only given a responses filename,
     *   and an injective mapping from DATag label to Response Template String will be generated for you
     * @param the responses file name
     * @return an injective mapping from DATag Objects to a mapping of Response Template names
     */
    public static HashMap<DialogueActTag, String> populateMappingDATagsUnique(String fName) {
        HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(fName);
        if (multinomMap == null)
            return null;
        HashMap<String, String> likelyhoodMap = populateMappingUnique(multinomMap);
        if (likelyhoodMap == null)
            return null;
        return populateMappingDATags(likelyhoodMap);
    }

    /**
     * Helper method
     * Call this if you want the above, only given the default responses filename 'responses.json', and the multinomial mapping will be generated for you
     * @return an injective mapping from DATag Objects to a mapping of Response Template names
     */
    public static HashMap<DialogueActTag, String> populateMappingDATagsUnique() {
        return populateMappingDATagsUnique("responses.json");
    }

    /**
     * Just write the mapping to a file
     * @param the file name
     * @param the mapping
     */
    protected static void writeOut(String fName, HashMap<?,?> map) {
        try (FileWriter file = new FileWriter(fName)) {
            file.write((new JSONObject(map)).toJSONString());
        } catch (IOException e) {
            System.out.println("Error: IOException");
            e.printStackTrace();
        }
    }


    public static void main(String a[]) {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(a));
        if (args.contains("-h") || args.contains("--help")) {
            System.out.println( 
                "MappingGenerator solves the problem of 'given an utterance with a DATag, determine which type of response the utterance should be considered'"
                +"Three types of maps can be constructed by this class:"
                +"   populateMappingMultinomial: a mapping of DATags to probabilities for how adherent each response template is to each DATag"
                +"   populateMapping: a mapping of DATags to the highest probability Response template"
                +"   populateMappingUnique: an injective mapping of DATags to the highest probability Response template"
                +""
                +"Run the main method to save the result of populateMapping to a file in json format in the processactions directory"
                +"The filename will be 'DATagToSRT.json' by default, but can be set by the LAST argument to the main method (cant have a dash in it!)"
                +""
                +"Arguments are as follows:"
                +"  -h, --help: Display this help message" 
                +"  -u, --unique, -i, --injective: Only do & save populateMappingUnique(). For each distinct DATag, ensure that it's corrosponding Response Template is distinct & unique, such that the mapping is injective"
                +"  -m, --multinomial: Only do & save the Multinomial mapping, populateMappingMultinomial()"
                +"  -d, --datag: Use the DATag toString method to save mappings instead of their labels. Can be used with populateMappingUnique or populateMapping.");
        } 
        else if (args.size() > 3){
            System.out.println("Error with arguments: need one or two. You provided "+args.size());
        } 
        else {
            String rootDir = "../src/edu/pugetsound/mathcs/nlp/processactions/";
            if (args.size() == 0 || args.get(args.size()-1).contains("-")) {
                args.add("responses.json");
                System.out.print("Using default input file name at ");
            }
            else 
                System.out.print("Using custom input file name at ");
            System.out.print(args.get(args.size()-1)+'\n');

            HashMap<String, HashMap<String, Double>> multinomMap = populateMappingMultinomial(args.get(args.size()-1));
            
            HashMap<String,String> map = null;

            if (args.contains("-u") || args.contains("--unique") || args.contains("-i") || args.contains("--injective")) 
                map = populateMappingUnique(multinomMap);
            else if (!args.contains("-m") && !args.contains("--multinomial")) 
                map = populateMapping(multinomMap);
            else
                writeOut(rootDir+"DATagToSRT.json", multinomMap);
            if (args.contains("-d") || args.contains("--datag"))
                writeOut(rootDir+"DATagToSRT.json", populateMappingDATags(map));
            else if (!args.contains("-m") && !args.contains("--multinomial")) 
                writeOut(rootDir+"DATagToSRT.json", map);
        }
    }

}
