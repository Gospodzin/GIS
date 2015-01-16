package gis.stats;

import java.util.Stack;

/**
 * Created by gospo on 16.01.15.
 */
public class T {
    private static Stack<Long> start = new Stack<>();

    public static void S() {
        start.push(System.nanoTime());
    }

    public static long E() {
        return System.nanoTime() - start.pop();
    }

    public static void L(String s) {
        System.out.println(s);
    }
}
