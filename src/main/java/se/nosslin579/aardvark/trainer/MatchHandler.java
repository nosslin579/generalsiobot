package se.nosslin579.aardvark.trainer;

import se.nosslin579.aardvark.config.Config;

public class MatchHandler {

    static int pl1 = 1000;
    static int pl2 = 1000;


    public void match(Config player1, Config player2) {

    }


    private int getRatingChange(int winner, int loser) {
        double winOdds = 1.0 / (1 + Math.pow(10, (loser - winner) / 400f));
        int k = 25;
        return (int) Math.round(k * (1 - winOdds));
    }

    public static void main(String[] args) {
        MatchHandler matchHandler = new MatchHandler();
        double newScore = matchHandler.getRatingChange(987, 1013);
        System.out.println(newScore);
    }
}
