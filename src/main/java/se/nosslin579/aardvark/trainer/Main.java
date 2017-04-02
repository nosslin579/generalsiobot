package se.nosslin579.aardvark.trainer;

import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.nosslin579.aardvark.Aardvark;
import se.nosslin579.aardvark.config.Config;
import se.nosslin579.aardvark.config.Repo;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile("test.json");
        Repo repo = Repo.getInstance();
        Config c2 = repo.getConfig(1);
        Config c1 = repo.getConfig(2);
        Simulator of = SimulatorFactory.of(playerMap, Aardvark.provider(c1), Aardvark.provider(c2));
        of.setMaxTurns(400);
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
