package se.nosslin579.aardvark.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.fieldlisteners.FieldListener;

public class FoundItLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private int[] scores = new int[10000];

    @Override
    public int[] getLocationScore() {
        return scores;
    }

    @Override
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        if (fieldFound.isGeneral() && fieldFound.isOwnedByEnemy()) {
            log.info("Found enemy crown at {}", fieldFound.getPosition());
            scores[fieldFound.getIndex()] = 1000000;
        }
    }
}
