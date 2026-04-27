

/**
 *
 * Nom et prénom : HOUNSOU Césaire
 * Nom et prénom de l'enseignant : François Picard Légaré
 * Date de la derniere modification : 26-04-2026
 * Description :
 *   Contrôleur de l'écran de jeu (JeuView.fxml).
 *   Gère l'affichage du plateau, des pions, du dé, des tours de jeu et des évènements.
 *   Le modèle sous-jacent (Grille, Mecanisme) est conservé du TP1.
 */


package csi.tp2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.Optional;
import java.util.Random;





public class JeuController {

    /** ImageView de l'image de fond du plateau */
    @FXML private ImageView imagePlateau;
    /** Label indiquant à qui est le tour (Joueur ou Ordinateur) */
    @FXML private Label labelTour;
    /** ImageView affichant la face actuelle du dé */
    @FXML private ImageView imageDe;
    /** Label affichant le nom du gagnant en fin de partie (caché sinon) */
    @FXML private Label labelGagnant;
    /** Conteneur principal regroupant l'image de fond et la grille */
    @FXML private StackPane conteneurPlateau;
    /** Grille 10×10 pour positionner les pions */
    @FXML private GridPane grid;

    /** MenuItem pour recommencer une partie depuis la barre de menu */
    @FXML private MenuItem menuRecommencer;
    /** MenuItem pour quitter la partie et revenir à l'accueil depuis la barre de menu */
    @FXML private MenuItem menuQuitter;

    // Modèle du jeu
    private Grille grille;
    private Mecanisme mecanisme;
    private Random aleatoire = new Random();

    /** Position actuelle du pion du joueur (0 = pas encore entré sur le plateau) */
    private int positionJoueur = 0;
    /** Position actuelle du pion de l'ordinateur */
    private int positionOrdinateur = 0;

    /** ImageView représentant le pion du joueur */
    private ImageView pionJoueur;
    /** ImageView représentant le pion de l'ordinateur */
    private ImageView pionOrdinateur;

    /** true = tour du joueur, false = tour de l'ordinateur */
    private boolean tourJoueur = true;
    /** true quand la partie est terminée (un gagnant est déclaré) */
    private boolean partieTerminee = false;

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX après le chargement du FXML.
     * Prépare le modèle, charge les images, place les pions et masque le label du gagnant.
     */
    @FXML
    private void initialize() {
        grille = new Grille();
        mecanisme = new Mecanisme(grille);

        // Image du plateau
        try {
            Image plateau = new Image(getClass().getResourceAsStream("/images/plateau.png"));
            imagePlateau.setImage(plateau);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image /images/plateau.png");
        }

        // Le conteneur s'adapte à la taille de l'image
        conteneurPlateau.prefWidthProperty().bind(imagePlateau.fitWidthProperty());
        conteneurPlateau.prefHeightProperty().bind(imagePlateau.fitHeightProperty());

        // Pion du joueur
        try {
            Image imgJoueur = new Image(getClass().getResourceAsStream("/images/pionJoueur.png"));
            pionJoueur = new ImageView(imgJoueur);
            pionJoueur.setFitWidth(40);
            pionJoueur.setPreserveRatio(true);
        } catch (Exception e) {
            pionJoueur = new ImageView();
        }

        // Pion de l'ordinateur
        try {
            Image imgOrdi = new Image(getClass().getResourceAsStream("/images/pionOrdinateur.png"));
            pionOrdinateur = new ImageView(imgOrdi);
            pionOrdinateur.setFitWidth(40);
            pionOrdinateur.setPreserveRatio(true);
        } catch (Exception e) {
            pionOrdinateur = new ImageView();
        }

        grid.getChildren().addAll(pionJoueur, pionOrdinateur);

        afficherFaceDe(1);
        mettreAJourAffichage();

        labelGagnant.setVisible(false); // masqué tant qu'il n'y a pas de gagnant
    }

    /**
     * Affiche la face du dé correspondant à la valeur donnée (1 à 6).
     * Charge l'image depuis le dossier /images/.
     *
     * @param valeur valeur du dé
     */
    private void afficherFaceDe(int valeur) {
        String chemin = "/images/de" + valeur + ".png";
        try {
            Image face = new Image(getClass().getResourceAsStream(chemin));
            imageDe.setImage(face);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image " + chemin);
        }
    }

