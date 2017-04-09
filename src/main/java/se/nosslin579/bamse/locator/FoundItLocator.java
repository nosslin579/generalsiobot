package se.nosslin579.bamse.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.bamse.Scores;
import se.nosslin579.bamse.TileHandler;
import se.nosslin579.bamse.fieldlisteners.FieldListener;

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
            scores.put(fieldFound.getIndex(), 1000000d);
        }
    }
}
