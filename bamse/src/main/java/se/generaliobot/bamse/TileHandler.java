package se.generaliobot.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import se.generaliobot.bamse.config.Config;
import se.generaliobot.bamse.fieldlisteners.FieldListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Config config;
    private final Tile[] tiles;
    private final Tile myGeneral;
    private final int width;
    private final int height;
    private List<FieldListener> fieldListeners = new ArrayList<>();
    private int turn = 0;

    public TileHandler(Config config, Tile[] tiles, Tile myGeneral, int width, int height) {
        this.config = config;
        this.tiles = tiles;
        this.myGeneral = myGeneral;
        this.width = width;
        this.height = height;
    }

    public static TileHandler of(GameState gameState, Config config) {
        Tile[] tiles = new Tile[gameState.getFields().size()];

        //populate fields and own general
        Tile ownGeneral = null;
        for (Field field : gameState.getFields()) {
            tiles[field.getIndex()] = new Tile(field);
            if (field.isVisible() && field.asVisibleField().isGeneral()) {
                ownGeneral = tiles[field.getIndex()];
            }
        }

        //set neighbours
        for (Tile tile : tiles) {
            Tile[] objects = tile.getField().getNeighbours().stream().map(field -> tiles[field.getIndex()]).toArray(Tile[]::new);
            tile.setNeighbours(objects);
        }

        return new TileHandler(config, tiles, ownGeneral, gameState.getColumns(), gameState.getRows());
    }

    public Tile getMyGeneral() {
        return myGeneral;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.getX() == x && tile.getY() == y) {
                return tile;
            }
        }
        throw new IllegalArgumentException("No tile at x:" + x + " y:" + y);
    }

    public Tile getTile(int index) {
        return tiles[index];
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void update(GameState gameState) {
        Optional<Tile> captured = Optional.empty();
        for (Field newField : gameState.getFields()) {
            Tile fw = tiles[newField.getIndex()];
            if (!fw.isViewed() && newField.isVisible()) {
                fieldListeners.forEach(fieldFound -> fieldFound.onFieldFound(newField.asVisibleField(), this));
            }
            if (fw.getField().isVisible() && !fw.getField().asVisibleField().isOwnedByEnemy() && newField.isVisible() && newField.asVisibleField().isOwnedByEnemy()) {
                captured = Optional.of(fw);
            }
            fw.updateField(newField).ifPresent(old -> fieldListeners.forEach(fieldListener -> fieldListener.onFieldChange(fw, old)));
        }
        turn = gameState.getTurn();
        captured.ifPresent(cap -> fieldListeners.forEach(fieldListener -> fieldListener.onCaptured(this, cap)));
    }

    private void printMap() {
        StringBuilder sb = new StringBuilder();
        for (Tile field : tiles) {
            if (field.getField().getPosition().getCol() == 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(field.getLastKnown().getSymbol()).append(" ");
        }
        log.info(sb.toString());
    }

    public int getTurn() {
        return turn;
    }

    public Config getConfig() {
        return config;
    }

    public List<FieldListener> getFieldListeners() {
        return fieldListeners;
    }
}
