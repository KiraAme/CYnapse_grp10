import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Classe abstraite représentant un algorithme de résolution de labyrinthe.
 * Les classes dérivées doivent implémenter les méthodes pour exécuter l'algorithme
 * de manière directe et pas à pas.
 * @version 1.0
 * @author Groupe 10
 */
public abstract class Algorithme {
    /**
     * Exécute l'algorithme de manière directe.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     * @param label      Le Label pour afficher les messages.
     */
    public abstract void algoDirect(Labyrinthe labyrinthe, GridPane gridPane, Label label);
    /**
     * Exécute l'algorithme pas à pas.
     *
     * @param labyrinthe Le labyrinthe à traiter.
     * @param gridPane   Le GridPane pour l'affichage.
     * @param label      Le Label pour afficher les messages.
     * @param cancelRequested Un tableau de booléens pour indiquer si l'annulation a été demandée.
     */
    public abstract void algoPasAPas(Labyrinthe labyrinthe, GridPane gridPane, Label label,boolean[] cancelRequested);
}
