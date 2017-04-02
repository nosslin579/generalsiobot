package se.nosslin579.aardvark.locator;

import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;

import java.util.Map;

public class MirrorOwnGeneralLocator implements Locator {
    private final ScoreMap scoreMap;

    public MirrorOwnGeneralLocator(ScoreMap scoreMap) {
        this.scoreMap = scoreMap;
    }

    @Override
    public Scores getLocationScore() {
        FieldWrapper myGeneral = scoreMap.getMyGeneral();
        int x = scoreMap.getWidth() - myGeneral.getX();
        int y = scoreMap.getHeight() - myGeneral.getY();
        FieldWrapper mostLikely = scoreMap.getTile(x, y);
        Map<Integer, Double> negativeDistances = mostLikely.getMovePenalty(scoreMap, fieldWrapper -> -1d);
        return new Scores(negativeDistances, -10000d);
    }
}
