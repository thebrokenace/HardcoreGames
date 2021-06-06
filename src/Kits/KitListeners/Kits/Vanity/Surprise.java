package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.Kits;

import java.util.Random;

public class Surprise {


    public static Kits getRandomKit () {
        Kits kits = Kits.SURPRISE;
        int pick;
        while (kits == Kits.SURPRISE || kits == Kits.NONE) {
            pick = new Random(System.currentTimeMillis()).nextInt(Kits.values().length);
            kits = Kits.values()[pick];
        }

        return kits;
    }
}
