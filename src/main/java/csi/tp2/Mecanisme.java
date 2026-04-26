

/**
 * Nom et prénom : HOUNSOU Césaire
 * Nom et prénom de l'enseignant : François Picard Légaré
 * Date de la derniere modification : 26-04-2026
 * Description :
 *   Gère les déplacements des joueurs et applique les effets des échelles et des serpents.
 *   S'appuie sur une instance de {@link Grille} pour connaître les cases spéciales.
 */


package csi.tp2;

public class Mecanisme {

    /** Grille de référence pour consulter les échelles et les serpents */
    private Grille grille;

    /**
     * Construit un mécanisme lié à une grille.
     *
     * @param grille la grille contenant les échelles et les serpents
     */
    public Mecanisme(Grille grille) {
        this.grille = grille;
    }

    /**
     * Déplace un joueur d'un nombre de cases égal à la valeur du dé.
     * Si la nouvelle case est déjà occupée par l'adversaire, on avance d'une case
     * supplémentaire. La position ne dépasse jamais 100.
     *
     * @param position         position actuelle du joueur (0 à 100, 0 = avant le plateau)
     * @param de               valeur du dé (1 à 6)
     * @param positionAdverse  position de l'adversaire (pour éviter la superposition)
     * @param nom              nom du joueur (utilisé pour les messages console)
     * @return la nouvelle position après le déplacement (sans les effets échelle/serpent)
     */
    public int deplacer(int position, int de, int positionAdverse, String nom) {
        int nouvellePosition = position + de;
        if (nouvellePosition > 100) nouvellePosition = 100;

        // Évite de partager la même case que l'adversaire
        while (nouvellePosition == positionAdverse && nouvellePosition < 100) {
            nouvellePosition++;
        }

        System.out.println(nom + " se déplace de " + position + " à " + nouvellePosition);
        return nouvellePosition;
    }

    /**
     * Vérifie si le joueur se trouve sur une case spéciale (échelle ou serpent)
     * et applique l'effet correspondant. Si aucune particularité, la position reste inchangée.
     *
     * @param position la position à vérifier (après le déplacement normal)
     * @param nom      nom du joueur (pour l'affichage console)
     * @return la position finale après application éventuelle d'une échelle ou d'un serpent
     */
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