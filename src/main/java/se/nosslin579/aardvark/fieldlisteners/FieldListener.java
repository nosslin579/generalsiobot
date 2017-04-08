package se.nosslin579.aardvark.fieldlisteners;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;

public interface FieldListener {
    default void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
    }

    default void onFieldChange(FieldWrapper wrapper, Field old) {
    }

    default void onCaptured(ScoreMap scoreMap, FieldWrapper captured) {}
}
