package gis;

import gis.coloring.Coloring;
import gis.coloring.TabuSearch;
import gis.graph.Graph;
import gis.stats.SColoring;
import gis.stats.StatsCollector;
import gis.stats.StatsEngine;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by gospo on 15.01.15.
 */
public class App {

    static String path = App.class.getClassLoader().getResource("graph_300_0.03.txt").getPath();

    public static void main(String[] args) throws InterruptedException {
        Graph graph = Utils.loadGraphFromFile(path);
        new StatsEngine().perform(graph, 100, 15000);
         //   Utils.writeGraphToFile(Utils.generateRandomGraph(300, 0.03), "/home/gospo/mine/GIS/src/main/resources/graph_300_0.03.txt");

        //List<Integer> coloring = new Coloring().color(graph, 10000);
        //showGraph(graph, coloring);
    }

    public static void showGraph(Graph graph, List<Integer> coloring) throws InterruptedException {
        // graph view
        org.graphstream.graph.Graph gsGraph = Utils.convertGraphToGSGraph(graph, coloring);
        Viewer viewer = gsGraph.display();
        Thread.sleep(3000);
        viewer.disableAutoLayout();
    }
}
