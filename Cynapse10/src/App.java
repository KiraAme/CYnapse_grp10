import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application{
    private static final int TILE_SIZE = 20; // Taille d'une case (en pixels)

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setTitle("Générateur de Labyrinthe");
        Scene scene = new Scene(root, 600, 600);
        Labyrinthe[] labyrintheHolder = new Labyrinthe[1];

        Button buttonGenerer = new Button("Générer Labyrinthe");
        Button buttonTremauxdirect = new Button("Trémaux version directe");
        Button buttonTremauxPasAPas = new Button("Trémaux version pas a pas");
        buttonTremauxdirect.setVisible(false); // CACHÉ au départ
        buttonTremauxPasAPas.setVisible(false); // CACHÉ au départ

        GridPane gridPane = new GridPane(); // Utilisé pour afficher le labyrinthe

        // Action du bouton "Générer"
        buttonGenerer.setOnMouseClicked(event -> {
            labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", 10, 10, 0L);
            labyrintheHolder[0].genererLabyrinthe();
            AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);

            buttonTremauxdirect.setVisible(true); // Affiche le bouton Trémaux après génération
            buttonTremauxPasAPas.setVisible(true); // Affiche le bouton Trémaux après génération
            buttonGenerer.setVisible(false);
        });

        // Action du bouton "Trémaux"
        buttonTremauxdirect.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Trémaux, gridPane);
            }
            buttonTremauxdirect.setVisible(false); // Cache le bouton après clic
            buttonTremauxPasAPas.setVisible(false); // Cache le bouton après clic
        });

        // Action du bouton "Trémaux pas à pas"
        buttonTremauxPasAPas.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Trémaux, gridPane);
            }
            buttonTremauxdirect.setVisible(false); // Cache le bouton après clic
            buttonTremauxPasAPas.setVisible(false); // Cache le bouton après clic
        });
        root.getChildren().addAll(buttonGenerer, buttonTremauxdirect,buttonTremauxPasAPas, gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    

    public static void main(String[] args) throws Exception {
        launch(args);
        Labyrinthe labyrinthe = new Labyrinthe("MonLabyrinthe", 10, 10, 0L);
        labyrinthe.genererLabyrinthe(); // Génère le labyrinthe
        
    }

    
}
