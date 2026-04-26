package csi.tp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *Nom et prénom : HOUNSOU Césaire
 *Nom et prénom de l'enseignant : François Picard Légaré
 *Date de la derniere modification : 26-04-2026
 *Description :
 *   Classe principale de l'application JavaFX.
 *   Charge la vue principale (helloView.fxml) qui contient le menu latéral permanent
 *   et la zone centrale interchangeable (Accueil, Règles, Jeu).
 *   Les dimensions de la fenêtre sont restreintes entre 800×600 et 1200×700.
 */
public class HelloApplication extends Application {

    /**
     * Point d'entrée de JavaFX. Configure et affiche la fenêtre principale.
     *
     * @param stage le stage principal fourni par le framework JavaFX
     * @throws IOException si le fichier FXML principal est introuvable ou illisible
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Chargement du fichier FXML de la vue principale (menu latéral + conteneur)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("helloView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Configuration du titre de la fenêtre
        stage.setTitle("Jeu Serpents et Échelles");
        stage.setScene(scene);

        // Dimensions minimales de la fenêtre (800x600)
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Dimensions maximales de la fenêtre (1200x700)
        stage.setMaxWidth(1200);
        stage.setMaxHeight(700);

        // Affichage de la fenêtre
        stage.show();
    }

    /**
     * Lance l'application.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch();
    }
}