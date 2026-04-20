package csi.tp2;

import java.util.HashMap;
import java.util.Map;

public class Grille {

    private Map<Integer, Integer> echelles = new HashMap<>();
    private Map<Integer, Integer> serpents = new HashMap<>();

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

    public boolean estEchelle(int position) {
        return echelles.containsKey(position);
    }

    public int destinationEchelle(int position) {
        return echelles.get(position);
    }

    public boolean estSerpent(int position) {
        return serpents.containsKey(position);
    }

    public int destinationSerpent(int position) {
        return serpents.get(position);
    }
}
