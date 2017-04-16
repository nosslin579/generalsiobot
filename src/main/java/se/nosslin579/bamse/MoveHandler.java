package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.bamse.locator.Locator;
import se.nosslin579.bamse.scorer.Scorer;

import java.util.*;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locators = new ArrayList<>();

    private GameState gameState = null;
    private Tile cursor;


    private Deque<Move> movePath = new ArrayDeque<>();

    public MoveHandler() {
    }

    public Move[] getMoves(TileHandler tileHandler) {
        if (movePath.isEmpty() || cursor == null || !cursor.isMine() || cursor.getField().asVisibleField().getArmy() < 2) {
            cursor = tileHandler.getMyGeneral();

            int loopSafety = 0;
            Scores penalties = Scores.of(scorers, tileHandler);
            Tile goal = penalties.getMin(tileHandler.getTiles());
            Tile moveFrom = tileHandler.getMyGeneral();
            while (moveFrom != goal && loopSafety++ < 200) {
                Tile moveTo = penalties.getMin(moveFrom.getNeighbours());
                movePath.add(new Move(moveFrom, moveTo));
                moveFrom = moveTo;
            }
        }

        //add expanding
        //add aggregation

        Move move = movePath.pop();
        log.info("Moving to {} path size is now:{}", move, movePath.size());
        return new Move[]{move};
    }

    private Change getChanges(GameState newGameState) {
        Change ret = new Change();

        for (Field newField : newGameState.getFields()) {
            Field oldField = gameState.getFieldsMap().get(newField.getPosition());
            if (oldField.isVisible() && newField.isVisible()) {
                VisibleField of = oldField.asVisibleField();
                VisibleField nf = newField.asVisibleField();
                boolean sameOwner = of.getOwnerIndex().equals(nf.getOwnerIndex());
                if (sameOwner && of.getArmy() > 2 && nf.getArmy() <= 2) {
                    if (of.isOwnedByEnemy()) {
                        ret.moveFromEnemy = Optional.of(of.getIndex());
                    } else if (of.isOwnedByMe()) {
                        ret.moveFromOwn = Optional.of(of.getIndex());
                    }
                } else if (!sameOwner) {
                    if (nf.isOwnedByEnemy()) {
                        ret.moveToEnemy = Optional.of(nf.getIndex());
                    } else if (nf.isOwnedByMe()) {
                        ret.moveToOwn = Optional.of(nf.getIndex());
                    }
                } else if (nf.getArmy() - of.getArmy() > 2) {
                    if (nf.isOwnedByEnemy()) {
                        ret.moveToEnemy = Optional.of(nf.getIndex());
                    } else if (nf.isOwnedByMe()) {
                        ret.moveToOwn = Optional.of(nf.getIndex());
                    }
                }
            }
        }
        return ret;

    }

    public List<Locator> getLocators() {
        return locators;
    }

    public List<Scorer> getScorers() {
        return scorers;
    }

    private class Change {
        Optional<Integer> moveFromOwn = Optional.empty();
        Optional<Integer> moveFromEnemy = Optional.empty();
        Optional<Integer> moveToOwn = Optional.empty();
        Optional<Integer> moveToEnemy = Optional.empty();
    }
}