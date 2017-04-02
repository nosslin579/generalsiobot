package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.aardvark.config.Config;

import java.util.function.Function;

public class Aardvark implements Bot {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    final Actions actions;
    private final Config config;
    ScoreMap scoreMap;

    public Aardvark(Actions actions, Config config) {
        this.actions = actions;
        this.config = config;
    }

    public static Function<Actions, Bot> provider(Config config) {
        return actions -> new Aardvark(actions, config);
    }

    @Override
    public void onGameStateUpdate(GameState newGameState) {
        if (scoreMap == null) {
            scoreMap = ScoreMap.of(newGameState, config);
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
