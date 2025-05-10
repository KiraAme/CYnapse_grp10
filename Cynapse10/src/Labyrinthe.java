import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javafx.scene.layout.GridPane;

public class Labyrinthe {
    private String nom;
    private int longueur;
    private int largeur;
    private Random random;
    private Case[][] carte;
    private Case entree;
    private Case sortie;

    public Labyrinthe(String n, int lo, int la, long seed) {
        this.nom = n;
        this.largeur = la;
        this.longueur = lo;
        this.random = new Random(seed);
        
    }
    
    /**
     * Vérifie si les coordonnées (x, y) sont dans les limites de la carte.
     *
     * @param x La coordonnée x.
     * @param y La coordonnée y.
     * @return true si les coordonnées sont dans les limites, false sinon.
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < this.largeur && y < this.longueur;
    }
    /**
     * Récupère les cases voisines non visitées d'une case donnée.
     *
     * @param caseActuelle La case actuelle.
     * @param visitees     Le tableau des cases visitées.
     * @return Une liste de cases voisines non visitées.
     */
    private List<Case> getVoisinsNonVisites(Case caseActuelle, boolean[][] visitees) {
        List<Case> voisins = new ArrayList<>();
        int x = caseActuelle.getX();
        int y = caseActuelle.getY();

        if (isInBounds(x - 1, y) && !visitees[x - 1][y]) { voisins.add(this.carte[x - 1][y]); }
        if (isInBounds(x + 1, y) && !visitees[x + 1][y]) { voisins.add(this.carte[x + 1][y]); }
        if (isInBounds(x, y - 1) && !visitees[x][y - 1]) { voisins.add(this.carte[x][y - 1]); }
        if (isInBounds(x, y + 1) && !visitees[x][y + 1]) { voisins.add(this.carte[x][y + 1]); }

        return voisins;
    }

    /**
     * Supprime le mur entre deux cases voisines.
     *
     * @param current La case actuelle.
     * @param voisin  La case voisine.
     */
    private void supprimerMurEntre(Case current, Case voisin) {
        int dx = voisin.getX() - current.getX();
        int dy = voisin.getY() - current.getY();

        if (dx == -1) { current.murNord = false; voisin.murSud = false; }
        else if (dx == 1) { current.murSud = false; voisin.murNord = false; }
        else if (dy == -1) { current.murOuest = false; voisin.murEst = false; }
        else if (dy == 1) { current.murEst = false; voisin.murOuest = false; }
    }
    /**
     * Génère le labyrinthe en utilisant l'algorithme de backtracking.
     */
    public void genererLabyrinthe() {
        this.carte = new Case[this.largeur][this.longueur];

        // Initialisation des cases avec tous les murs
        for (int x = 0; x < this.largeur; x++) {
            for (int y = 0; y < this.longueur; y++) {
                this.carte[x][y] = new Case(x, y, true, true, true, true, false, false);
            }
        }

        boolean[][] visitees = new boolean[this.largeur][this.longueur];
        Stack<Case> stack = new Stack<>();
        this.entree = this.carte[0][0];
        this.sortie = this.carte[this.largeur - 1][this.longueur - 1];
        this.entree.estEntree = true;
        visitees[0][0] = true;
        stack.push(this.entree);

        while (!stack.isEmpty()) {
            Case current = stack.peek();
            List<Case> voisinsNonVisites = getVoisinsNonVisites(current, visitees);

            if (!voisinsNonVisites.isEmpty()) {
                Case voisin = voisinsNonVisites.get(random.nextInt(voisinsNonVisites.size()));
                supprimerMurEntre(current, voisin);
                visitees[voisin.getX()][voisin.getY()] = true;
                stack.push(voisin);
            } else {
                stack.pop();
            }
        }

        this.carte[this.largeur - 1][this.longueur - 1].estSortie = true;
    }
    /**
     * Résout le labyrinthe en utilisant l'algorithme spécifié.
     *
     * @param algo      L'algorithme à utiliser pour résoudre le labyrinthe.
     * @param gridPane  Le GridPane dans lequel afficher le labyrinthe.
     */
    public void résoudredirect(Algo algo,GridPane gridPane) {
        Algorithme algorithme = null;
    
        switch (algo) {
            case Trémaux:
                algorithme = new Tremaux();
                break;
            case Deadend:
                // algorithme = new Deadend(); // à créer plus tard
                System.out.println("Algorithme Deadend pas encore implémenté.");
                return;
            case ShortestPath:
                // algorithme = new ShortestPath(); // à créer plus tard
                System.out.println("Algorithme ShortestPath pas encore implémenté.");
                return;
            default:
                System.out.println("Algorithme non reconnu.");
                return;
        }
    
        algorithme.algoDirect(this, gridPane); // Passer le GridPane si nécessaire
    }
    /**
     * Résout le labyrinthe pas à pas en utilisant l'algorithme spécifié.
     *
     * @param algo      L'algorithme à utiliser pour résoudre le labyrinthe.
     * @param gridPane  Le GridPane dans lequel afficher le labyrinthe.
     */
    public void résoudrePasAPas(Algo algo,GridPane gridPane) {
        Algorithme algorithme = null;
    
        switch (algo) {
            case Trémaux:
                algorithme = new Tremaux();
                break;
            case Deadend:
                // algorithme = new Deadend(); // à créer plus tard
                System.out.println("Algorithme Deadend pas encore implémenté.");
                return;
            case ShortestPath:
                // algorithme = new ShortestPath(); // à créer plus tard
                System.out.println("Algorithme ShortestPath pas encore implémenté.");
                return;
            default:
                System.out.println("Algorithme non reconnu.");
                return;
        }
    
        algorithme.algoPasAPas(this, gridPane); // Passer le GridPane si nécessaire
    }

    /**
     * Affiche le labyrinthe sous forme de chaîne de caractères.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < this.largeur; x++) {
            for (int y = 0; y < this.longueur; y++) {
                sb.append("+");
                sb.append(this.carte[x][y].murNord ? "---" : "   ");
            }
            sb.append("+\n");

            for (int y = 0; y < this.longueur; y++) {
                sb.append(this.carte[x][y].murOuest ? "| " : "  ");
                if (this.carte[x][y].estEntree) {
                    sb.append("E ");
                } else if (this.carte[x][y].estSortie) {
                    sb.append("S ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("|\n");
        }

        for (int y = 0; y < this.longueur; y++) {
            sb.append("+---");
        }
        sb.append("+\n");

        return sb.toString();
    }
    /**
     * @return le nom du labyrinthe
     */
    public String getNom() { return nom; }
    /**
     * @return la longueur du labyrinthe
     */

    public int getLongueur() { return longueur; }
    /**
     * @return la largeur du labyrinthe
     */
    public int getLargeur() { return largeur; }
    /**
     * @return la carte du labyrinthe
     */
    public Case[][] getCarte() { return carte; }
    /**
     * @return le générateur de nombres aléatoires
     */
    public Random getRandom() { return random; }
    /**
     * @return la case d'entrée du labyrinthe
     */
    public Case getEntree() { return entree; }
    /**
     * @return la case de sortie du labyrinthe
     */
    public Case getSortie() { return sortie; }

    
    
}
