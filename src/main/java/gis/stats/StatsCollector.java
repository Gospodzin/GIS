package gis.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gospo on 16.01.15.
 */
public class StatsCollector {

    private Stats stats = new Stats();

    public Stats next() {
        Stats tmp = stats;
        stats = new Stats();
        return tmp;
    }

    public void registerOverallTime(long time) {
        stats.setOverallTime(time);
        System.out.println("Overall time: " + time/1e9);
    }

    public void registerTabuSearchExec(long time, int k, int it) {
        stats.addTsExec(time, k, it);
        System.out.println(it + " iterations for " + k + " colors | time[s]: " + time/1e9);
    }

    public Stats getStats() {
        return stats;
    }
}
