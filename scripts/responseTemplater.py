from sys import stdin
from functools import reduce
from json import dump
import msrsplat
lines = [x.strip().strip('\"') for x in stdin.readlines()]
data = dict(reduce(lambda x,y: x[:-1]+[(x[-1][0],x[-1][1]+[{y[:-1].strip('\"'):msrsplat.main(y[:-1].strip('\"'),strRes=False)[0]['Value']}])] if '.java' not in y else x+[(y,[])],lines,[]))
with open('/tmp/responses_with_amr.json','w') as f:
    dump(data,f)