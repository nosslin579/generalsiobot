package se.nosslin579.aardvark;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class Util {
    public static int getIndexOfMaxValue(int[] ret) {
        int maxScore = IntStream.of(ret).max().getAsInt();
        for (int i = 0; i < ret.length; i++) {
            if (ret[i] == maxScore) {
                return i;
            }
        }
        throw new IllegalArgumentException("No max found, " + Arrays.toString(ret));
    }

    public static int[] invertDistance(ScoreMap scoreMap, int fieldIndex) {
        Map<Integer, Integer> distances = scoreMap.getTile(fieldIndex).getDistancesDynamic(1);
        int[] scores = new int[scoreMap.getFieldWrappers().length];
        for (int i = 0; i < scoreMap.getFieldWrappers().length; i++) {
            Integer distance = distances.get(i);
            scores[i] = distance == null ? -5000 : -distance;
        }
        return scores;
    }

}
