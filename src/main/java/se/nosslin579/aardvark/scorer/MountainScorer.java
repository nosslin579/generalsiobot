package se.nosslin579.aardvark.scorer;

import se.nosslin579.aardvark.ScoreMap;

import java.util.Arrays;

public class MountainScorer implements Scorer {
    @Override
    public int[] getScores(ScoreMap scoreMap) {
        int[] ret = new int[scoreMap.getFieldWrappers().length];
        Arrays.stream(scoreMap.getFieldWrappers()).forEach(tile -> ret[tile.getIndex()] = tile.getField().isObstacle() ? -100000 : 0);
        return ret;
    }
}
