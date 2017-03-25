package se.nosslin579.aardvark.scorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Util;
import se.nosslin579.aardvark.locator.Locator;

import java.util.List;

public class LocatorScorer implements Scorer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Locator> locators;

    public LocatorScorer(List<Locator> locators) {
        this.locators = locators;
    }

    @Override
    public int[] getScores(ScoreMap scoreMap) {
        int[] crownScores = getCrownScores(scoreMap);
        int mostLikelyIndex = Util.getIndexOfMaxValue(crownScores);
        log.info("Guessing general is at index: {} with a score of {}", mostLikelyIndex, crownScores[mostLikelyIndex]);
        int[] ints = Util.invertDistance(scoreMap, mostLikelyIndex);
        return ints;
//        log.info("Square 50 is {}",ret[50]);
    }

    private int[] getCrownScores(ScoreMap scoreMap) {
        int[] scores = new int[scoreMap.getFieldWrappers().length];
        for (Locator locator : locators) {
            int[] locationScore = locator.getLocationScore();
            for (int i = 0; i < scores.length; i++) {
                scores[i] += locationScore[i];
            }
        }
        return scores;
    }
}
