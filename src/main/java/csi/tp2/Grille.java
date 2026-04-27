

/****************************************************************************************************
 * Nom et prénom : HOUNSOU Césaire                                                                   *
 * Nom et prénom de l'enseignant : François Picard Légaré                                            *
 * Date de la derniere modification : 26-04-2026                                                     *
 * Description:                                                                                      *
 *   Représente la grille de jeu du Serpents et Échelles.                                            *
 *   Elle définit les positions de départ et d'arrivée des échelles et des serpents.                 *
 *   Ces données sont utilisées par {@link Mecanisme} pour appliquer les déplacements spéciaux.      *
 ****************************************************************************************************/


package csi.tp2;

import java.util.HashMap;
import java.util.Map;



public class Grille {

    /**
     * Associe une case de départ (clé) à une case d'arrivée (valeur) pour les échelles.
     * Les échelles font monter le joueur.
     */
    private Map<Integer, Integer> echelles = new HashMap<>();

    /**
     * Associe une case de départ (tête du serpent, clé) à une case d'arrivée (queue, valeur).
     * Les serpents font redescendre le joueur.
     */
    private Map<Integer, Integer> serpents = new HashMap<>();

    /**
     * Constructeur qui initialise les emplacements des 7 échelles et 6 serpents.
     */
    public Grille() {

        // ÉCHELLES (bas → haut)
        echelles.put(5, 25);
        echelles.put(12, 31);
        echelles.put(20, 42);
        echelles.put(33, 66);
        echelles.put(57, 96);
        echelles.put(62, 81);
        echelles.put(72, 93);

        // SERPENTS (tête → queue)
        serpents.put(50, 4);
        serpents.put(26, 9);
        serpents.put(75, 18);
        serpents.put(63, 35);
        serpents.put(98, 38);
        serpents.put(87, 49);
    }

    /**
     * Vérifie si la case donnée correspond au bas d'une échelle.
     *
     * @param position la case à tester (de 1 à 100)
     * @return {@code true} si une échelle part de cette case
     */
    public boolean estEchelle(int position) {
        return echelles.containsKey(position);
    }

    /**
     * Retourne la case d'arrivée (sommet) d'une échelle.
     *
     * @param position la case de départ de l'échelle
     * @return le numéro de la case d'arrivée
     */
    public int destinationEchelle(int position) {
        return echelles.get(position);
    }

    /**
     * Vérifie si la case donnée contient une tête de serpent.
     *
     * @param position la case à tester (de 1 à 100)
     * @return {@code true} si un serpent a sa tête sur cette case
     */
    public boolean estSerpent(int position) {
        return serpents.containsKey(position);
    }

    /**
     * Retourne la case d'arrivée (queue) d'un serpent.
     *
     * @param position la case de la tête du serpent
     * @return le numéro de la case de la queue
     */
    public int destinationSerpent(int position) {
        return serpents.get(position);
    }
}