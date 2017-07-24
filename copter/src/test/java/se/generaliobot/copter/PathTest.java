package se.generaliobot.copter;

import org.junit.Test;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;

import java.util.Optional;
import java.util.function.Function;

public class PathTest {
    @Test
    public void runPathTest() {
        GameMap playerMap = SimulatorFactory.createMapFromReplayFile(getClass().getResource("/squaremap.json").getFile());
//        GameMap playerMap = SimulatorFactory.createMapFromReplayFile(getClass().getResource("/rectanglemap.json").getFile());
//        playerMap.tiles[0].moveTo(50, 1, playerMap.tiles);

        Simulator of = SimulatorFactory.of(playerMap, 400, doNothingBot(), Copter.provider());
        of.getListeners().add(new SaveHistorySimulatorListener("../"));
        Optional<Integer> start = of.start();
        System.out.println(start);
//        Assert.assertEquals(Optional.of(1), start);
    }

    private Function<Actions, Bot> doNothingBot() {
        return actions -> new Bot() {
        };
    }
}
