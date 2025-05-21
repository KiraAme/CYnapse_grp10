import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import java.io.*;
import javafx.scene.layout.Region;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        final int CELL_SIZE = 30;
        final boolean[] cancelRequested = {false};
        primaryStage.setTitle("Générateur de Labyrinthe");
        Scene scene = new Scene(root, 1000, 1000);

        Labyrinthe[] labyrintheHolder = new Labyrinthe[1];
        final boolean[] modificationAutorisee = {true};
        Button buttonSauvegarder = new Button("Sauvegarder");
        Button buttonRestaurer = new Button("Restaurer");
        buttonSauvegarder.setVisible(false);
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
        Button buttonGenererImparfait = new Button("Générer Labyrinthe Imparfait");
        Button buttonGenererPasAPas = new Button("Générer Labyrinthe pas à pas");
        Button buttonGenererImparfaitPasAPas = new Button("Générer Labyrinthe Imparfait pas à pas");
        Button buttonTremauxdirect = new Button("Trémaux version directe");
        Button buttonTremauxPasAPas = new Button("Trémaux version pas a pas");
        Button buttonDeadEndPasaPas = new Button("DeadEnd version Pas a Pas");
        Button buttonDeadEnddirect = new Button("DeadEnd version directe");
        Button buttonDijkstraPasAPas = new Button("Dijkstra version Pas a Pas");
        Button buttonDijkstradirect = new Button("Dijkstra version directe");
        buttonDeadEndPasaPas.setVisible(false);
        buttonDeadEnddirect.setVisible(false);
        buttonTremauxdirect.setVisible(false);
        buttonTremauxPasAPas.setVisible(false);
        buttonDijkstraPasAPas.setVisible(false);
        buttonDijkstradirect.setVisible(false);
        TextArea historiqueArea = new TextArea();
        historiqueArea.setEditable(false);
        historiqueArea.setPrefRowCount(8);
        historiqueArea.setPrefColumnCount(30);
        historiqueArea.setWrapText(true);
        historiqueArea.setPromptText("Historique des modifications...");
        historiqueArea.setVisible(false);

        

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(javafx.geometry.Insets.EMPTY);
        Label infoLabel = new Label("Statistiques :");
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(Double.MAX_VALUE);
        infoLabel.setMinHeight(100);
        VBox.setVgrow(infoLabel, javafx.scene.layout.Priority.ALWAYS);
        
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setPannable(true);

        VBox saisieFieldsBox = new VBox(5,
            new Label("Longueur :"), longueurField,
            new Label("Largeur :"), largeurField,
            new Label("Seed :"), seedField,
            new Label("Vitesse :"), vitesse,
            buttonGenerer,
            buttonGenererImparfait,
            buttonGenererPasAPas,
            buttonGenererImparfaitPasAPas,
            buttonRestaurer
        );
        saisieFieldsBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox algoButtonsBox = new VBox(7, buttonTremauxdirect, buttonTremauxPasAPas, buttonDeadEnddirect, buttonDeadEndPasaPas, buttonDijkstradirect, buttonDijkstraPasAPas);
        algoButtonsBox.setAlignment(Pos.CENTER_LEFT);

        VBox saisieBox = new VBox(5, 
            saisieFieldsBox, algoButtonsBox,buttonSauvegarder
        );
        saisieBox.setAlignment(Pos.CENTER_LEFT);
        // Champ pour la direction à modifier
        ComboBox<String> directionCombo = new ComboBox<>();
        directionCombo.getItems().addAll("nord", "sud", "est", "ouest");
        Label directionLabel = new Label("Entrez la direction à modifier :");
        directionCombo.setPromptText("Choisir une direction");
        directionCombo.setVisible(false);
        
        directionLabel.setVisible(false);
        Button buttonRetour = new Button("Retour");
        buttonRetour.setVisible(false);
        // Action du bouton "Générer"
        buttonGenerer.setOnMouseClicked(event -> {
            try {
                cancelRequested[0] = false;
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());

                if (longueur > 20) longueur = 20;
                if (largeur > 20) largeur = 20;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;

                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));

                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                gridPane.getChildren().clear();
                gridPane.getColumnConstraints().clear();
                gridPane.getRowConstraints().clear();
                labyrintheHolder[0].genererLabyrinthe();
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                else {
                    gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                }
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                buttonTremauxdirect.setVisible(true);
                buttonTremauxPasAPas.setVisible(true);
                buttonDeadEndPasaPas.setVisible(true);
                buttonDeadEnddirect.setVisible(true);
                buttonDijkstraPasAPas.setVisible(true);
                buttonDijkstradirect.setVisible(true);
                algoButtonsBox.setVisible(true);
                modificationAutorisee[0] = true;
                buttonRetour.setVisible(true);
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                buttonGenerer.setVisible(false);
                buttonGenererImparfait.setVisible(false);
                saisieFieldsBox.setVisible(false);
                historiqueArea.setVisible(true);
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                
               
                /* if (labyrintheHolder[0] != null) {
                    for (String ligne : labyrintheHolder[0].getHistorique()) {
                        historiqueArea.appendText(ligne + "\n");
                    }
                }*/
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });
        buttonSauvegarder.setOnMouseClicked(event -> {
            if (labyrintheHolder[0] == null) return;
                try (PrintWriter writer = new PrintWriter(new FileWriter("labyrinthe_save.txt"))) {
                    writer.println(labyrintheHolder[0].getLongueur());
                    writer.println(labyrintheHolder[0].getLargeur());
                    writer.println(labyrintheHolder[0].getSeed());
                    for (String ligne : labyrintheHolder[0].getHistorique()) {
                        writer.println(ligne);
                    }
                    infoLabel.setText("Labyrinthe sauvegardé !");
                } catch (IOException e) {
                    infoLabel.setText("Erreur lors de la sauvegarde.");
                }
            });
        buttonRestaurer.setOnMouseClicked(event -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("labyrinthe_save.txt"))) {
                int longueur = Integer.parseInt(reader.readLine());
                int largeur = Integer.parseInt(reader.readLine());
                long seed = Long.parseLong(reader.readLine());
                gridPane.getChildren().clear();
                gridPane.getColumnConstraints().clear();
                gridPane.getRowConstraints().clear();
                Labyrinthe lab = new Labyrinthe("Restauré", longueur, largeur, seed);
                lab.genererLabyrinthe();

                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    if (ligne.startsWith("Case")) {
                        try {
                            // Extrait les coordonnées
                            String[] parts = ligne.split("[(),:]");
                            int x = Integer.parseInt(parts[1].trim());
                            int y = Integer.parseInt(parts[2].trim());
                            // Extrait la direction (toujours après "mur ")
                            String[] murParts = ligne.split("mur ");
                            String direction = murParts[1].split(" ")[0].trim().toLowerCase();
                            // Normalise la direction (enlève accents, majuscules)
                            direction = direction.replaceAll("[éèêë]", "e");
                            direction = direction.replaceAll("[àâä]", "a");
                            direction = direction.replaceAll("[ûùü]", "u");
                            direction = direction.replaceAll("[ôö]", "o");
                            direction = direction.replaceAll("[îï]", "i");
                            // Vérifie que la direction est valide
                            if (direction.equals("nord") || direction.equals("sud") || direction.equals("est") || direction.equals("ouest")) {
                                lab.modifierLabyrinthe(lab.getCarte()[x][y], direction);
                            }
                        } catch (Exception ex) {
                            // Ignore la ligne si erreur de parsing
                        }
                    }
                }
                labyrintheHolder[0] = lab;
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, lab);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                else {
                    gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                }
                buttonTremauxdirect.setVisible(true);
                buttonTremauxPasAPas.setVisible(true);
                buttonDeadEndPasaPas.setVisible(true);
                buttonDeadEnddirect.setVisible(true);
                buttonDijkstraPasAPas.setVisible(true);
                buttonDijkstradirect.setVisible(true);
                algoButtonsBox.setVisible(true);
                modificationAutorisee[0] = true;
                buttonRetour.setVisible(true);
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                buttonGenerer.setVisible(false);
                buttonGenererImparfait.setVisible(false);
                saisieFieldsBox.setVisible(false);
                historiqueArea.setVisible(true);
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                
                if (labyrintheHolder[0] != null) {
                    for (String li : labyrintheHolder[0].getHistorique()) {
                        historiqueArea.appendText(li + "\n");
                    }
                }
            } catch (Exception e) {
                infoLabel.setText("Erreur lors de la restauration.");
            }
        });

        // Action du bouton "Générer Labyrinthe Imparfait"
        buttonGenererImparfait.setOnMouseClicked(event -> {
            try {
                cancelRequested[0] = false;
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());

                if (longueur > 20) longueur = 20;
                if (largeur > 20) largeur = 20;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;

                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));

                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                gridPane.getChildren().clear();
                gridPane.getColumnConstraints().clear();
                gridPane.getRowConstraints().clear();
                labyrintheHolder[0].genererImparfait();
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                else {
                    gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                }
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                buttonTremauxdirect.setVisible(true);
                buttonTremauxPasAPas.setVisible(true);
                buttonDeadEndPasaPas.setVisible(true);
                buttonDeadEnddirect.setVisible(true);
                buttonDijkstraPasAPas.setVisible(true);
                buttonDijkstradirect.setVisible(true);
                algoButtonsBox.setVisible(true);
                modificationAutorisee[0] = true;
                buttonRetour.setVisible(true);
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                buttonGenerer.setVisible(false);
                buttonGenererImparfait.setVisible(false);
                saisieFieldsBox.setVisible(false);
                historiqueArea.setVisible(true);
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                
               
                if (labyrintheHolder[0] != null) {
                    for (String ligne : labyrintheHolder[0].getHistorique()) {
                        historiqueArea.appendText(ligne + "\n");
                    }
                    historiqueArea.appendText("Labyrinthe Imparfait généré avec succès !\n");
                }
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

                if (longueur > 20) longueur = 20;
                if (largeur > 20) largeur = 20;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;

                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));
                gridPane.getChildren().clear();
                gridPane.getColumnConstraints().clear();
                gridPane.getRowConstraints().clear();
                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);
                labyrintheHolder[0].genererLabyrinthePasAPas(gridPane, infoLabel, () -> {
                    buttonTremauxdirect.setVisible(true);
                    buttonTremauxPasAPas.setVisible(true);
                    buttonDeadEndPasaPas.setVisible(true);
                    buttonDeadEnddirect.setVisible(true);
                    buttonDijkstraPasAPas.setVisible(true);
                    buttonDijkstradirect.setVisible(true);
                    algoButtonsBox.setVisible(true);
                    modificationAutorisee[0] = true;
                }, Integer.parseInt(vitesse.getText()), cancelRequested);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                else {
                    gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                }
                buttonGenerer.setVisible(false);
                buttonGenererImparfait.setVisible(false);
                buttonRetour.setVisible(true);
                buttonGenererPasAPas.setVisible(false);
                saisieFieldsBox.setVisible(false);
                historiqueArea.setVisible(true);
                historiqueArea.clear();
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                 if (labyrintheHolder[0] != null) {
                    for (String ligne : labyrintheHolder[0].getHistorique()) {
                        historiqueArea.appendText(ligne + "\n");
                    }
                }
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        // Action du bouton "Générer Labyrinthe Imparfait pas à pas"
        buttonGenererImparfaitPasAPas.setOnMouseClicked(event -> {
            try {
                cancelRequested[0] = false;
                int longueur = Integer.parseInt(longueurField.getText());
                int largeur = Integer.parseInt(largeurField.getText());

                if (longueur > 20) longueur = 20;
                if (largeur > 20) largeur = 20;
                if (longueur < 1) longueur = 1;
                if (largeur < 1) largeur = 1;
                gridPane.getChildren().clear();
                gridPane.getColumnConstraints().clear();
                gridPane.getRowConstraints().clear();
                longueurField.setText(String.valueOf(longueur));
                largeurField.setText(String.valueOf(largeur));

                long seed = Long.parseLong(seedField.getText());
                labyrintheHolder[0] = new Labyrinthe("MonLabyrinthe", longueur, largeur, seed);

                labyrintheHolder[0].genererImparfaitPasAPas(gridPane, infoLabel, () -> {
                    buttonTremauxdirect.setVisible(true);
                    buttonTremauxPasAPas.setVisible(true);
                    buttonDeadEndPasaPas.setVisible(true);
                    buttonDeadEnddirect.setVisible(true);
                    buttonDijkstraPasAPas.setVisible(true);
                    buttonDijkstradirect.setVisible(true);
                    algoButtonsBox.setVisible(true);
                    modificationAutorisee[0] = true;
                }, Integer.parseInt(vitesse.getText()), cancelRequested);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }else {
                    gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                }
                
                buttonRetour.setVisible(true);
                saisieFieldsBox.setVisible(false);
                historiqueArea.setVisible(true);
                buttonSauvegarder.setVisible(true);
                buttonRestaurer.setVisible(false);
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                
            } catch (NumberFormatException e) {
                infoLabel.setText("Veuillez entrer des valeurs valides pour la longueur, la largeur et la seed.");
            }
        });

        

        // Action du bouton "Trémaux"
        buttonTremauxdirect.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Trémaux, gridPane, infoLabel);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });

        // Action du bouton "Trémaux pas à pas"
        buttonTremauxPasAPas.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Trémaux, gridPane, infoLabel, cancelRequested);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });
        buttonDeadEndPasaPas.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.Deadend, gridPane, infoLabel, cancelRequested);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });
        buttonDeadEnddirect.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.Deadend, gridPane, infoLabel);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });
        buttonDijkstraPasAPas.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudrePasAPas(Algo.ShortestPath, gridPane, infoLabel, cancelRequested);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });
        buttonDijkstradirect.setOnMouseClicked(event -> {
            modificationAutorisee[0] = false;
            labyrintheHolder[0].reset();
            if (labyrintheHolder[0] != null) {
                labyrintheHolder[0].résoudredirect(Algo.ShortestPath, gridPane, infoLabel);
            }
            algoButtonsBox.setVisible(false);
            buttonRetour.setVisible(true);
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
        });

        // Action du bouton "Retour"
        buttonRetour.setOnAction(event -> {
            cancelRequested[0] = true;
            modificationAutorisee[0] = false;
            gridPane.getChildren().clear();
            infoLabel.setText("Statistiques :");
            buttonRetour.setVisible(false);
            saisieFieldsBox.setVisible(true);
            buttonGenerer.setVisible(true);
            buttonGenererImparfait.setVisible(true);
            buttonGenererPasAPas.setVisible(true);
            algoButtonsBox.setVisible(false);
            historiqueArea.setVisible(false);
            buttonRestaurer.setVisible(true);
            buttonSauvegarder.setVisible(false); 
            directionCombo.setVisible(false);
            directionLabel.setVisible(false);
            historiqueArea.clear();   
            
        });

        final Case[] selectedCase = new Case[1];
        final Color[] previousColor = new Color[1];

        // Gestion du clic sur une case du GridPane
        gridPane.setOnMouseClicked(event -> {
            if (!modificationAutorisee[0]) return;
            double scale = gridPane.getScaleX();
            double mouseX = event.getX() / scale;
            double mouseY = event.getY() / scale;
            if (labyrintheHolder[0] == null) return;
            double cellWidth = gridPane.getWidth() / labyrintheHolder[0].getLongueur();
            double cellHeight = gridPane.getHeight() / labyrintheHolder[0].getLargeur();
            int col = (int) Math.floor(mouseX / cellWidth);
            int row = (int) Math.floor(mouseY / cellHeight);

            Labyrinthe lab = labyrintheHolder[0];
            if (lab != null && lab.getCarte() != null && lab.isInBounds(row, col)) {
                // Remet la couleur de la case précédemment sélectionnée
                if (selectedCase[0] != null && previousColor[0] != null) {
                    selectedCase[0].setCouleur(previousColor[0]);
                }
                // Mémorise la nouvelle case et sa couleur d'origine
                selectedCase[0] = lab.getCarte()[row][col];
                previousColor[0] = selectedCase[0].getCouleur();
                // Mets la case sélectionnée en rouge
                selectedCase[0].setCouleur(Color.RED);
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                directionCombo.setVisible(true);
                directionLabel.setVisible(true);
                directionCombo.getSelectionModel().clearSelection();
                directionCombo.requestFocus();
            }
        });

        // Quand l'utilisateur entre une direction et appuie sur Entrée
        directionCombo.setOnAction(e -> {
            String dir = directionCombo.getValue();
            if (selectedCase[0] != null && dir != null &&
                (dir.equals("nord") || dir.equals("sud") || dir.equals("est") || dir.equals("ouest"))) {
                labyrintheHolder[0].modifierLabyrinthe(selectedCase[0], dir);
                selectedCase[0].setCouleur(Color.WHITE);
                AfficheurLabyrinthe.afficherLabyrinthe(gridPane, labyrintheHolder[0]);
                if (labyrintheHolder[0].getLongueur() * labyrintheHolder[0].getLargeur() < 400) {
                    gridPane.setMinSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setMaxSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                    gridPane.setPrefSize(labyrintheHolder[0].getLongueur() * CELL_SIZE, labyrintheHolder[0].getLargeur() * CELL_SIZE);
                }
                directionCombo.setVisible(false);
                directionLabel.setVisible(false);
                // Réinitialise la sélection
                selectedCase[0] = null;
                previousColor[0] = null;
                // Après la modification et l'affichage du labyrinthe
                historiqueArea.clear();
                if (labyrintheHolder[0] != null) {
                    for (String ligne : labyrintheHolder[0].getHistorique()) {
                        historiqueArea.appendText(ligne + "\n");
                    }
                }
            } else {
                directionCombo.setPromptText("Nord, Sud, Est ou Ouest");
            }
        });

        HBox labyBox = new HBox(gridPane, saisieBox);
        labyBox.setSpacing(30);
        labyBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(directionLabel, directionCombo, labyBox, infoLabel,historiqueArea, buttonRetour);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}