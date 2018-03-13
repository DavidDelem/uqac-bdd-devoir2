#http://paizo.com/pathfinderRPG/prd/indices/bestiary.html

import urllib2
import re
import json

from bs4 import BeautifulSoup

url='http://paizo.com/pathfinderRPG/prd/indices/bestiary.html'
nbMonsters=2685
A="A"
#for x in range(1, nbMonsters):

response = urllib2.urlopen(url)
html = response.read()

hmtlNoHeader =  html.split('<ul id="index-monsters-A" title="A Monsters">',1)[1]
htmlNoHeaderNoFooter =  hmtlNoHeader.split('<div class = "footer">',1)[0]

allLink= re.findall ('(\/pathfinderRPG\/prd[^"]*)">([^<]*)',htmlNoHeaderNoFooter, re.DOTALL)
print len(allLink)



for link in allLink:
    print 'http://paizo.com'+link[0]
    print link[1]
    mr = urllib2.urlopen('http://paizo.com'+link[0])
    monsterHtml = mr.read()
    monsterHtmlFromH1 =  monsterHtml.split(link[1].split(',')[0],1)[1]
#    monsterHtmlOffense=monsterHtmlFromH1.split('<p class = "stat-block-breaker">Offense</p>',1)[1]
#    monsterHtmlOnlyOffense=monsterHtmlOffense.split('<p class = "stat-block-breaker">Statistics</p>',1)[0]
#
#    print 'http://paizo.com'+link[0]





