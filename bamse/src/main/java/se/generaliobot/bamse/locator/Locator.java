package se.generaliobot.bamse.locator;

import se.generaliobot.bamse.Scores;

public interface Locator {

    Double GARANTEED_NOT_HERE = -6000000d;

    Scores getLocationScore();
}
