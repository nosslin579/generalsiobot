package se.generaliobot.copter;

import java.util.Optional;

public interface MoveStrategy {
    boolean isComplete();

    Optional<Move> getMove(Tile crown);
}
