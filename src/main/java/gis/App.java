package gis;

import gis.coloring.Coloring;
import gis.graph.Graph;
import gis.stats.StatsEngine;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by gospo on 15.01.15.
 */
public class App {

    static String path = App.class.getClassLoader().getResource("graph2_300_0.03.txt").getPath();

    public static void main(String[] args) throws InterruptedException {
        List argS = Arrays.asList(args);
        if(argS.stream().anyMatch(a -> a.equals("-r"))) {
            int size = readInt(argS.stream(), "-s=");
            double qProb = readDouble(argS.stream(), "-q=");

            Graph graph = Utils.generateRandomGraph(size, qProb);
            Utils.writeGraphToFile(graph, "graph_" + size + "_" + qProb + ".txt");
        } else if(argS.stream().anyMatch(a -> a.equals("-c"))) {
            String path = readString(argS.stream(), "-p=");
            int it = readInt(argS.stream(), "-it=");
            boolean view = argS.stream().anyMatch(a -> a.equals("-v"));
            boolean longMemory = argS.stream().anyMatch(a -> a.equals("-l"));

            Graph graph = Utils.loadGraphFromFile(path);
            List<Integer> colors = new Coloring().color(graph, it, longMemory);
            Utils.writeColoringToFile(colors, "colors.txt");
            if(view) showGraph(graph, colors);
        } else if(argS.stream().anyMatch(a -> a.equals("-m"))) {
            String path = readString(argS.stream(), "-p=");
            int it = readInt(argS.stream(), "-it=");
            int n = readInt(argS.stream(), "-n=");

            Graph graph = Utils.loadGraphFromFile(path);
            new StatsEngine().perform(graph, n, it);
        } else {
            System.out.println("Options\n" +
                    "-r -s=[INT] -q=[DOUBLE] - generate random graph\n" +
                    "\ts - number of nodes\n" +
                    "\tq - probability of edge generation\n" +
                    "\n" +
                    "-c -p=[STRING] -it=[INT] -v -l - find coloring\n" +
                    "\tp - graph file\n" +
                    "\tit - tabu search max iterations\n" +
                    "\tv - (optional) show graph\n" +
                    "\tl - (optional) use long-term memory\n" +
                    "\n" +
                    "-m -p=[STRING] -it=[INT] -n=[INT] - generate stats\n" +
                    "\tp - graph file\n" +
                    "\tit - tabu search max iterations\n" +
                    "\tn - number of averaged tests");
        }
    }

    private static int readInt(Stream<String> args, String arg) {
        return args.filter(a->a.startsWith(arg))
                .map(a->a.split("=")[1])
                .map(s -> Integer.parseInt(s))
                .mapToInt(s -> s.intValue())
                .findFirst().getAsInt();
    }

    private static double readDouble(Stream<String> args, String arg) {
        return args.filter(a->a.startsWith(arg))
                .map(a -> a.split("=")[1])
                .map(s -> Double.parseDouble(s))
                .mapToDouble(s -> s.doubleValue())
                .findFirst().getAsDouble();
    }

    private static String readString(Stream<String> args, String arg) {
        return args.filter(a -> a.startsWith(arg))
                .map(a -> a.split("=")[1])
                .findFirst().get();
    }

    public static void showGraph(Graph graph, List<Integer> coloring) throws InterruptedException {
        // graph view
        org.graphstream.graph.Graph gsGraph = Utils.convertGraphToGSGraph(graph, coloring);
        Viewer viewer = gsGraph.display();
        Thread.sleep(3000);
        viewer.disableAutoLayout();
    }
}
