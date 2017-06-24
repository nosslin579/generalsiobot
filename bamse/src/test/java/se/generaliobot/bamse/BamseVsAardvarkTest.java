package se.generaliobot.bamse;

import org.junit.Assert;
import org.junit.Test;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.generaliobot.aardvark.Aardvark;
import se.generaliobot.bamse.config.Config;

import java.util.Optional;

public class BamseVsAardvarkTest {

    @Test
    public void runGameBamseVsAardvark() {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile("../test.json");
        Simulator of = SimulatorFactory.of(playerMap, 400, Aardvark.provider(new se.generaliobot.aardvark.config.Config()), Bamse.provider(new Config()));
        of.getListeners().add(new SaveHistorySimulatorListener("../"));
        Optional<Integer> start = of.start();
        System.out.println(start);
        Assert.assertEquals(Optional.of(1), start);
    }
}
