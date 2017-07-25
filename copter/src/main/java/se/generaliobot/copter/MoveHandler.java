package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Player;
import se.generaliobot.copter.locator.Locator;
import se.generaliobot.copter.strategy.*;

import java.util.*;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Locator> locators = new ArrayList<>();
    private final TileHandler tileHandler;

    private Deque<MoveStrategy> que = new LinkedList<>();

    public MoveHandler(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
    }

    public void initNewGame() {
        que.add(new InitialExpansion(tileHandler));
    }

    public void initializeNewRound(int roundNo) {
        log.info("New round {}", roundNo);
        if (roundNo == 1) {
            que.addLast(new InitialExpansion(tileHandler));
            que.addLast(new MicroExpansion(tileHandler, 10));
            que.addLast(new InitialExpansion(tileHandler));
        }

        if (roundNo == 2) {
            que.addLast(new MicroExpansion(tileHandler, 20));
            que.addLast(new DogSniff(tileHandler));
        }

        if (tileHandler.getEnemyGeneral() != null) {
            que.addLast(new WinAttempt(tileHandler));
        }

    }


    public Optional<Move> getMove(int roundNo, int turnsToNextRound, Player enemy) {
        Tile crown = getCrownTile();

        Optional<Move> ret = que.getFirst().getMove(crown).flatMap(this::validate);
        if (ret.isPresent()) {
            return ret;
        }
        MoveStrategy p = que.pollFirst();
        log.info("Move strategy done: {}", p);

        if (que.isEmpty()) {
            que.add(p.createNew());
        }

        return que.getFirst().getMove(crown).flatMap(this::validate);
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