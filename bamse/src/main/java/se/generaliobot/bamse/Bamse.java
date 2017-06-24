package se.generaliobot.bamse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameResult;
import pl.joegreen.sergeants.framework.model.GameStarted;
import pl.joegreen.sergeants.framework.model.GameState;
import se.generaliobot.bamse.config.Config;
import se.generaliobot.bamse.fieldlisteners.FieldListener;
import se.generaliobot.bamse.fieldlisteners.SetViewedFieldListener;
import se.generaliobot.bamse.locator.*;
import se.generaliobot.bamse.scorer.LocatorScorer;
import se.generaliobot.bamse.scorer.Scorer;

import java.util.function.Function;
import java.util.stream.Collectors;

public class Bamse implements Bot {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    final Actions actions;
    private final Config config;
    TileHandler tileHandler;
    MoveHandler moveHandler;

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
            moveHandler = new MoveHandler(tileHandler);

            addBean(new SetViewedFieldListener());
            addBean(new VisitedFieldsLocator());
            addBean(new FoundItLocator());
            addBean(new CutOffLocator(tileHandler));
            addBean(new ExcludeEdgeLocator(tileHandler));
            addBean(new UnreachableLocator(tileHandler));
            addBean(new LocatorScorer(moveHandler.getLocators(), config));
            addBean(new MirrorOwnGeneralLocator(tileHandler));
            addBean(new BorderEnemyLocator(tileHandler));

            int width = newGameState.getColumns();
            int height = newGameState.getRows();
            String obstacles = newGameState.getFields().stream()
                    .filter(Field::isObstacle)
                    .map(Field::getIndex)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            int myGeneral = tileHandler.getMyGeneral().getIndex();
            log.info("Map is {} x {}, my general is at index {} and obstacles is at {}", width, height, myGeneral, obstacles);

        }

        tileHandler.update(newGameState);

//        Object[] scores = Arrays.stream(scoreMap.getFieldWrappers()).filter(field -> field.getField().isVisible()).toArray();


        if (newGameState.getTurn() > 24) {
            int turnOnRound = newGameState.getTurn() % 50;
            int turnsToNextRound = turnOnRound - 50;
            if (turnOnRound == 0) {
                log.debug("New round");
                moveHandler.initializeNewRound();
            }

            if (newGameState.getTurn() < 50) {
                Move move = moveHandler.getFirstRoundMove();
                actions.move(move.getFrom(), move.getTo());
            } else {
                moveHandler.getMove(turnsToNextRound).ifPresent(move -> {
                    log.debug("At turn:{} doing move:{}", newGameState.getTurn(), move);
                    actions.move(move.getFrom(), move.getTo());
                });
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

    @Override
    public String toString() {
        return "Bamse";
    }
}
