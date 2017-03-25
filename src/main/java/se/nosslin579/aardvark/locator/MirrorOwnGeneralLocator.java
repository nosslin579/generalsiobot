package se.nosslin579.aardvark.locator;

import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Util;

public class MirrorOwnGeneralLocator implements Locator {
    private final int[] scores;

    public MirrorOwnGeneralLocator(ScoreMap scoreMap) {
        FieldWrapper myGeneral = scoreMap.getMyGeneral();
        int x = scoreMap.getWidth() - myGeneral.getX();
        int y = scoreMap.getHeight() - myGeneral.getY();
        FieldWrapper mostLikely = scoreMap.getTile(x, y);
        scores = Util.invertDistance(scoreMap, mostLikely.getIndex());
    }

    @Override
    public int[] getLocationScore() {
        return scores;
    }
}
