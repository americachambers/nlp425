package edu.pugetsound.mathcs.nlp.lang;

/**
 * Represents a filled-in AMR node
 * @author tgagne
 */
public class AMR {

	/**
	 * A list of most of the semantic roles in AMR
     * Full list available at https://github.com/amrisi/amr-guidelines/blob/master/amr.md#part-ii--concepts-and-relations
	 */
    public enum AMR.SemanticRelation {
    	// Core argX roles, following OntoNotes style
        arg0, arg1, arg2, arg3, arg4, arg5,
        
        //Non-core roles
        accompanier, age, beneficiary, cause, compared_to, concession, condition, consist_of,
        degree, destination, direction, domain, duration, employed_by, example, extent,
        frequency, instrument, li, location, manner, medium, mod, mode, name, part, path,
        polarity, poss, purpose, source, subevent, sub_set, time, topic, value,
        
        // Quantity roles
        quant, unit, scale,
        
        // Date-entity roles
        day, month, year, weekday, time, timezone, quarter, dayperiod, season, year2, decade,
        century,calendar, era,
        
        // Used in conjunctions and certain date-times. Might not be used
        op1, op2, op3, op4, op5,op6, op7, op8, op9, op10,
        
        // Note that we lack any prep-X roles for the time being, due to ambiguity
        
        // Inverse relations
        arg0_of, arg1_of, arg2_of, arg3_of, arg4_of, arg5_of,
        
        accompanier_of, age_of, beneficiary_of, cause_of, compared_to_of, concession_of,
        condition_of, consist_of_of, degree_of, destination_of, direction_of, domain_of,
        duration_of, employed_by_of, example_of, extent_of,frequency_of, instrument_of,
        li_of, location_of, manner_of, medium_of, mod_of, mode_of, name_of, part_of,
        path_of, polarity_of, poss_of, purpose_of, source_of, subevent_of, sub_set_of, time_of,
        topic_of, value_of,
        
        quant_of, unit_of, scale_of,
        
        day_of, month_of, year_of, weekday_of, time_of, timezone_of, quarter_of, dayperiod_of,
        season_of, year2_of, decade_of, century_of, calendar_of, era_of, 
        
        op1_of, op2_of, op3_of, op4_of, op5_of,op6_of, op7_of, op8_of, op9_of, op10
    }
    
    /**
     * A list of the possible types an AMR node can be
     * Ex: verb, noun, adjectives (for copulas), possibleness, amr-unknown, etc.
     */
    public enum AMR.Type {
    	verb, noun, adjective,
    	possibleness, obligatoriness,
    	amr_unknown, // Used in wh-questions
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
