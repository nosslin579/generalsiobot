package se.nosslin579.aardvark.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;

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
