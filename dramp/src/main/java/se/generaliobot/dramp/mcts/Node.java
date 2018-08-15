package se.generaliobot.dramp.mcts;

import se.generaliobot.dramp.Board;
import se.generaliobot.dramp.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node {
  private final Board board;
  private final State state;
  private Node parent;
  private final List<Node> childArray = new ArrayList<>();

  public static int nodes = 0;

  public Node(Board board, State state) {
    this.board = board;
    this.state = state;
    nodes++;
  }

  public State getState() {
    return state;
  }

  public Board getBoard() {
    return board;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public List<Node> getChildArray() {
    return childArray;
  }

  public Node getRandomChildNode() {
    int noOfPossibleMoves = this.childArray.size();
    int selectRandom = (int) (Math.random() * ((noOfPossibleMoves - 1) + 1));
    return this.childArray.get(selectRandom);
  }

  public Node getChildWithMaxScore() {
    return Collections.max(this.childArray, Comparator.comparing(c -> c.getState().getVisitCount()));
  }

}
