package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;

public class Aardvark implements Bot {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    final Actions actions;
    ScoreMap scoreMap;

    public Aardvark(Actions actions) {
        this.actions = actions;
    }


    @Override
    public void onGameStateUpdate(GameState newGameState) {
        if (newGameState.getTurn() == 0) {
            scoreMap = ScoreMap.of(newGameState);
            return;
        }

        scoreMap.update(newGameState);

//        Object[] scores = Arrays.stream(scoreMap.getFieldWrappers()).filter(field -> field.getField().isVisible()).toArray();

        if (newGameState.getTurn() > 24) {
            Move m = scoreMap.getMove();
            actions.move(m.getFrom(), m.getTo());
        }
    }

    @Override
    public void onGameFinished(GameResult gameResult) {

    }

    @Override
    public void onGameStarted(GameStarted gameStarted) {
    }

}
