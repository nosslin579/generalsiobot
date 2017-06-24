package se.generaliobot.bamse.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.bamse.Scores;
import se.generaliobot.bamse.Tile;
import se.generaliobot.bamse.TileHandler;

import java.util.HashMap;
import java.util.Map;

public class UnreachableLocator implements Locator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Scores scores = new Scores(new HashMap<>(), 0d);

    public UnreachableLocator(TileHandler tileHandler) {
        Map<Integer, Double> movePenalty = tileHandler.getMyGeneral().getMovePenalty(tileHandler, fieldWrapper1 -> 1d);
        for (Tile tile : tileHandler.getTiles()) {
            if (!movePenalty.containsKey(tile.getIndex())) {
                scores.add(tile.getIndex(), GARANTEED_NOT_HERE);
            }
        }

    }

    @Override
    public Scores getLocationScore() {
        return scores;
    }

}
