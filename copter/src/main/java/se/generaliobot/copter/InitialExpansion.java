package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.locator.Locator;

import java.util.*;

public class InitialExpansion implements MoveStrategy {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TileHandler tileHandler;
    private final Deque<Tile> path = new ArrayDeque<>();

    public InitialExpansion(TileHandler tileHandler, List<Locator> locators) {
        this.tileHandler = tileHandler;
    }


    public static MoveStrategy create(TileHandler tileHandler, List<Locator> locators) {
        InitialExpansion ret = new InitialExpansion(tileHandler, locators);
        ret.path.add(tileHandler.getMyGeneral());
        return ret;
    }

    @Override
    public Optional<Move> getMove(Tile crown) {
        Tile current = path.getLast();
        Map<Integer, Double> moveScore = crown.getMoveScore(tileHandler, this::getScore, -1d);
        Scores scores = new Scores(moveScore, 10000d);
        tileHandler.getNextInline(path).map(tile -> new TileScore(tile, .1d)).ifPresent(scores::add);
//        log.info(scores.getPrettyPrint(tileHandler));
        Tile to = scores.getMax(current.getNeighbours());
        path.addLast(to);
        return Optional.of(new Move(current, to, getClass().getSimpleName()));

    }

    private Double getScore(Tile tile) {
        return Arrays.asList(TileType.FOG, TileType.EMPTY).contains(tile.getLastKnown()) ? .5d : 0d;
    }

    @Override
    public boolean isComplete() {
        return path.getLast().getMyArmySize() < 2;
    }

    private List<Move> createPath(Scores penalties, Tile goal, Tile moveFrom) {
        List<Move> ret = new ArrayList<>();
        int maxMoves = 10;
        while (moveFrom != goal && tileHandler.getTile(moveFrom.getIndex()).getField().isVisible() && maxMoves-- != 0) {
            Tile moveTo = penalties.getMin(moveFrom.getNeighbours());
            ret.add(new Move(moveFrom, moveTo, "Checkpoint"));
            moveFrom = moveTo;
            if (moveTo == goal) {
                return ret;
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
