package se.generaliobot.copter.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.TileHandler;

public class UnreachableLocator implements Locator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Scores scores;

    public UnreachableLocator(TileHandler tileHandler) {
        scores = tileHandler.getMyGeneral().getMoveScore(tile -> 0.01d);
    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

}
