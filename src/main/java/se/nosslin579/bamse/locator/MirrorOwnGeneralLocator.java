package se.nosslin579.bamse.locator;

import se.nosslin579.bamse.Scores;
import se.nosslin579.bamse.Tile;
import se.nosslin579.bamse.TileHandler;

import java.util.Map;

public class MirrorOwnGeneralLocator implements Locator {
    private final TileHandler tileHandler;

    public MirrorOwnGeneralLocator(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
    }

    @Override
    public Scores getLocationScore() {
        Tile myGeneral = tileHandler.getMyGeneral();
        int x = tileHandler.getWidth() - myGeneral.getX();
        int y = tileHandler.getHeight() - myGeneral.getY();
        Tile mostLikely = tileHandler.getTile(x, y);
        Map<Integer, Double> negativeDistances = mostLikely.getMovePenalty(tileHandler, fieldWrapper -> -1d);
        return new Scores(negativeDistances, -10000d);
    }
}
