package se.generaliobot.aardvark.locator;

import se.generaliobot.aardvark.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -6000000d;

    Scores getLocationScore();
}
