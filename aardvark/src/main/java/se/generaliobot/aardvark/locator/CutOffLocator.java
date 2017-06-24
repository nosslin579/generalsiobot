package se.generaliobot.aardvark.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import se.generaliobot.aardvark.FieldWrapper;
import se.generaliobot.aardvark.ScoreMap;
import se.generaliobot.aardvark.Scores;
import se.generaliobot.aardvark.fieldlisteners.FieldListener;

import java.util.*;

public class CutOffLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ScoreMap scoreMap;

    private boolean start = false;
    private Scores scores = new Scores();

    public CutOffLocator(ScoreMap scoreMap) {
        this.scoreMap = scoreMap;
    }

    @Override
    public Scores getLocationScore() {
        if (!start) {
            return scores;
        }
        List<FieldWrapper> touched = new ArrayList<>();

        while (true) {
            FieldWrapper startField = getField(touched);
            if (startField != null) {
                ProcessedFields cutOffFields = getCutOffFields(startField);
                touched.addAll(cutOffFields.getProcessed());
                if (cutOffFields.isCutoff()) {
                    int size = cutOffFields.getProcessed().size();
                    Optional<Integer> ownerIndex = scoreMap.getMyGeneral().getField().asVisibleField().getOwnerIndex();
                    log.debug("At turn {} Found cut off fields. Size:{} Player:{}", scoreMap.getTurn(), size, ownerIndex);

                    for (FieldWrapper cutOffField : cutOffFields.getProcessed()) {
                        scores.add(cutOffField.getIndex(), GARANTEED_NOT_HERE);
                    }
                }

            } else {
                return scores;
            }
        }
    }

    /**
     * Get untouched, empty field reachable within one move.
     *
     * @param touched
     * @return
     */
    private FieldWrapper getField(List<FieldWrapper> touched) {
        for (FieldWrapper wrapper : scoreMap.getFieldWrappers()) {
            if (wrapper.getField().isVisible() && wrapper.getField().asVisibleField().isOwnedByMe()) {
                for (FieldWrapper reachable : wrapper.getNeighbours()) {
                    if (reachable.getField().asVisibleField().isBlank() && !touched.contains(reachable) && !scores.contains(reachable)) {
                        return reachable;
                    }
                }
            }
        }
        return null;
    }

    private ProcessedFields getCutOffFields(FieldWrapper startField) {
        ProcessedFields ret = new ProcessedFields();
        Deque<FieldWrapper> stack = new ArrayDeque<>();
        stack.add(startField);
        while (!stack.isEmpty()) {
            FieldWrapper cursor = stack.pop();
            ret.getProcessed().add(cursor);
            for (FieldWrapper neighbour : cursor.getNeighbours()) {
                Field field = neighbour.getField();
                if (field.isVisible() && field.asVisibleField().isOwnedByEnemy()) {
                    ret.setCutoff(false);
                } else if (stack.contains(neighbour) || field.isObstacle() || field.isVisible() && field.asVisibleField().isOwnedByMe()) {
                    continue;
                } else if (!ret.getProcessed().contains(neighbour)) {
                    stack.add(neighbour);
                }
            }
        }
        return ret;
    }

    @Override
    public void onCaptured(ScoreMap scoreMap, FieldWrapper captured) {
        log.debug("Field was captured {}", captured.getField());
        start = true;
    }

    private class ProcessedFields {
        private List<FieldWrapper> processed = new ArrayList<>();
        private boolean cutoff = true;

        public List<FieldWrapper> getProcessed() {
            return processed;
        }

        public boolean isCutoff() {
            return cutoff && !processed.isEmpty();
        }

        public void setCutoff(boolean cutoff) {
            this.cutoff = cutoff;
        }
    }
}
