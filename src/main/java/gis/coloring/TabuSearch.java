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


    private CircularFifoQueue<Move> tabu = new CircularFifoQueue<>();

    private int maxIters;

    private Graph graph;

    // number of colors
    private int k;

    public TabuSearch(Graph graph, int k, int maxIters) {
        this.graph = graph;
        this.k = k;
        this.maxIters = maxIters;
    }

    public List<Integer> color() {
        List<Integer> colors = randomColors();
        List<Integer> bestColors = colors;
        if(k==1) return isColoring(colors) ? colors : null;
        int i;
        for(i = 0; !isColoring(colors) && i < maxIters;i++) {
            Move bestNeighbour = bestNeighbour(colors);
            tabu.add(getReverseMove(bestNeighbour, colors));
            applyMove(colors, bestNeighbour);
        }
        System.out.println("Tabu Search finished after " + i + " iteartions.");
        if(!isColoring(colors)) return null;
        return colors;
    }

    private Move bestNeighbour(List<Integer> colors) {
        // no. of neighbours generated
        int rep = 10;
        // current objective value
        int objective = objective(colors);

        // select rep random conflictingNodes
        List<Integer> nodes = conflictingNodes(colors);
        if(nodes.size() < rep) rep = nodes.size();
        List<Integer> range = IntStream.range(0, nodes.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(range);
        List<Integer> selectedNodes = IntStream.range(0, rep).map(i -> nodes.get(range.get(i))).boxed().collect(Collectors.toList());

        // generate neighbour moves
        Move bestNeighbour = null;
        Random random = new Random();
        HashSet<Move> tabuSet = new HashSet<>(tabu);
        for(Integer node : selectedNodes) {
            int c = random.nextInt(k - 1);
            Move neighbour = new Move(node, c == colors.get(node) ? k - 1 : c);
            int neighbourObjective = objective(colors, neighbour);
            if(!tabu.contains(neighbour) || aspirationCondition(neighbourObjective, objective)) {
                bestNeighbour = neighbour;
                if(neighbourObjective < objective) return neighbour; // suggested improvement condition
            }
        }

        return bestNeighbour;
    }

    private Integer lastNotWorse;
    public boolean aspirationCondition(int neighbourObjective, int objective) {
        if(lastNotWorse == null) {
            if(neighbourObjective <= objective - 1) {
                lastNotWorse = neighbourObjective;
                return true;
            }
            return false;
        }
        if(neighbourObjective <= lastNotWorse - 1) return true;
        return false;
    }

    private List<Integer> randomColors() {
        return new Random().ints(0, k).limit(graph.getSize())
                .boxed().collect(Collectors.toList());
    }

    // number of edges for which both ends are of the same color
    // the less the better
    private int objective(List<Integer> colors) {
        return conflictingEdges(colors).size();
    }

    private int objective(List<Integer> colors, Move move) {
        applyMove(colors, move);
        int objective = objective(colors);
        // reverse changes
        applyMove(colors, getReverseMove(move, colors));
        return objective;
    }

    private Move getReverseMove(Move move, List<Integer> colors) {
        return new Move(move.getNode(), colors.get(move.getNode()));
    }

    private void applyMove(List<Integer> colors, Move move) {
        colors.set(move.getNode(), move.getColor());
    }

    private List<Edge> conflictingEdges(List<Integer> colors) {
        List<Edge> edges = new ArrayList<>();
        for(Edge edge : graph.getEdges())
            if(colors.get(edge.getNode1()) == colors.get(edge.getNode2()))
                edges.add(edge);
        return edges;
    }

    private List<Integer> conflictingNodes(List<Integer> coloring) {
        Set<Integer> nodes = new HashSet<>();
        conflictingEdges(coloring).stream()
                .forEach(e -> {nodes.add(e.getNode1());nodes.add(e.getNode2());});
        return new ArrayList(nodes);
    }

    // coloring if there is no adjacent nodes of the same color
    private boolean isColoring(List<Integer> colors) {
        return objective(colors) == 0;
    }
}
