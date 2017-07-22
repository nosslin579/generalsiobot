package se.generaliobot.copter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Scores {
    private final static Logger log = LoggerFactory.getLogger(Scores.class);
    public static final Scores EMPTY = new Scores();
    private final Map<Integer, Double> map;
    private final Double defaultValue;

    public Scores(Map<Integer, Double> map, Double defaultValue) {
        this.map = map;
        this.defaultValue = defaultValue;
    }

    public Scores(Map<Integer, Double> map) {
        this(map, 0d);
    }

    public Scores() {
        this(new HashMap<>());
    }


    public void setScore(int index, Double score) {
        map.put(index, score);
    }

    public Tile getMax(Tile fw1, Tile fw2) {
        return map.get(fw1.getIndex()) > map.get(fw2.getIndex()) ? fw1 : fw2;
    }

    public Tile getMin(Tile fw1, Tile fw2) {
        return map.get(fw1.getIndex()) < map.get(fw2.getIndex()) ? fw1 : fw2;
    }

    public void log(Object source, int... index) {
        for (int i : index) {
            log.info("Scores from {} gave {} score {}", source.getClass().getSimpleName(), i, getScore(i));
        }
    }

    public void add(TileScore score) {
        add(score.getTile().getIndex(), score.getScore());
    }

    public void add(Scores scores) {
        for (Map.Entry<Integer, Double> entry : scores.map.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public void add(Integer index, Double score) {
        Double current = map.getOrDefault(index, defaultValue);
        this.map.put(index, score + current);
    }

    public int getMax() {
        return map.entrySet().stream()
                .reduce((e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2)
                .get()
                .getKey();
    }

    public Double getScore(int index) {
        return map.getOrDefault(index, defaultValue);
    }

    public boolean contains(Tile tile) {
        return map.containsKey(tile.getIndex());
    }

    public Tile getMin(Tile[] tiles) {
        return Arrays.stream(tiles)
                .filter(this::contains)
                .reduce(this::getMin)
                .get();
    }

    public Tile getMax(Tile[] tiles) {
        return Arrays.stream(tiles)
                .filter(this::contains)
                .reduce(this::getMax)
                .get();
    }


    public String getPrettyPrint(TileHandler tileHandler) {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tileHandler.getTiles()) {
            if (tile.getX() == 0) {
                sb.append(System.lineSeparator());
            }
            Double scoreAsInt = map.get(tile.getIndex());
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
