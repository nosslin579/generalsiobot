package se.nosslin579.aardvark.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.ScoreMap;

public interface FieldListener {
    void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap);
}
