package se.generaliobot.bamse.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.bamse.Tile;
import se.generaliobot.bamse.TileHandler;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
        Tile field = tileHandler.getTile(fieldFound.getIndex());
        field.setViewed(true);
    }
}
