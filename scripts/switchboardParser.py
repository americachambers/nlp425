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

getUttInfo = re.compile("\s+")
currentLine = ['','']

f = open("models/responses/swb_parsed.csv", 'w')
for folder in os.listdir("resources/swb1_dialogact_annot/scrubbed"):
    for uttFile in os.listdir("resources/swb1_dialogact_annot/scrubbed/"+folder):
        if '.utt' in uttFile:
            with open('/'.join(["resources/swb1_dialogact_annot/scrubbed",folder,uttFile]), 'r') as uF:
                lastSpeaker = None
                for line in uF:
                    if len(line) > 2:
                        try:
                            thisDALabel,thisAgentNum,thisUttNum,thisUtt = [x.strip() for x in getUttInfo.split(line if line[0]!=' ' else line.strip(), maxsplit=3)]
                            if len(thisUtt.strip()) > 1:
                                thisAgent, convoNum = thisAgentNum.split('.')
                                thisAgent = thisAgent == "A"
                                convoNum = int(convoNum)
                                thisUttNum = int(thisUttNum[3:])
                                if thisUttNum > 1 and thisAgent==lastSpeaker:
                                    if (thisDALabel == currentLine[0] or thisDALabel == '%') and thisUtt[0].islower():
                                        currentLine[1] += " "+thisUtt
                                    elif thisDALabel in daTagLabels and '@' not in line:
                                        currentLine = [thisDALabel, thisUtt]
                                elif thisAgent!=lastSpeaker and thisUttNum == 1:
                                    if "" not in currentLine:
                                        f.write(','.join([x.replace(","," ") for x in currentLine]) +"\n") 
                                    if thisDALabel in daTagLabels and '@' not in line:
                                        lastSpeaker = thisAgent
                                        currentLine = [thisDALabel, thisUtt]
                                    else:
                                        f.write("\n")
                                        lastSpeaker = None
                                        currentLine = ["",""]
                                else:
                                    f.write("\n") 
                        except Exception as e:
                            pass

f.close()