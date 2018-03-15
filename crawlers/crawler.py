import urllib2
import re
import json

from bs4 import BeautifulSoup




def crawl_bestiary(url):
    baseUrl='http://paizo.com'
    monsters = []
    response = urllib2.urlopen(baseUrl + url[0] + url[1])
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
        if url[0] == '/pathfinderRPG/prd/bestiary/':
            monsterUrl = baseUrl + url[0] + linkWithNoAnchor
        else:
            monsterUrl = baseUrl + linkWithNoAnchor
            
        monsterName = link[1]

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
            if allMonsterSpells:
                for monsterSpell in allMonsterSpells:
                    monster['spells'].append(monsterSpell)
                
            monsters.append(monster)
            
    print json.dumps(monsters, sort_keys=True, indent=4, separators=(',', ': '))
    
    
def main():
    bestiary1 = (['/pathfinderRPG/prd/bestiary/','monsterIndex.html'])
    bestiary2 = (['/pathfinderRPG/prd/bestiary2/','additionalMonsterIndex.html'])
    bestiary3 = (['/pathfinderRPG/prd/bestiary3/','monsterIndex.html'])
    bestiary4 = (['/pathfinderRPG/prd/bestiary4/','monsterIndex.html'])
    bestiary5 = (['/pathfinderRPG/prd/bestiary5/','index.html'])
    
    crawl_bestiary(bestiary1)
    
if __name__ == '__main__':
    main()