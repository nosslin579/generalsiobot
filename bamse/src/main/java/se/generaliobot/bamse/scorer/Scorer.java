package se.generaliobot.bamse.scorer;

import se.generaliobot.bamse.Scores;
import se.generaliobot.bamse.TileHandler;

public interface Scorer {
    Scores getScores(TileHandler tileHandler);
}
