package se.nosslin579.aardvark.scorer;

import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;

public interface Scorer {
    Scores getScores(ScoreMap scoreMap);
}
