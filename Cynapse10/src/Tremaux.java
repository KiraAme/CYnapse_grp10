import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tremaux extends Algorithme {

    /**
     * Exécute l'algorithme de Trémaux pas à pas.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     */
    @Override
    public void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();

        int[][] passages = new int[largeur][longueur];

        // Utilisation directe des attributs entree et sortie
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

        executerEtapePasAPas(labyrinthe, gridPane, carte, passages, stack, largeur, longueur, sortie);
    }
    /**
     * Exécute l'algorithme de Trémaux de manière directe.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     */
    @Override
    public void algoDirect(Labyrinthe labyrinthe, GridPane gridPane) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();

        int[][] passages = new int[largeur][longueur];

        // Utilisation directe des attributs entree et sortie
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

            

            if (current == sortie) {
                System.out.println("Sortie trouvée !");
                break;
            }

            List<Case> voisinsDisponibles = getVoisinsDisponibles(current, carte, passages, largeur, longueur);

            if (!voisinsDisponibles.isEmpty()) {
                Case next = choisirVoisin(voisinsDisponibles, passages);
                stack.push(next);
                passages[next.getX()][next.getY()] += 1;
            } else {
                stack.pop();
            }
        }

        // Affiche le labyrinthe à la fin
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
    }
    /**
     * Exécute une étape de l'algorithme de Trémaux pas à pas.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     * @param carte      La carte du labyrinthe.
     * @param passages   Le tableau des passages.
     * @param stack      La pile pour le parcours.
     * @param largeur    La largeur du labyrinthe.
     * @param longueur   La longueur du labyrinthe.
     * @param sortie     La case de sortie.
     */
    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, int[][] passages, Stack<Case> stack, int largeur, int longueur, Case sortie) {
        if (stack.isEmpty()) {
            System.out.println("Exploration terminée (pile vide).");
            return;
        }

        Case current = stack.peek();
        int x = current.getX();
        int y = current.getY();

        current.setParcourue(true);
        // Met à jour la couleur de la case actuelle pour la différencier
        current.setCouleur(Color.RED);  // Changez cette couleur à votre goût

        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        current.setCouleur(Color.YELLOW);
        if (current == sortie) {
            System.out.println("Sortie trouvée !");
            return;
        }

        List<Case> voisinsDisponibles = getVoisinsDisponibles(current, carte, passages, largeur, longueur);

        if (!voisinsDisponibles.isEmpty()) {
            Case next = choisirVoisin(voisinsDisponibles, passages);
            stack.push(next);
            passages[next.getX()][next.getY()] += 1;
        } else {
            stack.pop();
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> executerEtapePasAPas(labyrinthe, gridPane, carte, passages, stack, largeur, longueur, sortie));
        pause.play();
    }

    private List<Case> getVoisinsDisponibles(Case current, Case[][] carte, int[][] passages, int largeur, int longueur) {
        List<Case> voisins = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();

        if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] < 2) {
            voisins.add(carte[x - 1][y]);
        }
        if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] < 2) {
            voisins.add(carte[x + 1][y]);
        }
        if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] < 2) {
            voisins.add(carte[x][y - 1]);
        }
        if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] < 2) {
            voisins.add(carte[x][y + 1]);
        }

        return voisins;
    }

    private Case choisirVoisin(List<Case> voisins, int[][] passages) {
        for (Case v : voisins) {
            if (passages[v.getX()][v.getY()] == 0) {
                return v;
            }
        }
        return voisins.get(0);
    }

    private boolean isInBounds(int x, int y, int largeur, int longueur) {
        return x >= 0 && y >= 0 && x < largeur && y < longueur;
    }
}
