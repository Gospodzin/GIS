package gis;

import gis.coloring.TabuSearch;
import gis.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by gospo on 15.01.15.
 */
public class App {

    static String path = App.class.getClassLoader().getResource("graph.txt").getPath();

    public static void main(String[] args) throws InterruptedException {
        Graph graph = Utils.loadGraphFromFile(path);
        TabuSearch tabuSearch = new TabuSearch(graph);
        List<Integer> coloring = tabuSearch.color();
        org.graphstream.graph.Graph gsGraph = Utils.convertGraphToGSGraph(graph, coloring);
        Viewer viewer = gsGraph.display();
        Thread.sleep(2000);
        viewer.disableAutoLayout();
    }
}
