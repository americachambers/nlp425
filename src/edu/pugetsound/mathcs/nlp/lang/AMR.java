package edu.pugetsound.mathcs.nlp.lang;

import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

//Requires Jython 2.5: http://www.jython.org/
//http://search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar
import org.python.util.PythonInterpreter;
import org.python.core.*;

//Requires Simple Json: https://code.google.com/archive/p/json-simple/
//https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar
import org.json.simple.*;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import edu.pugetsound.mathcs.nlp.lang.Token;

/**
 * Represents a filled-in AMR node
 * @author tgagne
 */
public class AMR {

    public AMR(String nodeVar, String nodeValue, AMR.AMRType nodeType) {
        this.nodeValue = new String[]{nodeVar, nodeValue};
        this.nodeType = nodeType;
    }

    public AMR(){};

    /**
     * A list of most of the semantic roles in AMR
     * Current list (used below) is version 1.2.2, updated as of September 18, 2015
     * Source: https://github.com/amrisi/amr-guidelines/blob/master/amr.md#part-ii--concepts-and-relations
     */
    public enum SemanticRelation {
        // align role, for consistency with msrsplat
        align("align"),

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
        arg0_of("arg0-of"), arg1_of("arg1-of"), arg2_of("arg2-of"), arg3_of("arg3-of"),
        arg4_of("arg4-of"), arg5_o("arg5-of"),

        //Non_core roles
        accompanier_of("accompanier-of"), age_of("age-of"), beneficiary_of("beneficiary-of"),
        compared_to_of("compared_to-of"), concession_of("concession-of"),
        condition_of("condition-of"),consist_of_of("consist_of-of"), degree_of("degree-of"),
        destination_of("destination-of"), direction_of("direction-of"), domain_of("domain-of"),
        duration_of("duration-of"),example_of("example-of"), extent_of("extent-of"),
        frequency_of("frequency-of"), instrument_of("instrument-of"), location_of("location-of"),
        manner_of("manner-of"), medium_of("medium-of"),mod_of("mod-of"), mode_of("mode-of"),
        name_of("name-of"), ord_of("ord-of"), part_of("part-of"), path_of("path-of"),
        polarity_of("polarity-of"), poss_of("poss-of"), purpose_of("purpose-of"),
        quant_of("uant-of"), scale_of("scale-of"), source_of("source-of"), subevent_of("subevent-of"),
        time_of("time-of"), topic_of("topic-of"), unit_of("unit-of"), value_of("value-of"),
        wiki_of("wiki-of"),

        // Date_entity roles
        calendar_of("calendar-of"), century_of("century-of"), day_of("day-of"),
        dayperiod_of("dayperiod-of"), decade_of("decade-of"), era_of("era-of"),
        month_of("month-of"), quarter_of("quarter-of"),season_of("season-of"),
        timezone_of("timezone-of"), weekday_of("weekday-of"), year_of("year-of"), year2_of("year2-of"),

        // Used in conjunctions and certain date_times and locations. Might not be used
        op1_of("op1-of"), op2_of("op2-of"), op3_of("op3-of"), op4_of("op4-of"), op5_of("op5-of"),
        op6_of("op6-of"), op7_of("op7-of"), op8_of("op8-of"), op9_of("op9-of"), op10_of("op10-of"),

        // Not sure what this is for, but msrsplat uses it sometimes
        lf_measure("lf-measure");

        private String label;

        SemanticRelation(String label) {
            this.label = label;
        }

        public String getLabel() {
            return ":" + label;
        }
    }

    // Used for parsing an AMR string to an object
    private static final HashMap<String, AMR.SemanticRelation> nameToRelation = new HashMap<String, AMR.SemanticRelation>() {{
            for(AMR.SemanticRelation sr : AMR.SemanticRelation.values()) {
                put(sr.label, sr);
            }
        }};

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
    public HashMap<AMR.SemanticRelation, AMR> semanticRelations = new HashMap<AMR.SemanticRelation, AMR>();

    /**
     * Adds a semantic relation from this AMR to `amr`
     * @param relationName The string name of the relation
     * @param amr The endpoint AMR of this relation
     */
    public void addSemanticRelation(String relationName, AMR amr) {
        semanticRelations.put(AMR.nameToRelation.get(relationName), amr);
    }

