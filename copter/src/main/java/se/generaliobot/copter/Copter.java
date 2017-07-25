package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.Actions;
import pl.joegreen.sergeants.framework.Bot;
import pl.joegreen.sergeants.framework.model.*;
import se.generaliobot.copter.config.Config;
import se.generaliobot.copter.fieldlisteners.FieldListener;
import se.generaliobot.copter.fieldlisteners.SetViewedFieldListener;
import se.generaliobot.copter.locator.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Copter implements Bot {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    final Actions actions;
    private final Config config;
    TileHandler tileHandler;
    MoveHandler moveHandler;

    public Copter(Actions actions, Config config) {
        this.actions = actions;
        this.config = config;
    }

    public static Function<Actions, Bot> provider() {
        return actions -> new Copter(actions, new Config());
    }

    @Override
    public void onGameStateUpdate(GameState newGameState) {
        if (tileHandler == null) {
            tileHandler = TileHandler.of(newGameState, config);
            moveHandler = new MoveHandler(tileHandler);

            addBean(new SetViewedFieldListener());
            addBean(new VisitedFieldsLocator());
            addBean(new FoundItLocator(tileHandler));
            addBean(new CutOffLocator(tileHandler));
            addBean(new ExcludeEdgeLocator(tileHandler));
            addBean(new UnreachableLocator(tileHandler));
            addBean(new MirrorOwnGeneralLocator(tileHandler));
            addBean(new BorderEnemyLocator());

            int width = newGameState.getColumns();
            int height = newGameState.getRows();
            String obstacles = newGameState.getFields().stream()
                    .filter(Field::isObstacle)
                    .map(Field::getIndex)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            int myGeneral = tileHandler.getMyGeneral().getIndex();
            log.info("Map is {} x {}, my general is at index {} and obstacles is at {}", width, height, myGeneral, obstacles);
            moveHandler.initNewGame();

        }

        tileHandler.update(newGameState);

//        Object[] scores = Arrays.stream(scoreMap.getFieldWrappers()).filter(field -> field.getField().isVisible()).toArray();


        if (newGameState.getTurn() > 24) {
            int roundNo = newGameState.getTurn() / 50;
            int turnOnRound = newGameState.getTurn() % 50;
            int turnsToNextRound = turnOnRound - 50;
            if (turnOnRound == 0) {
                moveHandler.initializeNewRound(roundNo);
            }
            int myPlayerIndex = newGameState.getMyPlayerIndex();
            Player enemy = newGameState.getPlayers().stream().filter(player -> player.getIndex().equals(myPlayerIndex)).findAny().get();
            if (newGameState.getTurn() == 60) {
                System.out.println("");
            }

            Optional<Move> m = moveHandler.getMove(roundNo, turnsToNextRound, enemy);
            if (m.isPresent()) {
                Move move = m.get();
                log.debug("At turn:{} doing move:{}", newGameState.getTurn(), move);
                actions.move(move.getFrom(), move.getTo());
            } else {
                log.warn("No move at turn:{}", newGameState.getTurn());
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
    }

    @Override
    public void onGameFinished(GameResult gameResult) {

    }

    @Override
    public void onGameStarted(GameStarted gameStarted) {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
