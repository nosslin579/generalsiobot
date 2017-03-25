package se.nosslin579.trainer;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.GameState;
import pl.joegreen.sergeants.framework.model.VisibleField;

public class GameStateUtil {
    public static VisibleField getMyGeneral(GameState gameState) {
        return gameState.getFields().stream()
                .filter(Field::isVisible)
                .map(Field::asVisibleField)
                .filter(VisibleField::isOwnedByMe)
                .findAny()
                .orElseGet(() -> {
                    throw new IllegalStateException("No general");
                });
    }

    public static int getDistance(Field f1, Field f2) {
        int cols = Math.abs(f1.getPosition().getCol() - f2.getPosition().getCol());
        int rows = Math.abs(f1.getPosition().getRow() - f2.getPosition().getRow());
        return cols + rows;
    }
}
