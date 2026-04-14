package csi.tp2;

import java.util.HashMap;

/**
 * Représente la grille du jeu.
 */
public class Grille {

    private int[][] grille;
    private HashMap<Integer, Integer> echelles = new HashMap<>();
    private HashMap<Integer, Integer> serpents = new HashMap<>();
    private HashMap<Integer, String> noms = new HashMap<>();

    public Grille() {
        grille = new int[10][10];
        initialiserGrille();
        initialiserMecanismes();
    }

    private void initialiserGrille() {
        int numero = 1;
        for (int ligne = 9; ligne >= 0; ligne--) {
            if ((9 - ligne) % 2 == 0) {
                for (int colonne = 0; colonne < 10; colonne++)
                    grille[ligne][colonne] = numero++;
            } else {
                for (int colonne = 9; colonne >= 0; colonne--)
                    grille[ligne][colonne] = numero++;
            }
        }
    }

    private void initialiserMecanismes() {
        echelles.put(3, 18);
        noms.put(3, "e1");
        noms.put(18, "e1");

        echelles.put(20, 35);
        noms.put(20, "e2");
        noms.put(35, "e2");

        serpents.put(50, 30);
        noms.put(50, "s1");
        noms.put(30, "s1");

        serpents.put(80, 60);
        noms.put(80, "s2");
        noms.put(60, "s2");
    }

    public int[][] obtenirGrille() { return grille; }

    public boolean estEchelleBas(int pos) { return echelles.containsKey(pos); }
    public boolean estEchelleHaut(int pos) { return echelles.containsValue(pos); }
    public boolean estSerpentHaut(int pos) { return serpents.containsKey(pos); }
    public boolean estSerpentBas(int pos) { return serpents.containsValue(pos); }

    public String getNom(int pos) { return noms.get(pos); }

    public boolean estEchelle(int pos) { return echelles.containsKey(pos); }
    public boolean estSerpent(int pos) { return serpents.containsKey(pos); }

    public int destinationEchelle(int pos) { return echelles.get(pos); }
    public int destinationSerpent(int pos) { return serpents.get(pos); }
}