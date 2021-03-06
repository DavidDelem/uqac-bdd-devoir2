import urllib2
import re
import json

from bs4 import BeautifulSoup

from multiprocessing import Pool



def crawl_bestiary(url, regex):
    baseUrl='http://paizo.com'
    monsters = []
    response = urllib2.urlopen(baseUrl + url[0] + url[1])
    html = response.read()

    hmtlNoHeader =  html.split('A Monsters',1)[1]
    htmlNoHeaderNoFooter =  hmtlNoHeader.split('footer',1)[0]

    allMonsterLink= re.findall('href ?= ?"([^"]*)">([^<]*)',htmlNoHeaderNoFooter, re.DOTALL)

    counter = 0.0
    # Loop over all monsters of one bestiary
    for link in allMonsterLink:
        print url[0]," is at ",round(counter/len(allMonsterLink)*100,1),"%"
        counter+=1
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
            # print(monsterName)
            allMonsterSpells = re.findall(regex,monsterHtml, re.DOTALL)
            # print(allMonsterSpells)
            if allMonsterSpells:
                for monsterSpell in allMonsterSpells:
                    monster['spells'].append(monsterSpell)
                
            monsters.append(monster)
            
    return monsters
    
    
def main():
    pool = Pool(processes=5)
        
    bestiary1 = (['/pathfinderRPG/prd/bestiary/','monsterIndex.html'])
    bestiary2 = (['/pathfinderRPG/prd/bestiary2/','additionalMonsterIndex.html'])
    bestiary3 = (['/pathfinderRPG/prd/bestiary3/','monsterIndex.html'])
    bestiary4 = (['/pathfinderRPG/prd/bestiary4/','monsterIndex.html'])
    bestiary5 = (['/pathfinderRPG/prd/bestiary5/','index.html'])
    
    b1 = pool.apply_async(crawl_bestiary, [bestiary1, '\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?>([^<]*)']) 
    b2 = pool.apply_async(crawl_bestiary, [bestiary2, '\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?>([^<]*)']) 
    b3 = pool.apply_async(crawl_bestiary, [bestiary3, '\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?>([^<]*)']) 
    b4 = pool.apply_async(crawl_bestiary, [bestiary4, '\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?>([^<]*)']) 
    b5 = pool.apply_async(crawl_bestiary, [bestiary5, '\/pathfinderRPG\/prd\/coreRulebook\/spells[^"]*" ?><em>([^<]*)']) 

    a1 = b1.get(timeout=99999)
    a2 = b2.get(timeout=99999)
    a3 = b3.get(timeout=99999)
    a4 = b4.get(timeout=99999)
    a5 = b5.get(timeout=99999)
    allMonsters = a1+a2+a3+a4+a5
    allMonsterFile = open("allMonsters.json","w")
    allMonsterFile.write(json.dumps(allMonsters, sort_keys=True, indent=4, separators=(',', ': ')))

    
if __name__ == '__main__':
    main()