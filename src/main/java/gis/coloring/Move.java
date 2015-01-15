package gis.coloring;

/**
 * Created by gospo on 15.01.15.
 */
public class Move {
    private int node;
    private int color;

    public Move(int node, int color) {
        this.node = node;
        this.color = color;
    }

    public int getNode() {
        return node;
    }

    public int getColor() {
        return color;
    }
}
