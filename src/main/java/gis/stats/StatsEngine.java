package gis.stats;

import gis.graph.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gis.stats.T.*;

/**
 * Created by gospo on 16.01.15.
 */
public class StatsEngine {
    public void perform(Graph graph, int repeat, int iterMax) {
        StatsCollector statsCollector = new StatsCollector();
        List<Stats> stats = new ArrayList<>();

        // with long term memory
        for(int i = 0 ; i < repeat;i++) {
            new SColoring(statsCollector).color(graph, iterMax, true);
            stats.add(statsCollector.next());
            L("L:" + (i+1) + "/" + repeat);
        }
        writeStats(stats, "/home/gospo/mine/GIS/src/main/resources/stats_l.txt");
        stats.clear();

        // without long term memory
        for(int i = 0 ; i < repeat;i++) {
            new SColoring(statsCollector).color(graph, iterMax, false);
            stats.add(statsCollector.next());
            L("NL:" + (i+1) + "/" + repeat);
        }
        writeStats(stats, "/home/gospo/mine/GIS/src/main/resources/stats.txt");
    }

    private void writeStats(List<Stats> stats, String path) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for(Stats s : stats) {
                bw.write((s.isColoringFound() ? 1 : 0) + " " + s.getColoringK() + " " + s.getColoringIt() + " " + s.getOverallTime());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
