package gis;

import gis.graph.Edge;
import gis.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

/**
 * Created by gospo on 15.01.15.
 */
public class Utils {
    public static org.graphstream.graph.Graph convertGraphToGSGraph(Graph graph, List<Integer> colors) {
        org.graphstream.graph.Graph gsGraph = new SingleGraph(null);

        for (int node = 0; node < graph.getSize(); node++) {
            // create node
            gsGraph.addNode(String.valueOf(node));
            // set label
            org.graphstream.graph.Node gsNode = gsGraph.getNode(String.valueOf(node));
            gsNode.setAttribute("ui.label", colors.get(node));
            // create edges if necessary
            for (int adjNode : graph.getAdjacentNodes(node)) {
                if (gsGraph.getNode(String.valueOf(adjNode)) != null)
                    gsGraph.addEdge(node + ":" + adjNode, String.valueOf(node), String.valueOf(adjNode));
            }
        }

        return gsGraph;
    }

    public static Graph loadGraphFromFile(String path) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //read graph size
        int size = scanner.nextInt();
        Graph graph = new Graph(size);
        //read edges
        while (scanner.hasNextInt())
            graph.addEdge(new Edge(scanner.nextInt(), scanner.nextInt()));
        return graph;
    }
}
