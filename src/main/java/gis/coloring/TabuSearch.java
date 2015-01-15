package gis.coloring;

import gis.graph.Edge;
import gis.graph.Graph;
import jdk.nashorn.internal.codegen.types.Range;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import javax.xml.soap.Node;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.*;

/**
 * Created by gospo on 15.01.15.
 */
public class TabuSearch {


    CircularFifoQueue<Move> tabu;

    Graph graph;

    // number of colors
    int k;

    TabuSearch(Graph graph) {
        this.graph = graph;
        k = calcUpperBoundColorsNumber();
    }

    public void color() {
        final int maxIterations;

        List<Integer> initColors = randomColors();

    }

    public List<Move> neighbours(List<Integer> colors) {
        // no. of neighbours generated
        final int rep = 10;

        List<Integer> nodes = conflictingNodes(colors);
        Random random = new Random();
        return random.ints(0, k).limit(rep)
                .map(i -> nodes.get(i)).boxed()
                .map(n -> {int c = random.nextInt(k - 1); return new Move(n, c == colors.get(n) ? k - 1 : c);}) // assign different random color
                .collect(Collectors.toList());
    }

    private Integer lastNotWorse;
    public int aspiration(int objective) {
        return lastNotWorse == null ? objective - 1 : lastNotWorse - 1;
    }

    private int calcUpperBoundColorsNumber() {
        int maxDegree = IntStream.range(0, graph.getSize())
                .map(i -> graph.getAdjacentNodes(i).size())
                .min().getAsInt();
        return (int) round(min(0.5 + sqrt(2 * graph.getEdges().size() + 0.25), maxDegree + 1));
    }

    private List<Integer> randomColors() {
        return new Random().ints(0, k).limit(graph.getSize())
                .boxed().collect(Collectors.toList());
    }

    // number of edges for which both ends are of the same color
    // the less the better
    public int objective(List<Integer> colors) {
        return conflictingEdges(colors).size();
    }

    public List<Edge> conflictingEdges(List<Integer> colors) {
        List<Edge> edges = new ArrayList<>();
        for(Edge edge : graph.getEdges())
            if(colors.get(edge.getNode1()) == colors.get(edge.getNode2()))
                edges.add(edge);
        return edges;
    }

    public List<Integer> conflictingNodes(List<Integer> coloring) {
        Set<Integer> nodes = new HashSet<>();
        conflictingEdges(coloring).stream()
                .forEach(e -> {nodes.add(e.getNode1());nodes.add(e.getNode2());});
        return new ArrayList(nodes);
    }

    // coloring if there is no adjacent nodes of the same color
    public boolean isColoring(List<Integer> colors) {
        return objective(colors) == 0;
    }
}
