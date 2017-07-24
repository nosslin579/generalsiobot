package se.generaliobot.copter.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.copter.Tile;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(Tile tileFound, VisibleField field) {
        tileFound.setViewed(true);
    }
}
