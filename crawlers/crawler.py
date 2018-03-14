import urllib2
import re
import json

from bs4 import BeautifulSoup

baseUrl='http://paizo.com'

bestiaryLinkList = []
bestiaryLinkList.append(['/pathfinderRPG/prd/bestiary/','monsterIndex.html'])
#bestiaryLinkList.append(['/pathfinderRPG/prd/bestiary2/','additionalMonsterIndex.html'])
#bestiaryLinkList.append(['/pathfinderRPG/prd/bestiary3/','monsterIndex.html'])
#bestiaryLinkList.append(['/pathfinderRPG/prd/bestiary4/','monsterIndex.html'])
#bestiaryLinkList.append(['/pathfinderRPG/prd/bestiary5/','index.html'])


monsters = []

# Loop over bestiaries
for bestiaryLink in bestiaryLinkList:
    
    response = urllib2.urlopen(baseUrl + bestiaryLink[0] + bestiaryLink[1])
    html = response.read()

    hmtlNoHeader =  html.split('A Monsters',1)[1]
    htmlNoHeaderNoFooter =  hmtlNoHeader.split('footer',1)[0]

    allMonsterLink= re.findall('href ?= ?"([^"]*)">([^<]*)',htmlNoHeaderNoFooter, re.DOTALL)

    # Loop over all monsters of one bestiary
    for link in allMonsterLink:
        
        #Delete anchor part of monster link
        if '#' in link[0]:
            linkWithNoAnchor = link[0].split('#',1)[0]
        else:
            linkWithNoAnchor = link[0]
        
        #There are relative and absolute links
        if bestiaryLink[0] == '/pathfinderRPG/prd/bestiary/':
            monsterUrl = baseUrl + bestiaryLink[0] + linkWithNoAnchor
        else:
            monsterUrl = baseUrl + linkWithNoAnchor
            
        monsterName = link[1]
        print monsterName

        mr = urllib2.urlopen(monsterUrl)
        
        #Delete all span, a monster detail page can contain span in the monster title
        monsterHtml = re.sub('<\/?span[^>]*>', '', mr.read())
        
        if monsterName in monsterHtml:
            monsterHtml = monsterHtml.split(monsterName,1)[1]
            monsterHtml = monsterHtml.split('Statistics',1)[0]
            
            monster = {}
            
            monster['name'] = monsterName
            monster['spells'] = []
            
            allMonsterSpells = re.findall('\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?>([^<]*)',monsterHtml, re.DOTALL)
            for monsterSpell in allMonsterSpells:
                monster['spells'].append(monsterSpell)
            
            monsters.append(monster)
    
print(monsters)