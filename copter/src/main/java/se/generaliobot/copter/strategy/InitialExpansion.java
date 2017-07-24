package se.generaliobot.copter.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.*;

import java.util.*;

public class InitialExpansion implements MoveStrategy {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TileHandler tileHandler;
    private final Deque<Tile> path = new ArrayDeque<>();

    public InitialExpansion(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
        path.add(tileHandler.getMyGeneral());
    }


    @Override
    public Optional<Move> getMove(Tile crown) {
        Tile current = path.getLast();
        Map<Integer, Double> moveScore = crown.getMoveScore(tileHandler, this::getScore, 1d);
        Scores scores = new Scores(moveScore, -10000d);
        tileHandler.getNextInline(path).map(tile -> new TileScore(tile, .1d)).ifPresent(scores::add);
        Tile to = scores.getMax(Arrays.stream(current.getNeighbours()));
        path.addLast(to);
        return Optional.of(new Move(current, to, getClass().getSimpleName()));

    }

    private Double getScore(Tile tile) {
        if (tile.isMine()) {
            return 0d;
        }
        switch (tile.getLastKnown()) {
            case EMPTY:
            case FOG:
            case ENEMY:
                return .89d;
            default:
                return -1000d;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
