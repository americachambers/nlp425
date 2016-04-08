package edu.pugetsound.mathcs.nlp.lang;

import java.util.HashMap;
import java.util.List;

//Requires Jython 2.5: http://www.jython.org/
//http://search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar
import org.python.util.PythonInterpreter; 
import org.python.core.*; 

//Requires Simple Json: https://code.google.com/archive/p/json-simple/
//https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar
import org.json.simple.*;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;



/**
 * Represents a filled-in AMR node
 * @author tgagne
 */
public class AMR {

    /**
     * A list of most of the semantic roles in AMR
     * Current list (used below) is version 1.2.2, updated as of September 18, 2015
     * Source: https://github.com/amrisi/amr-guidelines/blob/master/amr.md#part-ii--concepts-and-relations
     */
    public enum SemanticRelation {
    	// Core argX roles, following OntoNotes style
        arg0("arg0"), arg1("arg1"), arg2("arg2"), arg3("arg3"), arg4("arg4"), arg5("arg5"),
        
        //Non-core roles
        accompanier("accompanier"), age("age"), beneficiary("beneficiary"),
        compared_to("compared_to"), concession("concession"), condition("condition"),
        consist_of("consist_of"), degree("degree"), destination("destination"),
        direction("direction"), domain("domain"), duration("duration"), example("example"),
        extent("extent"), frequency("frequency"), instrument("instrument"), location("location"),
        manner("manner"), medium("medium"), mod("mod"), mode("mode"), name("name"), ord("ord"),
        part("part"), path("path"), polarity("polarity"), poss("poss"), purpose("purpose"), 
        quant("quant"), scale("scale"), source("source"), subevent("subevent"), time("time"),
        topic("topic"), unit("unit"), value("value"), wiki("wiki"),
        
        // Date-entity roles
        calendar("calendar"), century("century"), day("day"), dayperiod("dayperiod"), decade("decade"),
        era("era"), month("month"), quarter("quarter"), season("season"), timezone("timezone"), 
        weekday("weekday"), year("year"), year2("year2"),
        
        // Used in conjunctions and certain date-times and locations. Might not be used
        op1("op1"), op2("op2"), op3("op3"), op4("op4"), op5("op5"),
        op6("op6"), op7("op7"), op8("op8"), op9("op9"), op10("op10"),
        
        // Note that we lack any prep-X roles for the time being, due to ambiguity
        // For an example, a partial list is given as following:
        //
        // prep_against, prep_along_with, prep_amid, prep_among, prep_as, prep_at,
        // prep_by,
        // prep_for, prep_from,
        // prep_in, prep_in_addition_to, prep_into,
        // prep_on, prep_on_behalf_of, prep_out_of,
        // prep_to, prep_toward,
        // prep_under,
        // prep_with, prep_without,

        // Some conjunctions are also not well-covered under the list of non-core roles. 
        // AMR also likes to avoid these, but sometimes we have no good alternative.
        // We lack any conjunctions outside the list of non-core roles for now, due to ambiguity.
        //
        // e.g. of what one might be: conj_as_if

        // Inverse relations
        // Core argX roles, following OntoNotes style
        arg0_of("arg0_of"), arg1_of("arg1_of"), arg2_of("arg2_of"), arg3_of("arg3_of"),
        arg4_of("arg4_of"), arg5_o("arg5_of"),
        
        //Non-core roles
        accompanier_of("accompanier_of"), age_of("age_of"), beneficiary_of("beneficiary_of"),
        compared_to_of("compared_to_of"), concession_of("concession_of"),
        condition_of("condition_of"),consist_of_of("consist_of_of"), degree_of("degree_of"),
        destination_of("destination_of"), direction_of("direction_of"), domain_of("domain_of"),
        duration_of("duration_of"),example_of("example_of"), extent_of("extent_of"),
        frequency_of("frequency_of"), instrument_of("instrument_of"), location_of("location_of"),
        manner_of("manner_of"), medium_of("medium_of"),mod_of("mod_of"), mode_of("mode_of"),
        name_of("name_of"), ord_of("ord_of"), part_of("part_of"), path_of("path_of"),
        polarity_of("polarity_of"), poss_of("poss_of"), purpose_of("purpose_of"),
        quant_of("uant_of"), scale_of("scale_of"), source_of("source_of"), subevent_of("subevent_of"),
        time_of("time_of"), topic_of("topic_of"), unit_of("unit_of"), value_of("value_of"),
        wiki_of("wiki_of"),
        
