package se.generaliobot.dramp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;
import se.generaliobot.dramp.mcts.MonteCarloTreeSearch;

import java.util.function.Function;

public class Dramp implements Bot {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Actions actions;
  private Board board;
  private MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();

  public Dramp(Actions actions) {
    this.actions = actions;
  }

  public static Function<Actions, Bot> provider() {
    return Dramp::new;
  }

  @Override
  public void onGameStateUpdate(GameState gameState) {
    if (board == null) {
      board = Board.of(gameState);
    }
    State bs = new State(gameState);
    Move m = mcts.findNextMove(board, bs);
    if (m != null) {
      actions.move(m.getFrom(), m.getTo());
    }
  }

  @Override
  public void onGameFinished(GameResult gameResult) {
    log.info("Game end {}", gameResult);
  }

  @Override
  public void onGameStarted(GameStarted gameStarted) {
    log.info("Game started {}", gameStarted);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
