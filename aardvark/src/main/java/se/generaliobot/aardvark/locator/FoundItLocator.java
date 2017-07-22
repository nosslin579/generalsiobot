package se.generaliobot.aardvark.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;
import se.generaliobot.aardvark.fieldlisteners.FieldListener;

public class FoundItLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores();

    @Override
    public Scores getLocationScore() {
        return scores;
    }

    @Override
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        if (fieldFound.isGeneral() && fieldFound.isOwnedByEnemy()) {
            log.info("Found enemy crown at {}", fieldFound.getPosition());
            scores.put(fieldFound.getIndex(), 100000000000d);
        }
    }
}
