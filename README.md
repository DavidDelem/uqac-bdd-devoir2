# uqac-bdd-devoir2

<h2>Rapport du devoir 2 de Bases de données réparties - Hiver 2018</h2>
<b>David Delemotte, Paul Michaud, Rénald Morice, Loïc Bothorel</b>

Ce rapport explique et montre les résultats du travail réalisé dans les deux exercices du devoir 2.

---

<h3>Exercice 1</h3>

---

<h4>Partie 1</h4>

`Fichiers` &nbsp;
Crawler: <i>crawlers/crawler.py</i> &nbsp;&nbsp; - &nbsp;&nbsp; Json obtenu: <i>crawlers/allMonsters.json</i><br>

`Ce qu'on a fait:` <br>Nous avons réalisé le crawler en Python en parcourant les bestiaires de paizo.com, puis nous avons stocké les données au format JSON dans le dossier <i>crawlers</i>. Il contient plus de 1400 monstres ! En voici un petit aperçu:
```json
[
    {
        "name": "Seaweed Siren",
        "spells": [
            "shatter",
            "charm monster",
            "confusion",
            "bestow curse"
        ]
    },
    ...
]
```
---
<h4>Partie 2</h4>

`Fichiers` &nbsp;
Code scala: <i>src/main/scala/Exercice1</i><br>

`Ce qu'on a fait`<br>
Nous avons mis les données crawlées dans un RDD Spark pour que toutes les opérations puissent être parallélisées entre nos 4 machines. Pour cela, on défini un Master et des Slaves, quelqu'un lance le master puis les Slaves font la commande suivante pour le rejoindre: <i>./spark-class org.apache.spark.deploy.worker.Worker spark://IP_MASTER:PORT</i>. Depuis l'interface on peut voir que les slaves ont bien réussi à rejoindre le master et la running application pour lesquelles les opérations sont parallélisées:
<p align="center">
  <img src="_imgreadme/cluster.PNG" width="700px"/>
</p>

---

<h4>Partie 3</h4>

`Fichiers`  <br>
Code scala: <i>src/main/scala/Exercice1</i> <br>
Résultat sorts de healing: <i>src/main/scala/Exercice1/batchViewHealSpellMonsters.html</i><br>
Résultat bonus tous les sorts: <i>src/main/scala/Exercice1/batchViewSpellMonsters.html</i><br>

`Ce qu'on a fait`<br>
Nous avons créé une batch view permettant à Pito de visualiser rapidement les créatures qui peuvent le tirer d’affaire grâce à un reduceByKey. C'est à dire tout les sorts de healing. Le résultat est le suivant:

<p align="center">
  <img src="_imgreadme/sortshealing.PNG" width="700px"/>
</p>

Nous l’avons également fait pour tous les sorts (bonus) en faisant un groupByKey. Voici une petite partie du résultat, vous pouvez consulter le fichier html complet dans le dossier de l'exercice 1:

<p align="center">
  <img src="_imgreadme/sortsall.PNG" width="700px"/>
</p>

---

<h3>Exercice 2</h3>

---

<h4>Combat 1. Solar vs Éclaireurs Orcs</h4>

`Fichiers:` &nbsp;  Code principal: <i>src/main/scala/Exercice2/Combat1</i> &nbsp;&nbsp; - &nbsp;&nbsp; Nos monstres: <i>src/main/Exercice2/Bestiary</i><br>


`Ce qu'on a fait`<br>

C'est un combat entre le Solar et les monstres pour protéger Pito, en utilisant GraphX de Spark. Le code spécifique au combat 1 se trouve dans  <i>src/main/scala/Exercice2/Combat1</i> et le code partagé des deux combats (comme le bestiaire et les utilitaires) se trouve un niveau plus haut dans <i>src/main/scala/Exercice2</i>.

On commence par créer nos RDD de vertex et de edge dans <i>Combat1.scala</i>, puis on appelle la boucle principale qui se trouve dans <i>Game.scala</i>. On réalise un certain nombre d'itérations de cette boucle et lors de chaque itération, différentes étapes font évoluer le graphe:

<b>1. Pour chaque monstre, on commence par vérifier si on a besoin de faire certaines choses</b>
- Regénération (en vérité seul le Solar va se regénérer puisque la regénération des autres est à zéro).
- Déplacement (en fonction de la vitesse de chaque monstre).

```scala
val newVerticesMove = myGraph.vertices.map(vertex => {
    vertex._2.regenerate()
    vertex._2.move()
    vertex
})
myGraph = Graph(newVerticesMove, myGraph.edges)
```

