package se.generaliobot.aardvark.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;

import java.util.HashMap;

public class ExcludeEdgeLocator implements Locator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores(new HashMap<>(), 0d);

    public ExcludeEdgeLocator(ScoreMap scoreMap) {
        int excludeEdgeDistance = scoreMap.getConfig().getExcludeEdgeDistance();
        int xThreshold = scoreMap.getWidth() - excludeEdgeDistance;
        int yThreshold = scoreMap.getHeight() - excludeEdgeDistance;
        for (FieldWrapper fieldWrapper : scoreMap.getFieldWrappers()) {
            if (fieldWrapper.getX() < excludeEdgeDistance || fieldWrapper.getX() >= xThreshold || fieldWrapper.getY() < excludeEdgeDistance || fieldWrapper.getY() >= yThreshold) {
                scores.add(fieldWrapper.getIndex(), -10d);
            }
        }
        log.debug("Exclude edge: {}", scores.getPrettyPrint(scoreMap));
    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

}
