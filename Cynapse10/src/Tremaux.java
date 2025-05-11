import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Tremaux extends Algorithme {

    @Override
    public void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();

        int[][] passages = new int[largeur][longueur];
        Case[][] pred = new Case[largeur][longueur];  // Tableau des prédécesseurs

        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();

        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }

        Stack<Case> stack = new Stack<>();
        stack.push(entree);
        passages[entree.getX()][entree.getY()] = 1;
        entree.setParcourue(true);

        // Mesure du temps et compteur de cases parcourues
        long startTime = System.nanoTime();
        int[] casesParcourues = new int[] {0}; // Utilisation d'un tableau pour modification dans la méthode récursive

        executerEtapePasAPas(labyrinthe, gridPane, carte, passages, pred, stack, largeur, longueur, sortie, startTime, casesParcourues, infoLabel);
    }

    @Override
    public void algoDirect(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();
        int[][] passages = new int[largeur][longueur];
        Case[][] pred = new Case[largeur][longueur];

        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();

        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }

        Stack<Case> stack = new Stack<>();
        stack.push(entree);
        passages[entree.getX()][entree.getY()] = 1;
        entree.setParcourue(true);

        long startTime = System.nanoTime();
        int casesParcourues = 0;

        while (!stack.isEmpty()) {
            Case current = stack.peek();
            int x = current.getX();
            int y = current.getY();

            current.setParcourue(true);
            current.setCouleur(Color.YELLOW);

            if (current == sortie) {
                long endTime = System.nanoTime();
                int cheminFinal = afficherChemin(labyrinthe, pred, sortie, gridPane);
                infoLabel.setText("Sortie trouvée !\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues + "\nNombre de cases du chemin final : " + cheminFinal);
                return;
            }

            casesParcourues++;

            // Obtenir les voisins disponibles
            List<Case> voisinsDisponibles = new ArrayList<>();

            // D'abord essayer les cases non visitées
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 0) {
                voisinsDisponibles.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 0) {
                voisinsDisponibles.add(carte[x + 1][y]);
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 0) {
                voisinsDisponibles.add(carte[x][y - 1]);
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 0) {
                voisinsDisponibles.add(carte[x][y + 1]);
            }

            // Si pas de cases non visitées et moins de 2 passages, essayer les cases visitées une fois
            if (voisinsDisponibles.isEmpty() && passages[x][y] < 2) {
                if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 1) {
                    voisinsDisponibles.add(carte[x - 1][y]);
                }
                if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 1) {
                    voisinsDisponibles.add(carte[x + 1][y]);
                }
                if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 1) {
                    voisinsDisponibles.add(carte[x][y - 1]);
                }
                if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 1) {
                    voisinsDisponibles.add(carte[x][y + 1]);
                }
            }

            if (!voisinsDisponibles.isEmpty()) {
                Case next = voisinsDisponibles.get(0);
                stack.push(next);
                passages[next.getX()][next.getY()]++;
                passages[x][y]++;
                pred[next.getX()][next.getY()] = current;
            } else {
                stack.pop();
            }
        }
        // Si la pile est vide sans trouver la sortie
        long endTime = System.nanoTime();
        infoLabel.setText("Exploration terminée (pile vide).\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues);
    }

    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, int[][] passages, Case[][] pred, Stack<Case> stack, int largeur, int longueur, Case sortie, long startTime, int[] casesParcourues, Label infoLabel) {
        if (stack.isEmpty()) {
            long endTime = System.nanoTime();
            infoLabel.setText("Exploration terminée (pile vide).\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);
            return;
        }

        Case current = stack.peek();

        // Si on a trouvé la sortie
        if (current == sortie) {
            long endTime = System.nanoTime();
            int cheminFinal = afficherChemin(labyrinthe, pred, sortie, gridPane);
            infoLabel.setText("Sortie trouvée !\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0] + "\nNombre de cases du chemin final : " + cheminFinal);
            return;
        }

        // Marquer et colorer la case courante
        current.setParcourue(true);
        current.setCouleur(Color.RED);
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        current.setCouleur(Color.YELLOW);

        // Incrémentation APRÈS le test de la sortie
        casesParcourues[0]++;

        // Affichage en direct
        long now = System.nanoTime();
        infoLabel.setText("En cours...\nTemps d'exécution : " + ((now - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);

        // Obtenir les voisins disponibles
        List<Case> voisinsDisponibles = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();

        // Vérifier chaque direction et ajouter uniquement les voisins accessibles
        // D'abord essayer les cases non visitées
        if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 0 ) {
            voisinsDisponibles.add(carte[x - 1][y]);
        }
        if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 0) {
            voisinsDisponibles.add(carte[x + 1][y]);
        }
        if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 0) {
            voisinsDisponibles.add(carte[x][y - 1]);
        }
        if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 0) {
            voisinsDisponibles.add(carte[x][y + 1]);
        }
        
        // Si pas de cases non visitées, essayer les cases visitées une fois
        if (voisinsDisponibles.isEmpty() && passages[x][y] < 2) {
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 1) {
                voisinsDisponibles.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 1) {
                voisinsDisponibles.add(carte[x + 1][y]);
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 1) {
                voisinsDisponibles.add(carte[x][y - 1]);
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 1) {
                voisinsDisponibles.add(carte[x][y + 1]);
            }
        }

        if (!voisinsDisponibles.isEmpty()) {
            Case next = voisinsDisponibles.get(0);
            stack.push(next);
            passages[next.getX()][next.getY()]++;
            passages[x][y]++;
            pred[next.getX()][next.getY()] = current;
        } else {
            stack.pop();
        }

        // Pause avant de continuer
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> executerEtapePasAPas(labyrinthe, gridPane, carte, passages, pred, stack, largeur, longueur, sortie, startTime, casesParcourues, infoLabel));
        pause.play();
    }

    


    /**
     * Vérifie si les coordonnées (x, y) sont dans les limites du labyrinthe.
     *
     * @param x        La coordonnée x.
     * @param y        La coordonnée y.
     * @param largeur  La largeur du labyrinthe.
     * @param longueur La longueur du labyrinthe.
     * @return true si (x, y) est dans les limites, false sinon.
     */
    private boolean isInBounds(int x, int y, int largeur, int longueur) {
        return x >= 0 && y >= 0 && x < largeur && y < longueur;
    }

    /**
     * Affiche le chemin le plus court de la sortie à l'entrée en inversant le tableau des prédécesseurs.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param pred       Le tableau des prédécesseurs.
     * @param sortie     La case de sortie.
     * @return           Le nombre de cases du chemin final.
     */
    private int afficherChemin(Labyrinthe labyrinthe, Case[][] pred, Case sortie, GridPane gridPane) {
        Case entree = labyrinthe.getEntree();
        Case current = sortie;
        List<Case> chemin = new ArrayList<>();

        // Remettre toutes les couleurs en blanc avant de colorer le chemin
        Case[][] carte = labyrinthe.getCarte();
        for (int i = 0; i < labyrinthe.getLargeur(); i++) {
            for (int j = 0; j < labyrinthe.getLongueur(); j++) {
                carte[i][j].setCouleur(Color.WHITE);
            }
        }

        // Reconstruire le chemin de la sortie vers l'entrée
        while (current != null && current != entree) {
            chemin.add(current);
            System.out.println("Case: (" + current.getX() + ", " + current.getY() + ")");
            current = pred[current.getX()][current.getY()];
        }

        // Ajouter l'entrée au chemin si elle a été trouvée
        if (current == entree) {
            chemin.add(entree);
        }

        // Colorer uniquement le chemin trouvé
        for (Case c : chemin) {
            if (c == entree) {
                c.setCouleur(Color.BLUE);      // Entrée en bleu
            } else if (c == sortie) {
                c.setCouleur(Color.GREEN);     // Sortie en vert
            } else {
                c.setCouleur(Color.YELLOW);      // Chemin en rose
            }
        }

        // Rafraîchir l'affichage
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);

        return chemin.size();
    }
    
}
