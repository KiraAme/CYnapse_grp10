import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AfficheurLabyrinthe {
    private static final int TILE_SIZE = 30; 

    /**
     * Affiche le labyrinthe dans le GridPane donné.
     *
     * @param gridPane  Le GridPane dans lequel afficher le labyrinthe.
     * @param labyrinthe Le labyrinthe à afficher.
     */
    public static void afficherLabyrinthe(GridPane gridPane, Labyrinthe labyrinthe) {
        gridPane.getChildren().clear();

        int nbLignes = labyrinthe.getLargeur();
        int nbColonnes = labyrinthe.getLongueur();

        // Fixe la taille du GridPane pour qu'il affiche tout le labyrinthe sans scroll
        gridPane.setPrefWidth(nbColonnes * TILE_SIZE);
        gridPane.setPrefHeight(nbLignes * TILE_SIZE);

        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                Case currentCase = labyrinthe.getCarte()[x][y];
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(TILE_SIZE, TILE_SIZE);

                Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);

                if (currentCase.estEntree) {
                    rectangle.setFill(Color.BLUE);
                } else if (currentCase.estSortie) {
                    rectangle.setFill(Color.GREEN);
                } else if (currentCase.getCouleur() == Color.RED) {
                    rectangle.setFill(Color.RED);
                } else if (currentCase.getCouleur() == Color.PINK) {
                    rectangle.setFill(Color.PINK);
                } else if (currentCase.getCouleur() == Color.YELLOW) {
                    rectangle.setFill(Color.YELLOW);
                } else {
                    rectangle.setFill(Color.WHITE);
                }

                rectangle.setStroke(Color.GRAY);
                stackPane.getChildren().add(rectangle);

                double wallThickness = 4.0;
                Color wallColor = Color.BLACK;

                if (currentCase.murNord) {
                    Rectangle murNord = new Rectangle(TILE_SIZE, wallThickness);
                    murNord.setFill(wallColor);
                    StackPane.setAlignment(murNord, Pos.TOP_CENTER);
                    stackPane.getChildren().add(murNord);
                }
                if (currentCase.murSud) {
                    Rectangle murSud = new Rectangle(TILE_SIZE, wallThickness);
                    murSud.setFill(wallColor);
                    StackPane.setAlignment(murSud, Pos.BOTTOM_CENTER);
                    stackPane.getChildren().add(murSud);
                }
                if (currentCase.murOuest) {
                    Rectangle murOuest = new Rectangle(wallThickness, TILE_SIZE);
                    murOuest.setFill(wallColor);
                    StackPane.setAlignment(murOuest, Pos.CENTER_LEFT);
                    stackPane.getChildren().add(murOuest);
                }
                if (currentCase.murEst) {
                    Rectangle murEst = new Rectangle(wallThickness, TILE_SIZE);
                    murEst.setFill(wallColor);
                    StackPane.setAlignment(murEst, Pos.CENTER_RIGHT);
                    stackPane.getChildren().add(murEst);
                }

                gridPane.add(stackPane, y, x);
            }
        }
    }
}

