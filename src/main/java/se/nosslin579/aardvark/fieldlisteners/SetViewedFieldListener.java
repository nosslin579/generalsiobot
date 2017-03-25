package se.nosslin579.aardvark.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.nosslin579.aardvark.FieldWrapper;
import se.nosslin579.aardvark.ScoreMap;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        FieldWrapper field = scoreMap.getTile(fieldFound.getIndex());
        field.setViewed(true);
    }
}
