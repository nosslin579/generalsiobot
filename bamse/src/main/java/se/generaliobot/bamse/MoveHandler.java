package se.generaliobot.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.bamse.locator.Locator;
import se.generaliobot.bamse.scorer.Scorer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Scorer> scorers = new ArrayList<>();
    private final List<Locator> locators = new ArrayList<>();
    private final TileHandler tileHandler;

    private final Deque<Move> checkpointMoves = new ArrayDeque<>();
    private final Deque<Move> aggregatedMoves = new ArrayDeque<>();
    private Tile cursour;

    public MoveHandler(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
        cursour = tileHandler.getMyGeneral();
    }

    public Move getFirstRoundMove() {
        if (cursour.getMyArmySize() < 2) {
            cursour = tileHandler.getMyGeneral();
        }
        return getCursourMove();
    }


    public Optional<Move> getMove() {
        while (!aggregatedMoves.isEmpty()) {
            Move move = aggregatedMoves.pop();
            if (isValid(move)) {
                return Optional.of(move);
            }
        }

        if (!checkpointMoves.isEmpty()) {
            return validate(checkpointMoves.pop());
        }

        if (cursour.getMyArmySize() > 1) {
            Move cursourMove = getCursourMove();
            return validate(cursourMove);
        }

        Optional<Move> expansionMove = getExpansionMove();
        if (expansionMove.isPresent()) {
            return validate(expansionMove.get());
        }

        return Optional.empty();
    }

    private Move getCursourMove() {
        Tile moveFrom = cursour;
        Scores penalties = Scores.of(scorers, tileHandler);
        cursour = penalties.getMin(cursour.getNeighbours());
        return new Move(moveFrom, cursour, "Cursour");
    }

    private Optional<Move> validate(Move move) {
        if (isValid(move)) {
            return Optional.of(move);
        } else {
            log.warn("Invalid move at turn:{} from:{} to:{}", tileHandler.getTurn(), tileHandler.getTile(move.getFrom()), tileHandler.getTile(move.getTo()));
            return Optional.empty();
        }
    }

    public void initializeNewRound() {
        createCheckpointPath();
        createAggregateMoves();
    }

    private void createCheckpointPath() {
        checkpointMoves.clear();
        Scores penalties = Scores.of(scorers, tileHandler);
        Tile goal = penalties.getMin(tileHandler.getTiles());
        Tile moveFrom = tileHandler.getMyGeneral();
        createPath(penalties, goal, moveFrom).stream()
                .filter(move -> tileHandler.getTile(move.getFrom()).getField().isVisible())
                .forEach(checkpointMoves::add);
        cursour = tileHandler.getTile(checkpointMoves.getLast().getTo());
        String path = checkpointMoves.stream().map(Move::getTo).map(Object::toString).collect(Collectors.joining(","));
        log.info("Created checkpoint path:{} with cursour:{}", path, cursour);
    }

    private List<Move> createPath(Scores penalties, Tile goal, Tile moveFrom) {
        List<Move> ret = new ArrayList<>();
        int loopSafety = 0;
        while (moveFrom != goal && tileHandler.getTile(moveFrom.getIndex()).getField().isVisible() && loopSafety++ < 200) {
            Tile moveTo = penalties.getMin(moveFrom.getNeighbours());
            ret.add(new Move(moveFrom, moveTo, "Path"));
            moveFrom = moveTo;
            if (moveTo == goal) {
                return ret;
            }
        }
        return ret;
    }

    private Optional<Move> getExpansionMove() {
        Set<Tile> checkpointTiles = checkpointMoves.stream()
                .flatMap(move -> Stream.of(move.getFrom(), move.getTo()))
                .map(tileHandler::getTile)
                .distinct()
                .collect(Collectors.toSet());
        return Arrays.stream(tileHandler.getTiles())
                .filter(Tile::isMine)
                .filter(tile -> tileHandler.getMyGeneral() != tile)
                .filter(tile -> !checkpointTiles.contains(tile))
                .map(from -> Arrays.stream(from.getNeighbours())
                        .filter(from::canCapture)
                        .findAny()
                        .map(to -> (new Move(from, to, "Expansion"))))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    private void createAggregateMoves() {
        aggregatedMoves.clear();

        for (int i = 0; i < 8; i++) {//todo config
            List<Integer> tagged = Stream.concat(checkpointMoves.stream(), aggregatedMoves.stream())
                    .flatMap(m -> Stream.of(m.getFrom(), m.getTo()))
                    .collect(Collectors.toList());

            List<Move> m = getNeighbourAggregationMoves(tagged);
            if (m == null) {
                break;
            }
            m.forEach(aggregatedMoves::addFirst);
        }

        String path = aggregatedMoves.stream().map(Move::getFrom).map(Object::toString).collect(Collectors.joining(","));
        log.info("Created aggregated path:{}", path);
    }

    private List<Move> getNeighbourAggregationMoves(List<Integer> tagged) {
        for (Integer toIndex : tagged) {
            List<Move> moves = Arrays.stream(tileHandler.getTile(toIndex).getNeighbours())
                    .filter(from -> from.getMyArmySize() > 1)
                    .map(Tile::getIndex)
                    .filter(from -> !tagged.contains(from))
                    .map(from -> new Move(from, toIndex, "Aggregation"))
                    .collect(Collectors.toList());
            if (!moves.isEmpty()) {
                return moves;
            }
        }

        return null;
    }

    private boolean isValid(Move move) {
        if (move == null) {
            return false;
        }
        Tile from = tileHandler.getTile(move.getFrom());
        Tile to = tileHandler.getTile(move.getTo());
        if (to.getField().isObstacle()) {
            throw new IllegalStateException("Can't move a mountain");
        }
        if (from.getMyArmySize() < 2) {
            //not my tile or not enough army
            return false;
        }
        if (!to.isMine() && from.getMyArmySize() < to.getField().asVisibleField().getArmy()) {
            //unsuccessful attack
            return false;
        }
        return true;
    }

    public List<Locator> getLocators() {
        return locators;
    }

    public List<Scorer> getScorers() {
        return scorers;
    }

}