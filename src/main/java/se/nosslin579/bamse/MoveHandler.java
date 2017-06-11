package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.bamse.locator.Locator;
import se.nosslin579.bamse.scorer.Scorer;

import java.util.*;
import java.util.stream.Collectors;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locators = new ArrayList<>();

    private GameState gameState = null;


    private Deque<Move> movePath = new ArrayDeque<>();

    public MoveHandler() {
    }

    public Optional<Move> getMove(TileHandler tileHandler) {
        boolean anyValid = movePath.stream().anyMatch(m -> isValid(m, tileHandler));
        if (!anyValid) {
            createMovePath(tileHandler);
        }

        while (!movePath.isEmpty()) {
            Move move = movePath.pop();
            if (isValid(move, tileHandler)) {
                return Optional.of(move);
            }
        }

        log.warn("No move found");
        return Optional.empty();
    }

    private void createMovePath(TileHandler tileHandler) {
        int loopSafety = 0;
        Scores penalties = Scores.of(scorers, tileHandler);
        Tile goal = penalties.getMin(tileHandler.getTiles());
        Tile moveFrom = tileHandler.getMyGeneral();
        while (moveFrom != goal && loopSafety++ < 200) {
            Tile moveTo = penalties.getMin(moveFrom.getNeighbours());
            movePath.add(new Move(moveFrom, moveTo, "Path"));
            moveFrom = moveTo;
        }
        //add expanding
        String path = movePath.stream().map(move -> move.getTo()).map(Object::toString).collect(Collectors.joining(","));
        int aggregatedMoves = aggregatePath(tileHandler);

        log.info("Created move path with {}/{} steps, final step is {}. Path:{}", aggregatedMoves, movePath.size(), movePath.getLast().getTo(), path);
    }

    private int aggregatePath(TileHandler tileHandler) {
        int originalSize = movePath.size();
        while (true) {
            int size = movePath.size();
            for (Move move : new ArrayList<>(movePath)) {
                Tile tileInPath = tileHandler.getTile(move.getFrom());
                for (Tile neighbour : tileInPath.getNeighbours()) {
                    if (neighbour.getMyArmySize() > 1 && tileInPath.isMine()) {
                        boolean inPath = movePath.stream().anyMatch(m -> m.getFrom() == neighbour.getIndex());
                        if (!inPath) {
                            movePath.addFirst(new Move(neighbour, tileInPath, "Aggregating path"));
                        }
                    }
                }
            }
            if (size == movePath.size()) {
                return movePath.size() - originalSize;
            }
        }
    }

    private boolean isValid(Move move, TileHandler tileHandler) {
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

    public List<Locator> getLocators() {
        return locators;
    }

    public List<Scorer> getScorers() {
        return scorers;
    }
}