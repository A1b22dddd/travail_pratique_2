package csi.tp2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import java.util.Random;

/**
 * Contrôleur de l'écran de jeu.
 * Utilise une image de fond pour le plateau.
 */
public class JeuController {

    // ================== ÉLÉMENTS DE L'INTERFACE ==================
    @FXML private ImageView imagePlateau;      // L'image du plateau de jeu
    @FXML private Pane panePions;              // Conteneur transparent pour les pions
    @FXML private Label labelTour;             // Affiche le tour actuel
    @FXML private ImageView imageDe;           // Affiche l'image du dé (remplace labelDe)
    @FXML private Label labelGagnant;          // Affiche le gagnant
    @FXML private StackPane conteneurPlateau;  // Conteneur principal (pour redimensionnement)

    // ================== MODÈLE ==================
    private Grille grille;
    private Mecanisme mecanisme;
    private Random aleatoire = new Random();

    private int positionJoueur = 0;
    private int positionOrdinateur = 0;

    // Pions temporaires (cercles)
    private Circle pionJoueur;
    private Circle pionOrdinateur;

    private boolean tourJoueur = true;
    private boolean partieTerminee = false;

    // Taille de référence de l'image (à adapter selon votre image)
    private static final double LARGEUR_PLATEAU = 600;
    private static final double HAUTEUR_PLATEAU = 600;

    // ================== INITIALISATION ==================
    @FXML
    private void initialiser() {
        grille = new Grille();
        mecanisme = new Mecanisme(grille);

        // Charger l'image du plateau
        try {
            Image plateau = new Image(getClass().getResourceAsStream("/images/plateau.png"));
            imagePlateau.setImage(plateau);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image /images/plateau.png");
        }

        //  AJOUT : empêcher la déformation du plateau
        conteneurPlateau.prefWidthProperty().bind(imagePlateau.fitWidthProperty());
        conteneurPlateau.prefHeightProperty().bind(imagePlateau.fitHeightProperty());
        conteneurPlateau.maxWidthProperty().bind(imagePlateau.fitWidthProperty());
        conteneurPlateau.maxHeightProperty().bind(imagePlateau.fitHeightProperty());

        panePions.prefWidthProperty().bind(imagePlateau.fitWidthProperty());
        panePions.prefHeightProperty().bind(imagePlateau.fitHeightProperty());
        panePions.maxWidthProperty().bind(imagePlateau.fitWidthProperty());
        panePions.maxHeightProperty().bind(imagePlateau.fitHeightProperty());
        //  FIN AJOUT

        // Créer les pions (cercles colorés)
        pionJoueur = new Circle(15, javafx.scene.paint.Color.BLUE);
        pionOrdinateur = new Circle(15, javafx.scene.paint.Color.RED);
        panePions.getChildren().addAll(pionJoueur, pionOrdinateur);

        // Adapter la taille du panePions à l'image affichée
        conteneurPlateau.layoutBoundsProperty().addListener((obs, anc, nouv) -> {
            double largeurImage = imagePlateau.getBoundsInParent().getWidth();
            double hauteurImage = imagePlateau.getBoundsInParent().getHeight();
            panePions.setPrefWidth(largeurImage);
            panePions.setPrefHeight(hauteurImage);
            mettreAJourAffichage();
        });

        // Afficher la face 1 par défaut
        afficherFaceDe(1);

        mettreAJourAffichage();
    }

    // ================== GESTION DES IMAGES DU DÉ ==================
    /**
     * Charge et affiche l'image correspondant à la valeur du dé.
     * Les images doivent être nommées de1.png, de2.png, ..., de6.png
     * et placées dans src/main/resources/images/
     */
    private void afficherFaceDe(int valeur) {
        try {
            String chemin = "/images/de" + valeur + ".png";
            Image face = new Image(getClass().getResourceAsStream(chemin));
            imageDe.setImage(face);
        } catch (Exception e) {
            System.err.println("Erreur : impossible de charger l'image " + "/images/de" + valeur + ".png");
        }
    }

    // ================== LANCER LE DÉ ==================
    @FXML
    private void lancerDe() {
        if (partieTerminee) return;

        int de = aleatoire.nextInt(6) + 1;
        afficherFaceDe(de);   // Affiche l'image

        if (tourJoueur) {
            positionJoueur = mecanisme.deplacer(positionJoueur, de, positionOrdinateur, "Joueur");
            positionJoueur = mecanisme.verifierMecanisme(positionJoueur, "Joueur");
            deplacerPion(pionJoueur, positionJoueur);

            if (positionJoueur >= 100) {
                partieTerminee = true;
                labelGagnant.setText("Joueur gagne !");
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
                return;
            }
            tourJoueur = true;
            labelTour.setText("Tour du joueur");
        }
        mettreAJourAffichage();
    }

    private void jouerTourOrdinateur() {
        if (partieTerminee || tourJoueur) return;
        new Thread(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
            javafx.application.Platform.runLater(() -> lancerDe());
        }).start();
    }

    // ================== DÉPLACEMENT DES PIONS ==================
    private void deplacerPion(Circle pion, int position) {
        if (position == 0) {
            pion.setVisible(false);
            return;
        }
        pion.setVisible(true);

        double largeurCase = imagePlateau.getBoundsInParent().getWidth() / 10;
        double hauteurCase = imagePlateau.getBoundsInParent().getHeight() / 10;

        int ligne, colonne;
        if (position == 100) {
            ligne = 0;
            colonne = 0;
        } else {
            ligne = (position - 1) / 10;
            int posDansLigne = (position - 1) % 10;
            if (ligne % 2 == 0) {
                colonne = posDansLigne;
            } else {
                colonne = 9 - posDansLigne;
            }
        }

        double centreX = colonne * largeurCase + largeurCase / 2;
        double centreY = (9 - ligne) * hauteurCase + hauteurCase / 2;

        pion.setCenterX(centreX);
        pion.setCenterY(centreY);
    }

    private void mettreAJourAffichage() {
        deplacerPion(pionJoueur, positionJoueur);
        deplacerPion(pionOrdinateur, positionOrdinateur);
    }

    // ================== RECOMMENCER ==================
    @FXML
    private void recommencerPartie() {
        positionJoueur = 0;
        positionOrdinateur = 0;
        tourJoueur = true;
        partieTerminee = false;
        labelGagnant.setText("");
        afficherFaceDe(1);   // Remet l'image du dé à 1
        labelTour.setText("Tour du joueur");
        mettreAJourAffichage();
    }

    @FXML
    private void quitterVersAccueil() {
        // À relier plus tard si besoin
    }
}