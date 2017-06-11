package se.nosslin579.bamse;

import pl.joegreen.sergeants.framework.model.Field;

public class Move {
    private final int from;
    private final int to;
    private final String type;

    public Move(int from, int to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Move(Field from, Field to, String type) {
        this(from.getIndex(), to.getIndex(), type);
    }

    public Move(Tile from, Tile to, String type) {
        this(from.getIndex(), to.getIndex(), type);
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + from +
                ", to=" + to +
                ", type=" + type +
                '}';
    }
}
