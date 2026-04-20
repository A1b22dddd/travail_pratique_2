package csi.tp2;

public class Mecanisme {

    private Grille grille;

    public Mecanisme(Grille grille) {
        this.grille = grille;
    }

    public int deplacer(int position, int de, int positionAdverse, String nom) {
        int nouvellePosition = position + de;
        if (nouvellePosition > 100) nouvellePosition = 100;

        while (nouvellePosition == positionAdverse && nouvellePosition < 100) {
            nouvellePosition++;
        }

        System.out.println(nom + " se déplace de " + position + " à " + nouvellePosition);
        return nouvellePosition;
    }

    public int verifierMecanisme(int position, String nom) {
        if (grille.estEchelle(position)) {
            int destination = grille.destinationEchelle(position);
            System.out.println("[Échelle] " + nom + " monte de " + position + " à " + destination);
            return destination;
        }
        if (grille.estSerpent(position)) {
            int destination = grille.destinationSerpent(position);
            System.out.println("[Serpent] " + nom + " descend de " + position + " à " + destination);
            return destination;
        }
        return position;
    }
}
