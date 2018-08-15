package se.generaliobot.copter;

import org.junit.Assert;
import org.junit.Test;
import pl.joegreen.sergeants.simulator.GameMap;
import pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener;
import pl.joegreen.sergeants.simulator.Simulator;
import pl.joegreen.sergeants.simulator.SimulatorFactory;
import se.generaliobot.bamse.Bamse;

import java.util.Optional;

public class DrampVsBamse {
  @Test
  public void runGame() {
    GameMap playerMap = SimulatorFactory.createMapFromReplayFile("../test.json");
    Simulator of = SimulatorFactory.of(playerMap, 400, Bamse.provider(new se.generaliobot.bamse.config.Config()), se.generaliobot.dramp.Dramp.provider());
    of.getListeners().add(new SaveHistorySimulatorListener("../"));
    Optional<Integer> start = of.start();
    System.out.println(start);
    Assert.assertEquals(Optional.of(1), start);
  }
}
