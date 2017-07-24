package se.generaliobot.copter.strategy;

import se.generaliobot.copter.*;
import se.generaliobot.copter.config.Config;

import java.util.*;
import java.util.stream.Stream;

public class WinAttempt implements MoveStrategy {
    private final TileHandler tileHandler;
    private final Config config;
    private Deque<Tile> path;
    private Deque<Move> aggregation;

    public WinAttempt(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
        this.config = tileHandler.getConfig();
    }


    @Override
    public Optional<Move> getMove(Tile crown) {
        if (path == null) {
            Map<Integer, Double> moveScore = crown.getMoveScore(tileHandler, this::getScore, config.getMandatoryMovePenalty());
            Scores scores = new Scores(moveScore, -1000d);
            this.path = createPath(scores, crown, tileHandler.getMyGeneral());
            this.aggregation = createAggregationMoves();
        }
        if (!aggregation.isEmpty()) {
            return Optional.of(aggregation.pollFirst());
        }
        if (path.size() > 1) {
            return Optional.of(new Move(path.pollFirst(), path.getFirst(), "Attack crown"));
        }
        return Optional.empty();
    }

    private Double getScore(Tile tile) {
        if (tile.isMine()) {
            return config.getWinAttemptOwnScore();
        }
        TileType lastKnown = tile.getLastKnown();
        switch (lastKnown) {
            case EMPTY:
                return config.getWinAttemptEmptyScore();
            case FOG:
                return config.getWinAttemptFogScore();
            case ENEMY:
                return 0d;
            case ENEMY_CROWN:
                return 0.9d;
            default:
                return -1000d;
        }
    }

    private Deque<Tile> createPath(Scores scores, Tile goal, Tile origin) {
        Deque<Tile> ret = new ArrayDeque<>();
        ret.add(origin);
        int maxMoves = 50;
        while (ret.getLast() != goal && maxMoves-- != 0) {
            Stream<Tile> neighbours = Arrays.stream(ret.getLast().getNeighbours());//.filter(tile -> !ret.contains(tile));
            Tile moveTo = scores.getMax(neighbours);
            ret.add(moveTo);
        }
        return ret;
    }

    private Deque<Move> createAggregationMoves() {
        Deque<Move> ret = new ArrayDeque<>();
        Set<Tile> inAggregationPath = new HashSet<>(path);
        Deque<Tile> process = new ArrayDeque<>(path);
        for (Tile to = process.poll(); to != null; to = process.poll()) {
            if (!to.isMine()) {
                continue;
            }
            for (Tile from : to.getNeighbours()) {
                if (from.getMyArmySize() > 1 && !inAggregationPath.contains(from)) {
                    ret.addFirst(new Move(from, to, "Aggregation"));
                    inAggregationPath.add(from);
                    process.add(from);
                }
            }
        }
        return ret;
    }
}
