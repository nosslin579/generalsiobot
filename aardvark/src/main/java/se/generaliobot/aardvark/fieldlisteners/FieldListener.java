package se.generaliobot.aardvark.fieldlisteners;

import pl.joegreen.sergeants.framework.model.Field;
import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;

public interface FieldListener {
    default void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
    }

    default void onFieldChange(FieldWrapper wrapper, Field old) {
    }

    default void onCaptured(ScoreMap scoreMap, FieldWrapper captured) {}
}
