package se.nosslin579.bamse.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.bamse.Tile;
import se.nosslin579.bamse.TileHandler;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
        Tile field = tileHandler.getTile(fieldFound.getIndex());
        field.setViewed(true);
    }
}
