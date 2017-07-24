package se.generaliobot.copter.strategy;

import se.generaliobot.copter.Move;
import se.generaliobot.copter.Tile;

import java.util.Optional;

public interface MoveStrategy {

    Optional<Move> getMove(Tile crown);

}
