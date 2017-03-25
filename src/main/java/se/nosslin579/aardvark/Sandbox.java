package se.nosslin579.aardvark;

import java.util.Arrays;

public class Sandbox {

    public static final int[] INTS = new int[]{3};

    public static void main(String[] args) {
        int[] a = new int[1];
        String s = Arrays.toString(a);
        System.out.println(s);

        System.out.println(Integer.MIN_VALUE - 1);

        int[] anInt = getInt();
        System.out.println(anInt == INTS);

        anInt[0]++;
        System.out.println(Arrays.toString(getInt()));
        ;
    }


    private static int[] getInt() {
        return INTS;
    }
}
