package se.nosslin579.aardvark.scorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nosslin579.aardvark.Config;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;
import se.nosslin579.aardvark.locator.Locator;

import java.util.List;
import java.util.Map;

public class LocatorScorer implements Scorer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<Locator> locators;
    private final Config config;

    public LocatorScorer(List<Locator> locators, Config config) {
        this.locators = locators;
        this.config = config;
    }

    @Override
    public Scores getScores(ScoreMap scoreMap) {
        Scores crownScores = getCrownScores(scoreMap);
        int mostLikelyIndex = crownScores.getMax();
        log.info("Guessing general is at index: {} with a score of {}", mostLikelyIndex, crownScores.getScore(mostLikelyIndex));
        Map<Integer, Double> distances = scoreMap.getTile(mostLikelyIndex).getMovePenalty(scoreMap, neighbour -> neighbour.getLastKnown().getPenalty(config));
        return new Scores(distances, 10000d);//config
    }

    private Scores getCrownScores(ScoreMap scoreMap) {
        Scores scores = new Scores();
        locators.stream()
                .map(Locator::getLocationScore)
                .forEach(scores::add);
        return scores;
    }
}
