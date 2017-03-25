package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;
import se.nosslin579.aardvark.fieldlisteners.SetViewedFieldListener;
import se.nosslin579.aardvark.locator.FoundItLocator;
import se.nosslin579.aardvark.locator.Locator;
import se.nosslin579.aardvark.locator.MirrorOwnGeneralLocator;
import se.nosslin579.aardvark.locator.VisitedFieldsLocator;
import se.nosslin579.aardvark.scorer.CityScorer;
import se.nosslin579.aardvark.scorer.LocatorScorer;
import se.nosslin579.aardvark.scorer.MountainScorer;
import se.nosslin579.aardvark.scorer.Scorer;

import java.util.*;

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
            int[] distances = getDistances(field, fieldWrappers.length);
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

        //update general score
        ScoreMap scoreMap = new ScoreMap(fieldWrappers, ownGeneral, gameState.getColumns(), gameState.getRows());
        VisitedFieldsLocator visitedFieldsLocator = new VisitedFieldsLocator(scoreMap);
        FoundItLocator foundItLocator = new FoundItLocator();

        scoreMap.fieldListeners.add(new SetViewedFieldListener());
        scoreMap.fieldListeners.add(visitedFieldsLocator);
        scoreMap.fieldListeners.add(foundItLocator);

        scoreMap.scorers.add(new MountainScorer());
        scoreMap.scorers.add(new CityScorer());
        scoreMap.scorers.add(new LocatorScorer(scoreMap.locator));

        scoreMap.locator.add(new MirrorOwnGeneralLocator(scoreMap));
        scoreMap.locator.add(visitedFieldsLocator);
        scoreMap.locator.add(foundItLocator);

        return scoreMap;
    }

    private static int[] getDistances(Field field, int fields) {
        int[] distances = new int[fields];
        List<FieldTerrainType> obstacles = Arrays.asList(FieldTerrainType.FOG_OBSTACLE, FieldTerrainType.MOUNTAIN);
        Deque<Field> que = new ArrayDeque<>();
        que.add(field);
        while (!que.isEmpty()) {
            Field t = que.pop();
            for (Field neighbour : t.getNeighbours()) {
                if (obstacles.contains(neighbour.getTerrainType())) {
                    //obstacles is unreachable
                    distances[neighbour.getIndex()] = 6000;//config
                } else if (distances[neighbour.getIndex()] == 0 && neighbour != field) {
                    //neighbour distance is not set
                    distances[neighbour.getIndex()] = distances[t.getIndex()] + 1;
                    que.addLast(neighbour);
                }
            }
        }
        return distances;
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
            FieldWrapper oldField = fieldWrappers[updatedField.getIndex()];
            if (!oldField.isViewed() && updatedField.isVisible()) {
                fieldListeners.forEach(fieldFound -> fieldFound.onFieldFound(updatedField.asVisibleField(), this));
            }
            oldField.setField(updatedField);
        }
        turn = gameState.getTurn();
    }

    public Move getMove() {
        Scores scores = Scores.of(scorers, this);

        if (!cursor.isMine() || cursor.getField().asVisibleField().getArmy() == 1) {
            cursor = myGeneral;
        }
        FieldWrapper moveTo = Arrays.stream(cursor.getNeighbours())
                .reduce(scores::getMax)
                .get();
//        log.info("Moving to {} with a score of {}", moveTo.getIndex(), moveTo.getGeneralScore());
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
