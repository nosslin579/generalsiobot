package se.generaliobot.copter.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.TileHandler;
import se.generaliobot.copter.fieldlisteners.FieldListener;

public class VisitedFieldsLocator implements Locator, FieldListener {

    private Scores scores;

    public VisitedFieldsLocator() {
        scores = new Scores();
    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

    @Override
    public void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
        scores.setScore(fieldFound.getIndex(), GARANTEED_NOT_HERE);
    }
}
