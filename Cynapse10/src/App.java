import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setTitle("Le test là");
        Scene scene = new Scene(root,300,400);
        Button button = new Button("Feu");
        Label label = new Label(""); // Label vide au départ
        button.setOnMouseClicked(event -> label.setText("Tu as cliqué !")); // Affiche le texte
        root.getChildren().addAll(button, label); // Ajoute bouton + label 
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
        
    }

    
}
