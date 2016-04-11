import msrsplat, json, sys


def getArgIndex(arg):

    args = {
      '-h': ['-h','--help'],
      '-j': ['-j','--json-file'],
    }

    if any([a in args[arg] for a in sys.argv]):
        for a in args[arg]:
            if a in sys.argv:
                return sys.argv.index(a)
    return -1


'''
Insert list of utterances with corresponding DATags into responses.json. 
Reads in and overwrites whole file (hence, not very efficient when called many times).
Call only once on a large list of items for batch use.
'''
def addToFile(utterances, 
    DATags,
    fName = "../src/edu/pugetsound/mathcs/nlp/processactions/responses.json"):
    
    with open(fName, 'r') as f:
        responses = json.load(f)

    for DATag, utterance in zip(DATags, utterances):
        if DATag not in responses:
            print("Error: DATag '"+DATag+"' not in responses json file; utterance '"+utterance+"' won't be inserted")
        else:
            responses[DATag][utterance] = msrsplat.main(utterance,strRes=False)[0]['Value']
    with open(fName,'w') as f:
        json.dump(responses,f)

if __name__ == '__main__' and len(sys.argv) > 2:

    if any([arg in ['-h','--help'] for arg in sys.argv]):
        print('''Usage: 
            \n  python responseTemplater.py args DATags utterances
            \n\nArgs:   
            \n  DATags: DA Tags as a list to be used as keys for each respective utterance in the next arg, enclosed by double-quotes
            \n  utterances: utterances (as a list enclsoed by quotes) to be used as resposnses to the user. AMR will be queried via msrsplat
            \n  -h, --help: Display this help message.
            \n  -j, --json-file fileName: Provide the file name for the json dump output. Default is responses.json in the processactions folder
            \n\nExampes:
            \n    python3 responseTemplater.py "[Hahaha!]" "[ExclamationTemplate]"
            \n    python3 responseTemplater.py -j "/tmp/responses.json" "[Hahaha!,Brahhhh\, that's laaaaame AF.]" "[ExclamationTemplate,DisagreementTemplate]"''')
        exit(0)

    i = getArgIndex('-j')+1
    if i>0:
        fName = sys.argv[i].strip()
    else:
        fName = "../src/edu/pugetsound/mathcs/nlp/processactions/responses.json"
    addToFile([s.strip() for s in sys.argv[-2][1:-1].split(',')], [s.strip() for s in sys.argv[-1][1:-1].split(',')], fName)




    

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