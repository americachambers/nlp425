package edu.pugetsound.mathcs.nlp.lang;

/**
 * Represents a filled-in AMR node
 * @author tgagne
 */
public class AMR {

	/**
	 * A list of most of the semantic roles in AMR
	 */
    public enum AMR.SemanticRelation {
    	// Core argX roles, following OntoNotes style
        arg0, arg1, arg2, arg3, arg4, arg5,
        
        //Non-core roles
        accompanier, age, beneficiary, cause, compared-to, concession, condition, consist-of,
        degree, destination, direction, domain, duration, employed-by, example, extent,
        frequency, instrument, li, location, manner, medium, mod, mode, name, part, path,
        polarity, poss, purpose, source, subevent, sub-set, time, topic, value,
        
        // Quantity roles
        quant, unit, scale,
        
        // Date-entity roles
        day, month, year, weekday, time, timezone, quarter, dayperiod, season, year2, decade,
        century,calendar, era,
        
        // Used in conjunctions and certain date-times. Might not be used
        op1, op2, op3, op4, op5,op6, op7, op8, op9, op10,
        
        // Note that we lack any prep-X roles for the time being, due to ambiguity
        
        // Inverse relations
        arg0-of, arg1-of, arg2-of, arg3-of, arg4-of, arg5-of,
        
        accompanier-of, age-of, beneficiary-of, cause-of, compared-to-of, concession-of,
        condition-of, consist-of-of, degree-of, destination-of, direction-of, domain-of,
        duration-of, employed-by-of, example-of, extent-of,frequency-of, instrument-of,
        li-of, location-of, manner-of, medium-of, mod-of, mode-of, name-of, part-of,
        path-of, polarity-of, poss-of, purpose-of, source-of, subevent-of, sub-set-of, time-of,
        topic-of, value-of,
        
        quant-of, unit-of, scale-of,
        
        day-of, month-of, year-of, weekday-of, time-of, timezone-of, quarter-of, dayperiod-of,
        season-of, year2-of, decade-of, century-of, calendar-of, era-of, 
        
        op1-of, op2-of, op3-of, op4-of, op5-of,op6-of, op7-of, op8-of, op9-of, op10
    }
    
    /**
     * A list of the possible types an AMR node can be
     * Ex: verb, noun, adjectives (for copulas), possibleness, amr-unknown, etc.
     */
    public enum AMR.Type {
    	verb, noun, adjective,
    	possibleness, obligatoriness,
    	amr-unknown, // Used in wh-questions
    	string // Used in cases such as names
    }

    /**
     * The root value of this AMR node
     * Format should be like: ["g", "go-01"], which corresponds to "g / go-01"
     */
    public String[2] nodeValue;
    
    /**
     * The type of node the AMR nodeValue as, for example: verb, noun, possibleness, amr-unknown
     */
    public AMR.Type nodeType;

    /**
     * The value of each semantic relation, which maps to another AMR or NULL
     */
    public HashMap<AMR.SemanticRelation, AMR> semanticRelations;

}
