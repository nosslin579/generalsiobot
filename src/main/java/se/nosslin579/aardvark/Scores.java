package se.nosslin579.aardvark;

import se.nosslin579.aardvark.scorer.Scorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scores {

    private final Map<Integer, Integer> scores;
    private final int defaultValue;

    public Scores(Map<Integer, Integer> scores, int defaultValue) {
        this.scores = scores;
        this.defaultValue = defaultValue;
    }

    public Scores(Map<Integer, Integer> scores) {
        this(scores, 0);
    }

    public Scores() {
        this(new HashMap<>());
    }


    public void put(int index, int score) {
        scores.put(index, score);
    }

    public FieldWrapper getMax(FieldWrapper fw1, FieldWrapper fw2) {
        return scores.get(fw1.getIndex()) > scores.get(fw2.getIndex()) ? fw1 : fw2;
    }

    public static Scores of(List<Scorer> scorers, ScoreMap scoreMap) {
        Scores ret = new Scores();
        for (Scorer scorer : scorers) {
            Scores scores = scorer.getScores(scoreMap);
            ret.add(scores);
        }
        return ret;
    }

    private void add(Scores scores) {
        for (Integer index : scores.scores.keySet()) {
            Integer score = scores.scores.get(index);
            add(index, score);
        }
    }

    private void add(Integer index, Integer score) {
        Integer current = scores.getOrDefault(index, defaultValue);
        this.scores.put(index, score + current);
    }

}
