package se.generaliobot.dramp;

import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.framework.model.VisibleField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class State {
  private final int tick;
  private final Field[] fields;
  private int visitCount;
  private final Move move;
  private int score = 0;

  public State(int tick, Field[] fields, int visitCount, Move move) {
    this.tick = tick;
    this.fields = fields;
    this.visitCount = visitCount;
    this.move = move;
  }

  public State(GameState state) {
    Collection<pl.joegreen.sergeants.framework.model.Field> fields = state.getFields();
    Field[] data = new Field[fields.size()];
    for (pl.joegreen.sergeants.framework.model.Field field : fields) {
      int index = field.getIndex();
      if (field.isVisible()) {
        VisibleField visibleField = field.asVisibleField();
        data[index] = new Field(index, visibleField.getArmy(), visibleField.isOwnedByMe(), visibleField.isOwnedByEnemy(), visibleField.isGeneral());
      }
    }
    this.tick = state.getTurn();
    this.fields = data;
    this.visitCount = 0;
    this.move = null;
  }

  public int getVisitCount() {
    return visitCount;
  }

  public int getTick() {
    return tick;
  }

  public int getScore() {
    return score;
  }

  public Move getMove() {
    return move;
  }

  public int getWinScore() {
    return Stream.of(fields)
        .filter(data -> data != null && data.ownedByMe)
        .mapToInt(mine -> mine.armySize)
        .sum();
  }

  public boolean isAlive() {
    for (Field td : fields) {
      if (td != null && td.ownedByMe) {
        return true;
      }
    }
    return false;
  }

  public List<State> getAllPossibleStates(Board board) {
    List<State> ret = new ArrayList<>();
    for (Field f : fields) {
      if (f != null && f.ownedByMe && f.armySize > 1) {
        Tile tile = board.getTile(f.index);
        for (Tile neighbour : tile.getNeighbours()) {
          if (neighbour.isObstacle()) {
            throw new RuntimeException("Illegal move, cant move to obstacle. Index:" + neighbour.getIndex());
          }

          Move move = new Move(f.index, neighbour.getIndex());
          Field[] newFields = move(f, neighbour.getIndex());

          if ((tick % 2) == 0) {
            //turn
            for (int i = 0; i < newFields.length; i++) {
              if (newFields[i] != null) {
                newFields[i] = newFields[i].turn();
              }
            }
          }
          if ((tick % 50) == 0) {
            //round
            for (int i = 0; i < newFields.length; i++) {
              if (newFields[i] != null) {
                newFields[i] = newFields[i].round();
              }
            }
          }

          ret.add(new State(tick + 1, newFields, visitCount, move));
        }
      }
    }
    ret.add(new State(tick + 1, fields, visitCount, null));
    return ret;
  }

  private Field[] move(Field from, int toIndex) {
    Field[] ret = new Field[fields.length];
    System.arraycopy(fields, 0, ret, 0, fields.length);
    ret[toIndex] = from.moveTo(toIndex);
    ret[from.index] = from.moveFrom();
    return ret;
  }

  public void incrementVisit() {
    visitCount++;
  }

  public void addScore(int score) {
    this.score += score;
  }

  private class Field {
    final int index;
    final int armySize;
    final boolean ownedByMe;
    final boolean ownedByEnemy;
    final boolean general;

    private Field(int index, int armySize, boolean ownedByMe, boolean ownedByEnemy, boolean general) {
      this.index = index;
      this.armySize = armySize;
      this.ownedByMe = ownedByMe;
      this.ownedByEnemy = ownedByEnemy;
      this.general = general;
    }


    public Field moveFrom() {
      if (!ownedByMe) {
        throw new RuntimeException("Invalid move, not owned by me. Index:" + index);
      } else if (armySize < 2) {
        throw new RuntimeException("Invalid move, not enough army. Index:" + index);
      }
      return new Field(index, 1, ownedByMe, ownedByEnemy, general);
    }

    public Field moveTo(int toIndex) {
      Field from = this;
      Field to = fields[toIndex];
      if (to == null) {
        return new Field(toIndex, armySize - 1, true, false, false);
      } else if (to.ownedByMe) {
        int army = from.armySize - 1 + to.armySize;
        return new Field(toIndex, army, true, false, to.general);
      } else {
        int army = from.armySize - 1 - to.armySize;
        boolean overtake = army > 0;
        return new Field(toIndex, Math.abs(army), overtake, !overtake && to.ownedByEnemy, to.general);//todo fix general overtake
      }
    }

    public Field turn() {
      return general ? new Field(index, armySize + 1, ownedByMe, ownedByEnemy, true) : this;
    }

    public Field round() {
      return ownedByMe || ownedByEnemy ? new Field(index, armySize + 1, ownedByMe, ownedByEnemy, general) : this;
    }
  }
}
