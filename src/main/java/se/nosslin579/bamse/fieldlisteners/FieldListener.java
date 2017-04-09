package se.nosslin579.bamse.fieldlisteners;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.bamse.Tile;
import se.nosslin579.bamse.TileHandler;

public interface FieldListener {
    default void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
    }

    default void onFieldChange(Tile wrapper, Field old) {
    }

    default void onCaptured(TileHandler tileHandler, Tile captured) {
    }
}
