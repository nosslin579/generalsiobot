package se.generaliobot.copter.fieldlisteners;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;

public interface FieldListener {
    default void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
    }

    default void onFieldChange(Tile wrapper, Field old) {
    }

    default void onCaptured(TileHandler tileHandler, Tile captured) {
    }
}
