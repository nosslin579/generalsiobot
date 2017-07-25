package se.generaliobot.copter.strategy;

import se.generaliobot.copter.Move;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;

import java.util.Arrays;
import java.util.Optional;

public class MicroExpansion implements MoveStrategy {

    private final TileHandler tileHandler;
    private int movesLeft;

    public MicroExpansion(TileHandler tileHandler, int maxMoves) {
        this.tileHandler = tileHandler;
        this.movesLeft = maxMoves;
    }

    @Override
    public Optional<Move> getMove(Tile crown) {
        if (movesLeft-- == 0) {
            return Optional.empty();
        }
        return Arrays.stream(this.tileHandler.getTiles())
                .filter(Tile::isMine)
                .filter(tile -> tile.getMyArmySize() > 1)
                .filter(this::canCaptureNeighbour)
                .sorted((t1, t2) -> Integer.compare(t1.getDistanceToOwnCrown(), t2.getDistanceToOwnCrown()))
                .findFirst()
                .flatMap(this::convertToMove);
    }

    @Override
    public MoveStrategy createNew() {
        return new MicroExpansion(tileHandler, 1);
    }

    private boolean canCaptureNeighbour(Tile tile) {
        return Arrays.stream(tile.getNeighbours()).anyMatch(tile::canCapture);
    }

    private Optional<Move> convertToMove(Tile from) {
        return Arrays.stream(from.getNeighbours())
                .filter(from::canCapture)
                .findAny()
                .map(to -> new Move(from, to, "MicroExpansion"));
    }
}
