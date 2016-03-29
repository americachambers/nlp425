package edu.pugetsound.mathcs.nlp.lang;

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
    public enum AMR.SemanticRelation {
    	// Core argX roles, following OntoNotes style
        arg0, arg1, arg2, arg3, arg4, arg5,
        
        //Non-core roles
        accompanier, age, beneficiary, compared_to, concession, condition, consist_of, degree, 
        destination, direction, domain, duration, example, extent, frequency, instrument, 
        location, manner, medium, mod, mode, name, ord, part, path, polarity, poss, purpose, 
        quant, scale, source, subevent, time, topic, unit, value, wiki,
        
        // Date-entity roles
        calendar, century, day, dayperiod, decade, era, month, quarter, season, timezone, 
        weekday, year, year2,
        
        // Used in conjunctions and certain date-times and locations. Might not be used
        op1, op2, op3, op4, op5,op6, op7, op8, op9, op10,
        
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
        arg0_of, arg1_of, arg2_of, arg3_of, arg4_of, arg5_of,
        
        //Non-core roles
        accompanier_of, age_of, beneficiary_of, compared_to_of, concession_of, condition_of, 
        consist_of_of, degree_of, destination_of, direction_of, domain_of, duration_of, 
        example_of, extent_of, frequency_of, instrument_of, location_of, manner_of, medium_of, 
        mod_of, mode_of, name_of, ord_of, part_of, path_of, polarity_of, poss_of, purpose_of, 
        quant_of, scale_of, source_of, subevent_of, time_of, topic_of, unit_of, value_of, wiki_of,
        
        // Date-entity roles
        calendar_of, century_of, day_of, dayperiod_of, decade_of, era_of, month_of, quarter_of, 
        season_of, timezone_of, weekday_of, year_of, year2_of,
        
        // Used in conjunctions and certain date-times and locations. Might not be used
        op1_of, op2_of, op3_of, op4_of, op5_of,op6_of, op7_of, op8_of, op9_of, op10_of,
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
    public String[2] nodeValue;
    
    /**
     * The type of node the AMR nodeValue as, for example: verb, noun, possibleness, amr-unknown
     */
    public AMRType nodeType;

    /**
     * The value of each semantic relation, which maps to another AMR or NULL
     */
    public HashMap<AMR.SemanticRelation, AMR> semanticRelations;

}
