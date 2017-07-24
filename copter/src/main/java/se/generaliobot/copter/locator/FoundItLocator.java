package se.generaliobot.copter.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.fieldlisteners.FieldListener;

public class FoundItLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores();

    @Override
    public Scores getLocationScore() {
        return scores;
    }

    @Override
    public void onFieldFound(Tile tileFound, VisibleField field) {
        if (field.isGeneral() && field.isOwnedByEnemy()) {
            log.info("Found enemy crown at {}", field.getPosition());
            scores.setScore(tileFound, 100000000000d);
        }
    }
}
