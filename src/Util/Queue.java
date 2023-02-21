package Util;

import Main.Config;

public class Queue {
    static Config config = new Config();
    public static int timeLeft (int currentPlayers) {
        int minPlayers = config.minPlayers(); //2
        int maxPlayers = config.maxPlayers(); //8

        if ((double) currentPlayers/maxPlayers < 0.50 && currentPlayers >= minPlayers && !tier2(currentPlayers) && !tier3(currentPlayers) && !tier4(currentPlayers)) {
            //tier1
            return tier1Time();


        }
        if ((double) currentPlayers/maxPlayers >= 0.50 && currentPlayers >= minPlayers && !tier3(currentPlayers) && !tier4(currentPlayers) && !tier1(currentPlayers)) {
            //tier2

            return tier2Time();


        }
        if ((double) currentPlayers/maxPlayers >= 0.75 && currentPlayers >= minPlayers  && !tier1(currentPlayers) && !tier4(currentPlayers)) {
            //tier3

            return tier3Time();
        }

        if ((double) currentPlayers/maxPlayers >= 0.99 && currentPlayers >= minPlayers) {
            //tier4

            return tier4Time();
        }


        return 0;
    }

    public static boolean tier1 (int currentPlayers) {
        int minPlayers = config.minPlayers(); //2
        int maxPlayers = config.maxPlayers(); //8

        if ((double) currentPlayers/maxPlayers < 0.50 && currentPlayers >= minPlayers && !tier2(currentPlayers) && !tier3(currentPlayers) && !tier4(currentPlayers)) {
            //
            return true;


        }
        return false;
    }

    public static boolean tier2 (int currentPlayers) {
        int minPlayers = config.minPlayers(); //2
        int maxPlayers = config.maxPlayers(); //8

        if ((double) currentPlayers/maxPlayers >= 0.50 && currentPlayers >= minPlayers && !tier3(currentPlayers) && !tier4(currentPlayers) && !tier1(currentPlayers)) {
            //

            return true;


        }
        return false;
    }




    public static boolean tier3 (int currentPlayers) {
        int minPlayers = config.minPlayers(); //2
        int maxPlayers = config.maxPlayers(); //8

        if ((double) currentPlayers/maxPlayers >= 0.75 && currentPlayers >= minPlayers  && !tier1(currentPlayers) && !tier4(currentPlayers)) {
            //
            if (tier4(currentPlayers)) {
                return false;
            }
            return true;


        }
        return false;
    }

    //fix this and forcefield, make it so time only goes up / next forcefield closes when last one is done
    public static boolean tier4 (int currentPlayers) {
        int minPlayers = config.minPlayers(); //2
        int maxPlayers = config.maxPlayers(); //8

        if ((double) currentPlayers/maxPlayers >= 0.99 && currentPlayers >= minPlayers) {
            //

            return true;


        }
        return false;
    }

    public static int tier1Time () {
        return 600;
    }
    public static int tier2Time () {
        return 300;
    }
    public static int tier3Time () {
        return 120;
    }
    public static int tier4Time () {
        return 60;
    }
}
