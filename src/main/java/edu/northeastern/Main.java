package edu.northeastern;

import edu.northeastern.algorithm.*;
import edu.northeastern.data.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creat graph
        Graph graph = new Graph();
        graph.createGraph("src/main/resources/crimeSample.csv");
//        graph.showGraph("Total graph");

        // Using Prim Algorithm to create Minimum Spanning Tree
        Prim prim = new Prim(graph);
        List<double[]>[] mst = prim.getMst();

        //test show graph
        Graph graphMst = new Graph();
        graphMst.setNodes(graph.getNodes());
        graphMst.setGraph(mst);
//        graphMst.showGraph("Min Spanning tree");
        //build multi graph that includes only even degree nodes except solo node
        GreedyMinWeightMatching gmw = new GreedyMinWeightMatching(graphMst, graph);
        Graph multiGraph = gmw.getGraph();
//        graphMst.showGraph("Multi Graph");

        // Generate Tour
        System.out.println("Tour");
        Tour tour = new Tour(multiGraph);

        // Get minimum tour (Traverse each node to get the minimum distance)
        double minDistance = Double.MAX_VALUE;
        int minNodeNumber = 0;
        for (int i = 0; i < 156; i++) {
            List<Node> path = tour.generateTSPTour(i);
            if (tour.getDistance() < minDistance) {
                minDistance = tour.getDistance();
                minNodeNumber = i;
            }
        }
        System.out.println(tour.getDistance());
        System.out.println(minNodeNumber);
        List<Node> path = tour.generateTSPTour(minNodeNumber);
        System.out.println(path);
    }
}