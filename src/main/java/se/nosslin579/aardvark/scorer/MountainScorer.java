package se.nosslin579.aardvark.scorer;

import pl.joegreen.sergeants.framework.model.Field;
import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MountainScorer implements Scorer {
    @Override
    public Scores getScores(ScoreMap scoreMap) {
        Map<Integer, Integer> collect = Arrays.stream(scoreMap.getFieldWrappers())
                .map(FieldWrapper::getField)
                .filter(Field::isObstacle)
                .collect(Collectors.toMap(Field::getIndex, fw -> -10000));
        return new Scores(collect, 0);
    }
}
