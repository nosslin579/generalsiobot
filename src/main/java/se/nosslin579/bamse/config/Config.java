package se.nosslin579.bamse.config;

import org.springframework.util.Assert;

public class Config {
    private int id;
    private Double rating = 1000d;
    private Double emptyPenalty = 1d;
    private Double enemyPenalty = 0.9d;
    private Double obstaclePenalty = 100d;
    private Double ownPenalty = 1.5d;
    private Double ownCityPenalty = 0.4d;
    private Double cityPenalty = 50d;
    private Double enemyCityPenalty = 5d;
    private Double fogPenalty = 1d;
    private Double ownCrownPenalty = 2d;
    private Double enemyCrownPenalty = -10d;
    private Double mandatoryMovePenalty = 0.1d;
    private int excludeEdgeDistance = 1;
    private Double moveBackPenalty = 1d;

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

    public Double getEmptyPenalty() {
        return emptyPenalty;
    }

    public void setEmptyPenalty(Double emptyPenalty) {
        this.emptyPenalty = emptyPenalty;
    }

    public Double getEnemyPenalty() {
        return enemyPenalty;
    }

    public void setEnemyPenalty(Double enemyPenalty) {
        this.enemyPenalty = enemyPenalty;
    }

    public Double getObstaclePenalty() {
        return obstaclePenalty;
    }

    public void setObstaclePenalty(Double obstaclePenalty) {
        this.obstaclePenalty = obstaclePenalty;
    }

    public Double getOwnPenalty() {
        return ownPenalty;
    }

    public void setOwnPenalty(Double ownPenalty) {
        this.ownPenalty = ownPenalty;
    }

    public Double getOwnCityPenalty() {
        return ownCityPenalty;
    }

    public void setOwnCityPenalty(Double ownCityPenalty) {
        this.ownCityPenalty = ownCityPenalty;
    }

    public Double getCityPenalty() {
        return cityPenalty;
    }

    public void setCityPenalty(Double cityPenalty) {
        this.cityPenalty = cityPenalty;
    }

    public Double getEnemyCityPenalty() {
        return enemyCityPenalty;
    }

    public void setEnemyCityPenalty(Double enemyCityPenalty) {
        this.enemyCityPenalty = enemyCityPenalty;
    }

    public Double getFogPenalty() {
        return fogPenalty;
    }

    public void setFogPenalty(Double fogPenalty) {
        this.fogPenalty = fogPenalty;
    }

    public Double getOwnCrownPenalty() {
        return ownCrownPenalty;
    }

    public void setOwnCrownPenalty(Double ownCrownPenalty) {
        this.ownCrownPenalty = ownCrownPenalty;
    }

    public Double getEnemyCrownPenalty() {
        return enemyCrownPenalty;
    }

    public void setEnemyCrownPenalty(Double enemyCrownPenalty) {
        this.enemyCrownPenalty = enemyCrownPenalty;
    }

    public Double getMandatoryMovePenalty() {
        return mandatoryMovePenalty;
    }

    public void setMandatoryMovePenalty(Double mandatoryMovePenalty) {
        this.mandatoryMovePenalty = mandatoryMovePenalty;
    }

    public int getExcludeEdgeDistance() {
        return excludeEdgeDistance;
    }

    public void setExcludeEdgeDistance(int excludeEdgeDistance) {
        Assert.isTrue(excludeEdgeDistance >= 0, "Must be greater then zero");
        Assert.isTrue(excludeEdgeDistance < 15, "Too much");
        this.excludeEdgeDistance = excludeEdgeDistance;
    }

    public Double getMoveBackPenalty() {
        return moveBackPenalty;
    }

    public void setMoveBackPenalty(Double moveBackPenalty) {
        this.moveBackPenalty = moveBackPenalty;
    }
}
