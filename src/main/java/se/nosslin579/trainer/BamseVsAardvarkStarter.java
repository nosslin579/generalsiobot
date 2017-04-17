package se.nosslin579.trainer;

import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.nosslin579.aardvark.Aardvark;
import se.nosslin579.bamse.Bamse;
import se.nosslin579.bamse.config.Config;

import java.util.Optional;

public class BamseVsAardvarkStarter {
    public static void main(String[] args) {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile("test.json");
        Config c2 = new Config();
        Simulator of = SimulatorFactory.of(playerMap, 400, Aardvark.provider(new se.nosslin579.aardvark.config.Config()), Bamse.provider(c2));
        of.getListeners().add(new SaveHistorySimulatorListener());
        Optional<Integer> start = of.start();
        System.out.println(start);
    }
}
