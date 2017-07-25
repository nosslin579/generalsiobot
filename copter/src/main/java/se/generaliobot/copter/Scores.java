package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Scores {
    private final static Logger log = LoggerFactory.getLogger(Scores.class);
    public static final Scores EMPTY = new Scores();
    private final Map<Tile, Double> map = new HashMap<>();
    private final Double defaultValue;

    public Scores(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Scores() {
        this(0d);
    }


    public void setScore(Tile tile, Double score) {
        map.put(tile, score);
    }

    public Tile getMax(Tile t1, Tile t2) {
        return getScore(t1) > getScore(t2) ? t1 : t2;
    }

    public Tile getMin(Tile fw1, Tile fw2) {
        return getScore(fw1) < getScore(fw2) ? fw1 : fw2;
    }

    public void log(Object source, Tile... tiles) {
        for (Tile tile : tiles) {
            log.info("Scores from {} gave {} score {}", source.getClass().getSimpleName(), tile, getScore(tile));
        }
    }

    public void add(TileScore score) {
        add(score.getTile(), score.getScore());
    }

    public void add(Scores scores) {
        for (Map.Entry<Tile, Double> entry : map.entrySet()) {
            add(entry.getKey(), scores.getScore(entry.getKey()));
        }
    }

    public void add(Tile tile, Double score) {
        Double current = map.getOrDefault(tile, defaultValue);
        this.map.put(tile, score + current);
    }

    public Tile getMax() {
        return map.entrySet().stream()
                .reduce((e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2)
                .get()
                .getKey();
    }

    public Double getScore(Tile tile) {
        return map.getOrDefault(tile, defaultValue);
    }

    public boolean contains(Tile tile) {
        return map.containsKey(tile);
    }

    public Tile getMin(Tile[] tiles) {
        return Arrays.stream(tiles)
                .reduce(this::getMin)
                .get();
    }

    public Tile getMax(Stream<Tile> tiles) {
        return tiles
                .reduce(this::getMax)
                .get();
    }


    public String getPrettyPrint(TileHandler tileHandler) {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tileHandler.getTiles()) {
            if (tile.getX() == 0) {
                sb.append(System.lineSeparator());
            }
            Double scoreAsInt = map.get(tile);
            String scoreAsString = String.valueOf(scoreAsInt == null ? "x" : scoreAsInt.intValue());
            if (scoreAsString.length() == 1) {
                sb.append("  ");
            } else if (scoreAsString.length() == 2) {
                sb.append(" ");
            } else if (scoreAsString.length() > 3) {
                scoreAsString = ">99";
            }
            sb.append(scoreAsString).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Scores{" +
                "defult=" + defaultValue +
                "map=" + map +
                '}';
    }
}