    /**
     * Returns a human-readable representation of this AMR node in nested notation
     * @return A String representation of this AMR
     */
    public String toString() {
        if (amrString != null)
            return amrString;

        String stringForm = "(" + nodeValue[0] + " / " + nodeValue[1];

        for(SemanticRelation sr : SemanticRelation.values()) {
            if(semanticRelations.containsKey(sr) && semanticRelations.get(sr) != null) {
                stringForm += " :" + sr.toString() + " " + semanticRelations.get(sr).toString();
            }
        }

        stringForm += ")";
        return stringForm;
    }


    /**
     * The String representation of the AMR as given my MSR Splat
     */
    public String amrString;

    /**
     * The time since the MSR Splat API was last queried
     */
    private static long queryTime = 0;


    /**
     * @param text   the String to be converted into AMR
     * @return An array of AMR, which are translations of each sentences' AMR
     */
    public static AMR[] convertTextToAMR(String text) {

        PythonInterpreter python = new PythonInterpreter();
        python.execfile("../scripts/msrsplat.py");
        python.set("text", new PyString(text));
        System.out.println("Querying MSR_SPLAT for the AMR string for '"+text+"'");
        
        JSONParser parser = new JSONParser();
        try {
            if (System.currentTimeMillis() - queryTime < 2000)
                Thread.sleep(2000 - System.currentTimeMillis() + queryTime);
            queryTime = System.currentTimeMillis();
            python.exec("amr = main(text)");
            System.out.println("Got the AMR String; parsing in a moment...");
            Object obj = parser.parse(python.get("amr").toString());
            JSONArray amrStrs = (JSONArray) ((JSONObject)(((JSONArray) obj).get(0))).get("Value");
            System.out.println("About to convert the parsed AMR Strings into AMR Objects");
            
            String temp;
            AMR[] amrs = new AMR[amrStrs.size()];
            for (int i=0; i<amrs.length; i++) {
                System.out.println(amrStrs.get(i));
                System.out.println(AMR.parseAMRString(amrStrs.get(i).toString()));
                amrs[i] = AMR.parseAMRString(amrStrs.get(i).toString());
            }
            return amrs;
        } catch(ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        } catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }


    /**
     * Returns an English-legible representation of this AMR using the text template
     * @return A String representation of this AMR
     */
    public String convertAMRToText(String[] text) {
        this.convertAMRToTextHelper(text);
        return Token.detokenize(text);
    }

    public void convertAMRToTextHelper(String[] text) {
        if (nodeAlignment != null && nodeAlignment.length > 0)
            text[nodeAlignment[0]] = nodeValue[1];
        for (AMR child: semanticRelations.values())
            child.convertAMRToTextHelper(text);
    }


    public static void main(String a[]) {
        for (String s: a)
            System.out.println(AMR.convertTextToAMR(s));

        //AMR fluffy = new AMR("f", "fluffy", AMRType.noun);
        //AMR cute = new AMR("c", "cute", AMRType.adjective);
        //cute.addSemanticRelation("domain", fluffy);
        //System.out.println(cute.toString());

        // AMR result = parseAMRString("(ff / fluffy :arg0 30 :arg1 (c / cat) :arg2 (g / goaty))");
        // System.out.println(result.toString());
    }

