package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Scores scores = new Scores(moveScore, 10000d);
        tileHandler.getNextInline(path).map(tile -> new TileScore(tile, .1d)).ifPresent(scores::add);
        Tile to = scores.getMax(Arrays.stream(current.getNeighbours()));
        path.addLast(to);
        return Optional.of(new Move(current, to, getClass().getSimpleName()));

    }

    private Double getScore(Tile tile) {
        return Arrays.asList(TileType.FOG, TileType.EMPTY).contains(tile.getLastKnown()) ? .89d : 0d;
    }

    @Override
    public boolean isComplete() {
        return path.getLast().getMyArmySize() < 2;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
