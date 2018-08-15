package se.generaliobot.dramp;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;

public class Board {
  private final Tile[] tiles;

  public Board(Tile[] tiles) {
    this.tiles = tiles;
  }

  public static Board of(GameState state) {
    Tile[] tiles = new Tile[state.getFields().size()];
    for (Field field : state.getFields()) {
      tiles[field.getIndex()] = new Tile(field.getIndex(), field.isObstacle());
    }

    for (Field field : state.getFields()) {
      int index = field.getIndex();
      Tile[] objects = field.getNeighbours().stream()
          .filter(field1 -> !field1.isObstacle())
          .map(field1 -> tiles[field1.getIndex()])
          .sorted((Tile t1, Tile t2) -> t1.getIndex() - t2.getIndex())
          .toArray(Tile[]::new);
      tiles[index].setNeighbours(objects);
    }

    return new Board(tiles);
  }

  public Tile[] getTiles() {
    return tiles;
  }

  public Tile getTile(int index) {
    return tiles[index];
  }
}
