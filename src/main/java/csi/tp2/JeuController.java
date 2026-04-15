package csi.tp2;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import java.util.Random;

/**
 * Contrôleur de l'écran de jeu.
 * Gère la grille, les pions, le dé et la logique du jeu.
 */
public class JeuController {

    // ================== ÉLÉMENTS DE L'INTERFACE ==================
    @FXML private GridPane grillePane;        // La grille de 10x10 cases
    @FXML private Pane panePions;             // Le conteneur transparent pour les pions
    @FXML private Label labelTour;            // Affiche "Tour du joueur" ou "Tour de l'ordinateur"
    @FXML private Label labelDe;              // Affiche le résultat du dé
    @FXML private Label labelGagnant;         // Affiche le gagnant en fin de partie
    @FXML private StackPane conteneurPlateau; // Conteneur principal du plateau

    // ================== MODÈLE ==================
    private Grille grille;                    // La grille du jeu (données)
    private Mecanisme mecanisme;              // Gère les déplacements et mécanismes
    private Random aleatoire = new Random();  // Générateur de nombres aléatoires pour le dé

    // Positions des joueurs (0 = pas encore sur le plateau)
    private int positionJoueur = 0;
    private int positionOrdinateur = 0;

    // Pions temporaires (cercles colorés)
    private Circle pionJoueur;
    private Circle pionOrdinateur;

    // Gestion du tour et de l'état de la partie
    private boolean tourJoueur = true;        // true = joueur, false = ordinateur
    private boolean partieTerminee = false;

    // ================== INITIALISATION ==================
    @FXML
    private void initialiser() {
        // Créer les objets du modèle
        grille = new Grille();
        mecanisme = new Mecanisme(grille);

        // Construire la grille visuelle
        creerGrille();

        // Créer les pions temporaires (cercles bleu et rouge)
        pionJoueur = new Circle(15, javafx.scene.paint.Color.BLUE);
        pionOrdinateur = new Circle(15, javafx.scene.paint.Color.RED);
        panePions.getChildren().addAll(pionJoueur, pionOrdinateur);

        // Écouter les changements de taille du plateau pour adapter le pane des pions
        conteneurPlateau.layoutBoundsProperty().addListener((obs, ancValeur, nouvValeur) -> {
            panePions.setPrefWidth(nouvValeur.getWidth());
            panePions.setPrefHeight(nouvValeur.getHeight());
            mettreAJourAffichage();
        });

        // Afficher les positions initiales
        mettreAJourAffichage();
    }

    /**
     * Construit la grille 10x10 avec les numéros de 1 à 100 en zigzag.
     * La ligne 0 est en haut, la ligne 9 en bas.
     * Les numéros commencent en bas à gauche et montent en zigzag.
     */
    private void creerGrille() {
        grillePane.getChildren().clear();
        grillePane.setGridLinesVisible(true); // Affiche les bordures des cases
        // Taille fixe pour que la grille soit visible (500x500 pixels)
        grillePane.setPrefSize(500, 500);

        int numero = 1;
        for (int ligne = 0; ligne < 10; ligne++) {
            if (ligne % 2 == 0) {
                // Ligne paire : remplir de gauche à droite
                for (int col = 0; col < 10; col++) {
                    ajouterCase(numero++, col, ligne);
                }
            } else {
                // Ligne impaire : remplir de droite à gauche
                for (int col = 9; col >= 0; col--) {
                    ajouterCase(numero++, col, ligne);
                }
            }
        }
    }

    /**
     * Ajoute une case numérotée dans le GridPane.
     */
    private void ajouterCase(int numero, int colonne, int ligne) {
        Label etiquette = new Label(String.valueOf(numero));
        etiquette.setPrefSize(50, 50);
        etiquette.setAlignment(Pos.CENTER);
        etiquette.setStyle("-fx-border-color: black; -fx-background-color: beige;");
        grillePane.add(etiquette, colonne, ligne);
    }

    /**
     * Retrouve un Label dans le GridPane à partir de ses indices colonne/ligne.
     */
    private Label obtenirNoeudDeGrillePane(int colonne, int ligne) {
        for (Node noeud : grillePane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(noeud);
            Integer ligIndex = GridPane.getRowIndex(noeud);
            if (colIndex != null && ligIndex != null && colIndex == colonne && ligIndex == ligne) {
                return (Label) noeud;
            }
        }
        return null;
    }

    // ================== LANCER LE DÉ ==================
    @FXML
    private void lancerDe() {
        if (partieTerminee) return; // Partie finie, on ne fait rien

        int de = aleatoire.nextInt(6) + 1;
        labelDe.setText("Dé : " + de);

        if (tourJoueur) {
            // Tour du joueur
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
            jouerTourOrdinateur(); // L'ordinateur joue automatiquement après un délai
        } else {
            // Tour de l'ordinateur
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

    /**
     * Lance le tour de l'ordinateur après un court délai.
     */
    private void jouerTourOrdinateur() {
        if (partieTerminee || tourJoueur) return;
        new Thread(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
            javafx.application.Platform.runLater(() -> lancerDe());
        }).start();
    }

    // ================== DÉPLACEMENT DES PIONS ==================
    /**
     * Place un pion (cercle) sur la case correspondant à sa position.
     */
    private void deplacerPion(Circle pion, int position) {
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
            ligne = (position - 1) / 10;           // 0 = cases 1-10, 1 = 11-20, ...
            int posDansLigne = (position - 1) % 10;
            if (ligne % 2 == 0) {
                colonne = posDansLigne;            // Ligne paire : gauche → droite
            } else {
                colonne = 9 - posDansLigne;        // Ligne impaire : droite → gauche
            }
        }

        Label caseEtiquette = obtenirNoeudDeGrillePane(colonne, ligne);
        if (caseEtiquette == null) return;

        // Convertir les coordonnées de la case dans le repère du panePions
        Bounds limitesScene = caseEtiquette.localToScene(caseEtiquette.getBoundsInLocal());
        Bounds limitesPane = panePions.sceneToLocal(limitesScene);

        double centreX = limitesPane.getMinX() + limitesPane.getWidth() / 2;
        double centreY = limitesPane.getMinY() + limitesPane.getHeight() / 2;

        pion.setCenterX(centreX);
        pion.setCenterY(centreY);
    }

    /**
     * Met à jour l'affichage des deux pions.
     */
    private void mettreAJourAffichage() {
        deplacerPion(pionJoueur, positionJoueur);
        deplacerPion(pionOrdinateur, positionOrdinateur);
    }

    // ================== RECOMMENCER / QUITTER ==================
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
        // À connecter plus tard au contrôleur principal si nécessaire
    }
}