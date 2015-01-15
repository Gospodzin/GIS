package gis.graph;

import java.util.*;

/**
 * Created by gospo on 15.01.15.
 */
public class Graph {
    static int INIT_COLOR = 0;

    List<Set<Integer>> adjacency;
    Set<Edge> edges;

    int size;

    public Graph(int size) {
        this.size = size;
        adjacency = new ArrayList<>(size);
        edges = new HashSet<>();
        for (int i = 0; i < size; i++) {
            adjacency.add(new HashSet<>());
        }
    }

    public int getSize() {
        return size;
    }

    public Set<Integer> getAdjacentNodes(int node) {
        return Collections.unmodifiableSet(adjacency.get(node));
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        adjacency.get(edge.getNode1()).add(edge.getNode2());
        adjacency.get(edge.getNode2()).add(edge.getNode1());
    }

    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }
}
