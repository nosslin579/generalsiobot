package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.aardvark.config.Config;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;
import se.nosslin579.aardvark.fieldlisteners.SetViewedFieldListener;
import se.nosslin579.aardvark.locator.*;
import se.nosslin579.aardvark.scorer.LocatorScorer;
import se.nosslin579.aardvark.scorer.Scorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreMap {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locator = new ArrayList<>();
    private final List<MoveListener> moveListeners = new ArrayList<>();
    private final Config config;
    private final FieldWrapper[] fieldWrappers;
    private final FieldWrapper myGeneral;
    private FieldWrapper cursor;
    private final int width;
    private final int height;
    private final int myPlayerIndex;
    private List<FieldListener> fieldListeners = new ArrayList<>();
    private int turn = 0;
    private Move previous = new Move(0, 0);

    public ScoreMap(Config config, FieldWrapper[] fieldWrappers, FieldWrapper myGeneral, int width, int height, int myPlayerIndex) {
        this.config = config;
        this.fieldWrappers = fieldWrappers;
        this.myGeneral = myGeneral;
        this.cursor = myGeneral;
        this.width = width;
        this.height = height;
        this.myPlayerIndex = myPlayerIndex;
    }

    public static ScoreMap of(GameState gameState, Config config) {
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
        ScoreMap scoreMap = new ScoreMap(config, fieldWrappers, ownGeneral, gameState.getColumns(), gameState.getRows(), gameState.getMyPlayerIndex());
        scoreMap.addBean(new SetViewedFieldListener());
        scoreMap.addBean(new VisitedFieldsLocator());
        scoreMap.addBean(new FoundItLocator());
        scoreMap.addBean(new CutOffLocator(scoreMap));
        scoreMap.addBean(new ExcludeEdgeLocator(scoreMap));
        scoreMap.addBean(new UnreachableLocator(scoreMap));
        scoreMap.addBean(new LocatorScorer(scoreMap.locator, config));
        scoreMap.addBean(new MirrorOwnGeneralLocator(scoreMap));
//        scoreMap.addBean(new MoveBackScorer(config));

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
        if (bean instanceof MoveListener) {
            moveListeners.add((MoveListener) bean);
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
        Optional<FieldWrapper> captured = Optional.empty();
        for (Field newField : gameState.getFields()) {
            FieldWrapper fw = fieldWrappers[newField.getIndex()];
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
        for (FieldWrapper field : fieldWrappers) {
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
        FieldWrapper moveTo = penalties.getMin(cursor.getNeighbours());
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
