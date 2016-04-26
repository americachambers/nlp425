'''

This script, when ran from the root NLP425 directory, 
can parse the switchboard corpus into a csv file wherein
field 1 is the DATag label and field 2 is the utterance.

Each conversation is seperated by a blank line, and within
each conversation, multiple sequential utterances are not 
recorded (only the first), with replacement occuring for
certain annotation tags.

For each line in a conversation:
  if the current speaker is on utterance 2+ or wrong speaker or daTag "%" 
    ignore
  elif DATag is in list, no @, last speaker is opposite of present, and utt# is 1
    write line, normal procedure
  elif the current speaker has an @ or the DATag not in list, 
    ignore & restart coonvo

'''
import sys,os,re

daTagLabels = ["q", "s", "b", "f", "a", "*", 
    "+", "^2", "^c", "^d", "^e", "^g", 
    "^h", "^m", "^q", "^t", "aap", "ad", 
    "aa", "am", "ar", "arp", "b", "ba", 
    "bd", "bf", "bk", "br", "by", "cc", 
    "co", "fa", "fc", "fe", "fp", "ft", 
    "fw", "fx", "na", "nd", "ng", "nn", 
    "no", "ny", "o", "oo", "qh", "qo", 
    "qr", "qrr", "qw", "qy", "sd", "sv"]

getUtteranceNum = re.compile("\s+utt\d+")
getAgentAndNum = re.compile("[AB]\.\d+\s+")
getDALabel = re.compile("^.{1,6}\s{5,}")
getUtt = re.compile("\s+.+$")


f = open("models/responses/swb_parsed.csv", 'w')

for folder in os.listdir("resources/swb1_dialogact_annot"):
    for uttFile in os.listdir("resources/swb1_dialogact_annot/"+folder):
        if '.utt' in uttFile:
            with open('/'.join(["resources/swb1_dialogact_annot",folder,uttFile]), 'r') as uF:
                lastSpeaker = None
                started = False
                for line in uF:
                    if '===========================' in line:
                        started = True
                    if started and len(line) > 2:
                        try:
                            thisDALabel = getDALabel.match(line).string.strip()
                            thisAgent, convoNum = getDALabel.match(line).string.strip().split('.')
                            thisAgent = thisAgent == "A"
                            convoNum = int(convoNum)
                            thisUttNum = int(getDALabel.match(line).string.replace("utt","").replace(":",""))
                            if thisUttNum > 1 or thisAgent==lastSpeaker or daTag == "%": 
                                pass
                            elif thisDALabel is in daTagLabels and '@' not in line and thisAgent!=lastSpeaker and thisUttNum == 1:
                                f.write(','.join([thisDALabel,line.split("utt"+str(thisUttNum)+":")[-1].replace(","," ")]) +"\n") 
                            else:
                                f.write("\n") 
                        except Exception:
                            pass


