package se.nosslin579.aardvark;

import pl.joegreen.sergeants.framework.model.Field;

class Move {
    private final int from;
    private final int to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public Move(Field from, Field to) {
        this(from.getIndex(), to.getIndex());
    }

    public Move(FieldWrapper from, FieldWrapper to) {
        this(from.getIndex(), to.getIndex());
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
                '}';
    }
}
