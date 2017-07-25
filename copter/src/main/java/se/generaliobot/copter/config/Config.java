package se.generaliobot.copter.config;

import org.springframework.util.Assert;

public class Config {


    private int id;
    private Double rating = 1000d;
    private int excludeEdgeDistance = 1;
    private Double winAttemptOwnPenalty = .1d;
    private Double winAttemptEmptyPenalty = .8d;
    private Double winAttemptFogPenalty = .9d;
    private Double sniffFogPenalty = .9d;
    private Double sniffEmptyPenalty = 1.1d;
    private Double sniffEnemyPenalty = 1.2d;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getExcludeEdgeDistance() {
        return excludeEdgeDistance;
    }

    public void setExcludeEdgeDistance(int excludeEdgeDistance) {
        Assert.isTrue(excludeEdgeDistance >= 0, "Must be greater then zero");
        Assert.isTrue(excludeEdgeDistance < 15, "Too much");
        this.excludeEdgeDistance = excludeEdgeDistance;
    }

    public Double getWinAttemptEmptyPenalty() {
        return winAttemptEmptyPenalty;
    }

    public void setWinAttemptEmptyPenalty(Double WinAttemptEmptyScore) {
        this.winAttemptEmptyPenalty = WinAttemptEmptyScore;
    }

    public Double getWinAttemptFogPenalty() {
        return winAttemptFogPenalty;
    }

    public void setWinAttemptFogPenalty(Double WinAttemptFogScore) {
        this.winAttemptFogPenalty = WinAttemptFogScore;
    }

    public Double getWinAttemptOwnPenalty() {
        return winAttemptOwnPenalty;
    }

    public void setWinAttemptOwnPenalty(Double WinAttemptOwnScore) {
        this.winAttemptOwnPenalty = WinAttemptOwnScore;
    }

    public Double getSniffFogPenalty() {
        return sniffFogPenalty;
    }

    public void setSniffFogPenalty(Double sniffFogPenalty) {
        this.sniffFogPenalty = sniffFogPenalty;
    }

    public Double getSniffEmptyPenalty() {
        return sniffEmptyPenalty;
    }

    public void setSniffEmptyPenalty(Double sniffEmptyPenalty) {
        this.sniffEmptyPenalty = sniffEmptyPenalty;
    }

    public Double getSniffEnemyPenalty() {
        return sniffEnemyPenalty;
    }

    public void setSniffEnemyPenalty(Double sniffEnemyPenalty) {
        this.sniffEnemyPenalty = sniffEnemyPenalty;
    }
}
