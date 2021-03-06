package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Tile {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Field field;
    private Tile[] neighbours;
    private boolean viewed = false;
    private TileType lastKnown = TileType.UNKNOWN;
    private Deque<TileType> history = new ArrayDeque<>();
    private final Map<Direction, Tile> neighboursByPosition = new HashMap<>();
    private int distanceToOwnCrown = Integer.MAX_VALUE;

    public Tile(Field field) {
        this.field = field;
    }

    public boolean isMine() {
        return field.isVisible() && field.asVisibleField().isOwnedByMe();
    }

    public int getMyArmySize() {
        return isMine() ? getField().asVisibleField().getArmy() : 0;
    }

    public boolean canCapture(Tile tile) {
        return isMine() && !tile.isMine() && !tile.getField().isObstacle() && ((field.asVisibleField().getArmy() - tile.field.asVisibleField().getArmy()) > 1);
    }

    public boolean isEnemy() {
        return field.isVisible() && field.asVisibleField().isOwnedByEnemy() || Arrays.asList(TileType.ENEMY, TileType.ENEMY_CITY, TileType.ENEMY_CROWN).contains(lastKnown);
    }

    public Optional<Field> updateField(Field field) {
        TileType updated = lastKnown.getByField(field);
        Optional<Field> ret = updated == lastKnown ? Optional.empty() : Optional.of(this.field);
        lastKnown = updated;
        history.addLast(lastKnown);
        this.field = field;
        return ret;
    }

    public int getIndex() {
        return field.getIndex();
    }

    public Field getField() {
        return field;
    }

    /**
     * Zero indexed
     */
    public int getX() {
        return field.getPosition().getCol();
    }

    /**
     * Zero indexed
     */
    public int getY() {
        return field.getPosition().getRow();
    }


    /**
     * Key=Index, Value=Score for moving to or null if obstacle
     */
    public Scores getMoveScore(Function<Tile, Double> penalty) {
        Scores ret = new Scores(-1000d);
        ret.setScore(this, 0d);
        Deque<Tile> que = new ArrayDeque<>();
        que.add(this);
        while (!que.isEmpty()) {
            Tile cursor = que.pop();
            for (Tile neighbour : cursor.getNeighbours()) {
                if (neighbour.getField().isObstacle()) {
                    continue;
                }

                Double currentScore = ret.getScore(neighbour);
                Double newScore = ret.getScore(cursor) - penalty.apply(neighbour);

                if (newScore > currentScore) {
                    ret.setScore(neighbour, newScore);
                    que.addLast(neighbour);
                }
            }
        }
        return ret;
    }

    public TileType getLastKnown() {
        return lastKnown;
    }

    public Deque<TileType> getHistory() {
        return history;
    }

    public Tile[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Tile[] neighbours) {
        this.neighbours = neighbours;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "index=" + getIndex() +
                ", type=" + field.getTerrainType() +
                ", mine=" + isMine() +
                '}';
    }

    public Tile[] getSurroundingTiles() {
        Map<Tile, Long> collect = Arrays.stream(neighbours)
                .map(Tile::getNeighbours)
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect.keySet().stream()
                .filter(tile -> collect.get(tile) > 1)
                .toArray(Tile[]::new);
    }

    public Map<Direction, Tile> getNeighboursByPosition() {
        return neighboursByPosition;
    }

    public int getDistanceToOwnCrown() {
        return distanceToOwnCrown;
    }

    public void setDistanceToOwnCrown(int distanceToOwnCrown) {
        this.distanceToOwnCrown = distanceToOwnCrown;
    }
}
