import javafx.scene.paint.Color;

public class Case {
    private int x;
    private int y;
    public boolean murNord;
    public boolean murSud;
    public boolean murOuest;
    public boolean murEst;
    public boolean estSortie;
    public boolean estEntree;
    public boolean parcourue;
    private Color couleur = Color.WHITE; // Couleur par défaut
    private int distance;


    /**
     * Constructeur d'une case
     * @param x la position x
     * @param y la position y 
     * @param mN si la case au Nord est un mur
     * @param mS si la case au Sud est un mur
     * @param mE si la case à l'Est est un mur
     * @param mO si la case à l'Ouest est un mur
     * @param estM si la case est un mur
     * @param estS si la case est une sortie
    */
    public Case(int x, int y, boolean mN, boolean mS, boolean mE, boolean mO, boolean estS, boolean estE) {
        this.x=x;
        this.y=y;
        this.murNord=mN;
        this.murSud=mS;
        this.murOuest=mO;
        this.murEst=mE;
        this.estSortie=estS;
        this.estEntree=estE;
        this.parcourue = false;
        this.distance=(this.estEntree)?0:Integer.MAX_VALUE;
        
        
    }
    /**
     * @return la distance au noeud de départ (pour dijkstra) initialisée à 0
     */
    public int getDistance() {
        return this.distance;
    }
    /**
     * @param la distance au noeud de départ (pour dijkstra) initialisée à 0
     */
    public void setDistance(int distance) {
        this.distance=distance;
    }
    /**
     * @return la position x
     */
    public int getX() {
        return x;
    }
    //fais toute la javadoc des getters
    /**
     * @return la position y
     */

    public int getY() {
        return y;
    }
    /**
     * @return si la case au Nord est un mur
     */
    public boolean isMurNord() {
        return murNord;
    }
    /**
     * @return si la case au Sud est un mur
     */
    public boolean isMurSud() {
        return murSud;
    }
    /**
     * @return si la case à l'Ouest est un mur
     */
    public boolean isMurOuest() {
        return murOuest;
    }
    /**
     * @return si la case à l'Est est un mur
     */
    public boolean isMurEst() {
        return murEst;
    }
    
    /**
     * @return si la case est une sortie
     */
    public boolean isEstSortie() {
        return estSortie;
    }
    /**
     * @return si la case est une entrée
     */
    public boolean isEstEntree() {
        return estEntree;
    }
    public boolean estParcourue() {
        return parcourue;
    }

    public void setParcourue(boolean parcourue) {
        this.parcourue = parcourue;
    }
    // Méthodes pour obtenir et modifier la couleur
    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
