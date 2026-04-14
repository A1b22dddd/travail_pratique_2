package csi.tp2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.util.Random;

public class JeuController {

    @FXML private Pane panePions;
    @FXML private Label labelTour;
    @FXML private Label labelDe;
    @FXML private Label labelGagnant;

    private Grille grille;
    private Mecanisme mecanisme;
    private Random random = new Random();

    private int positionJoueur = 0;
    private int positionOrdinateur = 0;

    private Circle pionJoueur;
    private Circle pionOrdinateur;

    private boolean tourJoueur = true;
    private boolean partieTerminee = false;

    @FXML
    private void initialize() {
        grille = new Grille();
        mecanisme = new Mecanisme(grille);

        pionJoueur = new Circle(15, javafx.scene.paint.Color.BLUE);
        pionOrdinateur = new Circle(15, javafx.scene.paint.Color.RED);

        panePions.getChildren().addAll(pionJoueur, pionOrdinateur);
        mettreAJourAffichage();
    }

    @FXML
    private void lancerDe() {
        if (partieTerminee) return;

        int de = random.nextInt(6) + 1;
        labelDe.setText("Dé : " + de);

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

    private void deplacerPion(Circle pion, int position) {
        if (position == 0) {
            pion.setVisible(false);
            return;
        }
        pion.setVisible(true);

        int caseX, caseY;
        if (position == 100) {
            caseX = 0; caseY = 0;
        } else {
            int ligne = (position - 1) / 10;
            int colonne = (position - 1) % 10;
            if (ligne % 2 == 1) colonne = 9 - colonne;
            caseX = colonne * 50 + 25;
            caseY = (9 - ligne) * 50 + 25;
        }
        pion.setCenterX(caseX);
        pion.setCenterY(caseY);
    }

    private void mettreAJourAffichage() {
        deplacerPion(pionJoueur, positionJoueur);
        deplacerPion(pionOrdinateur, positionOrdinateur);
    }

    @FXML
    private void recommencerPartie() {
        positionJoueur = 0;
        positionOrdinateur = 0;
        tourJoueur = true;
        partieTerminee = false;
        labelGagnant.setText("");
        labelDe.setText("");
        labelTour.setText("Tour du joueur");
        mettreAJourAffichage();
    }

    @FXML
    private void quitterVersAccueil() {
        // Sera relié plus tard
    }
}