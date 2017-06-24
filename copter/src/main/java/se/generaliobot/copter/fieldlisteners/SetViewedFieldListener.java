package se.generaliobot.copter.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
        Tile field = tileHandler.getTile(fieldFound.getIndex());
        field.setViewed(true);
    }
}
