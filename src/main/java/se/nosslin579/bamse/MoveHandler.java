package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nosslin579.bamse.fieldlisteners.FieldListener;
import se.nosslin579.bamse.locator.Locator;
import se.nosslin579.bamse.scorer.Scorer;

import java.util.ArrayList;
import java.util.List;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locator = new ArrayList<>();
    private final List<MoveListener> moveListeners = new ArrayList<>();
    private Tile cursor;
    private List<FieldListener> fieldListeners = new ArrayList<>();

    public MoveHandler() {
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

    public Move getMove() {
//        Scores penalties = Scores.of(scorers, this);

//        if (!cursor.isMine() || cursor.getField().asVisibleField().getArmy() < 2) {
//            cursor = myGeneral;
//        }
//        Tile moveTo = penalties.getMin(cursor.getNeighbours());
//        Move ret = new Move(cursor, moveTo);
//        cursor = moveTo;
//
//        moveListeners.forEach(moveListener -> moveListener.beforeMove(ret));
//        return ret;
        return null;
    }

}
