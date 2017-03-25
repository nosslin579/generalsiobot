package se.nosslin579.aardvark;

public class Config {
    private int id;
    private int rating = 1000;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
