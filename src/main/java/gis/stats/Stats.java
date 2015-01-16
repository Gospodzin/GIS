package gis.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gospo on 16.01.15.
 */
public class Stats {
    private long overallTime;
    private List<TSExec> tsExecs = new ArrayList<>();

    class TSExec {
        long time;
        int k;
        int it;

        public TSExec(long time, int k, int it) {
            this.time = time;
            this.k = k;
            this.it = it;
        }
    }

    public long getOverallTime() {
        return overallTime;
    }

    public void setOverallTime(long overallTime) {
        this.overallTime = overallTime;
    }

    public List<TSExec> getTsExecs() {
        return tsExecs;
    }

    public void addTsExec(long time, int k, int it) {
        tsExecs.add(new TSExec(time, k, it));
    }

    public boolean isColoringFound() {
        return tsExecs.size() > 1;
    }

    public int getColoringK() {
        return isColoringFound() ? tsExecs.get(tsExecs.size()-2).k : -1;
    }

    public int getColoringIt() {
        return isColoringFound() ? tsExecs.get(tsExecs.size()-2).it : -1;
    }
}
