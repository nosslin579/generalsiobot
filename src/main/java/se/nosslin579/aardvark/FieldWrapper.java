package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FieldWrapper {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Field field;
    private FieldWrapper[] neighbours;
    private boolean viewed = false;
    private FieldType lastKnown = FieldType.UNKNOWN;

    public FieldWrapper(Field field) {
        this.field = field;
    }

    public boolean isMine() {
        return field.isVisible() && field.asVisibleField().isOwnedByMe();
    }

    public void setField(Field field) {
        lastKnown = lastKnown.getByField(field);
        this.field = field;
    }

    public int getIndex() {
        return field.getIndex();
    }

    public Field getField() {
        return field;
    }

    public int getX() {
        return field.getPosition().getCol();
    }

    public int getY() {
        return field.getPosition().getRow();
    }

    /**
     * Key=Index, Value=Penalty score for moving to or null if obstacle
     */
    public Map<Integer, Double> getMovePenalty(ScoreMap scoreMap, Function<FieldWrapper, Double> diff) {
        Map<Integer, Double> penalties = new HashMap<>();
        penalties.put(field.getIndex(), 0d);
        Deque<Integer> que = new ArrayDeque<>();
        que.add(getIndex());
        while (!que.isEmpty()) {
            FieldWrapper cursor = scoreMap.getTile(que.pop());
            for (Field f : cursor.getField().getNeighbours()) {

                FieldWrapper neighbour = scoreMap.getTile(f.getIndex());
                if (neighbour.getField().isObstacle()) {
                    continue;
                }

                Double currentPenalty = penalties.get(neighbour.getIndex());
                Double newPenalty = penalties.get(cursor.getIndex()) + diff.apply(neighbour) + scoreMap.getConfig().getMandatoryMovePenalty();

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

    public FieldType getLastKnown() {
        return lastKnown;
    }

    public FieldWrapper[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(FieldWrapper[] neighbours) {
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
