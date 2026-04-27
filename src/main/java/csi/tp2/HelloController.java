
/**
 * Nom et prénom : HOUNSOU Césaire
 * Nom et prénom de l'enseignant : François Picard Légaré
 * Date de la derniere modification : 26-04-2026
 * Description :
 *   Contrôleur de la fenêtre principale (helloView.fxml).
 *   Gère le menu latéral permanent et le changement des vues centrales
 *   (Accueil, Règles, Jeu).
 **/


package csi.tp2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;


public class HelloController {

    /** Instance statique pour permettre à d'autres contrôleurs (ex: JeuController) d'appeler la navigation */
    private static HelloController instance;

    /**
     * Constructeur lancé automatiquement par JavaFX lors du chargement du FXML.
     * Enregistre l'instance courante dans le champ statique.
     */
    public HelloController() {
        instance = this;
    }

    /**
     * Retourne l'instance unique du contrôleur principal.
     *
     * @return l'instance de HelloController (peut être null avant le chargement du FXML)
     */
    public static HelloController getInstance() {
        return instance;
    }

    /** Conteneur central qui accueille les différentes vues (Accueil, Règles, Jeu) */
    @FXML
    private StackPane contentPane;

    /**
     * Initialisation automatique après le chargement du FXML.
     * Affiche l'écran d'accueil par défaut.
     */
    @FXML
    private void initialize() {
        loadView("AccueilView.fxml");
    }

    /**
     * Navigation vers l'écran d'accueil.
     * (Méthode publique pour permettre à JeuController de revenir à l'accueil.)
     */
    @FXML
    public void handleAccueil() {
        loadView("AccueilView.fxml");
    }

    /**
     * Navigation vers l'écran des règles.
     */
    @FXML
    private void handleRegles() {
        loadView("ReglesView.fxml");
    }

    /**
     * Navigation vers l'écran de jeu.
     */
    @FXML
    private void handleJouer() {
        loadView("JeuView.fxml");
    }

    /**
     * Quitte complètement l'application.
     */
    @FXML
    private void handleQuitter() {
        System.exit(0);
    }

    /**
     * Charge un fichier FXML et l'affiche dans la zone centrale (contentPane).
     * Remplace le contenu précédent.
     *
     * @param fxmlFile le nom du fichier FXML à charger (ex: "AccueilView.fxml")
     */
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