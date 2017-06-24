package se.generaliobot.bamse.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.bamse.Scores;
import se.generaliobot.bamse.TileHandler;
import se.generaliobot.bamse.fieldlisteners.FieldListener;

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
