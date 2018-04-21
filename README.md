# uqac-bdd-devoir2

<h2>Rapport du devoir 2 de Bases de données réparties - Hiver 2018</h2>
<b>David Delemotte, Paul Michaud, Rénald Morice, Loïc Bothorel</b>


---

<h3>Exercice 1</h3>

---

<h4>Partie 1</h4>

`Fichiers` &nbsp;
Crawler: <i>crawlers/crawler.py</i> &nbsp;&nbsp; - &nbsp;&nbsp; Json obtenu: <i>crawlers/allMonsters.json</i><br>

`Ce qu'on a fait:` &nbsp; Crawler en Python puis stockage des données au format JSON (chemin ci-dessus).

---
<h4>Partie 2</h4>

`Fichiers` &nbsp;
Code scala: <i>src/main/scala/Exercice1</i><br>

`Ce qu'on a fait`<br>
Nous avons mis les données crawlées dans un RDD Spark pour que toutes les opérations puissent être parallélisées entre nos 4 machines. Pour cela, on défini un Master et des Slaves. Le Master fait la commande suivante: <i>commande</i>, puis les Slaves font la suivante pour le rejoindre: <i>commande</i>. Voilà le résultat dans la console (1 master et 3 slaves):

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURE D’ECRAN DE LA CONSOLE [#f03c15](https://placehold.it/15/f03c15/000000?text=+)

---

<h4>Partie 3</h4>

`Fichiers:`  &nbsp;
Code scala: <i>src/main/scala/Exercice1</i>  &nbsp;&nbsp; - &nbsp;&nbsp; Sorts de healing: <i>chemain du fichier html ou on les a save</i><br>  &nbsp;&nbsp; - &nbsp;&nbsp; Bonus tous les sorts: <i> chemainfichier html ou on les a save</i><br>

`Ce qu'on a fait`<br>
Nous avons créé une batch view permettant à Pito de visualiser rapidement les créatures qui peuvent le tirer d’affaire grâce à un reduceByKey. Le résultat est le suivant:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURE D’ECRAN DES SORTS DE HEALING ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)

Nous l’avons également fait pour tous les sorts (bonus) en faisant un groupByKey. Voici une partie des résultats:

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) CAPTURES D’ECRAN PARTIELLE DE TOUS LES SORTS ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)

---

<h3>Exercice 2</h3>

---

<h4>Combat 1. Solar vs Éclaireurs Orcs</h4>

`Fichiers:` &nbsp;  Code principal: <i>src/main/scala/Exercice2/Combat1</i> &nbsp;&nbsp; - &nbsp;&nbsp; Nos monstres: <i>src/main/Exercice2/Bestiary</i><br>


`Ce qu'on a fait`<br>

C'est un combat entre le Solar et les monstres pour protéger Pito, en utilisant GraphX de Spark. Le code de la boucle principale, que nous allons expliquer ici, se trouve dans <i>src/main/scala/Exercice2/Game.scala</i>

On réalise un certain nombre d'itérations (jusqu'à atteindre une condition d'arrêt). Lors de chaque itérations, différentes étapes qui font évoluer le graphe ont lieues:

<b>1. La première étape consiste, pour chaque monstre, à choisir et mettre à jours la cible à attaquer, de la façon suivante:</b>
- Dans le premier AgregateMessages, on détermine pour chaque monstre l'énnemi le plus interessant à attaquer.
- Dans le premier JoinVertixes, met à jours le monstre avec la bone target (pour cela, on créé une nouvelle instance du monstre avec la nouvelle target, qu'on return pour remplacer la précédente, car si on modifie directement le monstre ça n'est pas pris en compte).

<b>2. La deuxiéme étape consiste à réaliser ces attaques en faisant perdre le nombre correct de HP:</b>

- Dans le deuxiéme AgregateMessages, on fait le calcul des domages pour chaque monstre (principe du map reduce)
- Dans le deuxiéme joinVertixes, on fait perdre les HP à chaque monstre (pour cela, on créé une nouvelle instance du monstre avec les nouveaux HP, qu'on return pour remplacer la précédente, car si on modifie directement le monstre ça n'est pas pris en compte).

<b>3. La dernière étape consiste à vérifier les conditions d'arrêt</b>
- On compte le nombre d'alliés et d'énemis encore vivant en faisant un filter suivi d'un count.
- Si il reste 0 énemis: Pito est sauvé :D
- Si il reste 0 alliés: Pito à perdu :(
- Sinon, on continue la boucle pour faire une nouvelle itération.

<b>4. Enfin, l'état du graphe est affichée à chaque tour dans la console de la façon suivante:</b>
- Le nom du monstre (son ID, ses HP) ---> le monstre qu'il attaque(son ID, ses HP)

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) IMAGE CONSOLE ![#f03c15](https://placehold.it/15/f03c15/000000?text=+)

---

<h4>Combat 2. Les Orcs et le dragon vert attaquent le village de Pito</h4>

`Fichiers:` &nbsp;  Code principal: <i>src/main/scala/Exercice2/Combat2</i> &nbsp;&nbsp; - &nbsp;&nbsp; Nos monstres: <i>src/main/Exercice2/Bestiary</i><br>

`Ce qu'on a fait`<br>

Ce combat reprends les bases du premier, en un peut plus compliqué ! Il y a beaucoup de monstres alors on créé certains RDD à partir d'un array créé avec une boucle for.
