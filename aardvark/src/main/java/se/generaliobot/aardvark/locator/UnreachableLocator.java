package se.generaliobot.aardvark.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;

import java.util.HashMap;
import java.util.Map;

public class UnreachableLocator implements Locator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores(new HashMap<>(), 0d);

    public UnreachableLocator(ScoreMap scoreMap) {
        Map<Integer, Double> movePenalty = scoreMap.getMyGeneral().getMovePenalty(scoreMap, fieldWrapper1 -> 1d);
        for (FieldWrapper fieldWrapper : scoreMap.getFieldWrappers()) {
            if (!movePenalty.containsKey(fieldWrapper.getIndex())) {
                scores.add(fieldWrapper.getIndex(), GARANTEED_NOT_HERE);
            }
        }

    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

}
