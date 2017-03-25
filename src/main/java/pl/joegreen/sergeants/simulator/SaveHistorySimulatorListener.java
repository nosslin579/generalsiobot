package pl.joegreen.sergeants.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SaveHistorySimulatorListener implements SimulatorListener {
    private final Logger LOGGER = LoggerFactory.getLogger(pl.joegreen.sergeants.simulator.SaveHistorySimulatorListener.class);

    private ObjectMapper om = new ObjectMapper();
    private List<String> history = new ArrayList<>();
    private GameMap gameMap;

    @Override
    public void afterHalfTurn(int halfTurnCounter, Tile[] tiles) {
        try {
            history.add(om.writeValueAsString(tiles));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Write json failed: " + halfTurnCounter, e);
        }

    }

    @Override
    public void beforeGameStart(Player[] players, GameMap gameMap) {
        this.gameMap = gameMap;

    }

    @Override
    public void onGameEnd(Player winner) {
        saveGameAsJson();

    }

    @Override
    public void onGameAborted(Player[] players) {
        saveGameAsJson();
    }

    private void saveGameAsJson() {
        try {
            final File file = new File("replay-data.js");
            final PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("var generalIoReplay = {};");
            printWriter.println("window.generalIoReplay = generalIoReplay;");
            printWriter.println("generalIoReplay.width = " + gameMap.getWidth() + ";");
            printWriter.println("generalIoReplay.height = " + gameMap.getHeight() + ";");
            printWriter.println("generalIoReplay.history = [];");
            history.forEach(s -> printWriter.println("generalIoReplay.history.push(JSON.parse('" + s + "'));"));
            printWriter.flush();
            printWriter.close();
            LOGGER.info("******* A js file with replay has been created at: {}", file.getAbsoluteFile());
        } catch (IOException e) {
            LOGGER.error("Could not write json data history", e);
        }
    }

}
