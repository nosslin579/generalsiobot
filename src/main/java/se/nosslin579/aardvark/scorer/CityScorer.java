package se.nosslin579.aardvark.scorer;

import pl.joegreen.sergeants.framework.model.Field;
import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;

import java.util.Arrays;

public class CityScorer implements Scorer {
    @Override
    public int[] getScores(ScoreMap scoreMap) {
        int[] ret = new int[scoreMap.getFieldWrappers().length];
        Arrays.stream(scoreMap.getFieldWrappers())
                .map(FieldWrapper::getField)
                .filter(Field::isVisible)
                .map(Field::asVisibleField)
                .forEach(field -> ret[field.getIndex()] = field.isCity() ? -100000 : 0);
        return ret;
    }
}
