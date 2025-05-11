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
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

public class App extends Application{
    private static final int TILE_SIZE = 20; // Taille d'une case (en pixels)

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setTitle("Générateur de Labyrinthe");
        Scene scene = new Scene(root, 800, 800); // Agrandis la fenêtre pour le zoom

        Labyrinthe[] labyrintheHolder = new Labyrinthe[1];

        // Champs de saisie utilisateur
        TextField longueurField = new TextField("20");
        longueurField.setPromptText("Longueur");
        TextField largeurField = new TextField("20");
        largeurField.setPromptText("Largeur");
        TextField seedField = new TextField("0");
        seedField.setPromptText("Seed");

        Button buttonGenerer = new Button("Générer Labyrinthe");
        Button buttonGenererPasAPas = new Button("Générer Labyrinthe pas à pas");
        Button buttonTremauxdirect = new Button("Trémaux version directe");
        Button buttonTremauxPasAPas = new Button("Trémaux version pas a pas");
        Button buttonDeadEndPasaPas = new Button("DeadEnd version Pas a Pas");
        Button buttonDeadEnddirect = new Button("DeadEnd version directe");
        buttonDeadEndPasaPas.setVisible(false);
        buttonDeadEnddirect.setVisible(false);
        buttonTremauxdirect.setVisible(false);
        buttonTremauxPasAPas.setVisible(false);

        GridPane gridPane = new GridPane();
        Label infoLabel = new Label("Statistiques :");

        // Ajoute le GridPane dans un ScrollPane pour pouvoir dézoomer/déplacer
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setPannable(true);
        // Organisation de l'UI
        VBox saisieFieldsBox = new VBox(5,
            new Label("Longueur :"), longueurField,
            new Label("Largeur :"), largeurField,
            new Label("Seed :"), seedField,
            buttonGenerer,
            buttonGenererPasAPas
        );
        saisieFieldsBox.setAlignment(Pos.CENTER_LEFT);

        VBox algoButtonsBox = new VBox(5, buttonTremauxdirect, buttonTremauxPasAPas, buttonDeadEnddirect,buttonDeadEndPasaPas);
        algoButtonsBox.setAlignment(Pos.CENTER_LEFT);

        VBox saisieBox = new VBox(5, 
            saisieFieldsBox, algoButtonsBox
        );
        saisieBox.setAlignment(Pos.CENTER_LEFT);
        // Action du bouton "Générer"
        buttonGenerer.setOnMouseClicked(event -> {
            try {
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());
                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                labyrintheHolder[0].genererLabyrinthe();
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                // Afficher les boutons d'algo seulement après la génération
                buttonTremauxdirect.setVisible(true);
                buttonTremauxPasAPas.setVisible(true);
                buttonDeadEndPasaPas.setVisible(true);
                buttonDeadEnddirect.setVisible(true);
                buttonGenerer.setVisible(false);
                saisieFieldsBox.setVisible(false);
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        // Action du bouton "Générer pas à pas"
        buttonGenererPasAPas.setOnMouseClicked(event -> {
            try {
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());
                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                // Passe un Runnable qui affiche les boutons à la fin
                labyrintheHolder[0].genererLabyrinthePasAPas(gridPane, infoLabel, () -> {
                    buttonTremauxdirect.setVisible(true);
                    buttonTremauxPasAPas.setVisible(true);
                    buttonDeadEndPasaPas.setVisible(true);
                    buttonDeadEnddirect.setVisible(true);
                });
                buttonGenerer.setVisible(false);
                buttonGenererPasAPas.setVisible(false);
                saisieFieldsBox.setVisible(false);
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        // Action du bouton "Trémaux"
        buttonTremauxdirect.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Trémaux, gridPane, infoLabel);
            }
            buttonTremauxdirect.setVisible(false);
            buttonTremauxPasAPas.setVisible(false);
        });

        // Action du bouton "Trémaux pas à pas"
        buttonTremauxPasAPas.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Trémaux, gridPane, infoLabel);
            }
            buttonTremauxdirect.setVisible(false);
            buttonTremauxPasAPas.setVisible(false);
        });
        buttonDeadEndPasaPas.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Deadend, gridPane, infoLabel);
            }
            buttonDeadEndPasaPas.setVisible(false);
            buttonTremauxdirect.setVisible(false);
            buttonTremauxPasAPas.setVisible(false);
        });
        buttonDeadEnddirect.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Deadend, gridPane, infoLabel);
            }
            buttonDeadEnddirect.setVisible(false);
            buttonTremauxdirect.setVisible(false);
            buttonTremauxPasAPas.setVisible(false);
        });

        // Ajoute un slider pour le zoom
        javafx.scene.control.Slider zoomSlider = new javafx.scene.control.Slider(0.1, 2.0, 1.0);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(0.5);
        zoomSlider.setMinorTickCount(4);
        zoomSlider.setBlockIncrement(0.1);
        Label zoomLabel = new Label("Zoom : 1.0x");

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double scale = newVal.doubleValue();
            gridPane.setScaleX(scale);
            gridPane.setScaleY(scale);
            zoomLabel.setText(String.format("Zoom : %.2fx", scale));
        });

        

        root.getChildren().addAll(saisieBox, scrollPane, infoLabel);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
        Labyrinthe labyrinthe = new Labyrinthe("MonLabyrinthe", 10, 10, 0L);
        labyrinthe.genererLabyrinthe(); // Génère le labyrinthe
        
    }
}
