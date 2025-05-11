import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class Algorithme {
    /**
     * Exécute l'algorithme de manière directe.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     */
    public abstract void algoDirect(Labyrinthe labyrinthe, GridPane gridPane, Label label);
    /**
     * Exécute l'algorithme pas à pas.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     */
    public abstract void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane, Label label);
}