    /**
     * Parses an AMR string to an AMR object
     * This is based on the Smatch parsing algorithm, which uses a shift-reduce style for parsing.
     * This method can easily break if the AMR is not well-formed. PLEASE delimit with spaces!
     * @param text The AMR string to parse. There should be exactly one AMR in the string
     * @return The parsed AMR object
     */
    public static AMR parseAMRString(String text) {
        // Convert text to lowercase for simplicity
        text = text.toLowerCase();
        
        // Force some extra spaces in there to help parsing
        text = text.replace(")", " )");
        text = text.replace("(", "( ");

        // Current state; denotes the last significant symbol encountered.
        // ":" indicates we should start processing the relation name
        // "/" indicates we should start processing the concept value
        // "(" indicates we are starting the AMR are should be processing the node's name and value
        // If any of these symbols are inside quotation marks, they are ignored
        char state = '(';

        // Current unreduced character sequence
        String cur_charseq = "";

        // The current AMR object we are filling
        AMR cur_amr = new AMR();
        cur_amr.amrString = text;

        // Flag for if we're insidez quotation marks
        boolean in_quote = false;

        // Used for jumping past the next AMR
        // When we encounter the first ( for an AMR we want to skip, this is set to 1
        // For each ( afterwards, we increment by 1. For each ), we decrement
        // When the counter reaches 0, we've passed the AMR
        // This is used for recursion
        int skip_amr = 0;

        // The last encountered string of characters before we hit a space
        String last_word = "";

        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Continually ignore chars if we're skipping the current AMR
            // Used for when we recurse and don't want to parse the AMR we recursed
            if(skip_amr > 0) {
                if(c == '(') {
                    skip_amr++;
                } else if(c == ')') {
                    skip_amr--;
                }

                continue;
            }

            if(c == ')') {
                break;

            } else if(c == '"') {

                // Make an AMR for the quote if we're closing the quote
                if(in_quote) {
                    AMR quote = new AMR(String.valueOf(cur_charseq.charAt(0)), cur_charseq, AMRType.string);
                    if(state == ':') {
                        AMR.SemanticRelation sr = AMR.nameToRelation.get(last_word);
                        if(sr == null) {
                            System.out.println("Error: Unrecognized relation " + last_word  + " while parsing AMR!");
                            return null;
                        } else {
                            // Add recursed to cur_amr's map of semantic relations
                            cur_amr.semanticRelations.put(sr, quote);
                        }

                        cur_charseq = "";
                        last_word = "";

                    } else {
                        System.out.println("Error: String without a relation!");
                        return null;
                    }
                }

                // Flip whether or not we're inside a quotation
                in_quote = !in_quote;

            } else if(c == '(') {

                // Not a significant character if inside quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                // Get the attribute name and recurse to create the new AMR
                if(state == ':') {
                    AMR recursed = parseAMRString(text.substring(i));
                    // Skip over the part of text corresponding to recursed
                    skip_amr = 1;
                    //i++; // Prevent skip_amr from counting the first ( twice
                    AMR.SemanticRelation sr = AMR.nameToRelation.get(last_word);
                    if(sr == null) {
                        System.out.println("Error: Unrecognized relation " + last_word  + " while parsing AMR!");
                        return null;
                    } else {
                        // If recursed == null, just don't add the AMR
                        if(recursed != null) {
                            // Add recursed to cur_amr's map of semantic relations
                            cur_amr.semanticRelations.put(sr, recursed);
                        }
                    }

                    cur_charseq = "";
                    last_word = "";
                    state = '\0';
                }

            } else if(c == '/') {

                // Not a significant character if inside quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                if(state == '(') {
                    // Add the node's variable name
                    cur_amr.nodeValue[0] = last_word;
                }

                state = '/';

            } else if(c == ':') {

                // Not a significant character if inside a quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                state = ':';

            } else if(c == ' ') {

                // Not a significant character if inside a quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                // If we have a space after some actual text,
                // it must be a variable, name, relation, or string
                if(!cur_charseq.equals("")) {

                    // If we're adding a relation which doesn't have a native AMR node attached to it
                    // ex: (f / frog :quant 30)
                    // The 30 doesn't have a node surrounding it, normally
                    // The second half of the && is a really quick 'n dirty way of checking that we're
                    // parsing the argument to a relation and not the relation name itself
                    if(state == ':' && text.charAt(i - cur_charseq.length() - 1) != ':') {
                        AMR.SemanticRelation sr = AMR.nameToRelation.get(last_word);
                        if(sr == null) {
                            System.out.println("Error: Unrecognized relation " + last_word  + " while parsing AMR!");
                            return null;
                        } else {
                            // Create a new AMR to hold the term
                            AMR relation_val = new AMR("" + cur_charseq.charAt(0), cur_charseq, AMRType.string);
                            cur_amr.semanticRelations.put(sr, relation_val);

                            last_word = cur_charseq;
                            cur_charseq = "";
                        }

                    } else {

                        // In all other cases, we can set last_word
                        last_word = cur_charseq;
                        cur_charseq = "";

                        // If we're at the node value after a '/'
                        if(state == '/') {
                            cur_amr.nodeValue[1] = last_word;
                        }
                    }


                    // If we're at the node variable after '('
                    /*
                    if(state == '(') {
                        System.out.println("Set the node variable as: " + last_word);
                        cur_amr.nodeValue[0] = last_word;
                        System.out.println("amr nodevar: " + cur_amr.nodeValue[0]);
                    }
                    */
                }

            } else {
                cur_charseq += c;
            }
        }

        // Eventually, I need to set the AMRTpe based upon the word
        return cur_amr;
    }

}
