import json, sys, os
sys.path.append("../scripts")
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

utterances, AMRs, DATags = None, None, None

def analyzeUtteranceString(utteranceStr):
    global utterances, AMRs
    for msRes in msrsplat.main(utteranceStr,analysers = ["AMR","Tokens"],strRes=False):
        if msRes['Key'] == "Tokens":
            utterances = [ 
                (' '.join([x["RawToken"] 
                    for x in tokendict['Tokens']]))
                .replace(' ,', ',')
                .replace(' .', '.')
                .replace(' !', '!')
                .replace(' ?', '?')
                .replace(' ;', ';')
                .replace(' \'', '\'')
                for tokendict in msRes['Value']]
        elif msRes['Key'] == "AMR":
            AMRs = msRes['Value']
        else:
            print("Error with MSResponse")
 


'''
Insert str of utterances with corresponding list of DATags into responses.json. 
Reads in and overwrites whole file (hence, not very efficient when called many times).
Call only once on a large list of items for batch use.
'''
def main(fName = "../src/edu/pugetsound/mathcs/nlp/processactions/srt/responses.json"):
    
    if os.path.isfile(fName):
        with open(fName, 'r') as f:
            responses = json.load(f)
    else:
        responses = {}

    if None in [utterances, AMRs, DATags]:
        print("Error, null pointer to either utterances, AMRs, or DATags")
        print(utterances, AMRs, DATags)
        exit(0)
    for i in range(len(AMRs)):
        if DATags[i] not in responses:
            responses[DATags[i]] = {}
        responses[DATags[i]][utterances[i]] = AMRs[i]

    with open(fName,'w') as f:
        json.dump(responses,f)

if __name__ == '__main__' and len(sys.argv) > 2:

    if any([arg in ['-h','--help'] for arg in sys.argv]):
        print('''Usage: 
            \n  python responseTemplater.py args utterances DATags 
            \n\nArgs:   
            \n  DATags: DA Tags as a list to be used as keys for each respective utterance in the next arg, enclosed by double-quotes
            \n  utterances: utterances, enclosed by double quotes, to be used as resposnses to the user. AMR will be queried via msrsplat
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
    analyzeUtteranceString(sys.argv[-2])
    DATags = [s.strip() for s in sys.argv[-1][1:-1].split(',')]
    main(fName)






    #   '-n': ['-n','--new-json-file'],
    #   '-r': ['-r','--read-java-files'],

    # if i>0:
    #     fName = sys.argv[i].strip()
    # else:
    #     i = getArgIndex('-n')+1
    #     if i>0:
    #         newFile = True
    #         fName = sys.argv[i].strip()
    #     else:
    #         fName = "../src/edu/pugetsound/mathcs/nlp/processactions/responses_with_amr.json"


    # if not newFile:
    #     with open(fName, 'r') as f:
    #         responses = json.load(f)
    # else:
    #     responses = {}
    #     i = getArgIndex('-r')+1
    #     if i>0:
    #         for key in os.listdir("../src/edu/pugetsound/mathcs/nlp/processactions/srt"):
    #             if ".java" in key:
    #                 key = key.strip(".java")
    #                 responses[key] = []
    #                 with open("../src/edu/pugetsound/mathcs/nlp/processactions/srt/"+key, 'r') as f:
    #                     (' '.join([x.strip() for x in f.readlines()])).split('}')

    #         lines = [x.strip().strip('\"') for x in stdin.readlines()]
    #         data = dict(reduce(lambda x,y: x[:-1]+[(x[-1][0],x[-1][1]+[{y[:-1].strip('\"'):msrsplat.main(y[:-1].strip('\"'),strRes=False)[0]['Value']}])] if '.java' not in y else x+[(y,[])],lines,[]))

    #     else: