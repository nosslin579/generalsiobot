package se.generaliobot.copter.strategy;

import se.generaliobot.copter.Move;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;

import java.util.*;

public abstract class AbstractMoveStrategy implements MoveStrategy {

    protected Deque<Tile> createPath(Scores scores, Tile from, Tile to) {
        Deque<Tile> ret = new ArrayDeque<>();
        ret.add(from);
        int maxMoves = 50;
        while (ret.getLast() != to && maxMoves-- != 0) {
            Tile moveTo = scores.getMax(Arrays.stream(ret.getLast().getNeighbours()));
            ret.add(moveTo);
        }
        return ret;
    }

    protected Deque<Move> createAggregationMoves(Deque<Tile> path) {
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