<i>A noter que toutes les fonctions qui regénérent, font les mouvements, calculent les dommages reçus ect... (comme regenerate() et move() ici) se trouvent dans LivingEntity (classe parente de tout les monstres).</i>

<b>2. Pour chaque monstre, on choisi et on met à jours la cible à attaquer, de la façon suivante:</b>
- Dans le premier AgregateMessages, on détermine pour chaque monstre l'énnemi le plus interessant à attaquer.
- Dans le premier JoinVertixes, on met à jours le monstre avec la bonne target (pour cela, on créé une nouvelle instance du monstre avec la nouvelle target, qu'on return pour remplacer la précédente, car si on modifie directement le monstre ça n'est pas pris en compte).

```scala
val targetMessages = myGraph.aggregateMessages[LivingEntity](
    sendTargetMsg,
    mergeTargetMsg,
    fields
)

targetMessages.collect()

myGraph = myGraph.joinVertices(targetMessages) {
    (_, fighter, target) => {
        val newFighter = LivingEntityPrototype.create(fighter)
        newFighter.target = target
        newFighter
    }
}
```

<i>Détail des fonction sendTargetMsg() et mergeTargetMsg() en bas de la boucle principale.</i>

<b>3. On réalise ces attaques en faisant perdre le nombre correct de HP:</b>

- Dans le deuxiéme AgregateMessages, on fait le calcul des domages pour chaque monstre (principe du map reduce)
- Dans le deuxiéme joinVertixes, on fait perdre les HP à chaque monstre (pour cela, on créé une nouvelle instance du monstre avec les nouveaux HP, qu'on return pour remplacer la précédente, car si on modifie directement le monstre ça n'est pas pris en compte).

```scala
val damageMessages = myGraph.aggregateMessages[Int](
    sendDamageMsg,
    mergeDamageMsg,
    fields
)

damageMessages.collect()

myGraph = myGraph.joinVertices(damageMessages) {
    (_, damageReceiver, damages) => {
        val newDamageReceiver = LivingEntityPrototype.create(damageReceiver)
        newDamageReceiver.takeDamage(damages)
        newDamageReceiver
    }
}
```
<i>Détail des fonction sendDamageMsg() et mergeDamageMsg() en bas de la boucle principale.</i>

<b>4. Le nouvel état du graphe est affichée dans la console.</b>
- Le nom du monstre (ID, HP, POSITION) --DISTANCE-A-SA-CIBLE--> le monstre qu'il attaque(ID, HP, POSITION)

```scala
GraphConsole.printLivingEntityGraphVertices(myGraph)
```

<b>5
    . On vérifie les conditions d'arrêt</b>
- On compte le nombre d'alliés et d'ennemis encore vivant en faisant un filter suivi d'un count.
- Si il reste 0 ennemis: Pito est sauvé :D
- Si il reste 0 alliés: Solar est mort, Pito à perdu :(
- Sinon, on continue la boucle pour faire une nouvelle itération.

```scala
val nbBadGuysAlive = myGraph.vertices.filter{vertex =>vertex._2.team =="BadGuys" && vertex._2.hp >0}.count
val nbGoodGuysAlive = myGraph.vertices.filter{vertex =>vertex._2.team =="GoodGuys" && vertex._2.hp >0}.count

if(nbBadGuysAlive == 0){
    println("END OF LOOP : Solar successfully saved Pito :D")
    return
}
else if(nbGoodGuysAlive == 0){
    println("END OF LOOP : Unfortunatly, Solar and Pito died! Bad guys won :(")
    return
}
```

Voici le résultat final, la plupart du temps Pito est sauvé car le Solar est trés puissant grâce à son bouclier et sa regénération ! Il faut quand même une quarantaine de rounds pour tuer tout les énemis (les Double Axe Fury sont trés résistants).

<p align="center">
  <img src="_imgreadme/victoire.PNG" width="700px"/>
</p>

<h4>Combat 2. Les Orcs et le dragon vert attaquent le village de Pito</h4>

`Fichiers:` &nbsp;  Code principal: <i>src/main/scala/Exercice2/Combat2</i> &nbsp;&nbsp; - &nbsp;&nbsp; Nos monstres: <i>src/main/Exercice2/Bestiary</i><br>

`Ce qu'on a fait`<br>

Ce combat reprends les bases du premier, mais maintenant c'est plus compliqué ! Il y a beaucoup de monstres alors on créé certains RDD à partir d'un array créé avec une boucle for. Il y a aussi de nouvelles attaques pour les monstres.


---

<h4>Question ouverte, Comment gérer efficacement un système de distance 3D avec un graphe d’agents distribué? On met la distance sur le sommet? sur l’arête? problèmes de collision etc.</h4>
---

Travailler avec des vecteurs.
