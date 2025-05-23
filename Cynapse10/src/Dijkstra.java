//import java.awt.image.MultiResolutionImage;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Classe représentant l'algorithme de Dijkstra pour résoudre un labyrinthe.
 * Cet algorithme utilise une approche de recherche de chemin en utilisant une
 * file de priorité pour explorer les cases du labyrinthe.
 *
 * @version 1.0
 * @author Groupe 10
 */
public class Dijkstra extends Algorithme {
    /**
     * Exécute l'algorithme de Dijkstra de manière pas à pas.
     *
     * @param labyrinthe      Le labyrinthe à traiter.
     * @param gridPane        Le GridPane pour l'affichage.
     * @param infoLabel       Le label pour afficher les informations.
     * @param cancelRequested Tableau de booléens pour demander l'annulation.
     */
    @Override
    public void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel, boolean[] cancelRequested) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();

        Case[][] pred = new Case[largeur][longueur];  // Tableau des prédécesseurs
        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();

        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }
        PriorityQueue<Case> file = new PriorityQueue<>(Comparator.comparingInt(c -> c.getDistance())); // récupérér les deadends

        long startTime = System.nanoTime();
        final int[] casesParcourues = {0};
        int x = entree.getX();
        int y =  entree.getY();
        entree.setDistance(0);
        pred[x][y]=entree;
        file.add(entree);
        if (cancelRequested[0]) {
            // Arrête la génération/résolution
            return;
        }
        executerEtapePasAPas(labyrinthe, gridPane, carte, pred, file, largeur, longueur, sortie, startTime,casesParcourues, infoLabel,cancelRequested);
    }
    /**
     * Exécute l'algorithme de Dijkstra de manière directe.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     * @param infoLabel  Le label pour afficher les informations.
     */
    @Override
    public void algoDirect(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();
        Case[][] pred = new Case[largeur][longueur];  // Tableau des prédécesseurs
        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();
        // Si la sortie  ou l'entrée mène à un cul de sac
        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }
        PriorityQueue<Case> file = new PriorityQueue<>(Comparator.comparingInt(c -> c.getDistance())); // récupérér les deadends

        long startTime = System.nanoTime();
        final int[] casesParcourues = {0};
        int x = entree.getX();
        int y =  entree.getY();
        entree.setDistance(0);
        pred[x][y]=entree;
        file.add(entree);
        while (!file.isEmpty()) {
            Case current = file.poll();
            current.setParcourue(true);
            casesParcourues[0]++;
             x = current.getX();
             y = current.getY();
            if (current==sortie){
                long endTime = System.nanoTime();
                int cheminFinal = afficherChemin(labyrinthe, pred, sortie, gridPane);
                if (infoLabel != null) {
                    infoLabel.setText(
                        "Sortie trouvée !\n" +
                        "Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\n" +
                        "Nombre de cases parcourues : " + casesParcourues[0] + "\n" +
                        "Nombre de cases du chemin final : " + cheminFinal
                    );
                } else {
                    System.out.println("Sortie trouvée !");
                    System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                    System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
                    System.out.println("Nombre de cases du chemin final : " + cheminFinal);
                }
                return;
            }

           // Obtenir les voisins disponibles
            List<Case> voisinsDisponibles = new ArrayList<>();
            // Récupérer les cases non visitées
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) ) {
                voisinsDisponibles.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur)) {
                    voisinsDisponibles.add(carte[x + 1][y]);
           
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur)  ) {
                    voisinsDisponibles.add(carte[x][y - 1]);
           
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) ) {
                voisinsDisponibles.add(carte[x][y + 1]);
     
            }
            if(current != entree){
                if(current.getDistance()<(pred[x][y].getDistance()+1)){
                    current.setDistance(pred[x][y].getDistance()+1);
                }
            }
            if(!voisinsDisponibles.isEmpty()){ //si il y a des voisins
                for (Case voisin : voisinsDisponibles){
                    if(voisin.getDistance()>current.getDistance()+1){
                        pred[voisin.getX()][voisin.getY()]=current;
                        voisin.setDistance(current.getDistance()+1);
                        if(!voisin.estParcourue()){
                            file.add(voisin);
                        }
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        if (infoLabel != null) {
            infoLabel.setText("Pas de chemin trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);
        } else {
            System.out.println("Pas de chemin trouvé.");
            System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
            System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
        }
        if (gridPane != null) {
            AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        } else {
            System.out.println(labyrinthe.toString());
        }
    }
    /**
     * Exécute une étape de l'algorithme de Dijkstra pas à pas.
     *
     * @param labyrinthe      Le labyrinthe à traiter.
     * @param gridPane        Le GridPane pour l'affichage.
     * @param carte           La carte du labyrinthe.
     * @param pred            Le tableau des prédécesseurs.
     * @param file            La file de priorité pour explorer les cases.
     * @param largeur         La largeur du labyrinthe.
     * @param longueur        La longueur du labyrinthe.
     * @param sortie          La case de sortie.
     * @param startTime       Le temps de début de l'exécution.
     * @param casesParcourues Tableau pour compter le nombre de cases parcourues.
     * @param infoLabel       Le label pour afficher les informations.
     */
    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, Case[][] pred, PriorityQueue<Case> file,int largeur, int longueur, Case sortie, long startTime, int[] casesParcourues, Label infoLabel,boolean[] cancelRequested) {
        if (file.isEmpty()) {
            long endTime = System.nanoTime();
            if (infoLabel != null) {
                infoLabel.setText("Pas de chemin trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);
            } else {
                System.out.println("Pas de chemin trouvé.");
                System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
            }
            if (gridPane != null) {
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
            } else {
                System.out.println(labyrinthe.toString());
            }
            return;
        }
        
        Case current = file.poll();
        if (cancelRequested[0]) {
            // Arrête la génération/résolution
            return;
        }


        // Marquer et colorer la case courante
        current.setCouleur(Color.RED);
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        current.setCouleur(Color.YELLOW);
         current.setParcourue(true);
        casesParcourues[0]++;
        // Affichage en direct
        long now = System.nanoTime();
        infoLabel.setText("En cours...\nTemps d'exécution : " + ((now - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);
            if (current==sortie){
                long endTime = System.nanoTime();
                int cheminFinal = afficherChemin(labyrinthe, pred, sortie, gridPane);
                if (infoLabel != null) {
                    infoLabel.setText(
                        "Sortie trouvée !\n" +
                        "Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\n" +
                        "Nombre de cases parcourues : " + casesParcourues[0] + "\n" +
                        "Nombre de cases du chemin final : " + cheminFinal
                    );
                } else {
                    System.out.println("Sortie trouvée !");
                    System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                    System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
                    System.out.println("Nombre de cases du chemin final : " + cheminFinal);
                }
                return;
            }

            Case entree = labyrinthe.getEntree();
            int x = current.getX();
            int y = current.getY();
          

           // Obtenir les voisins disponibles
            List<Case> voisinsDisponibles = new ArrayList<>();
            // Récupérer les cases non visitées
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) ) {
                voisinsDisponibles.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur)) {
                    voisinsDisponibles.add(carte[x + 1][y]);
           
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur)  ) {
                    voisinsDisponibles.add(carte[x][y - 1]);
           
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) ) {
                voisinsDisponibles.add(carte[x][y + 1]);
     
            }
            if(current != entree){
                if(current.getDistance()<(pred[x][y].getDistance()+1)){
                    current.setDistance(pred[x][y].getDistance()+1);
                }
            }
            if(!voisinsDisponibles.isEmpty()){ //si il y a des voisins
                for (Case voisin : voisinsDisponibles){
                    if(voisin.getDistance()>current.getDistance()+1){
                        pred[voisin.getX()][voisin.getY()]=current;
                        voisin.setDistance(current.getDistance()+1);
                        if(!voisin.estParcourue()){
                            file.add(voisin);
                        }
                    }
                }
            }
         

        // Pause avant de continuer
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> executerEtapePasAPas(labyrinthe, gridPane, carte, pred, file, largeur, longueur, sortie, startTime, casesParcourues, infoLabel,cancelRequested));
        pause.play();
    }



    /**
     * Vérifie si les coordonnées (x, y) sont dans les limites du labyrinthe.
     *
     * @param x        La coordonnée x.
     * @param y        La coordonnée y.
     * @param largeur  La largeur du labyrinthe.
     * @param longueur La longueur du labyrinthe.
     * @return true si les coordonnées sont dans les limites, false sinon.
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
                c.setCouleur(Color.YELLOW);    // Chemin en jaune
            }
        }

        // Rafraîchir l'affichage
        if (gridPane != null) {
            AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        } else {
            // Version terminale : affichage ASCII avec chemin
            System.out.println(labyrinthe.toString());
        }

        return chemin.size();
    }
    


    
}
