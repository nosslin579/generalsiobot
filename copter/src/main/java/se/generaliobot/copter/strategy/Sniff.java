package se.generaliobot.copter.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.*;
import se.generaliobot.copter.config.Config;

import java.util.*;

public class Sniff extends AbstractMoveStrategy {
    private final static Logger log = LoggerFactory.getLogger(Sniff.class);
    private final TileHandler tileHandler;
    private final Config config;
    private Deque<Tile> path;
    private Deque<Move> aggregation;
    private Tile cursor;

    public Sniff(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
        this.config = tileHandler.getConfig();
    }

    @Override
    public Optional<Move> getMove(Tile crown) {
        if (path == null) {
            Map<Integer, Double> moveScore = crown.getMoveScore(tileHandler, this::getScore, config.getMandatoryMovePenalty());
            Scores scores = new Scores(moveScore, -1000d);
            path = createPath(scores, tileHandler.getMyGeneral(), crown);
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

        Map<Integer, Double> moveScore = crown.getMoveScore(tileHandler, this::getScore, config.getMandatoryMovePenalty());
        Scores scores = new Scores(moveScore, -1000d);
        Deque<Tile> p = createPath(scores, cursor, crown);
        Tile from = p.pollFirst();
        cursor = p.getFirst();
        return Optional.of(new Move(from, cursor, "Sniff sniff"));
    }

    private Double getScore(Tile tile) {
        if (tile.isMine()) {
            return 0d;
        }
        TileType lastKnown = tile.getLastKnown();
        switch (lastKnown) {
            case FOG:
                return config.getSniffFogScore();
            case EMPTY:
                return config.getSniffEmptyScore();
            case ENEMY:
                return config.getSniffEnemyScore();
            case ENEMY_CROWN:
                log.error("Not supposed to find enemy crown here");
                return 0.9d;
            default:
                return -1000d;
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
