package se.nosslin579.trainer;

import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.nosslin579.aardvark.Aardvark;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile("test.json");
        Simulator of = SimulatorFactory.of(playerMap, DNBot::new, Aardvark::new);
        of.setMaxTurns(200);
        of.getListeners().add(new SaveHistorySimulatorListener());
        Optional<Integer> start = of.start();
        System.out.println(start);

    }

    private static class DNBot implements Bot {
        public DNBot(Actions actions) {
        }

        @Override
        public void onGameStateUpdate(GameState newGameState) {

        }

        @Override
        public void onGameFinished(GameResult gameResult) {

        }
    }
}
