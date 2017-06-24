package se.generaliobot.bamse.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.bamse.Scores;
import se.generaliobot.bamse.Tile;
import se.generaliobot.bamse.TileHandler;

import java.util.HashMap;

public class ExcludeEdgeLocator implements Locator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores(new HashMap<>(), 0d);

    public ExcludeEdgeLocator(TileHandler tileHandler) {
        int excludeEdgeDistance = tileHandler.getConfig().getExcludeEdgeDistance();
        int xThreshold = tileHandler.getWidth() - excludeEdgeDistance;
        int yThreshold = tileHandler.getHeight() - excludeEdgeDistance;
        for (Tile tile : tileHandler.getTiles()) {
            if (tile.getX() < excludeEdgeDistance || tile.getX() >= xThreshold || tile.getY() < excludeEdgeDistance || tile.getY() >= yThreshold) {
                scores.add(tile.getIndex(), -10d);
            }
        }
        log.debug("Exclude edge: {}", scores.getPrettyPrint(tileHandler));
    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

}
