package se.generaliobot.copter.locator;

import se.generaliobot.copter.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -1000d;

    Scores getLocationScore();
}
