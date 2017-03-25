package se.nosslin579.aardvark.scorer;

import se.nosslin579.aardvark.ScoreMap;

import java.util.Arrays;

public class MyFieldsScorer implements Scorer {
    @Override
    public int[] getScores(ScoreMap scoreMap) {
        int[] ret = new int[scoreMap.getFieldWrappers().length];
        Arrays.stream(scoreMap.getFieldWrappers()).forEach(tile -> ret[tile.getIndex()] = tile.isMine() ? -1000 : 0);
        return ret;
    }
}
