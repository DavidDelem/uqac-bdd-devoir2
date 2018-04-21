# uqac-bdd-devoir2

<h2>Rapport du devoir 2 de Bases de données réparties - Hiver 2018</h2>
<b>David Delemotte, Paul Michaud, Rénald Morice, Loïc Bothorel</b>


---

<h3>Exercice 1</h3>

---

<h4>Partie 1</h4>

<b>Fichiers:</b><br>
Le crawler: <i>crawlers/crawler.py</i><br>
Le json obtenu: <i>crawlers/allMonsters.json</i><br>

<b>Ce qu'on a fait:</b><br>
Nous avons réalisé le crawler en Python puis avons stocké les données au format JSON (voir ci-dessus).

---
<h4>Partie 2</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Main.scala</i><br>
Représente un monstre: <i>src/main/Monster.scala</i><br>

<b>Ce qu'on a fait:</b><br>
Nous avons mis les données crawlées dans un RDD Spark pour que toutes les opérations puissent être parallélisées entre nos machines. Pour cela, on défini un Master et des Slaves.

Le Master fait la commande suivante dans spark: commande
Puis les Slaves font la commande suivante pour le rejoindre: commande

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15` CAPTURE D’ECRAN DE LA CONSOLE [#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15`

---

<h4>Partie 3</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Exercice1/Main.scala</i><br>
Sorts de healing: <i>chemain du fichier html ou on les a save</i><br>
Bonus tous les sorts: <i> chemainfichier html ou on les a save</i><br>

<b>Ce qu'on a fait:</b><br>
Nous avons créé une batch view permettant à Pito de visualiser rapidement les créatures qui peuvent le tirer d’affaire grâce à un reduceByKey. Le résultat est le suivant:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15` CAPTURE D’ECRAN DES SORTS DE HEALING ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15` 

Nous l’avons également fait pour tous les sorts (bonus) en faisant un groupByKey. Voici une partie des résultats:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15` CAPTURES D’ECRAN PARTIELLE DE TOUS LES SORTS ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `#f03c15` 


---

<h3>Exercice 2</h3>

<h4>Combat 1. Solar vs Éclaireurs Orcs</h4>

<b>Ce qu'on doit faire:</b><br>

graph Graph
var g: graph

while(true) {<br>
 <b>1 . génrere messages</b>

val msg = g.agregatemessages(
	fct 1
	fct 2
)

<b>2. join joinVertices( function qui dit comment est fait le join)</b>

g = g.joinVertixes( beaucoup de code la dedans)

<b>3. check fin</b>

}


<h4>Combat 2. Les Orcs et le dragon vert attaquent le village de Pito</h4>

