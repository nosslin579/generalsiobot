package se.generaliobot.copter.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.*;
import se.generaliobot.copter.config.Config;

import java.util.*;

public class DogSniff extends AbstractMoveStrategy {
    private final static Logger log = LoggerFactory.getLogger(DogSniff.class);
    private final TileHandler tileHandler;
    private final Config config;
    private Deque<Tile> path;
    private Deque<Move> aggregation;
    private Tile cursor;

    public DogSniff(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
        this.config = tileHandler.getConfig();
    }

    @Override
    public Optional<Move> getMove(Tile crown) {
        if (path == null) {
            Scores moveScore = crown.getMoveScore(this::getScore);
            path = createPath(moveScore, tileHandler.getMyGeneral(), crown);
            removeFogAtTheEnd();
            cursor = path.getLast();
            aggregation = createAggregationMoves(path);
        }

        if (!aggregation.isEmpty()) {
            return Optional.of(aggregation.pollFirst());
        }

        if (path.size() > 1) {
            return Optional.of(new Move(path.pollFirst(), path.getFirst(), "Sniff path"));
        }

        Scores moveScore = crown.getMoveScore(this::getScore);
        Deque<Tile> p = createPath(moveScore, cursor, crown);
        Tile from = p.pollFirst();
        cursor = p.getFirst();
        return Optional.of(new Move(from, cursor, "Sniff sniff"));
    }

    private Double getScore(Tile tile) {
        if (tile.isMine()) {
            return 1d;
        }
        TileType lastKnown = tile.getLastKnown();
        switch (lastKnown) {
            case FOG:
                return config.getSniffFogPenalty();
            case EMPTY:
                return config.getSniffEmptyPenalty();
            case ENEMY:
                return config.getSniffEnemyPenalty();
            case ENEMY_CROWN:
                log.error("Not supposed to find enemy crown here");
                return 0.9d;
            default:
                return 1000d;
        }
    }

    private void removeFogAtTheEnd() {
        List<Tile> list = new ArrayList<>(path);
        Collections.reverse(list);
        for (Tile tile : list) {
            if (tile.getLastKnown() == TileType.FOG) {
                path.remove(tile);
            } else {
                return;
            }
        }

    }

}
