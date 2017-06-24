package se.generaliobot.copter.locator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.joegreen.sergeants.framework.model.Field;
import se.generaliobot.copter.Scores;
import se.generaliobot.copter.Tile;
import se.generaliobot.copter.TileHandler;
import se.generaliobot.copter.fieldlisteners.FieldListener;

import java.util.*;

public class CutOffLocator implements Locator, FieldListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TileHandler tileHandler;

    private boolean start = false;
    private Scores scores = new Scores();

    public CutOffLocator(TileHandler tileHandler) {
        this.tileHandler = tileHandler;
    }

    @Override
    public Scores getLocationScore() {
        if (!start) {
            return scores;
        }
        List<Tile> touched = new ArrayList<>();

        while (true) {
            Tile startField = getField(touched);
            if (startField != null) {
                ProcessedFields cutOffFields = getCutOffFields(startField);
                touched.addAll(cutOffFields.getProcessed());
                if (cutOffFields.isCutoff()) {
                    int size = cutOffFields.getProcessed().size();
                    Optional<Integer> ownerIndex = tileHandler.getMyGeneral().getField().asVisibleField().getOwnerIndex();
                    log.debug("At turn {} Found cut off fields. Size:{} Player:{}", tileHandler.getTurn(), size, ownerIndex);

                    for (Tile cutOffField : cutOffFields.getProcessed()) {
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
    private Tile getField(List<Tile> touched) {
        for (Tile wrapper : tileHandler.getTiles()) {
            if (wrapper.getField().isVisible() && wrapper.getField().asVisibleField().isOwnedByMe()) {
                for (Tile reachable : wrapper.getNeighbours()) {
                    if (reachable.getField().asVisibleField().isBlank() && !touched.contains(reachable) && !scores.contains(reachable)) {
                        return reachable;
                    }
                }
            }
        }
        return null;
    }

    private ProcessedFields getCutOffFields(Tile startField) {
        ProcessedFields ret = new ProcessedFields();
        Deque<Tile> stack = new ArrayDeque<>();
        stack.add(startField);
        while (!stack.isEmpty()) {
            Tile cursor = stack.pop();
            ret.getProcessed().add(cursor);
            for (Tile neighbour : cursor.getNeighbours()) {
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
    public void onCaptured(TileHandler tileHandler, Tile captured) {
        log.debug("Field was captured {}", captured.getField());
        start = true;
    }

    private class ProcessedFields {
        private List<Tile> processed = new ArrayList<>();
        private boolean cutoff = true;

        public List<Tile> getProcessed() {
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
