package se.generaliobot.dramp.mcts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.dramp.Board;
import se.generaliobot.dramp.Move;
import se.generaliobot.dramp.State;

import java.util.List;

public class MonteCarloTreeSearch {
  private Logger log = LoggerFactory.getLogger(this.getClass());
  private int depth = 0;

  public Move findNextMove(Board board, State state) {
    depth = 0;
    Node.nodes = 0;
    long end = System.currentTimeMillis() + 500;

    Node rootNode = new Node(board, state);

    while (System.currentTimeMillis() < end) {
      // Phase 1 - Selection
      Node promisingNode = selectPromisingNode(rootNode);
      // Phase 2 - Expansion
      if (promisingNode.getState().isAlive()) {
        expandNode(promisingNode);
      }

      // Phase 3 - Simulation
      Node nodeToExplore = promisingNode;
      if (promisingNode.getChildArray().size() > 0) {
        nodeToExplore = promisingNode.getRandomChildNode();
      }
      int score = simulateRandomPlayout(nodeToExplore);
      // Phase 4 - Update
      backPropagation(nodeToExplore, score);
    }
    if (rootNode.getChildArray().isEmpty()) {
      return null;
    }
    Node winnerNode = rootNode.getChildWithMaxScore();
    log.info("Found node, depth:{}, nodes:{}", depth, Node.nodes);
    return winnerNode.getState().getMove();
  }

  private Node selectPromisingNode(Node rootNode) {
    Node node = rootNode;
    while (node.getChildArray().size() != 0) {
      node = UCT.findBestNodeWithUCT(node);
    }
    return node;
  }

  private void expandNode(Node parent) {
    List<State> possibleStates = parent.getState().getAllPossibleStates(parent.getBoard());
    possibleStates.forEach(state -> {
      Node newChild = new Node(parent.getBoard(), state);
      newChild.setParent(parent);
      parent.getChildArray().add(newChild);
    });
  }

  private void backPropagation(Node nodeToExplore, int score) {
    Node tempNode = nodeToExplore;
    int depth = 0;
    while (tempNode != null) {
      tempNode.getState().incrementVisit();
      tempNode.getState().addScore(score);
      tempNode = tempNode.getParent();
      depth++;
    }

    if (depth > this.depth) {
      this.depth = depth;
    }
  }

  private int simulateRandomPlayout(Node node) {
    if (true) {
      return node.getState().getWinScore();
    }
    State state = node.getState();
    int current = state.getTick();
    int max = current + 50;
    for (int i = current; i < max; i++) {
      List<State> allPossibleStates = state.getAllPossibleStates(node.getBoard());
      int noOfPossibleMoves = allPossibleStates.size();
      int selectRandom = (int) (Math.random() * ((noOfPossibleMoves - 1) + 1));
      state = allPossibleStates.get(selectRandom);
    }
    return state.getWinScore();
  }

}
