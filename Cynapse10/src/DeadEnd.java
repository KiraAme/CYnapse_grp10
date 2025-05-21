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
            int x = current.getX();
            int y = current.getY();
           
            casesParcourues[0]++;
            
          

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
                passages[x][y]++;             
                current.setCouleur(Color.YELLOW);
                if(!voisinsDisponibles.isEmpty()){
                    Case next = voisinsDisponibles.get(0);
                    if (!next.estParcourue()) {  
                        stack.push(next);
                    
                    }
                }
            }
            if (voisins<=1 && (current == sortie || current == entree)) { //si l'entrée ou la sortie mène à un cul de sac
                long endTime = System.nanoTime();
                if (infoLabel != null) {
                    infoLabel.setText("Pas de passage trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);
                } else {
                    System.out.println("Pas de passage trouvé.");
                    System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                    System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
                }
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
                return;
            }
        }
        long endTime = System.nanoTime();
        ArrayList<Case> cheminFinal = afficherChemin(labyrinthe, sortie, gridPane);
        if(cheminFinal.contains(entree) && cheminFinal.contains(sortie)){
            if (infoLabel != null) {
                infoLabel.setText(
                    "Sortie trouvée !\n" +
                    "Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\n" +
                    "Nombre de cases parcourues : " + casesParcourues[0] + "\n" +
                    "Nombre de cases du chemin final : " + cheminFinal.size()
                );
            } else {
                System.out.println("Sortie trouvée !");
                System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                System.out.println("Nombre de cases parcourues : " + casesParcourues[0]);
                System.out.println("Nombre de cases du chemin final : " + cheminFinal.size());
            }
        }
        else{
             if (infoLabel != null) {
                infoLabel.setText(
                    "Pas de chemin trouvé.\n" +
                    "Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\n" +
                    "Nombre de cases parcourues : " + casesParcourues[0] + cheminFinal.size() + "\n" 
                );
            } else {
                System.out.println("Pas de chemin trouvé.");
                System.out.println("Temps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s");
                System.out.println("Nombre de cases parcourues : " + casesParcourues[0]+ cheminFinal.size());
            }
        }
    }

    private void executerEtapePasAPas(Labyrinthe labyrinthe, GridPane gridPane, Case[][] carte, int[][] passages, Stack<Case> stack, int largeur, int longueur, Case sortie, long startTime, int[] casesParcourues, Label infoLabel,boolean[] cancelRequested) {
        if (stack.isEmpty()) {
            long endTime = System.nanoTime();
            ArrayList<Case> cheminFinal = afficherChemin(labyrinthe, sortie, gridPane);
            infoLabel.setText("Exploration terminée !\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0] + "\nNombre de cases du chemin final : " + cheminFinal.size());
            return;
        }

        Case current = stack.pop();
        int x = current.getX();
        int y = current.getY();
        casesParcourues[0]++;
        Case entree = labyrinthe.getEntree();
        if (cancelRequested[0]) {
            // Arrête la génération/résolution
            return;
        }


        // Marquer et colorer la case courante
        current.setCouleur(Color.RED);
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        if (current.isEstEntree()) {
            current.setCouleur(Color.BLUE);
        } else if (current.isEstSortie()) {
            current.setCouleur(Color.GREEN);
        } else if (current.estParcourue()) {
            current.setCouleur(Color.YELLOW);
        } else {
            current.setCouleur(Color.WHITE);
        }
        
        // Affichage en direct
        long now = System.nanoTime();
        infoLabel.setText("En cours...\nTemps d'exécution : " + ((now - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0]);

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
            passages[x][y]++;                
            current.setCouleur(Color.YELLOW);
            if(!voisinsDisponibles.isEmpty()){
                Case next = voisinsDisponibles.get(0);
                if (!next.estParcourue()) {  
                    stack.push(next);

                }
            }
        }
        if (voisins<=1 && (current == sortie || current == entree)) {//si l'entrée ou la sortie mène à un cul de sac
            long endTime = System.nanoTime();

            infoLabel.setText("Pas de passage trouvé.\nTemps d'exécution : " + ((endTime - startTime) / 1_000_000_000.0) + " s\nNombre de cases parcourues : " + casesParcourues[0] );

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
    private ArrayList<Case> afficherChemin(Labyrinthe labyrinthe, Case sortie, GridPane gridPane) {
        Case entree = labyrinthe.getEntree();
        ArrayList<Case> chemin = new ArrayList<>();
        Case current = entree;
        int largeur = labyrinthe.getLargeur();
        int longueur = labyrinthe.getLongueur();
        Case[][] carte = labyrinthe.getCarte();
        // Reconstruire le chemin de l'entrée vers la sortie
         for (int i=0; i<largeur;i++){
            for (int j=0;j<longueur;j++){
                current = carte[i][j];
                if(!current.estParcourue()){
                    chemin.add(current);
                }
            }
        }
        chemin = trouverComposanteConnexe(sortie, chemin, labyrinthe);
        labyrinthe.reset();
        // Colorer uniquement le chemin trouvé
        for (Case c : chemin) {
            if (c == entree) {
                c.setCouleur(Color.BLUE);      // Entrée en bleu
            } else if (c == sortie) {
                c.setCouleur(Color.GREEN);     // Sortie en vert
            } else {
                c.setCouleur(Color.YELLOW);      // Chemin en jaune
            }
        }
    
        // Rafraîchir l'affichage
        AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrinthe);
        return chemin;
    }

    private ArrayList<Case> trouverComposanteConnexe (Case sortie, ArrayList<Case> chemin, Labyrinthe labyrinthe){
        ArrayList<Case> cheminConnexe = new ArrayList<Case>();
        cheminConnexe.add(sortie);
        cheminConnexe = dfs(sortie, chemin, cheminConnexe, labyrinthe);
        return cheminConnexe;
    }
    

    private ArrayList<Case> dfs(Case c, ArrayList<Case> cases, ArrayList<Case> casesParcourues, Labyrinthe labyrinthe){
        if(casesParcourues.contains(c)){
            int x = c.getX();
            int y = c.getY();
            int largeur = labyrinthe.getLargeur();
            int longueur = labyrinthe.getLongueur();
            Case[][] carte = labyrinthe.getCarte();
            if (!c.murNord && isInBounds(x - 1, y, largeur, longueur) && cases.contains(carte[x-1][y]) && !casesParcourues.contains(carte[x-1][y])) {
                casesParcourues.add(carte[x-1][y]);
                casesParcourues = dfs(carte[x-1][y], cases, casesParcourues, labyrinthe);
            }
            if (!c.murSud && isInBounds(x + 1, y, largeur, longueur) && cases.contains(carte[x+1][y]) && !casesParcourues.contains(carte[x+1][y])) {
                casesParcourues.add(carte[x+1][y]);
                casesParcourues = dfs(carte[x+1][y], cases, casesParcourues, labyrinthe);
            }
            if (!c.murOuest && isInBounds(x, y-1, largeur, longueur) && cases.contains(carte[x][y-1]) && !casesParcourues.contains(carte[x][y-1])) {
                casesParcourues.add(carte[x][y-1]);
                casesParcourues = dfs(carte[x][y-1], cases, casesParcourues, labyrinthe);
            }
            if (!c.murEst && isInBounds(x, y + 1, largeur, longueur) && cases.contains(carte[x][y + 1]) && !casesParcourues.contains(carte[x][y + 1])) {
                casesParcourues.add(carte[x][y + 1]);
                casesParcourues = dfs(carte[x][y + 1], cases, casesParcourues, labyrinthe);
            }
        }

        return casesParcourues;
    }
}
