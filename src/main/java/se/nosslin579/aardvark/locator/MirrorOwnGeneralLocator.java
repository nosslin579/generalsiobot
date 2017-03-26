package se.nosslin579.aardvark.locator;

import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;

import java.util.Map;

public class MirrorOwnGeneralLocator implements Locator {
    private final Scores scores;

    public MirrorOwnGeneralLocator(ScoreMap scoreMap) {
        FieldWrapper myGeneral = scoreMap.getMyGeneral();
        int x = scoreMap.getWidth() - myGeneral.getX();
        int y = scoreMap.getHeight() - myGeneral.getY();
        FieldWrapper mostLikely = scoreMap.getTile(x, y);
        Map<Integer, Integer> negativeDistances = mostLikely.getDistancesDynamic(-1);
        scores = new Scores(negativeDistances, -10000);//config
    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }
}
