package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.framework.model.Position;
import se.generaliobot.copter.config.Config;
import se.generaliobot.copter.fieldlisteners.FieldListener;

import java.util.*;

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
            Tile[] neighbours = tile.getField().getNeighbours().stream().map(field -> tiles[field.getIndex()]).toArray(Tile[]::new);
            tile.setNeighbours(neighbours);
        }

        //set distance to own crown
        ownGeneral.setDistanceToOwnCrown(0);
        Deque<Tile> distanceQue = new ArrayDeque<>();
        distanceQue.add(ownGeneral);
        while (!distanceQue.isEmpty()) {
            Tile current = distanceQue.pollFirst();
            for (Tile tile : current.getNeighbours()) {
                int newDistance = current.getDistanceToOwnCrown() + 1;
                if (!tile.getField().isObstacle() && newDistance < tile.getDistanceToOwnCrown()) {
                    tile.setDistanceToOwnCrown(newDistance);
                    distanceQue.addLast(tile);
                }
            }
        }

        //set positions
        for (Tile tile : tiles) {
            Position position = tile.getField().getPosition();
            Field right = gameState.getFieldsMap().get(position.withCol(position.getCol() + 1));
            Field left = gameState.getFieldsMap().get(position.withCol(position.getCol() - 1));
            Field down = gameState.getFieldsMap().get(position.withRow(position.getRow() + 1));
            Field up = gameState.getFieldsMap().get(position.withRow(position.getRow() - 1));
            tile.getNeighboursByPosition().put(Direction.RIGHT, right == null ? null : tiles[right.getIndex()]);
            tile.getNeighboursByPosition().put(Direction.LEFT, left == null ? null : tiles[left.getIndex()]);
            tile.getNeighboursByPosition().put(Direction.DOWN, down == null ? null : tiles[down.getIndex()]);
            tile.getNeighboursByPosition().put(Direction.UP, up == null ? null : tiles[up.getIndex()]);
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

    public Optional<Tile> getNextInline(Collection<Tile> path) {
        if (path.size() < 2) {
            return Optional.empty();
        }
        Deque<Tile> tiles = new ArrayDeque<>(path);
        Tile origo = tiles.pollLast();
        Tile tail = tiles.pollLast();
        Map<Direction, Tile> neighboursByPosition = origo.getNeighboursByPosition();
        return neighboursByPosition.entrySet().stream()
                .filter(entry -> tail.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findAny()
                .map(Direction::getOpposite)
                .map(neighboursByPosition::get);
    }
}
