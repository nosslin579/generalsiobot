package se.generaliobot.copter.config;

import org.springframework.util.Assert;

public class Config {
    private int id;
    private Double rating = 1000d;
    private Double emptyScore = 0.5d;
    private Double enemyScore = 0.9d;
    private Double obstacleScore = 0d;
    private Double ownScore = .5d;
    private Double ownCityScore = 0.4d;
    private Double cityScore = 0d;
    private Double enemyCityScore = 5d;
    private Double fogScore = .5d;
    private Double ownCrownScore = 0d;
    private Double enemyCrownScore = 10d;
    private Double mandatoryMoveScore = 1d;
    private int excludeEdgeDistance = 1;
    private Double WinAttemptOwnScore = .4d;
    private Double WinAttemptEmptyScore = .2d;
    private Double WinAttemptFogScore = .1d;

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

    public Double getEmptyScore() {
        return emptyScore;
    }

    public void setEmptyScore(Double emptyScore) {
        this.emptyScore = emptyScore;
    }

    public Double getEnemyScore() {
        return enemyScore;
    }

    public void setEnemyScore(Double enemyScore) {
        this.enemyScore = enemyScore;
    }

    public Double getObstacleScore() {
        return obstacleScore;
    }

    public void setObstacleScore(Double obstacleScore) {
        this.obstacleScore = obstacleScore;
    }

    public Double getOwnScore() {
        return ownScore;
    }

    public void setOwnScore(Double ownScore) {
        this.ownScore = ownScore;
    }

    public Double getOwnCityScore() {
        return ownCityScore;
    }

    public void setOwnCityScore(Double ownCityScore) {
        this.ownCityScore = ownCityScore;
    }

    public Double getCityScore() {
        return cityScore;
    }

    public void setCityScore(Double cityScore) {
        this.cityScore = cityScore;
    }

    public Double getEnemyCityScore() {
        return enemyCityScore;
    }

    public void setEnemyCityScore(Double enemyCityScore) {
        this.enemyCityScore = enemyCityScore;
    }

    public Double getFogScore() {
        return fogScore;
    }

    public void setFogScore(Double fogScore) {
        this.fogScore = fogScore;
    }

    public Double getOwnCrownScore() {
        return ownCrownScore;
    }

    public void setOwnCrownScore(Double ownCrownScore) {
        this.ownCrownScore = ownCrownScore;
    }

    public Double getEnemyCrownScore() {
        return enemyCrownScore;
    }

    public void setEnemyCrownScore(Double enemyCrownScore) {
        this.enemyCrownScore = enemyCrownScore;
    }

    public Double getMandatoryMovePenalty() {
        return mandatoryMoveScore;
    }

    public void setMandatoryMoveScore(Double mandatoryMoveScore) {
        this.mandatoryMoveScore = mandatoryMoveScore;
    }

    public int getExcludeEdgeDistance() {
        return excludeEdgeDistance;
    }

    public void setExcludeEdgeDistance(int excludeEdgeDistance) {
        Assert.isTrue(excludeEdgeDistance >= 0, "Must be greater then zero");
        Assert.isTrue(excludeEdgeDistance < 15, "Too much");
        this.excludeEdgeDistance = excludeEdgeDistance;
    }

    public Double getWinAttemptEmptyScore() {
        return WinAttemptEmptyScore;
    }

    public void setWinAttemptEmptyScore(Double WinAttemptEmptyScore) {
        this.WinAttemptEmptyScore = WinAttemptEmptyScore;
    }

    public Double getWinAttemptFogScore() {
        return WinAttemptFogScore;
    }

    public void setWinAttemptFogScore(Double WinAttemptFogScore) {
        this.WinAttemptFogScore = WinAttemptFogScore;
    }

    public Double getWinAttemptOwnScore() {
        return WinAttemptOwnScore;
    }

    public void setWinAttemptOwnScore(Double WinAttemptOwnScore) {
        this.WinAttemptOwnScore = WinAttemptOwnScore;
    }
}
