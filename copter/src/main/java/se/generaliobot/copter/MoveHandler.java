package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Player;
import se.generaliobot.copter.locator.Locator;
import se.generaliobot.copter.strategy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Locator> locators = new ArrayList<>();
    private final TileHandler tileHandler;

    private MoveStrategy moveStrategy;

    public MoveHandler(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
    }

    public void initNewGame() {
        this.moveStrategy = new InitialExpansion(tileHandler);
    }

    public void initializeNewRound(int roundNo) {
        log.info("New round {}", roundNo);
        if (roundNo == 1 || roundNo == 2) {
            moveStrategy = new MicroExpansion(tileHandler, 10 * roundNo);
        }
    }


    public Optional<Move> getMove(int roundNo, int turnsToNextRound, Player enemy) {
        Tile crown = getCrownTile();

        Optional<Move> ret = moveStrategy.getMove(crown).flatMap(this::validate);
        if (ret.isPresent()) {
            return ret;
        }

        log.info("Move strategy done: {}", moveStrategy);
        if (roundNo == 0 || roundNo == 1) {
            moveStrategy = new InitialExpansion(tileHandler);
        } else if (roundNo == 2) {
            moveStrategy = new Sniff(tileHandler);
        } else {
            moveStrategy = new WinAttempt(tileHandler);
        }
        return moveStrategy.getMove(crown).flatMap(this::validate);
//                moveStrategy = new DoNothing();
//            } else if (roundNo == 3) {
    }

    private Optional<Move> validate(Move move) {
        return isValid(move) ? Optional.of(move) : Optional.empty();
    }

    private boolean isValid(Move move) {
        if (move == null) {
            return false;
        }
        Tile from = tileHandler.getTile(move.getFrom());
        Tile to = tileHandler.getTile(move.getTo());

        if (!to.getField().isVisible()) {
            log.error("Moving to a field that is not visible, {}", move);
            return false;
        }

        if (to.getField().isObstacle()) {
            log.error("Can't move a mountain, {}", move);
            return false;
        }
        if (from.getMyArmySize() < 2) {
            //not my tile or not enough army
            return false;
        }
        if (!to.isMine() && from.getMyArmySize() < to.getField().asVisibleField().getArmy()) {
            //unsuccessful attack
            return false;
        }
        return true;
    }

    public Tile getCrownTile() {
        Scores scores = new Scores();
        Arrays.stream(tileHandler.getTiles()).forEach(tile -> scores.setScore(tile, 0d));
        locators.stream()
                .map(Locator::getLocationScore)
                .forEach(scores::add);
        Tile mostLikely = scores.getMax();
        log.debug("Guessing general is at: {} with a score of {}", mostLikely, scores.getScore(mostLikely));
        return mostLikely;
    }


    public List<Locator> getLocators() {
        return locators;
    }

}