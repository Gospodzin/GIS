package gis;

import gis.graph.Edge;
import gis.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.util.List;
import java.util.Random;
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

    public static Graph generateRandomGraph(int size, double qProb) {
        Graph graph = new Graph(size);
        Random random = new Random();
        for(int i = 0 ; i<size;i++) {
            for(int j = i+1 ; j<size;j++) {
                if(random.nextDouble() <= qProb)
                    graph.addEdge(new Edge(i, j));
            }
        }

        return graph;
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

    public static void writeGraphToFile(Graph graph, String path) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(String.valueOf(graph.getSize()));
            bw.newLine();
            for(Edge edge : graph.getEdges()) {
                bw.write(edge.getNode1() + " " + edge.getNode2());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countColors(List<Integer> colors) {
        return colors.stream().mapToInt(i->i.intValue()).max().getAsInt() + 1;
    }
}
