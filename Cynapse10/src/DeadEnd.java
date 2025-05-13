//import java.awt.image.MultiResolutionImage;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class DeadEnd extends Algorithme {

    @Override
    public void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel, boolean[] cancelRequested) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();

        int[][] passages = new int[largeur][longueur];
        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();

        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }

        Stack<Case> stack = new Stack<>(); // récupérér les deadends

        long startTime = System.nanoTime();
        final int[] casesParcourues = {0};

        for (int i=0; i<longueur;i++){
            for (int j=0; j<largeur; j++){
                
                Case current = carte[j][i];
                int murs = 0;
                if(!current.isEstEntree() && !current.isEstSortie()){
                    if (current.isMurEst()){
                        murs++;
                    }
                    if (current.isMurNord()){
                        murs++;
                    }
                    if (current.isMurOuest()){
                        murs++;
                    }
                    if (current.isMurSud()){
                        murs++;
                    }
                    if(murs>=3){
                        stack.push(current);
                        current.setParcourue(true);
                        current.setCouleur(Color.YELLOW);
                    }
                }

            }
        }
        if (cancelRequested[0]) {
            // Arrête la génération/résolution
            return;
        }
        executerEtapePasAPas(labyrinthe, gridPane, carte, passages, stack, largeur, longueur, sortie, startTime,casesParcourues, infoLabel,cancelRequested);
    }

    @Override
    public void algoDirect(Labyrinthe labyrinthe, GridPane gridPane, Label infoLabel) {
        Case[][] carte = labyrinthe.getCarte();
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();
        int[][] passages = new int[largeur][longueur];
        Case entree = labyrinthe.getEntree();
        Case sortie = labyrinthe.getSortie();
        // Si la sortie  ou l'entrée mène à un cul de sac
        if (entree == null || sortie == null) {
            System.out.println("Erreur : entrée ou sortie non définie.");
            return;
        }
        int i;
        int j;

        long startTime = System.nanoTime();
        int[] casesParcourues = {0};


        Stack<Case> stack = new Stack<>(); // récupérér les deadends
        for (i=0; i<longueur;i++){
            for (j=0; j<largeur; j++){
                casesParcourues[0]++;
                Case current = carte[j][i];
                int murs = 0;
                if(!current.isEstEntree() && !current.isEstSortie()){
                    if (current.isMurEst()){
                        murs++;
                    }
                    if (current.isMurNord()){
                        murs++;
                    }
                    if (current.isMurOuest()){
                        murs++;
                    }
                    if (current.isMurSud()){
                        murs++;
                    }
                    if(murs>=3){
                        stack.push(current);
                        current.setParcourue(true);
                        current.setCouleur(Color.YELLOW);
                    }
                }

            }
        }

        while (!stack.isEmpty()) {
            Case current = stack.pop();
            casesParcourues[0]++;
            int x = current.getX();
            int y = current.getY();
          

           // Obtenir les voisins disponibles
            int voisins = 0;
            List<Case> voisinsDisponibles = new ArrayList<>();
            // Récupérer les cases non visitées
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 0) {
                voisinsDisponibles.add(carte[x - 1][y]);
                voisins++;
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 0) {
                    voisinsDisponibles.add(carte[x + 1][y]);
                    voisins++;
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 0) {
                    voisinsDisponibles.add(carte[x][y - 1]);
                    voisins++;
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 0) {
                voisinsDisponibles.add(carte[x][y + 1]);
                voisins++;
            }
            if(current.isEstEntree() || current.isEstSortie()){
                voisins++;
            }
            if(voisins==1){ //si la case n'est pas une intersection
                current.setParcourue(true);                    
                current.setCouleur(Color.YELLOW);
                if(!voisinsDisponibles.isEmpty()){
                    Case next = voisinsDisponibles.get(0);
                    stack.push(next);
                    passages[x][y]++;
                }
            }
            if (voisins<=1 && (current == sortie || current == entree)) {//si l'entrée ou la sortie mène à un cul de sac
                long endTime = System.nanoTime();
                infoLabel.setText("Pas de passage trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues );
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
                return;
            }
        }
        long endTime = System.nanoTime();
        int cheminFinal = afficherChemin(labyrinthe, sortie, gridPane);
        infoLabel.setText("Sortie trouvée !\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0] + "\nNombre de cases du chemin final : " + cheminFinal);
    }

    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, int[][] passages, Stack<Case> stack, int largeur, int longueur, Case sortie, long startTime, int[] casesParcourues, Label infoLabel,boolean[] cancelRequested) {
        if (stack.isEmpty()) {
            long endTime = System.nanoTime();
            int cheminFinal = afficherChemin(labyrinthe, sortie, gridPane);
            infoLabel.setText("Exploration terminée !\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues + "\nNombre de cases du chemin final : " + cheminFinal);
            return;
        }

        Case current = stack.pop();
        Case entree = labyrinthe.getEntree();
        if (cancelRequested[0]) {
            // Arrête la génération/résolution
            return;
        }


        // Marquer et colorer la case courante
        current.setCouleur(Color.RED);
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        current.setCouleur(Color.WHITE);
        casesParcourues[0]++;
        // Affichage en direct
        long now = System.nanoTime();
        infoLabel.setText("En cours...\nTemps d'exécution : " + ((now - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);

        // Obtenir les voisins disponibles
        int voisins = 0;
        List<Case> voisinsDisponibles = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();
        // Récupérer les cases non visitées
        if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && passages[x - 1][y] == 0) {
            voisinsDisponibles.add(carte[x - 1][y]);
            voisins++;
        }
        if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && passages[x + 1][y] == 0) {
                voisinsDisponibles.add(carte[x + 1][y]);
                voisins++;
        }
        if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && passages[x][y - 1] == 0) {
                voisinsDisponibles.add(carte[x][y - 1]);
                voisins++;
        }
        if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && passages[x][y + 1] == 0) {
            voisinsDisponibles.add(carte[x][y + 1]);
            voisins++;
        }
        if(current.isEstEntree() || current.isEstSortie()){
            voisins++;
        }
        if(voisins==1){ //si la case n'est pas une intersection
            current.setParcourue(true);                    
            current.setCouleur(Color.YELLOW);
            if(!voisinsDisponibles.isEmpty()){
                Case next = voisinsDisponibles.get(0);
                stack.push(next);
                passages[x][y]++;
            }
        }
        if (voisins<=1 && (current == sortie || current == entree)) {//si l'entrée ou la sortie mène à un cul de sac
            long endTime = System.nanoTime();
            infoLabel.setText("Pas de passage trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues );
            AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
            return;
        }
         

        // Pause avant de continuer
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(e -> executerEtapePasAPas(labyrinthe, gridPane, carte, passages, stack, largeur, longueur, sortie, startTime, casesParcourues, infoLabel,cancelRequested));
        pause.play();
    }




    private boolean isInBounds(int x, int y, int largeur, int longueur) {
        return x >= 0 && y >= 0 && x < largeur && y < longueur;
    }

    /**
     * Affiche le chemin de l'entrée à la sortie (chemin non rempli).
     *
     * @param labyrinthe Le labyrinthe à traiter
     * @param sortie     La case de sortie.
     * @return 
     */
    private int afficherChemin(Labyrinthe labyrinthe, Case sortie, GridPane gridPane) {
    	int casesParcourues =0;
        Case entree = labyrinthe.getEntree();
        List<Case> chemin = new ArrayList<>();
        Case current = entree;
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();
        Case[][] carte = labyrinthe.getCarte();
        // Reconstruire le chemin de l'entrée vers la sortie
        while (current != null && current != sortie) {
        	casesParcourues++;
            chemin.add(current);
            System.out.println("Case: (" + current.getX() + ", " + current.getY() + ")");
            // Obtenir les voisins disponibles
            List<Case> voisinsDisponibles = new ArrayList<>();
            int x = current.getX();
            int y = current.getY();
            // Récupérer les cases non visitées
            if (!current.murNord && isInBounds(x - 1, y, largeur, longueur) && !carte[x-1][y].estParcourue()) {
                voisinsDisponibles.add(carte[x - 1][y]);
            }
            if (!current.murSud && isInBounds(x + 1, y, largeur, longueur) && !carte[x+1][y].estParcourue()) {
                    voisinsDisponibles.add(carte[x + 1][y]);
            }
            if (!current.murOuest && isInBounds(x, y - 1, largeur, longueur) && !carte[x][y-1].estParcourue()) {
                    voisinsDisponibles.add(carte[x][y - 1]);
            }
            if (!current.murEst && isInBounds(x, y + 1, largeur, longueur) && !carte[x][y+1].estParcourue()) {
                voisinsDisponibles.add(carte[x][y + 1]);
            }
            if (!voisinsDisponibles.isEmpty()) {
                current.setParcourue(true);                    
                current.setCouleur(Color.YELLOW);
                current = voisinsDisponibles.get(0);
            }
        } 
        // Ajouter la sortie au chemin si elle a été trouvée
        if (current == sortie) {
            chemin.add(sortie);
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
        return casesParcourues;
    }


    
}
