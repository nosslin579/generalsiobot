package se.generaliobot.copter;

public class TileScore {

    private Tile tile;
    private Double score;

    public TileScore(Tile tile, double score) {
        this.tile = tile;
        this.score = score;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
