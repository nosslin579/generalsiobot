package se.generaliobot.copter.scorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;
import se.generaliobot.copter.config.Config;
import se.generaliobot.copter.locator.Locator;

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
    public Scores getScores(TileHandler tileHandler) {
        Scores crownScores = getCrownScores(tileHandler);
        int mostLikelyIndex = crownScores.getMax();
        log.debug("Guessing general is at index: {} with a score of {}", mostLikelyIndex, crownScores.getScore(mostLikelyIndex));
        Map<Integer, Double> distances = tileHandler.getTile(mostLikelyIndex).getMovePenalty(tileHandler, this::getPenaltyFor);
        return new Scores(distances, 10000d);//config
    }

    private Scores getCrownScores(TileHandler tileHandler) {
        Scores scores = new Scores();
        locators.stream()
                .map(Locator::getLocationScore)
                .forEach(scores::add);
        return scores;
    }

    private Double getPenaltyFor(Tile tile) {
        if (tile.getField().isVisible()) {
            int army = tile.getField().asVisibleField().getArmy();
            Double penalty = tile.getLastKnown().getPenalty(config, army);
            return penalty < 0d ? 0d : penalty;
        } else {
            Double penalty = tile.getLastKnown().getPenalty(config);
            return penalty < 0d ? 0d : penalty;
        }
    }

}
