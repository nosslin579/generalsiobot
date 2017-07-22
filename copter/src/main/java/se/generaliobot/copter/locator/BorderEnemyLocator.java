package se.generaliobot.copter.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileType;
import se.generaliobot.copter.fieldlisteners.FieldListener;

import java.util.Arrays;

/**
 * Fog tiles around enemy tiles increase chance of crown
 */
public class BorderEnemyLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Scores scores = new Scores();

    @Override
    public Scores getLocationScore() {
        return scores;
    }


    @Override
    public void onFieldChange(Tile tile, Field old) {
        if (tile.isEnemy()) {
            Arrays.stream(tile.getSurroundingTiles())
                    .filter(t -> t.getLastKnown() == TileType.FOG)
                    .forEach(t -> scores.setScore(t.getIndex(), 50d));
        }
    }
}
