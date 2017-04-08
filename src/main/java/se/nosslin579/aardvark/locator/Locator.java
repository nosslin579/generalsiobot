package se.nosslin579.aardvark.locator;

import se.nosslin579.aardvark.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -6000000d;

    Scores getLocationScore();
}
