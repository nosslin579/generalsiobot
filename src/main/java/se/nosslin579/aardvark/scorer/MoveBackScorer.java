package se.nosslin579.aardvark.scorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nosslin579.aardvark.Move;
import se.nosslin579.aardvark.MoveListener;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;
import se.nosslin579.aardvark.config.Config;

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
