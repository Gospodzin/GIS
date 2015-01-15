package gis.coloring;

import gis.graph.Graph;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * Created by gospo on 15.01.15.
 */
public class Coloring {
    public static List<Integer> color(Graph graph, int tabuIters) {
        int k = calcUpperBoundColorsNumber(graph);
        List<Integer> colors;
        List<Integer> next = null;
        do {
            colors = next;
            System.out.println("Attempting to color with " + k + " colors...");
            long start = System.nanoTime();
            next = new TabuSearch(graph, k--, tabuIters).color();
            System.out.println("Iteration processing time: " + (System.nanoTime() - start)/1e9);
        } while (next != null);

        return colors;
    }

    private static int calcUpperBoundColorsNumber(Graph graph) {
        int maxDegree = IntStream.range(0, graph.getSize())
                .map(i -> graph.getAdjacentNodes(i).size())
                .max().getAsInt();

        double edgeConditionVal = 0.5 + sqrt(2 * graph.getEdges().size() + 0.25);
        double degreeConditonVal = maxDegree + 1;

        return (int) round(min(edgeConditionVal, degreeConditonVal));
    }
}