    /**
     * Action déclenchée par le bouton "Lancer le dé".
     * Génère une valeur aléatoire, déplace le joueur actif, vérifie les conditions de victoire
     * et gère l'alternance des tours (y compris le tour automatique de l'ordinateur).
     */
    @FXML
    private void lancerDe() {
        if (partieTerminee) return;

        int de = aleatoire.nextInt(6) + 1;
        afficherFaceDe(de);

        if (tourJoueur) {
            positionJoueur = mecanisme.deplacer(positionJoueur, de, positionOrdinateur, "Joueur");
            positionJoueur = mecanisme.verifierMecanisme(positionJoueur, "Joueur");
            deplacerPion(pionJoueur, positionJoueur);

            if (positionJoueur >= 100) {
                partieTerminee = true;
                labelGagnant.setText("Joueur gagne !");
                labelGagnant.setVisible(true);
                return;
            }

            tourJoueur = false;
            labelTour.setText("Tour de l'ordinateur");
            jouerTourOrdinateur();
        } else {
            positionOrdinateur = mecanisme.deplacer(positionOrdinateur, de, positionJoueur, "Ordinateur");
            positionOrdinateur = mecanisme.verifierMecanisme(positionOrdinateur, "Ordinateur");
            deplacerPion(pionOrdinateur, positionOrdinateur);

            if (positionOrdinateur >= 100) {
                partieTerminee = true;
                labelGagnant.setText("Ordinateur gagne !");
                labelGagnant.setVisible(true);
                return;
            }

            tourJoueur = true;
            labelTour.setText("Tour du joueur");
        }

        mettreAJourAffichage();
    }

    /**
     * Lance le tour de l'ordinateur avec un délai de 800 ms pour un effet visuel.
     * Le tour est exécuté sur un thread séparé puis rapatrié sur le thread JavaFX.
     */
    private void jouerTourOrdinateur() {
        if (partieTerminee || tourJoueur) return;

        new Thread(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(this::lancerDe);
        }).start();
    }

    /**
     * Place un pion sur la grille à la position donnée (1 à 100).
     * La position 0 rend le pion invisible (pas encore sur le plateau).
     * Le calcul tient compte du parcours en zigzag classique du jeu.
     *
     * @param pion     l'ImageView du pion à déplacer
     * @param position position sur le plateau (1 à 100, 0 = hors plateau)
     */
    private void deplacerPion(ImageView pion, int position) {
        if (position == 0) {
            pion.setVisible(false);
            return;
        }
        pion.setVisible(true);

        int ligne, colonne;
        if (position == 100) {
            ligne = 0;
            colonne = 0;
        } else {
            ligne = (position - 1) / 10;               // 0 = bas du plateau
            int posDansLigne = (position - 1) % 10;
            if (ligne % 2 == 0) {
                colonne = posDansLigne;                // ligne paire : de gauche à droite
            } else {
                colonne = 9 - posDansLigne;            // ligne impaire : de droite à gauche
            }
        }
        ligne = 9 - ligne; // inversion axe vertical pour correspondre au GridPane (0 = haut)

        GridPane.setColumnIndex(pion, colonne);
        GridPane.setRowIndex(pion, ligne);
    }

    /**
     * Met à jour l'affichage des deux pions selon leurs positions actuelles.
     */
    private void mettreAJourAffichage() {
        deplacerPion(pionJoueur, positionJoueur);
        deplacerPion(pionOrdinateur, positionOrdinateur);
    }

    /**
     * Réinitialise tous les paramètres pour recommencer une nouvelle partie.
     */
    @FXML
    private void recommencerPartie() {
        positionJoueur = 0;
        positionOrdinateur = 0;
        tourJoueur = true;
        partieTerminee = false;
        labelGagnant.setText("");
        labelGagnant.setVisible(false);
        afficherFaceDe(1);
        labelTour.setText("Tour du joueur");
        mettreAJourAffichage();
    }

    /**
     * Action "Recommencer" depuis la barre de menu.
     * Affiche une boîte de confirmation avant de réinitialiser la partie.
     */
    @FXML
    private void handleRecommencer() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Recommencer la partie");
        confirmation.setHeaderText("Voulez-vous vraiment recommencer la partie ?");
        confirmation.setContentText("Toute progression sera perdue.");

        Optional<ButtonType> resultat = confirmation.showAndWait();
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            recommencerPartie();
        }
    }

    /**
     * Action "Quitter" depuis la barre de menu.
     * Propose une confirmation avant de retourner à l'écran d'accueil.
     */
    @FXML
    private void handleQuitterPartie() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Quitter la partie");
        confirmation.setHeaderText("Voulez-vous vraiment quitter la partie ?");
        confirmation.setContentText("Vous retournerez à l'écran d'accueil.");

        Optional<ButtonType> resultat = confirmation.showAndWait();
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            retournerAccueil();
        }
    }

    /**
     * Retourne à l'écran d'accueil en utilisant l'instance du HelloController principal.
     */
    private void retournerAccueil() {
        labelGagnant.setVisible(false);
        HelloController.getInstance().handleAccueil();
    }

    /**
     * Méthode conservée (peut être appelée depuis d'anciens boutons) qui renvoie vers l'accueil.
     */
    @FXML
    private void quitterVersAccueil() {
        retournerAccueil();
    }
}