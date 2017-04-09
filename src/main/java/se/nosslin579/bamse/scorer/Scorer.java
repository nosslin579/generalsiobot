package se.nosslin579.bamse.scorer;

import se.nosslin579.bamse.Scores;
import se.nosslin579.bamse.TileHandler;

public interface Scorer {
    Scores getScores(TileHandler tileHandler);
}
