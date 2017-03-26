package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;
import se.nosslin579.aardvark.fieldlisteners.SetViewedFieldListener;
import se.nosslin579.aardvark.locator.FoundItLocator;
import se.nosslin579.aardvark.locator.Locator;
import se.nosslin579.aardvark.locator.MirrorOwnGeneralLocator;
import se.nosslin579.aardvark.locator.VisitedFieldsLocator;
import se.nosslin579.aardvark.scorer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreMap {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locator = new ArrayList<>();
    private final FieldWrapper[] fieldWrappers;
    private final FieldWrapper myGeneral;
    private FieldWrapper cursor;
    private final int width;
    private final int height;
    private List<FieldListener> fieldListeners = new ArrayList<>();
    private int turn = 0;

    public ScoreMap(FieldWrapper[] fieldWrappers, FieldWrapper myGeneral, int width, int height) {
        this.fieldWrappers = fieldWrappers;
        this.myGeneral = myGeneral;
        this.cursor = myGeneral;
        this.width = width;
        this.height = height;
    }

    public static ScoreMap of(GameState gameState) {
        FieldWrapper[] fieldWrappers = new FieldWrapper[gameState.getFields().size()];

        //populate fields and own general
        FieldWrapper ownGeneral = null;
        for (Field field : gameState.getFields()) {
            fieldWrappers[field.getIndex()] = new FieldWrapper(field);
            if (field.isVisible() && field.asVisibleField().isGeneral()) {
                ownGeneral = fieldWrappers[field.getIndex()];
            }
        }

        //set neighbours
        for (FieldWrapper fieldWrapper : fieldWrappers) {
            FieldWrapper[] objects = fieldWrapper.getField().getNeighbours().stream().map(field -> fieldWrappers[field.getIndex()]).toArray(FieldWrapper[]::new);
            fieldWrapper.setNeighbours(objects);
        }

        //add beans
        ScoreMap scoreMap = new ScoreMap(fieldWrappers, ownGeneral, gameState.getColumns(), gameState.getRows());
        scoreMap.addBean(new SetViewedFieldListener());
        scoreMap.addBean(new VisitedFieldsLocator());
        scoreMap.addBean(new FoundItLocator());
        scoreMap.addBean(new MountainScorer());
        scoreMap.addBean(new CityScorer());
        scoreMap.addBean(new LocatorScorer(scoreMap.locator));
        scoreMap.addBean(new MirrorOwnGeneralLocator(scoreMap));
        scoreMap.addBean(new MyFieldsScorer());

        return scoreMap;
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
    }

    public FieldWrapper getMyGeneral() {
        return myGeneral;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public FieldWrapper getTile(int x, int y) {
        for (FieldWrapper fieldWrapper : fieldWrappers) {
            if (fieldWrapper.getX() == x && fieldWrapper.getY() == y) {
                return fieldWrapper;
            }
        }
        throw new IllegalArgumentException("No tile at x:" + x + " y:" + y);
    }

    public FieldWrapper getTile(int index) {
        return fieldWrappers[index];
    }

    public FieldWrapper[] getFieldWrappers() {
        return fieldWrappers;
    }

    public List<Scorer> getScorers() {
        return scorers;
    }

    public void update(GameState gameState) {
        for (Field updatedField : gameState.getFields()) {
            FieldWrapper fw = fieldWrappers[updatedField.getIndex()];
            if (!fw.isViewed() && updatedField.isVisible()) {
                fieldListeners.forEach(fieldFound -> fieldFound.onFieldFound(updatedField.asVisibleField(), this));
            }
            fw.setField(updatedField);
        }
        turn = gameState.getTurn();
    }

    public Move getMove() {
        Scores scores = Scores.of(scorers, this);

        if (!cursor.isMine() || cursor.getField().asVisibleField().getArmy() < 2) {
            cursor = myGeneral;
        }
        FieldWrapper moveTo = Arrays.stream(cursor.getNeighbours())
                .reduce(scores::getMax)
                .get();
//        log.info("Moving to {} with a score of {}", moveTo.getIndex(), scores.getScore(moveTo.getIndex()));
        Move ret = new Move(cursor, moveTo);
        cursor = moveTo;
        return ret;
    }

    public int getTurn() {
        return turn;
    }

    /*
    private static void setDistanceGeneral(int distance, List<Tile> tiles) {
        List<Tile> next = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.isMovable() && tile.getDistanceGeneral() == Tile.UNSET) {
                tile.setDistanceGeneral(distance);
                next.addAll(Arrays.asList(tile.getNeighbours()));
            }
        }
        if (!next.isEmpty()) {
            System.out.println(next.size() + " - " + distance);
            setDistanceGeneral(distance + 1, next);
        }
    }*/
}
