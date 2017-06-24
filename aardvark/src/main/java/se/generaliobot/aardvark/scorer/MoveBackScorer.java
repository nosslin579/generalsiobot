package se.generaliobot.aardvark.scorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.generaliobot.aardvark.Move;
import se.generaliobot.aardvark.MoveListener;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;
import se.generaliobot.aardvark.config.Config;

public class MoveBackScorer implements Scorer, MoveListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Config config;
    private Move move = new Move(0, 0);

    public MoveBackScorer(Config config) {
        this.config = config;
    }

    @Override
    public Scores getScores(ScoreMap scoreMap) {
        Scores ret = new Scores();
        ret.add(move.getFrom(), config.getMoveBackPenalty());
        return ret;
    }

    @Override
    public void beforeMove(Move move) {
        this.move = move;
    }
}
