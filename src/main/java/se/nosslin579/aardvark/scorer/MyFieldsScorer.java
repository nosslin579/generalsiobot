package se.nosslin579.aardvark.scorer;

import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;
import se.nosslin579.aardvark.Scores;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MyFieldsScorer implements Scorer {
    @Override
    public Scores getScores(ScoreMap scoreMap) {
        Map<Integer, Integer> collect = Arrays.stream(scoreMap.getFieldWrappers())
                .filter(FieldWrapper::isMine)
                .collect(Collectors.toMap(FieldWrapper::getIndex, fieldWrapper -> -1000));
        return new Scores(collect);
    }
}
