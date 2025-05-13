import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

public class App extends Application{
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        final boolean[] cancelRequested = {false};
        primaryStage.setTitle("Générateur de Labyrinthe");
        Scene scene = new Scene(root,1000, 1000); // Agrandis la fenêtre pour le zoom

        Labyrinthe[] labyrintheHolder = new Labyrinthe[1];
        final boolean[] modificationAutorisee = {true};

        // Champs de saisie utilisateur
        TextField longueurField = new TextField("20");
        longueurField.setPromptText("Longueur");
        TextField largeurField = new TextField("20");
        largeurField.setPromptText("Largeur");
        TextField seedField = new TextField("0");
        seedField.setPromptText("Seed");
        TextField vitesse = new TextField("100");
        vitesse.setPromptText("Vitesse (ms)");

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

        Button buttonRetour = new Button("Retour");
        buttonRetour.setVisible(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(javafx.geometry.Insets.EMPTY);
        Label infoLabel = new Label("Statistiques :");
        infoLabel.setWrapText(true); 
        infoLabel.setMaxWidth(Double.MAX_VALUE); // <-- Ajoute cette ligne
        infoLabel.setMinHeight(100); // <-- Ajoute cette ligne pour forcer une hauteur minimale
        VBox.setVgrow(infoLabel, javafx.scene.layout.Priority.ALWAYS); // <-- Ajoute cette ligne
        
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setPannable(true);
        // Organisation de l'UI
        VBox saisieFieldsBox = new VBox(5,
            new Label("Longueur :"), longueurField,
            new Label("Largeur :"), largeurField,
            new Label("Seed :"), seedField,
            new Label("Vitesse :"), vitesse,
            buttonGenerer,
            buttonGenererPasAPas
        );
        saisieFieldsBox.setAlignment(Pos.CENTER_LEFT);

        VBox algoButtonsBox = new VBox(5, buttonTremauxdirect, buttonTremauxPasAPas, buttonDeadEnddirect, buttonDeadEndPasaPas);
        algoButtonsBox.setAlignment(Pos.CENTER_LEFT);

        VBox saisieBox = new VBox(5, 
            saisieFieldsBox, algoButtonsBox
        );
        saisieBox.setAlignment(Pos.CENTER_LEFT);
        // Action du bouton "Générer"
        buttonGenerer.setOnMouseClicked(event -> {
            try {
                cancelRequested[0] = false;
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());

                // Limite la taille à 30x30
                if (longueur > 30) longueur = 30;
                if (largeur > 30) largeur = 30;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;

                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));

                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);

                labyrintheHolder[0].genererLabyrinthe();
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                buttonTremauxdirect.setVisible(true);
                buttonTremauxPasAPas.setVisible(true);
                buttonDeadEndPasaPas.setVisible(true);
                buttonDeadEnddirect.setVisible(true);
                algoButtonsBox.setVisible(true);
                modificationAutorisee[0] = true; // <-- Ajoute cette ligne

                buttonGenerer.setVisible(false);
                saisieFieldsBox.setVisible(false);
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        // Action du bouton "Générer pas à pas"
        buttonGenererPasAPas.setOnMouseClicked(event -> {
            try {
                cancelRequested[0] = false;
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());

                // Limite la taille à  30x30
                if (longueur > 30) longueur = 30;
                if (largeur > 30) largeur = 30;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;

                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));

                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                // Passe un Runnable qui affiche les boutons à la fin
                labyrintheHolder[0].genererLabyrinthePasAPas(gridPane, infoLabel, () -> {
                    buttonTremauxdirect.setVisible(true);
                    buttonTremauxPasAPas.setVisible(true);
                    buttonDeadEndPasaPas.setVisible(true);
                    buttonDeadEnddirect.setVisible(true);
                    algoButtonsBox.setVisible(true);
                    modificationAutorisee[0] = true; 
                }, Integer.parseInt(vitesse.getText()), cancelRequested); 
                buttonGenerer.setVisible(false);
                buttonRetour.setVisible(true); 
                buttonGenererPasAPas.setVisible(false);
                saisieFieldsBox.setVisible(false);
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        // Action du bouton "Trémaux"
        buttonTremauxdirect.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Trémaux, gridPane, infoLabel);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true); // <-- ici
        });

        // Action du bouton "Trémaux pas à pas"
        buttonTremauxPasAPas.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Trémaux, gridPane, infoLabel, cancelRequested);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true); // <-- ici
        });
        buttonDeadEndPasaPas.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Deadend, gridPane, infoLabel, cancelRequested);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true); // <-- ici
        });
        buttonDeadEnddirect.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Deadend, gridPane, infoLabel);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true); // <-- ici
        });

        // Action du bouton "Retour"
        buttonRetour.setOnAction(event -> {
            // Réinitialise l'affichage
            cancelRequested[0] = true;
            modificationAutorisee[0] = false;
            gridPane.getChildren().clear();
            infoLabel.setText("Statistiques :");
            buttonRetour.setVisible(false);

            // Réaffiche les champs de saisie et le bouton générer
            saisieFieldsBox.setVisible(true);
            buttonGenerer.setVisible(true);
            buttonGenererPasAPas.setVisible(true);

            // Cache les boutons d'algo
            algoButtonsBox.setVisible(false);

        });

        // Champ pour la direction à modifier
        TextField directionField = new TextField();
        directionField.setPromptText("Nord, Sud, Est ou Ouest");
        directionField.setVisible(false);
        Label directionLabel = new Label("Entrez la direction à modifier :");
        directionLabel.setVisible(false);

        final Case[] selectedCase = new Case[1];

        // Gestion du clic sur une case du GridPane
        gridPane.setOnMouseClicked(event -> {
            if (!modificationAutorisee[0]) return; // Bloque la modification si un algo a été lancé

                double scale = gridPane.getScaleX();
                double mouseX = event.getX() / scale;
                double mouseY = event.getY() / scale;

                double cellWidth = gridPane.getWidth() / labyrintheHolder[0].getLongueur();
                double cellHeight = gridPane.getHeight() / labyrintheHolder[0].getLargeur();

                int col = (int) Math.floor(mouseX / cellWidth);
                int row = (int) Math.floor(mouseY / cellHeight);

                Labyrinthe lab = labyrintheHolder[0];
                if (lab != null && lab.getCarte() != null && lab.isInBounds(row, col)) {
                    selectedCase[0] = lab.getCarte()[row][col];
                    directionField.setVisible(true);
                    directionLabel.setVisible(true);
                    directionField.clear();
                    directionField.requestFocus();
                }
        });

        // Quand l'utilisateur entre une direction et appuie sur Entrée
        directionField.setOnAction(e -> {
            String dir = directionField.getText().trim().toLowerCase();
            if (selectedCase[0] != null && (dir.equals("nord") || dir.equals("sud") || dir.equals("est") || dir.equals("ouest"))) {
                labyrintheHolder[0].modifierLabyrinthe(selectedCase[0], dir);
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                directionField.setVisible(false);
                directionLabel.setVisible(false);
            } else {
                directionField.setText("");
                directionField.setPromptText("Nord, Sud, Est ou Ouest");
            }
        });
        HBox labyBox = new HBox(gridPane, saisieBox);
        labyBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(directionLabel, directionField, labyBox, infoLabel, buttonRetour);
        
        

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
        Labyrinthe labyrinthe = new Labyrinthe("MonLabyrinthe", 10, 10, 0L);
        labyrinthe.genererLabyrinthe(); // Génère le labyrinthe
        
    }
}
