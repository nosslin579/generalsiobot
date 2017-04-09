package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.bamse.config.Config;
import se.nosslin579.bamse.fieldlisteners.FieldListener;
import se.nosslin579.bamse.fieldlisteners.SetViewedFieldListener;
import se.nosslin579.bamse.locator.*;
import se.nosslin579.bamse.scorer.LocatorScorer;
import se.nosslin579.bamse.scorer.MoveBackScorer;
import se.nosslin579.bamse.scorer.Scorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locator = new ArrayList<>();
    private final List<MoveListener> moveListeners = new ArrayList<>();
    private final Config config;
    private final Tile[] tiles;
    private final Tile myGeneral;
    private Tile cursor;
    private final int width;
    private final int height;
    private final int myPlayerIndex;
    private List<FieldListener> fieldListeners = new ArrayList<>();
    private int turn = 0;
    private Move previous = new Move(0, 0);

    public TileHandler(Config config, Tile[] tiles, Tile myGeneral, int width, int height, int myPlayerIndex) {
        this.config = config;
        this.tiles = tiles;
        this.myGeneral = myGeneral;
        this.cursor = myGeneral;
        this.width = width;
        this.height = height;
        this.myPlayerIndex = myPlayerIndex;
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

        //add beans
        TileHandler tileHandler = new TileHandler(config, tiles, ownGeneral, gameState.getColumns(), gameState.getRows(), gameState.getMyPlayerIndex());
        tileHandler.addBean(new SetViewedFieldListener());
        tileHandler.addBean(new VisitedFieldsLocator());
        tileHandler.addBean(new FoundItLocator());
        tileHandler.addBean(new CutOffLocator(tileHandler));
        tileHandler.addBean(new ExcludeEdgeLocator(tileHandler));
        tileHandler.addBean(new UnreachableLocator(tileHandler));
        tileHandler.addBean(new LocatorScorer(tileHandler.locator, config));
        tileHandler.addBean(new MirrorOwnGeneralLocator(tileHandler));
        tileHandler.addBean(new MoveBackScorer(config));

        return tileHandler;
    }

    private void addBean(Object bean) {
        if (bean instanceof FieldListener) {
            fieldListeners.add((FieldListener) bean);
        }
        if (bean instanceof Locator) {
            locator.add((Locator) bean);
        }
        if (bean instanceof Scorer) {
            scorers.add((Scorer) bean);
        }
        if (bean instanceof MoveListener) {
            moveListeners.add((MoveListener) bean);
        }
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

    public List<Scorer> getScorers() {
        return scorers;
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

    public Move getMove() {
        Scores penalties = Scores.of(scorers, this);

        if (!cursor.isMine() || cursor.getField().asVisibleField().getArmy() < 2) {
            cursor = myGeneral;
        }
        Tile moveTo = penalties.getMin(cursor.getNeighbours());
        Move ret = new Move(cursor, moveTo);
        cursor = moveTo;

        if (previous.getTo() == ret.getFrom() && previous.getFrom() == ret.getTo()) {
            log.warn("Moving back to previous. Turn={} {} {}", turn, ret, myPlayerIndex);
        }
        moveListeners.forEach(moveListener -> moveListener.beforeMove(ret));
        previous = ret;
        return ret;
    }

    public int getTurn() {
        return turn;
    }

    public Config getConfig() {
        return config;
    }
}
