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

    // penality factor
    private static final double ALFA = 1;

    // long term penlity switch
    private boolean ltp;

    private CircularFifoQueue<Move> tabu = new CircularFifoQueue<>(100);

    private int maxIters;

    private Graph graph;

    // number of colors
    protected int k;

    public TabuSearch(Graph graph, int k, int maxIters) {
        this(graph, k, maxIters, true);
    }

    public TabuSearch(Graph graph, int k, int maxIters, boolean longTermPenality) {
        this.graph = graph;
        this.k = k;
        this.maxIters = maxIters;
        this.ltp = longTermPenality;
    }

    public List<Integer> color() {
        List<Integer> colors = randomColors();
        List<Integer> bestColors = colors;
        if(k==1) return isColoring(colors) ? colors : null;
        int i;
        for(i = 0; !isColoring(colors) && i < maxIters;i++) iterate(colors);
        if(!isColoring(colors)) return null;
        return colors;
    }

    protected void iterate(List<Integer> colors) {
        Move bestNeighbour = bestNeighbour(colors);
        if(ltp) increaseMoveCount(bestNeighbour); // long-term memory
        tabu.add(getReverseMove(bestNeighbour, colors));
        applyMove(colors, bestNeighbour);
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
        Integer bestNeighbourObjective = null;
        Integer bestNeighbourPenality = null;
        Random random = new Random();
        HashSet<Move> tabuSet = new HashSet<>(tabu);
        for(Integer node : selectedNodes) {
            int c = random.nextInt(k - 1);
            Move neighbour = new Move(node, c == colors.get(node) ? k - 1 : c);
            int neighbourPenality = ltp ? getMovePenality(neighbour) : 0; // long-term penality
            int neighbourObjective = objective(colors, neighbour);
            if(!tabu.contains(neighbour) || aspirationCondition(neighbourObjective, objective)) {
                if(neighbourObjective == 0 ) return neighbour; // return when coloring found improvement
                if(neighbourObjective < objective) return neighbour; // suggested improvement condition
                if(bestNeighbour == null) {
                    bestNeighbour = neighbour;
                    bestNeighbourObjective = neighbourObjective;
                    bestNeighbourPenality = neighbourPenality;
                }
                else if(neighbourObjective + neighbourPenality < bestNeighbourObjective + bestNeighbourPenality) {
                    bestNeighbour = neighbour;
                    bestNeighbourObjective = neighbourObjective;
                    bestNeighbourPenality = neighbourPenality;
                }
            }
        }

        return bestNeighbour;
    }

    public int getMovePenality(Move move) {
        return (int)ALFA*getMoveCount(move);
    }

    private HashMap<Move, Integer> moveCounts = new HashMap<>();
    private int getMoveCount(Move move) {
        Integer count = moveCounts.get(move);
        return count == null ? 0 : count;
    }

    private void increaseMoveCount(Move move) {
        Integer count = moveCounts.get(move);
        moveCounts.put(move, count == null ? 1 : count + 1);
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
