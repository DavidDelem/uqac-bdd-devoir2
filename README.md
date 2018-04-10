# uqac-bdd-devoir2

<h2>Rapport du devoir 2 de Bases de données réparties - Hiver 2018</h2>
<b>David Delemotte, Paul Michaud, Rénald Morice, Loïc Bothorel</b>

<h3>Exercice 1</h3>

<h4>Partie 1</h4>

<b>Fichiers:</b>
Le crawler: crawlers/crawler.py
Le json obtenu: crawlers/allMonsters.json

<b>Ce qu'on a fait:</b>
Nous avons réalisé un crawler en Python puis avons choisi de faire un JSON.

<h4>Partie 2</h4>

<b>Fichiers:</b>
Classe principale: src/main/scala/Main.scala
Représente un monstre: src/main/Monster.scala

<b>Ce qu'on a fait:</b>
Nous avons mis les données crawlées dans un RDD Spark pour que toutes les opérations puissent être parallélisées entre nos machines. Pour cela, on défini un Master et des Slaves.

Le Master fait la commande suivante dans spark:
Puis les Slaves font la commande suivante pour le rejoindre:

CAPTURES D’ECRAN

<h4>Partie 3</h4>

<b>Fichiers:</b>
Classe principale: src/main/scala/Main.scala
Sorts de healing: fichier html ou on les a save
Bonus tous les sorts: fichier html ou on les a save

<b>Ce qu'on a fait:</b>
Nous avons créé une batch view permettant à Pito de visualiser rapidement les créatures qui peuvent le tirer d’affaire grâce à un reduceByKey:

CAPTURE D’ECRAN DES SORTS DE HEALING

Nous l’avons également fait pour tous les sorts (bonus) en faisant un groupByKey:

CAPTURES D’ECRAN PARTIELLE DE TOUS LES SORTS

<h3>Exercice 2</h3>

<b>Ce qu'on doit faire:</b>

graph Graph
var g: graph

while(true) {
 <b>1 . génrere messages</b>

val msg = g.agregatemessages(
	fct 1
	fct 2
)

<b>2. join joinVertices( function qui dit comment est fait le join)</b>

g = g.joinVertixes( beaucoup de code la dedans)

<b>3. check fin</b>

}

