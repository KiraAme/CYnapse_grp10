import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Tremaux extends Algorithme {

    @Override
    public void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane) {
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

        executerEtapePasAPas(labyrinthe, gridPane, carte, passages, pred, stack, largeur, longueur, sortie);
    }

    @Override
    public void algoDirect(Labyrinthe labyrinthe, GridPane gridPane) {
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

        while (!stack.isEmpty()) {
            Case current = stack.peek();
            int x = current.getX();
            int y = current.getY();

            current.setParcourue(true);
            current.setCouleur(Color.YELLOW);

            if (current == sortie) {
                System.out.println("Sortie trouvée !");
                afficherChemin(labyrinthe, pred, sortie, gridPane);
                return;
            }

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
    }

    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, int[][] passages, Case[][] pred, Stack<Case> stack, int largeur, int longueur, Case sortie) {
        if (stack.isEmpty()) {
            System.out.println("Exploration terminée (pile vide).");
            return;
        }

        Case current = stack.peek();

        // Si on a trouvé la sortie
        if (current == sortie) {
            System.out.println("Sortie trouvée !");
            afficherChemin(labyrinthe, pred, sortie, gridPane);
            return;
        }

        // Marquer et colorer la case courante
        current.setParcourue(true);
        current.setCouleur(Color.RED);
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        current.setCouleur(Color.YELLOW);

        // Obtenir les voisins disponibles
        List<Case> voisinsDisponibles = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();

        // Vérifier chaque direction et ajouter uniquement les voisins accessibles
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
        pause.setOnFinished(e -> executerEtapePasAPas(labyrinthe, gridPane, carte, passages, pred, stack, largeur, longueur, sortie));
        pause.play();
    }

    private List<Case> getVoisinsDisponibles(Case current, Case[][] carte, int[][] passages, int largeur, int longueur) {
        List<Case> voisins = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();
        int currentPassages = passages[x][y];

        // En retour en arrière, on ne peut aller que vers des cases visitées une fois
        if (currentPassages == 2) {
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 1) {
                voisins.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 1) {
                voisins.add(carte[x + 1][y]);
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 1) {
                voisins.add(carte[x][y - 1]);
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 1) {
                voisins.add(carte[x][y + 1]);
            }
        } 
        // En exploration normale, on préfère les cases non visitées
        else {
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 0) {
                voisins.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 0) {
                voisins.add(carte[x + 1][y]);
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 0) {
                voisins.add(carte[x][y - 1]);
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 0) {
                voisins.add(carte[x][y + 1]);
            }
            
            // S'il n'y a pas de cases non visitées, on prend les cases visitées une fois
            if (voisins.isEmpty()) {
                if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 1) {
                    voisins.add(carte[x - 1][y]);
                }
                if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 1) {
                    voisins.add(carte[x + 1][y]);
                }
                if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 1) {
                    voisins.add(carte[x][y - 1]);
                }
                if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 1) {
                    voisins.add(carte[x][y + 1]);
                }
            }
        }

        return voisins;
    }



    private boolean isInBounds(int x, int y, int largeur, int longueur) {
        return x >= 0 && y >= 0 && x < largeur && y < longueur;
    }

    /**
     * Affiche le chemin le plus court de la sortie à l'entrée en inversant le tableau des prédécesseurs.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param pred       Le tableau des prédécesseurs.
     * @param sortie     La case de sortie.
     */
    private void afficherChemin(Labyrinthe labyrinthe, Case[][] pred, Case sortie, GridPane gridPane) {
        Case entree = labyrinthe.getEntree();
        Case current = sortie;
        List<Case> chemin = new ArrayList<>();
        
    
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
                c.setCouleur(Color.PINK);      // Chemin en rose
            }
        }
    
        // Rafraîchir l'affichage
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
    }
    
}
