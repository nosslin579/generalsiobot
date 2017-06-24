package se.generaliobot.copter;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move = (Move) o;

        if (from != move.from) return false;
        if (to != move.to) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }
}
