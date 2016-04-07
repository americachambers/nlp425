"""Wrapper for getting linguistic analyses from Splat service.

MSR-SPLAT (http://msrsplat.cloudapp.net) is a service provided by the NLP
group for performing linguistic analyses, such as parsing and POS tagging.
"""

import requests
import json
import sys
import ast

class Client:
    """Client for accessing the SPLAT service."""

    def __init__(self, appId, protocol="http:", verify=True, host="msrsplat.cloudapp.net", endpoint="SplatServiceJson.svc"):
        """Construct a new client.

        Args:
            appId (str): your application ID, provided by the SPLAT maintainer
            protocol (str): which protocol to use, default of "http"; could be "https"
            verify (bool/filename): True to verify the SSL certificate normally; False to ignore verification; filename of cert otherwise.
            host (str): hostname to contact, normally msrsplat.cloudapp.net
            endpoint (str): path to the service endpoint, normally SplatServiceJson.svc

        """
        self.appId     = appId
        self.protocol  = protocol
        self.verify    = verify
        self.host      = host
        self.endpoint  = endpoint
    #request   = "http://msrsplat.cloudapp.net/SplatServiceJson.svc/Analyze?language="+language+"&analyzers="+analyzers+"&appId=&jsonp=&input="

    def languages(self):
        """Get the list of supported languages, identified by their two letter ISO code (e.g. 'en' for 'English')
        
        Returns:
            List of strings of language codes"""
        url = self.serviceUrl() + "/Languages"
        res = requests.request("get", url, verify=self.verify)
        print(res.text)
        return res.json()

    def analyzers(self, language):
        """Get the list of supported analyzers for the given language

        Returns:
            List of analyzer names available for that language"""
        url = self.serviceUrl() + "/Analyzers?language=" + language
        res = requests.request("get", url, verify=self.verify)
        return res.json()

    def analyze(self, language, analyzers, inputString):
        """Get analyzed version of given text

        Args:
            language (str): two letter ISO code for the input language
            analyzers (list of str): list of analyze names to apply
            inputString (str): raw input text to be analyzed

        Returns:
            list of key value pairs, key being the analyzer name, and
            value being the analyzer result as JSON"""
        url = self.serviceUrl() + "/Analyze"
        url = url + "?language=" + language
        url = url + "&analyzers=" + ','.join(analyzers)
        url = url + "&appId=" + self.appId
        url = url + "&jsonp="
        url = url + "&input=" + inputString
        res = requests.request("get", url, verify=self.verify)
        return res.json()

    def serviceUrl(self):
        """Fully qualified URL of the service"""
        return self.protocol + "//" + self.host + "/" + self.endpoint;


def getArgIndex(arg):

    args = {
      '-h': ['-h','--help'],
      '-a': ['-a','--analyzers'],
    }

    if any([a in args[arg] for a in sys.argv]):
        for a in args[arg]:
            if a in sys.argv:
                return sys.argv.index(a)
    return -1

def main():
    c = Client("D348B4FB-0B53-4E8A-8862-91E668EBCE17", protocol="http:", verify=False)

    lang = "en"
    if any([arg in ['-h','--help'] for arg in sys.argv]) or len(sys.argv) == 1:
        print('''Usage: 
            \n  python projects.py args sentence
            \n\nArgs:
            \n  sentence: a sentence to be analyzed in English, enclosed by double-quotes
            \n  -h, --help: Display this help message.
            \n  -a, --analyzers list: Set the analyzers to be used on the sentence, in the form of a comma-seperated list enclosed by double quotes. Defaults to [AMR]
            \n\nPossible options for analyzers:
              AMR
              Base Forms
              Chunker
              Constituency_Tree
              ContentWords
              CoRef
              Dependency_Tree
              Katakana_Transliterator
              Labeled_Dependency_Tree
              Lemmas
              Named_Entities
              POS_Tags
              Semantic_Roles
              Semantic_Roles_Scores
              Sentiment
              Stemmer
              Tokens''')
        exit(0)

    i = getArgIndex('-a')+1
    if i>0:
        analysers = [s.strip() for s in sys.argv[i][1:-1].split(',')]
    else:
        analysers = ["AMR"]

    return c.analyze(lang, analysers, sys.argv[-1])

if __name__ == '__main__':
  print(main())