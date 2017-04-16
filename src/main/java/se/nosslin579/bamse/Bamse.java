package se.nosslin579.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;
import se.nosslin579.bamse.config.Config;
import se.nosslin579.bamse.fieldlisteners.FieldListener;
import se.nosslin579.bamse.fieldlisteners.SetViewedFieldListener;
import se.nosslin579.bamse.locator.*;
import se.nosslin579.bamse.scorer.LocatorScorer;
import se.nosslin579.bamse.scorer.Scorer;

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

            addBean(new SetViewedFieldListener());
            addBean(new VisitedFieldsLocator());
            addBean(new FoundItLocator());
            addBean(new CutOffLocator(tileHandler));
            addBean(new ExcludeEdgeLocator(tileHandler));
            addBean(new UnreachableLocator(tileHandler));
            addBean(new LocatorScorer(moveHandler.getLocators(), config));
            addBean(new MirrorOwnGeneralLocator(tileHandler));

            return;
        }

        tileHandler.update(newGameState);
//        moveHandler.update(newGameState, tileHandler);


//        Object[] scores = Arrays.stream(scoreMap.getFieldWrappers()).filter(field -> field.getField().isVisible()).toArray();

        if (newGameState.getTurn() > 24) {
            for (Move move : moveHandler.getMoves(tileHandler)) {
                actions.move(move.getFrom(), move.getTo());
            }
        }
    }

    private void addBean(Object bean) {
        if (bean instanceof FieldListener) {
            tileHandler.getFieldListeners().add((FieldListener) bean);
        }
        if (bean instanceof Locator) {
            moveHandler.getLocators().add((Locator) bean);
        }
        if (bean instanceof Scorer) {
            moveHandler.getScorers().add((Scorer) bean);
        }

    }

    @Override
    public void onGameFinished(GameResult gameResult) {

    }

    @Override
    public void onGameStarted(GameStarted gameStarted) {
    }

}
