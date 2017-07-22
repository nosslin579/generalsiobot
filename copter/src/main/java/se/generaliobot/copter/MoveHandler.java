package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.locator.Locator;

import java.util.ArrayList;
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
        this.moveStrategy = InitialExpansion.create(tileHandler, locators);
    }

    public void initializeNewRound(int roundNo) {
    }


    public Optional<Move> getMove(int roundNo, int turnsToNextRound) {
        if (moveStrategy.isComplete()) {
            log.info("MoveStrategy complete {}", moveStrategy);
            moveStrategy = InitialExpansion.create(tileHandler, locators);
        }
        Tile crownTile = getCrownTile();
        return moveStrategy.getMove(crownTile).flatMap(this::validate);
    }

    private Optional<Move> validate(Move move) {
        if (isValid(move)) {
            return Optional.of(move);
        } else {
            log.warn("Invalid move at turn:{} from:{} to:{}", tileHandler.getTurn(), tileHandler.getTile(move.getFrom()), tileHandler.getTile(move.getTo()));
            return Optional.empty();
        }
    }

    private boolean isValid(Move move) {
        if (move == null) {
            return false;
        }
        Tile from = tileHandler.getTile(move.getFrom());
        Tile to = tileHandler.getTile(move.getTo());
        if (to.getField().isObstacle()) {
            throw new IllegalStateException("Can't move a mountain");
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
        locators.stream()
                .map(Locator::getLocationScore)
                .forEach(scores::add);
        int mostLikelyIndex = scores.getMax();
        log.debug("Guessing general is at index: {} with a score of {}", mostLikelyIndex, scores.getScore(mostLikelyIndex));
        return tileHandler.getTile(mostLikelyIndex);
    }


    public List<Locator> getLocators() {
        return locators;
    }

}