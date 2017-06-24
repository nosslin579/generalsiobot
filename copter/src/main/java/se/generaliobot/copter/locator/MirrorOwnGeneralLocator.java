package se.generaliobot.copter.locator;

import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;

import java.util.Map;

public class MirrorOwnGeneralLocator implements Locator {
    private final TileHandler tileHandler;

    public MirrorOwnGeneralLocator(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
    }

    @Override
    public Scores getLocationScore() {
        Tile myGeneral = tileHandler.getMyGeneral();
        int x = tileHandler.getWidth() - myGeneral.getX() - 1;
        int y = tileHandler.getHeight() - myGeneral.getY() - 1;
        Tile mostLikely = tileHandler.getTile(x, y);
        Map<Integer, Double> negativeDistances = mostLikely.getMovePenalty(tileHandler, fieldWrapper -> -1d);
        return new Scores(negativeDistances, -10000d);
    }
}
