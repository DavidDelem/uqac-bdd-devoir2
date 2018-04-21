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
Classe principale: <i>src/main/scala/Exercice1/Main.scala</i><br>
Représente un monstre: <i>src/main/Exercice1/Monster.scala</i><br>

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

---

<h4>Combat 1. Solar vs Éclaireurs Orcs</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Exercice2/Main.scala</i><br>
Le package ou sont stockés nos monstres: <i>src/main/Exercice2/Monsters/*.scala</i><br>


<b>Ce qu'on a fait:</b><br>

L'objectif était de faire un combat entre le Solar et les monstres pour protéger Pito, en utilisant GraphX de Spark. Le code de la boucle principale, que nous allons expiquer ici, se trouve dans <i>src/main/scala/Exercice2/Main.scala</i>

A chaque itérations, nous suivons le processus suivant:

La première étape consiste, pour chaque monstre, à choisir qui attaquer
- Dans le premier AgregateMessages, on 
- Dans le premier JoinVertixes, on détermine pour chaque monstre le monstre le plus interessant à attaquer

La deuxiéme étape consiste à réaliser ces attaques et faire perdre le nombre correct de HP à chaque monstre attaqué (qui dépend de plusieurs paramétres comme la distance, l'armure, ect).

- Dans le deuxiéme AgregateMessages, on fait le calcul des domages pour chaque monstre (principe du map reduce)
- Dans le deuxiéme joinVertixes, on fait perdre les HP à chaque monstre (pour cela, on créé une nouvelle instance du monstre avec les nouveaux HP, qu'on retourne).

---
<h4>Combat 2. Les Orcs et le dragon vert attaquent le village de Pito</h4>

