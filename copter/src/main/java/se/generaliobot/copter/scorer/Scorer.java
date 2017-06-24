package se.generaliobot.copter.scorer;

import se.generaliobot.copter.Scores;
import se.generaliobot.copter.TileHandler;

public interface Scorer {
    Scores getScores(TileHandler tileHandler);
}
