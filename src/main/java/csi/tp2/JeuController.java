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

    // Références aux éléments du FXML
    @FXML private ImageView imagePlateau;   // Image du plateau
    @FXML private Label labelTour;          // Indique le tour actuel
    @FXML private ImageView imageDe;        // Image du dé
    @FXML private Label labelGagnant;       // Affiche le gagnant (déjà présent)
    @FXML private StackPane conteneurPlateau; // Conteneur principal du plateau
    @FXML private GridPane grid;            // Grille 10x10 pour placer les pions

    // Nouveaux attributs pour la barre de menu
    @FXML private MenuItem menuRecommencer;
    @FXML private MenuItem menuQuitter;

    // Modèle du jeu
    private Grille grille;
    private Mecanisme mecanisme;
    private Random aleatoire = new Random();

    // Positions des joueurs (1 à 100)
    private int positionJoueur = 0;
    private int positionOrdinateur = 0;

    // Pions affichés sur la grille
    private ImageView pionJoueur;
    private ImageView pionOrdinateur;

    private boolean tourJoueur = true;
    private boolean partieTerminee = false;

    // Méthode appelée automatiquement par JavaFX au chargement du FXML
    @FXML
    private void initialize() {

        // Initialisation du modèle
        grille = new Grille();
        mecanisme = new Mecanisme(grille);

        // Chargement de l'image du plateau
        try {
            Image plateau = new Image(getClass().getResourceAsStream("/images/plateau.png"));
            imagePlateau.setImage(plateau);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image /images/plateau.png");
        }

        // Le plateau suit la taille de l'image
        conteneurPlateau.prefWidthProperty().bind(imagePlateau.fitWidthProperty());
        conteneurPlateau.prefHeightProperty().bind(imagePlateau.fitHeightProperty());

        // Chargement du pion du joueur
        try {
            Image imgJoueur = new Image(getClass().getResourceAsStream("/images/pionJoueur.png"));
            pionJoueur = new ImageView(imgJoueur);
            pionJoueur.setFitWidth(40);
            pionJoueur.setPreserveRatio(true);
        } catch (Exception e) {
            pionJoueur = new ImageView();
        }

        // Chargement du pion de l'ordinateur
        try {
            Image imgOrdi = new Image(getClass().getResourceAsStream("/images/pionOrdinateur.png"));
            pionOrdinateur = new ImageView(imgOrdi);
            pionOrdinateur.setFitWidth(40);
            pionOrdinateur.setPreserveRatio(true);
        } catch (Exception e) {
            pionOrdinateur = new ImageView();
        }

        // Ajout des pions dans la grille
        grid.getChildren().addAll(pionJoueur, pionOrdinateur);

        // Affiche la face 1 du dé au départ
        afficherFaceDe(1);

        // Place les pions selon leurs positions initiales
        mettreAJourAffichage();

        // Label gagnant caché au démarrage
        labelGagnant.setVisible(false);
    }

    // Affiche l'image correspondant à la valeur du dé
    private void afficherFaceDe(int valeur) {
        String chemin = "/images/de" + valeur + ".png";
        try {
            Image face = new Image(getClass().getResourceAsStream(chemin));
            imageDe.setImage(face);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image " + chemin);
        }
    }

    // Action du bouton "Lancer le dé"
    @FXML
    private void lancerDe() {
        if (partieTerminee) return;

        int de = aleatoire.nextInt(6) + 1;
        afficherFaceDe(de);

        if (tourJoueur) {

            // Déplacement du joueur
            positionJoueur = mecanisme.deplacer(positionJoueur, de, positionOrdinateur, "Joueur");
            positionJoueur = mecanisme.verifierMecanisme(positionJoueur, "Joueur");
            deplacerPion(pionJoueur, positionJoueur);

            // Vérifie la victoire
            if (positionJoueur >= 100) {
                partieTerminee = true;
                labelGagnant.setText("Joueur gagne !");
                labelGagnant.setVisible(true);
                return;
            }

            // Passage au tour de l'ordinateur
            tourJoueur = false;
            labelTour.setText("Tour de l'ordinateur");
            jouerTourOrdinateur();

        } else {

            // Déplacement de l'ordinateur
            positionOrdinateur = mecanisme.deplacer(positionOrdinateur, de, positionJoueur, "Ordinateur");
            positionOrdinateur = mecanisme.verifierMecanisme(positionOrdinateur, "Ordinateur");
            deplacerPion(pionOrdinateur, positionOrdinateur);

            // Vérifie la victoire
            if (positionOrdinateur >= 100) {
                partieTerminee = true;
                labelGagnant.setText("Ordinateur gagne !");
                labelGagnant.setVisible(true);
                return;
            }

            // Retour au joueur
            tourJoueur = true;
            labelTour.setText("Tour du joueur");
        }

        mettreAJourAffichage();
    }

    // L'ordinateur joue après un petit délai
    private void jouerTourOrdinateur() {
        if (partieTerminee || tourJoueur) return;

        new Thread(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(this::lancerDe);
        }).start();
    }

    // Déplace un pion sur la grille selon sa position (1 à 100)
    private void deplacerPion(ImageView pion, int position) {

        if (position == 0) {
            pion.setVisible(false);
            return;
        }

        pion.setVisible(true);

        int ligne, colonne;

        // Case 100 (coin supérieur gauche)
        if (position == 100) {
            ligne = 0;
            colonne = 0;
        } else {
            // Ligne de 0 à 9 (du bas vers le haut)
            ligne = (position - 1) / 10;

            // Position dans la ligne
            int posDansLigne = (position - 1) % 10;

            // Lignes paires → gauche vers droite
            // Lignes impaires → droite vers gauche
            if (ligne % 2 == 0) {
                colonne = posDansLigne;
            } else {
                colonne = 9 - posDansLigne;
            }
        }

        // Inversion car la grille commence en haut
        ligne = 9 - ligne;

        // Placement dans la GridPane
        GridPane.setColumnIndex(pion, colonne);
        GridPane.setRowIndex(pion, ligne);
    }

    // Met à jour l'affichage des deux pions
    private void mettreAJourAffichage() {
        deplacerPion(pionJoueur, positionJoueur);
        deplacerPion(pionOrdinateur, positionOrdinateur);
    }

    // Réinitialise la partie
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

    // Action « Recommencer » depuis la barre de menu (avec confirmation)
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

    // Action « Quitter » depuis la barre de menu (avec confirmation)
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

    // Retourne à l'écran d'accueil (nécessite HelloController.getInstance())
    private void retournerAccueil() {
        // On cache le label gagnant pour le prochain affichage
        labelGagnant.setVisible(false);
        HelloController.getInstance().handleAccueil();
    }

    // Ancienne méthode quitterVersAccueil conservée (appelle la nouvelle)
    @FXML
    private void quitterVersAccueil() {
        retournerAccueil();
    }
}