package se.generaliobot.copter.strategy;

import se.generaliobot.copter.Move;
import se.generaliobot.copter.Tile;

import java.util.Optional;

public class DoNothing implements MoveStrategy {

    @Override
    public Optional<Move> getMove(Tile crown) {
        return Optional.empty();
    }

    @Override
    public MoveStrategy createNew() {
        return new DoNothing();
    }
}
