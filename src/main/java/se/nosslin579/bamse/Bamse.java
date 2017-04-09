package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.bamse.config.Config;

import java.util.function.Function;

public class Bamse implements Bot {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    final Actions actions;
    private final Config config;
    TileHandler tileHandler;
    MoveHandler moveHandler = new MoveHandler();

    public Bamse(Actions actions, Config config) {
        this.actions = actions;
        this.config = config;
    }

    public static Function<Actions, Bot> provider(Config config) {
        return actions -> new Bamse(actions, config);
    }

    @Override
    public void onGameStateUpdate(GameState newGameState) {
        if (tileHandler == null) {
            tileHandler = TileHandler.of(newGameState, config);
            return;
        }

        tileHandler.update(newGameState);


//        Object[] scores = Arrays.stream(scoreMap.getFieldWrappers()).filter(field -> field.getField().isVisible()).toArray();

        if (newGameState.getTurn() > 24) {
            Move m = tileHandler.getMove();
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
