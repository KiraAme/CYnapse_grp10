# CYnapse_grp10

Version JavaFX :
-nécessite soit : -  une version antérieure à Java 11, Java 8 marche très bien
		  -  une version plus récente avec sa version de javafx à télécharger en plus
-comment ça marche:
		  - le labyrinthe est contraint à une taille de 20 x 20 
		  - l'utilisateur rentre : longueur, largeur, seed, vitesse et choisi s'il veut un labyrinthe parfait/imparfait et le type de génération, pas à pas ou direct. Il peut aussi restaurer un labyritnhe précédemment sauvegarder
		  - une fois généré, on peut modifier le labyrinthe en cliquant sur une case puis en choisissant la direction du mur à changer. On peut sauvegarder le labyrinthe également. Et on peut le résoudre avec : Trémaux, Dead-end et Dijkstra. Les 3 en versions directes ou pas à pas.
		  - en cours de résolution pas à pas les statistiques s'affichent en temps réel et en direct elles s'affichent dès la fin de la résolution.
		  - un bouton retour est aussi disponible qui annule les algos pas à pas et qui retourne au menu principal de génération.
		  - un bouton sauvegarder est aussi disponible qui sauvegarde le labyrinthe affiché.

Version Terminal :

-comment ça marche:
		  - le labyrinthe est contraint à une taille de 30 x 30
		  - l'utilisateur rentre : longueur, largeur, seed et choisi s'il veut un labyrinthe parfait/imparfaitt.
		  - une fois généré, on peut modifier le labyrinthe en indiquant les coordonées de la case puis en choisissant la direction du mur à changer (n/s/e/o). 
		  - on peut sauvegarder le labyrinthe également. Et on peut le résoudre avec : Trémaux, Dead-end et Dijkstra. Il peut aussi restaurer un labyrinthe précédemment sauvegarder.
		  - et on peut quitter, ce qui met fin au programme.
		  🧩 Générateur et Solveur de Labyrinthes — JavaFX & Terminal
Ce projet permet de générer, modifier, sauvegarder et résoudre des labyrinthes via une interface graphique JavaFX ou en mode terminal. Plusieurs algorithmes de résolution sont disponibles, ainsi qu'un mode pas à pas pour observer leur fonctionnement en direct.

