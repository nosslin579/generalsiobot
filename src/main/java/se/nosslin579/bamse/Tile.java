package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;

import java.util.*;
import java.util.function.Function;

public class Tile {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Field field;
    private Tile[] neighbours;
    private boolean viewed = false;
    private TileType lastKnown = TileType.UNKNOWN;

    public Tile(Field field) {
        this.field = field;
    }

    public boolean isMine() {
        return field.isVisible() && field.asVisibleField().isOwnedByMe();
    }

    public int getMyArmySize() {
        return isMine() ? getField().asVisibleField().getArmy() : 0;
    }

    public Optional<Field> updateField(Field field) {
        TileType updated = lastKnown.getByField(field);
        Optional<Field> ret = updated == lastKnown ? Optional.empty() : Optional.of(this.field);
        lastKnown = updated;
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
     * Key=Index, Value=Penalty score for moving to or null if obstacle
     */
    public Map<Integer, Double> getMovePenalty(TileHandler tileHandler, Function<Tile, Double> diff) {
        Map<Integer, Double> penalties = new HashMap<>();
        penalties.put(field.getIndex(), 0d);
        Deque<Integer> que = new ArrayDeque<>();
        que.add(getIndex());
        while (!que.isEmpty()) {
            Tile cursor = tileHandler.getTile(que.pop());
            for (Field f : cursor.getField().getNeighbours()) {

                Tile neighbour = tileHandler.getTile(f.getIndex());
                if (neighbour.getField().isObstacle()) {
                    continue;
                }

                Double currentPenalty = penalties.get(neighbour.getIndex());
                Double newPenalty = penalties.get(cursor.getIndex()) + diff.apply(neighbour) + tileHandler.getConfig().getMandatoryMovePenalty();

                if (currentPenalty == null || newPenalty < currentPenalty) {
                    penalties.put(neighbour.getIndex(), newPenalty);
                }

                if (currentPenalty == null) {
                    que.addLast(neighbour.getIndex());
                }

            }
        }
        return penalties;
    }

    public TileType getLastKnown() {
        return lastKnown;
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
        return "FieldWrapper{" +
                "index=" + field.getIndex() +
                ", field=" + field.getTerrainType() +
                '}';
    }
}
