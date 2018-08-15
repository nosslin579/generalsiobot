package se.generaliobot.dramp;

import pl.joegreen.sergeants.framework.model.Field;

public class Move {
  private final int from;
  private final int to;

  public Move(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public Move(Field from, Field to) {
    this(from.getIndex(), to.getIndex());
  }

  public Move(Tile from, Tile to) {
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
