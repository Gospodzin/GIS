package gis.stats;

import gis.coloring.Coloring;
import gis.coloring.TabuSearch;
import gis.graph.Graph;

import java.util.List;
import static gis.stats.T.*;

/**
 * Created by gospo on 16.01.15.
 */
public class SColoring extends Coloring {

    StatsCollector statsCollector;

    public SColoring(StatsCollector statsCollector) {
        this.statsCollector = statsCollector;
    }

    @Override
    public List<Integer> color(Graph graph, int tabuIters, boolean longTermMemory) {
        S();
        List<Integer> colors =  super.color(graph, tabuIters, longTermMemory);
        statsCollector.registerOverallTime(E());
        if(statsCollector.getStats().isColoringFound())
            L("Coloring with " + statsCollector.getStats().getColoringK());

        else
            L("Coloring not found");
        return colors;
    }

    @Override
    protected TabuSearch createAlgorithm(Graph graph, int k, int tabuIters, boolean longTermMemory) {
        return new STabuSearch(graph, k, tabuIters, longTermMemory, statsCollector);
    }
}
