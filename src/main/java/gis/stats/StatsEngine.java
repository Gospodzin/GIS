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

        // with long term memory
        List<Stats> l_stats = new ArrayList<>();
        for(int i = 0 ; i < repeat;i++) {
            new SColoring(statsCollector).color(graph, iterMax, true);
            l_stats.add(statsCollector.next());
            L("L:" + (i+1) + "/" + repeat);
        }

        // without long term memory
        List<Stats> stats = new ArrayList<>();
        for(int i = 0 ; i < repeat;i++) {
            new SColoring(statsCollector).color(graph, iterMax, false);
            stats.add(statsCollector.next());
            L("NL:" + (i+1) + "/" + repeat);
        }

        System.out.print("L: ");preview(l_stats);
        System.out.print("NL: ");preview(stats);

        writeStats(l_stats, "/home/gospo/mine/GIS/src/main/resources/stats_l.txt");
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

    private void preview(List<Stats> stats) {
        int bestK = stats.stream()
                .map(s -> s.getColoringK())
                .mapToInt(k -> k.intValue())
                .min().getAsInt();

        long bestCount = stats.stream()
                .filter(s -> s.getColoringK() == bestK)
                .count();

        double itsMean = stats.stream()
                .filter(s -> s.getColoringK() == bestK)
                .map(s -> s.getColoringIt())
                .mapToInt(it -> it.intValue())
                .average().getAsDouble();

        L("k: " + bestK + " c: " + bestCount + " m: " + itsMean);
    }
}
