🧩 Générateur et Solveur de Labyrinthes — JavaFX & Terminal
Ce projet permet de générer, modifier, sauvegarder et résoudre des labyrinthes via une interface graphique JavaFX ou en mode terminal. Plusieurs algorithmes de résolution sont disponibles, ainsi qu'un mode pas à pas pour observer leur fonctionnement en direct.

📦 Prérequis
JavaFX
Fonctionne avec :

Java 8 (JavaFX intégré)

Java 11+ (nécessite de télécharger JavaFX séparément)

🎮 Interface Graphique (JavaFX)
✅ Fonctionnalités
Taille fixe : 20 x 20 cases

L'utilisateur peut saisir :

Longueur et largeur

Graine aléatoire (seed)

Vitesse d'animation

Type de labyrinthe : Parfait ou Imparfait

Mode de génération : Pas à pas ou Direct

Option de restauration depuis un fichier sauvegardé

✏️ Interaction
Une fois le labyrinthe généré :

Cliquer sur une case pour modifier ses murs (choisir la direction)

Possibilité de sauvegarder le labyrinthe

Résolution possible via 3 algorithmes :

Trémaux

Dead-end Filling

Dijkstra

Chaque algorithme peut être lancé en mode direct ou pas à pas

📊 Statistiques
En mode pas à pas : affichage des statistiques en temps réel

En mode direct : statistiques affichées à la fin de la résolution

🔄 Navigation
Bouton Retour : annule l’algorithme en cours (si pas à pas) et revient au menu principal

Bouton Sauvegarder : enregistre le labyrinthe affiché

🖥️ Mode Terminal
✅ Fonctionnalités
Taille fixe : 30 x 30 cases

L’utilisateur saisit :

Longueur et largeur

Graine aléatoire (seed)

Type de labyrinthe : Parfait ou Imparfait

✏️ Interaction
Modification des murs via :

Saisie des coordonnées de la case

Choix de la direction du mur à modifier (n / s / e / o)

Possibilité de :

Sauvegarder le labyrinthe

Restaurer un labyrinthe existant

Résoudre le labyrinthe avec :

Trémaux

Dead-end Filling

Dijkstra

Option de quitter le programme

📁 Sauvegarde et Restauration
Sauvegardes compatibles avec les deux interfaces

Possibilité de reprendre un labyrinthe à tout moment

🧠 Algorithmes Implémentés
Trémaux : parcours avec marquage de chemin

Dead-end Filling : élimination des impasses

Dijkstra : calcul de plus court chemin