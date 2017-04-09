package se.nosslin579.bamse.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.bamse.Scores;
import se.nosslin579.bamse.TileHandler;
import se.nosslin579.bamse.fieldlisteners.FieldListener;

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
        scores.put(fieldFound.getIndex(), GARANTEED_NOT_HERE);
    }
}
