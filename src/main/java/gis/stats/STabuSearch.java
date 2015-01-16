package gis.stats;

import gis.coloring.TabuSearch;
import gis.graph.Graph;
import javafx.scene.control.Tab;

import java.util.List;

import static gis.stats.T.*;

/**
 * Created by gospo on 16.01.15.
 */
public class STabuSearch extends TabuSearch {
    StatsCollector statsCollector;
    public STabuSearch(Graph graph, int k, int maxIters, boolean longTermMemory, StatsCollector statsCollector) {
        super(graph, k, maxIters, longTermMemory);
        this.statsCollector = statsCollector;
    }

    int iterationCounter;

    @Override
    public List<Integer> color() {
        S();
        iterationCounter = 0;
        List<Integer> colors = super.color();
        statsCollector.registerTabuSearchExec(E(), k, iterationCounter);
        return colors;
    }

    @Override
    protected void iterate(List<Integer> colors) {
        super.iterate(colors);
        iterationCounter++;
    }
}
