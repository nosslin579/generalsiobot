package se.nosslin579.aardvark.scorer;

import se.nosslin579.aardvark.ScoreMap;

public interface Scorer {
    int[] getScores(ScoreMap scoreMap);
}
