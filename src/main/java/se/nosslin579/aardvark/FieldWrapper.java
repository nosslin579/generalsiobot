package se.nosslin579.aardvark;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.FieldTerrainType;

import java.util.*;

public class FieldWrapper {
    public static final int UNSET = Integer.MIN_VALUE;
    private Field field;
    private FieldWrapper[] neighbours;
    private boolean viewed = false;

    public FieldWrapper(Field field) {
        this.field = field;
    }

    private static Type getField(FieldTerrainType terrainType) {
        Map<FieldTerrainType, Type> map = new HashMap<>();
        map.put(FieldTerrainType.EMPTY, Type.EMPTY);
        map.put(FieldTerrainType.FOG, Type.EMPTY);
        map.put(FieldTerrainType.FOG_OBSTACLE, Type.OBSTACLE);
        map.put(FieldTerrainType.MOUNTAIN, Type.OBSTACLE);
        map.put(FieldTerrainType.OWNED, Type.OWN);
        return map.get(terrainType);
    }

    public boolean isMine() {
        return field.isVisible() && field.asVisibleField().isOwnedByMe();
    }

    public void setField(Field field) {
        this.field = field;
    }

    enum Type {EMPTY, ENEMY, OBSTACLE, OWN}

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
     * Key=Index, Value=Distance to field with index or null if unreachable
     */
    public Map<Integer, Double> getDistancesDynamic(int distanceModifier) {
        Map<Integer, Double> distances = new HashMap<>();
        distances.put(field.getIndex(), 0d);
        List<FieldTerrainType> obstacles = Arrays.asList(FieldTerrainType.FOG_OBSTACLE, FieldTerrainType.MOUNTAIN);
        Deque<Field> que = new ArrayDeque<>();
        que.add(field);
        while (!que.isEmpty()) {
            Field t = que.pop();
            for (Field neighbour : t.getNeighbours()) {
                boolean isObstacle = obstacles.contains(neighbour.getTerrainType());
                boolean isFreeCity = neighbour.isVisible() && neighbour.asVisibleField().isCity() && !neighbour.asVisibleField().hasOwner();
                boolean isAlreadySet = distances.containsKey(neighbour.getIndex());
                boolean isSource = neighbour == field;
                if (!isFreeCity && !isObstacle && !isAlreadySet && !isSource) {
                    Double newDistance = distances.get(t.getIndex()) + distanceModifier;
                    distances.put(neighbour.getIndex(), newDistance);
                    que.addLast(neighbour);
                }
            }
        }
        return distances;
    }

    /**
     * Key=Index, Value=Penalty score for moving to or null if obstacle
     */
    public Map<Integer, Double> getMovePenalty(ScoreMap scoreMap) {
        Map<Integer, Double> penalties = new HashMap<>();
        penalties.put(field.getIndex(), 0d);
        List<FieldTerrainType> obstacles = Arrays.asList(FieldTerrainType.FOG_OBSTACLE, FieldTerrainType.MOUNTAIN);
        Deque<Integer> que = new ArrayDeque<>();
        que.add(getIndex());
        while (!que.isEmpty()) {
            FieldWrapper cursor = scoreMap.getTile(que.pop());
            for (Field f : cursor.getField().getNeighbours()) {
                FieldWrapper neighbour = scoreMap.getTile(f.getIndex());
                Double currentPenalty = penalties.get(neighbour.getIndex());
                Double newPenalty = penalties.get(cursor.getIndex()) + scoreMap.getPenalty(neighbour);
                if (currentPenalty == null || newPenalty < currentPenalty) {
                    penalties.put(neighbour.getIndex(), newPenalty);
                    que.addLast(neighbour.getIndex());
                }
            }
        }
        return penalties;
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
