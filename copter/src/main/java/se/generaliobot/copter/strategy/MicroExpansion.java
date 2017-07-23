package se.generaliobot.copter.strategy;

import se.generaliobot.copter.Move;
import se.generaliobot.copter.MoveStrategy;
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
    public boolean isComplete() {
        return movesLeft == 0;
    }

    @Override
    public Optional<Move> getMove(Tile crown) {
        movesLeft--;
        Optional<Move> ret = Arrays.stream(tileHandler.getTiles())
                .filter(Tile::isMine)
                .filter(tile -> tile.getMyArmySize() > 1)
                .filter(this::canCaptureNeighbour)
                .sorted((t1, t2) -> Integer.compare(t1.getDistanceToOwnCrown(), t2.getDistanceToOwnCrown()))
                .findFirst()
                .flatMap(this::convertToMove);
        if (!ret.isPresent()) {
            movesLeft = 0;
        }
        return ret;
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
