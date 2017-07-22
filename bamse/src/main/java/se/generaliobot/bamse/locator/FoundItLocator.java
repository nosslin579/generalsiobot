package se.generaliobot.bamse.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.bamse.Scores;
import se.generaliobot.bamse.TileHandler;
import se.generaliobot.bamse.fieldlisteners.FieldListener;

public class FoundItLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores();

    @Override
    public Scores getLocationScore() {
        return scores;
    }

    @Override
    public void onFieldFound(VisibleField fieldFound, TileHandler tileHandler) {
        if (fieldFound.isGeneral() && fieldFound.isOwnedByEnemy()) {
            log.info("Found enemy crown at {}", fieldFound.getPosition());
            scores.setScore(fieldFound.getIndex(), 100000000000d);
        }
    }
}
