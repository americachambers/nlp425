package edu.pugetsound.mathcs.nlp.lang;

import edu.pugetsound.mathcs.nlp.util.Logger;

import java.util.HashMap;

/**
 * A list of most of the semantic roles in AMR
 * Current list (used below) is version 1.2.2, updated as of September 18, 2015
 * Source: https://github.com/amrisi/amr-guidelines/blob/master/amr.md#part-ii--concepts-and-relations
 * @author Thomas Gagne & Jon Sims
 * @version 04/27/16
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
    prep_against("prep-against"), prep_along_with("prep-along-with"), prep_amid("prep-amid"),
    prep_among("prep-among"), prep_as("prep-as"), prep_at("prep-at"),prep_by("prep-by"),
    prep_for("prep-for"), prep_from("prep-for"), prep_in("prep-in"),
    prep_in_addition_to("prep-in-addition-to"), prep_into("prep-into"),prep_on("prep-on"),
    prep_on_behalf_of("prep-on-behalf-of"), prep_out_of("prep-out-of"), prep_to("prep-to"),
    prep_toward("prep-toward"), prep_under("prep-under"), prep_with("prep-with"),
    prep_without("prep-without"), prep_about("prep-about"), prep_of("prep-of"),
    prep_since("prep-since"),

    // Adding in these preparations since they're in responses.json
    //prep_with("prep-with"), prep_for("prep-for"), prep_into("prep-into"), prep_about("prep-about"),

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

    // Not sure what these are for, but msrsplat uses them sometimes
    lf_measure("lf-measure"), lf_dadj("lf-dadj"), lf_classifier("lf-classifier");

    private String label;

    /**
     * A mapping from SemanticRelation labels to the corresponding object
     * Ex: "arg-0" -> SemanticRelation.arg_0
     */
    /*
    public static final HashMap<String, SemanticRelation> NAME_TO_RELATION =
    new HashMap<String, SemanticRelation>() {{
            for(SemanticRelation sr : SemanticRelation.values()) {
                put(sr.getLabel().substring(1), sr);
            }
        }};
    */

    SemanticRelation(String label) {
        this.label = label;
    }

    /**
     * Returns the string representation of this semantic relation
     * ex: arg0_of.getLabel() = ":arg0-of"
     * @return The string representation of this semantic relation
     */
    public String getLabel() {
        return ":" + label;
    }

    /**
     * Returns the SemanticRelation object with the given label
     * Ex: SemanticRelation.getByLabel(":arg-0") -> SemanticRelation.arg_0
     * You can include or exclude the ':' at the start
     * @param label The string label
     */
    public static SemanticRelation getByLabel(String label) {
        try {
            // valueOf maps string names to SemanticRelations
            return SemanticRelation.valueOf(label
                                            .replace("-", "_")
                                            .replace(":", ""));
        } catch(IllegalArgumentException|NullPointerException e) {
            if(Logger.debug()) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
