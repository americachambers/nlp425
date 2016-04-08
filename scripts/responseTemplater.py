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

def addToFile(utterance, 
    DATag,
    fName = "../src/edu/pugetsound/mathcs/nlp/processactions/responses.json"):
    
    with open(fName, 'r') as f:
        responses = json.load(f)

    if DATag+'.java' not in responses:
        print("Error: DATag '"+DATag+"' not in responses json file")
        return -1

    responses[DATag+'.java'].append({utterance: msrsplat.main(utterance,strRes=False)[0]['Value']})
    data = {}
    for key, value in responses.items():
        data[key.strip('.java')] = value
    with open(fName,'w') as f:
        json.dump(data,f)

if __name__ == '__main__' and len(sys.argv) > 2:

    if any([arg in ['-h','--help'] for arg in sys.argv]):
        print('''Usage: 
            \n  python projects.py args sentence
            \n\nArgs:
            \n  sentence: a sentence to be analyzed in English, enclosed by double-quotes
            \n  -h, --help: Display this help message.
            \n  -j, --json-file: ''')
        exit(0)

    i = getArgIndex('-j')+1
    if i>0:
        fName = sys.argv[i].strip()
    else:
        fName = "../src/edu/pugetsound/mathcs/nlp/processactions/responses.json"
    addToFile(sys.argv[-2], sys.argv[-1], fName)


      # '-n': ['-n','--new-json-file'],
      # '-r': ['-r','--read-java-files'],

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