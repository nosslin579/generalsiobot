package se.generaliobot.aardvark.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;
import se.generaliobot.aardvark.fieldlisteners.FieldListener;

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
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        scores.put(fieldFound.getIndex(), GARANTEED_NOT_HERE);
    }
}
