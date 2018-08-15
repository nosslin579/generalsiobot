package se.generaliobot.dramp;

public class Tile {
  private final int index;
  private final boolean obstacle;
  private Tile[] neighbours;

  public Tile(int index, boolean obstacle) {
    this.index = index;
    this.obstacle = obstacle;
  }

  public void setNeighbours(Tile[] neighbours) {
    this.neighbours = neighbours;
  }

  public Tile[] getNeighbours() {
    return neighbours;
  }

  public int getIndex() {
    return index;
  }

  public boolean isObstacle() {
    return obstacle;
  }
}
