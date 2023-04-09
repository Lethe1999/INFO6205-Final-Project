package edu.northeastern;

import edu.northeastern.algorithm.*;
import edu.northeastern.data.*;
import edu.northeastern.optimization.TacticalOpt;

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
        List<Node> path = tour.generateTSPTour(6);
        System.out.println(path);

        // Random swapping
        TacticalOpt randomSwapping = new TacticalOpt(tour, graph);
        path = randomSwapping.randomSwapping();
        System.out.println(path);
        System.out.println(randomSwapping.getBestDistance());


        // 2-opt
        TacticalOpt twoOpt = new TacticalOpt(tour, graph);
        path = twoOpt.twoOpt();
        System.out.println(path);
        System.out.println(twoOpt.getBestDistance());

    }
}