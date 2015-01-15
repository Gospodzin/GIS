package gis.graph;

/**
 * Created by gospo on 15.01.15.
 */
public class Edge {
    private int node1;
    private int node2;

    public Edge(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() { return node2; }
}
