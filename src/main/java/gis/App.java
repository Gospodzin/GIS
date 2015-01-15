package gis;

import gis.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;

/**
 * Created by gospo on 15.01.15.
 */
public class App {

    static String path = App.class.getClassLoader().getResource("peterson.txt").getPath();

    public static void main(String[] args) throws InterruptedException {
        Graph graph = Utils.loadGraphFromFile(path);
        org.graphstream.graph.Graph gsGraph = Utils.convertGraphToGSGraph(graph);
        Viewer viewer = gsGraph.display();
        Thread.sleep(2000);
        viewer.disableAutoLayout();
    }
}
