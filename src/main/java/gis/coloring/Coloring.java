package gis.coloring;

import gis.graph.Graph;
import javafx.scene.control.Tab;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * Created by gospo on 15.01.15.
 */
public class Coloring {
    public List<Integer> color(Graph graph, int tabuIters, boolean longTermMemory) {
        int k = calcUpperBoundColorsNumber(graph);
        List<Integer> colors;
        List<Integer> next = null;
        do {
            colors = next;
            next = createAlgorithm(graph, k--, tabuIters, longTermMemory).color();
        } while (next != null);

        return colors;
    }

    protected TabuSearch createAlgorithm(Graph graph, int k, int tabuIters, boolean longTermMemory) {
        return new TabuSearch(graph, k, tabuIters, longTermMemory);
    }

    protected int calcUpperBoundColorsNumber(Graph graph) {
        int maxDegree = IntStream.range(0, graph.getSize())
                .map(i -> graph.getAdjacentNodes(i).size())
                .max().getAsInt();

        double edgeConditionVal = 0.5 + sqrt(2 * graph.getEdges().size() + 0.25);
        double degreeConditonVal = maxDegree + 1;

        return (int) round(min(edgeConditionVal, degreeConditonVal));
    }
}
