package se.generaliobot.copter;

import java.util.Optional;

public interface MoveStrategy {
    Optional<Move> getMove(Tile crown);

    boolean isComplete();
}
