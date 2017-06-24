package se.generaliobot.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.generaliobot.aardvark.Aardvark;
import se.generaliobot.bamse.config.Config;

import java.util.Optional;

public class BamseOfflineStarter {
    private final static Logger log = LoggerFactory.getLogger(BamseOfflineStarter.class);

    public static void main(String[] args) {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile("test.json");
        Config c2 = new Config();
        Simulator of = SimulatorFactory.of(playerMap, 400, Aardvark.provider(new se.generaliobot.aardvark.config.Config()), Bamse.provider(c2));
        of.getListeners().add(new SaveHistorySimulatorListener());
        Optional<Integer> start = of.start();
        System.out.println(start);
    }
}
