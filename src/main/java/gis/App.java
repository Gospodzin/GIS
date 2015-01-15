package gis;

import gis.coloring.Coloring;
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

    static String path = App.class.getClassLoader().getResource("multi_graph.txt").getPath();

    public static void main(String[] args) throws InterruptedException {
        Graph graph = Utils.generateRandomGraph(100, 0.05);
        //Graph graph = Utils.loadGraphFromFile(path);
        List<Integer> coloring = Coloring.color(graph, 1000);
        if(coloring == null) { //coloring not found
            System.out.println("coloring found: false");
            coloring = IntStream.range(0, graph.getSize()).map(i -> -1).boxed().collect(Collectors.toList());
        } else {
            System.out.println("coloring found: true");
            System.out.println("k: " + Utils.countColors(coloring));
        }
        org.graphstream.graph.Graph gsGraph = Utils.convertGraphToGSGraph(graph, coloring);
        Viewer viewer = gsGraph.display();
        Thread.sleep(2000);
        viewer.disableAutoLayout();
    }
}
