package se.nosslin579.aardvark.locator;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;

import java.util.Arrays;

public class VisitedFieldsLocator implements Locator, FieldListener {

    private int[] scores;

    public VisitedFieldsLocator(ScoreMap scoreMap) {
        scores = new int[scoreMap.getFieldWrappers().length];
    }

    @Override
    public int[] getLocationScore() {
        int[] ints = Arrays.stream(scores).filter(value -> value < 0).toArray();
        if (scores[50] < 0) {
            System.out.print("");
        }
        return scores;
    }

    @Override
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        scores[fieldFound.getIndex()] = -20000;
    }
}
