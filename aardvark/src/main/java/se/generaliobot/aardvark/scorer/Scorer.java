package se.generaliobot.aardvark.scorer;

import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;

public interface Scorer {
    Scores getScores(ScoreMap scoreMap);
}
