package se.generaliobot.aardvark.fieldlisteners;

import pl.joegreen.sergeants.framework.model.VisibleField;
import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;

public class SetViewedFieldListener implements FieldListener {
    @Override
    public void onFieldFound(VisibleField fieldFound, ScoreMap scoreMap) {
        FieldWrapper field = scoreMap.getTile(fieldFound.getIndex());
        field.setViewed(true);
    }
}
