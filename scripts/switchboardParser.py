'''

This script, when ran from the root NLP425 directory, 
can parse the switchboard corpus into a csv file wherein
field 1 is the DATag label and field 2 is the utterance.

Each conversation is seperated by a blank line, and within
each conversation, multiple sequential utterances are not 
recorded (only the first), with replacement occuring for
certain annotation tags.

'''
import sys,os

# QUESTION("q"),
    
#     STATEMENT("s"),
    
#     BACKCHANNEL("b"),
    
#     FORWARD_LOOKING("f"),
    
#     AGREEMENTS("a"),
    
#     COMMENT("*"),
    
#     CONTINUED_FROM_PREVIOUS("+"),
    
#     COLLABORATIVE_COMPLETION("^2"),
    
#     ABOUT_COMMUNICATION("^c"),
    
#     DECLARATIVE_QUESTION("^d"),
    
#     ELABORATED_REPLY_Y_N_QUESTION("^e"),
    
#     TAG_QUESTION("^g"),
    
#     HOLD("^h"),
#     MIMIC_OTHER("^m"),
    
#     QUOTATION("^q"),
    
#     ABOUT_TASK("^t"),
    
#     ACCEPT_PART("aap"),
    
#     ACTION_DIRECTIVE("ad"),
    
#     ACCEPT("aa"),
    
#     MAYBE("am"),
    
#     REJECT("ar"),
    
#     REJECT_PART("arp"),
    
#     CONTINUER("b"),
#     ASSESSMENT_APPRECIATION("ba"),
    
#     DOWNPLAYING_SYMPATHY("bd"),
    
#     REFORMULATE_SUMMARIZE("bf"),
    
#     ACKNOWLEDGE_ANSWER("bk"),
    
#     SIGNAL_NON_UNDERSTANDING("br"),
    
#     SYMPATHETIC_COMMENT("by"),
    
#     COMMIT("cc"),
    
#     OFFER("co"),
    
#     APOLOGY("fa"),
    
#     CONVENTIONAL_CLOSING("fc"),
    
#     EXCLAMATION("fe"),
#     CONVENTIONAL_OPENING("fp"),
    
#     THANKS("ft"),
    
#     WELCOME("fw"),
    
#     EXPLICIT_PERFORMATIVE("fx"),
    
#     DESCRIPTIVE_AFFIRMATIVE_ANSWER("na"),
    
#     ANSWER_DISPREFERRED("nd"),
    
#     DESCRIPTIVE_NEGATIVE_ANSWER("ng"),
    
#     NO("nn"),
    
#     INDETERMINATE_RESPONSE("no"),
    
#     YES("ny"),
    
#     OTHER("o"),
    
#     OPEN_OPTION("oo"),
    
#     QUESTION_RHETORICAL("qh"),
    
#     QUESTION_OPEN_ENDED("qo"),
    
#     QUESTION_ALTERNATIVE("qr"),
    
#     QUESTION_YES_NO_OR("qrr"),
    
#     QUESTION_WH("qw"),
    
#     QUESTION_YES_NO("qy"),
    
#     NARRATIVE_DESCRIPTIVE("sd"),
    
#     VIEWPOINT("sv");



f = open("models/responses/swb_parsed.csv", 'w')

for folder in os.listdir("resources/swb1_dialogact_annot"):
    for uttFile in os.listdir("resources/swb1_dialogact_annot/"+folder):
        if '.utt' in uttFile:
            with open('/'.join(["resources/swb1_dialogact_annot",folder,uttFile]), 'r') as uF:
                for line in uF:
