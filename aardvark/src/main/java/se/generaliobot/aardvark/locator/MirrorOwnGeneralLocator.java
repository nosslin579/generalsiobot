package se.generaliobot.aardvark.locator;

import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;

import java.util.Map;

public class MirrorOwnGeneralLocator implements Locator {
    private final ScoreMap scoreMap;

    public MirrorOwnGeneralLocator(ScoreMap scoreMap) {
        this.scoreMap = scoreMap;
    }

    @Override
    public Scores getLocationScore() {
        FieldWrapper myGeneral = scoreMap.getMyGeneral();
        int x = scoreMap.getWidth() - myGeneral.getX() - 1;
        int y = scoreMap.getHeight() - myGeneral.getY() - 1;
        FieldWrapper mostLikely = scoreMap.getTile(x, y);
        Map<Integer, Double> negativeDistances = mostLikely.getMovePenalty(scoreMap, fieldWrapper -> -1d);
        return new Scores(negativeDistances, -10000d);
    }
}
