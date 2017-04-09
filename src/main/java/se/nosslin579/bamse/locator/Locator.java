package se.nosslin579.bamse.locator;

import se.nosslin579.bamse.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -6000000d;

    Scores getLocationScore();
}
