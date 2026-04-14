package csi.tp2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class HelloController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void initialize() {
        // Afficher l'écran d'accueil par défaut au démarrage
        loadView("AccueilView.fxml");
    }

    @FXML
    private void handleAccueil() {
        loadView("AccueilView.fxml");
    }

    @FXML
    private void handleRegles() {
        loadView("ReglesView.fxml");
    }

    @FXML
    private void handleJouer() {
        loadView("JeuView.fxml");
    }

    @FXML
    private void handleQuitter() {
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}