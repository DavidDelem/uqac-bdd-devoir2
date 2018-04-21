# uqac-bdd-devoir2

<h2>Rapport du devoir 2 de Bases de données réparties - Hiver 2018</h2>
<b>David Delemotte, Paul Michaud, Rénald Morice, Loïc Bothorel</b>


---

<h3>Exercice 1</h3>

---

<h4>Partie 1</h4>

`Fichiers` &nbsp;
Crawler: <i>crawlers/crawler.py</i> &nbsp;&nbsp; - &nbsp;&nbsp; Json obtenu: <i>crawlers/allMonsters.json</i><br>

`Ce qu'on a fait:`<br>
Nous avons réalisé le crawler en Python puis avons stocké les données au format JSON (voir ci-dessus).

---
<h4>Partie 2</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Exercice1/Main.scala</i><br>
Représente un monstre: <i>src/main/Exercice1/Monster.scala</i><br>

<b>Ce qu'on a fait:</b><br>
Nous avons mis les données crawlées dans un RDD Spark pour que toutes les opérations puissent être parallélisées entre nos machines. Pour cela, on défini un Master et des Slaves. Le Master fait la commande suivante dans spark: <i>commande</i>, puis les Slaves font la commande suivante pour le rejoindre: <i>commande</i>. Voilà le résultat dans la console (1 master et 3 slaves):

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURE D’ECRAN DE LA CONSOLE [#f03c15](https://placehold.it/15/f03c15/000000?text=+)

---

<h4>Partie 3</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Exercice1/Main.scala</i><br>
Sorts de healing: <i>chemain du fichier html ou on les a save</i><br>
Bonus tous les sorts: <i> chemainfichier html ou on les a save</i><br>

<b>Ce qu'on a fait:</b><br>
Nous avons créé une batch view permettant à Pito de visualiser rapidement les créatures qui peuvent le tirer d’affaire grâce à un reduceByKey. Le résultat est le suivant:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURE D’ECRAN DES SORTS DE HEALING ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)

Nous l’avons également fait pour tous les sorts (bonus) en faisant un groupByKey. Voici une partie des résultats:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURES D’ECRAN PARTIELLE DE TOUS LES SORTS ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)


---

<h3>Exercice 2</h3>

---

<h4>Combat 1. Solar vs Éclaireurs Orcs</h4>

<b>Fichiers:</b><br>
Classe principale: <i>src/main/scala/Exercice2/Game.scala</i><br>
Le package ou sont stockés nos monstres: <i>src/main/Exercice2/Monsters/*.scala</i><br>


<b>Ce qu'on a fait:</b><br>

L'objectif était de faire un combat entre le Solar et les monstres pour protéger Pito, en utilisant GraphX de Spark. Le code de la boucle principale, que nous allons expliquer ici, se trouve dans <i>src/main/scala/Exercice2/Game.scala</i>

On réalise un certain nombre d'itérations (jusqu'à atteindre une condition d'arrêt). Lors de chaque itérations, différentes étapes, qui font évoluer le graphe ont lieues:

La première étape consiste, pour chaque monstre, à choisir et mettre à jours la cible à attaquer, de la façon suivante:
- Dans le premier AgregateMessages, on 
- Dans le premier JoinVertixes, on détermine pour chaque monstre le monstre le plus interessant à attaquer.

La deuxiéme étape consiste à réaliser ces attaques en faisant perdre le nombre correct de HP:

- Dans le deuxiéme AgregateMessages, on fait le calcul des domages pour chaque monstre (principe du map reduce)
- Dans le deuxiéme joinVertixes, on fait perdre les HP à chaque monstre (pour cela, on créé une nouvelle instance du monstre avec les nouveaux HP, qu'on retourne).

Enfin, nous faisons un filter suivi d'un count pour calculer le nombre d'enemis encore vivants et le nombre d'alliés encore vivants sur le graphe. Si il reste 0 énemis: Pito est sauvé, si il reste 0 alliés: Pito à perdu... Sinon, on continue la boucle.

A chaque tour de bouble, nous mettons à jours notre affichage graphique réalisé avec ScalaFX (JavaFX pour scala). Voilà le résultat, on peut voir que nous avons réussi à sauver Pito ! 

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) GIF ANIME BATAILLE ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)

---

<h4>Combat 2. Les Orcs et le dragon vert attaquent le village de Pito</h4>

Ce combat reprends les bases du premier, en un peut plus compliqué !
