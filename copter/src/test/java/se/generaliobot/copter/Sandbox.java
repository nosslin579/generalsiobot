package se.generaliobot.copter;

import org.junit.Assert;
import org.junit.Test;

public class Sandbox {
    @Test
    public void sandboxTest() {
        double value = 1d;
        double[] arr = new double[]{value};
        arr[0] = 2d;
        Assert.assertEquals(1d, value, .1d);
    }
}