        // Date-entity roles
        calendar_of("calendar_of"), century_of("century_of"), day_of("day_of"),
        dayperiod_of("dayperiod_of"), decade_of("decade_of"), era_of("era_of"),
        month_of("month_of"), quarter_of("quarter_of"),season_of("season_of"),
        timezone_of("timezone_of"), weekday_of("weekday_of"), year_of("year_of"), year2_of("year2_of"),
        
        // Used in conjunctions and certain date-times and locations. Might not be used
        op1_of("op1_of"), op2_of("op2_of"), op3_of("op3_of"), op4_of("op4_of"), op5_of("op5_of"),
        op6_of("op6_of"), op7_of("op7_of"), op8_of("op8_of"), op9_of("op9_of"), op10_of("op10_of");

        private String label;

        SemanticRelation(String label) {
            this.label = label;
        }

        public String getLabel() {
            return ":" + label;
        }
    }
    
    /**
     * A list of the possible types an AMR node can be
     * Ex: verb, noun, adjectives (for copulas), possibleness, amr-unknown, etc.
     */
    public enum AMRType {
    	verb, noun, adjective,
    	possibleness, obligatoriness,
    	amr_unknown, // Used in wh-questions
    	string // Used in cases such as names
    }

    /**
     * The root value of this AMR node
     * Format should be like: ["g", "go-01"], which corresponds to "g / go-01"
     */
    public String[] nodeValue = new String[2];

    /**
     * The indecies of this AMR's corrosponding words in an English sentence
     * The first index is the one corrosponding to the exact word, and the rest
     *   corrospond to other words most closely related to this AMR.
     * Returned through Microsoft's SPLAT tool via the :align tag, where the "*"
     *   denotes the index of the exact word. From this tool, indecies start at 1.
     * See http://www.isi.edu/natural-language/mt/amr_eng_align.pdf for a better 
     *   description of alignment.
     */
    public Integer[] nodeAlignment;
    
    /**
     * The type of node the AMR nodeValue as, for example: verb, noun, possibleness, amr-unknown
     */
    public AMRType nodeType;

    /**
     * The value of each semantic relation, which maps to another AMR or NULL
     */
    public HashMap<AMR.SemanticRelation, AMR> semanticRelations;

    /**
     * Returns a human-readable representation of this AMR node in nested notation
     * @return A String representation of this AMR
     */
    public String toString() {
        String stringForm = "(" + nodeValue[0] + " / " + nodeValue[1];
        
        for(SemanticRelation sr : SemanticRelation.values()) {
            if(semanticRelations.containsKey(sr) && semanticRelations.get(sr) != null) {
                stringForm += " " + sr.toString() + " " + semanticRelations.get(sr).toString();
            }
        }

        stringForm += ")";
        return stringForm;
    }


    /**
     * @param text   the String to be converted into AMR
     * @return An array of Strings, which are translations of each sentences' AMR
     */
    public static String[] convertTextToAMR(String text) {

        PythonInterpreter python = new PythonInterpreter();
         
        python.execfile("../scripts/msrsplat.py");
        python.set("text", new PyString(text));
        python.exec("amr = main(text)");
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(python.get("amr").toString());
            JSONArray arr = (JSONArray) ((JSONObject)(((JSONArray) obj).get(0))).get("Value");
            String[] sentences = new String[arr.size()];
            for (int i=0; i<sentences.length; i++)
            sentences[i] = (String) arr.get(i);
            return sentences;
             
          } catch(ParseException pe) {

            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
          }
          return null;

    }

    public static void main(String a[]){
        String[] amrString = AMR.convertTextToAMR(a[0]);
        for (String s: amrString)
            System.out.println(s);
    }

}
