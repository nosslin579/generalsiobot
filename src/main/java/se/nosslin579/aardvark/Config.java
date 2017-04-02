package se.nosslin579.aardvark;

public class Config {
    private int id;
    private Double rating = 1000d;
    private Double emptyPenalty;
    private Double enemyPenalty;
    private Double obstaclePenalty;
    private Double ownPenalty;
    private Double ownCityPenalty;
    private Double cityPenalty;
    private Double enemyCityPenalty;
    private Double fogPenalty;
    private Double mountainPenalty;
    private Double ownCrownPenalty;
    private Double enemyCrownPenalty;

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

    public Double getMountainPenalty() {
        return mountainPenalty;
    }

    public void setMountainPenalty(Double mountainPenalty) {
        this.mountainPenalty = mountainPenalty;
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
}
