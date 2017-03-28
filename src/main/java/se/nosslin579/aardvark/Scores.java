package se.nosslin579.aardvark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nosslin579.aardvark.scorer.Scorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scores {
    private final static Logger log = LoggerFactory.getLogger(Scores.class);
    private final Map<Integer, Double> scores;
    private final Double defaultValue;

    public Scores(Map<Integer, Double> scores, Double defaultValue) {
        this.scores = scores;
        this.defaultValue = defaultValue;
    }

    public Scores(Map<Integer, Double> scores) {
        this(scores, 0d);
    }

    public Scores() {
        this(new HashMap<>());
    }


    public void put(int index, Double score) {
        scores.put(index, score);
    }

    public FieldWrapper getMax(FieldWrapper fw1, FieldWrapper fw2) {
        return scores.get(fw1.getIndex()) > scores.get(fw2.getIndex()) ? fw1 : fw2;
    }

    public FieldWrapper getMin(FieldWrapper fw1, FieldWrapper fw2) {
        return scores.get(fw1.getIndex()) < scores.get(fw2.getIndex()) ? fw1 : fw2;
    }

    public static Scores of(List<Scorer> scorers, ScoreMap scoreMap) {
        Scores ret = new Scores();
        for (Scorer scorer : scorers) {
            Scores scores = scorer.getScores(scoreMap);
            ret.add(scores);
        }
        return ret;
    }

    public void log(Object source, int... index) {
        for (int i : index) {
            log.info("Scores from {} gave {} score {}", source.getClass().getSimpleName(), i, getScore(i));
        }
    }

    public void add(Scores scores) {
        for (Integer index : scores.scores.keySet()) {
            Double score = scores.scores.get(index);
            add(index, score);
        }
    }

    public void add(Integer index, Double score) {
        Double current = scores.getOrDefault(index, defaultValue);
        this.scores.put(index, score + current);
    }

    public int getMax() {
        return scores.entrySet().stream()
                .reduce((e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2)
                .get()
                .getKey();
    }

    public int getIndexByLowestScore() {
        return scores.entrySet().stream()
                .reduce((e1, e2) -> e1.getValue() < e2.getValue() ? e1 : e2)
                .get()
                .getKey();
    }

    public Double getScore(int index) {
        return scores.getOrDefault(index, defaultValue);
    }
}
