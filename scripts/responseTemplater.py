import json, sys, os

cwd = os.getcwd()
while cwd[-6:] != "nlp425":
  cwd = cwd[:-1]
sys.path.append(cwd[-7].join([cwd, 'lib', 'requests']))
sys.path.append(cwd[-7].join([cwd, 'scripts']))

import msrsplat

def getArgIndex(arg):

    args = {
      '-h': ['-h','--help'],
      '-j': ['-j','--json-file']
    }

    if any([a in args[arg] for a in sys.argv]):
        for a in args[arg]:
            if a in sys.argv:
                return sys.argv.index(a)
    return -1

tokens, AMRs, DATags = [],[],[]

def analyzeUtteranceString(uttstr):
    global tokens, AMRs
    oldNum = len(tokens)
    for msRes in msrsplat.main(uttstr,analysers = ["AMR","Tokens"],strRes=False):
        if msRes['Key'] == "Tokens":
            tokens.append([x["RawToken"] for x in msRes['Value'][0]['Tokens']])
            # [ 
            #     (' '.join([x["RawToken"] 
            #         for x in tokendict['Tokens']]))
            #     .replace(' ,', ',')
            #     .replace(' .', '.')
            #     .replace(' !', '!')
            #     .replace(' ?', '?')
            #     .replace(' ;', ';')
            #     .replace(' \'', '\'')
            #     for tokendict in msRes['Value']]
        elif msRes['Key'] == "AMR":
            AMRs.append(msRes['Value'][0])
        else:
            print("Error with MSResponse")
    return len(tokens)-oldNum
 


'''
Insert str of tokens with corresponding list of DATags into responses.json. 
Reads in and overwrites whole file (hence, not very efficient when called many times).
Call only once on a large list of items for batch use.
'''
def main(fName = "../src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json"):
    
    if os.path.isfile(fName):
        with open(fName, 'r') as f:
            responses = json.load(f)
    else:
        responses = {}

    if [] in [tokens, AMRs, DATags]:
        print("Error, null pointer to either tokens, AMRs, or DATags")
        print(tokens, AMRs, DATags)
    else:
        for i in range(len(AMRs)):
            if DATags[i] not in responses:
                responses[DATags[i]] = {}
            if AMRs[i] not in responses[DATags[i]] or len(tokens[i]) < len(responses[DATags[i]][AMRs[i]]):
                responses[DATags[i]][AMRs[i]] = tokens[i]

        with open(fName,'w') as f:
            json.dump(responses,f)

if __name__ == '__main__' and len(sys.argv) > 2:

    if any([arg in ['-h','--help'] for arg in sys.argv]):
        print('''Usage: 
            \n  python responseTemplater.py args tokens DATags 
            \n\nArgs:   
            \n  DATags: DA Tags as a list to be used as keys for each respective utterance in the next arg, enclosed by double-quotes
            \n  tokens: tokens, enclosed by double quotes, to be used as resposnses to the user. AMR will be queried via msrsplat
            \n  -h, --help: Display this help message.
            \n  -j, --json-file fileName: Provide the file name for the json dump output. Default is responses.json in the processactions/srt folder
            \n\nExampes:
            \n    python3 responseTemplater.py "Hahaha!" "[ExclamationTemplate]"
            \n    python3 responseTemplater.py -j "/tmp/responses.json" "Hahaha! Brahhhh, that's laaaaame AF." "[ExclamationTemplate,DisagreementTemplate]"''')
        exit(0)

    i = getArgIndex('-j')+1
    if i>0:
        fName = sys.argv[i].strip()
    else:
        fName = "../src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json"
    analyzeuttstring(sys.argv[-2])
    DATags = [s.strip() for s in sys.argv[-1][1:-1].split(',')]
    main(fName)
