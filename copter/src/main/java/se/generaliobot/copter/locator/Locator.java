package se.generaliobot.copter.locator;

import se.generaliobot.copter.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -6000000d;

    Scores getLocationScore();
}
