package edu.northeastern;

import edu.northeastern.algorithm.*;
import edu.northeastern.data.*;
import edu.northeastern.optimization.AntColony;
import edu.northeastern.optimization.Genetic;
import edu.northeastern.optimization.TacticalOpt;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Creat graph
        Graph graph = new Graph();
//        graph.createGraph("src/main/resources/info6205.spring2023.teamproject.csv");
        graph.createGraph("src/main/resources/crimeSample.csv");

        // Using Prim Algorithm to create Minimum Spanning Tree
        Prim prim = new Prim(graph);
        List<double[]>[] mst = prim.getMst();

        // Build multi graph that includes only even degree nodes except solo node
        Graph graphMst = new Graph();
        graphMst.setNodes(graph.getNodes());
        graphMst.setGraph(mst);
        GreedyMinWeightMatching gmw = new GreedyMinWeightMatching(graphMst, graph);
        Graph multiGraph = gmw.getGraph();

        // Generate Tour
        System.out.println("Tour");
        Tour tour = new Tour(multiGraph);
        List<Node> path = tour.generateTSPTour(6);
        System.out.println(path);

        // Random swapping
        TacticalOpt randomSwapping = new TacticalOpt(tour, graph);
        path = randomSwapping.randomSwapping();
        System.out.println(path);
        System.out.println("Random swapping best result:" + randomSwapping.getBestDistance());

        // 2-opt
        TacticalOpt twoOpt = new TacticalOpt(tour, graph);
        path = twoOpt.twoOpt();
        System.out.println(path);
        System.out.println("opt2 best result:" + twoOpt.getBestDistance());

        // Genetic
        List<List<Node>> population = twoOpt.getSolutions();
        System.out.println("Population size = " + population.size());
        //System.out.println(population.size());
        Genetic genetic = new Genetic(graph, 0, path);
        List<Node> geResult = genetic.start(population, 0);
        System.out.println("Result is :");
        System.out.println(geResult);
        System.out.println("dis:" + genetic.calculateDistance(geResult));
        System.out.println("validation result for genetic: " + genetic.validation(geResult, 156));

        // Ant
        AntColony ac = new AntColony(graph);
        List<Node> acPath = ac.start(1000, population);
        System.out.println(acPath);
        System.out.println(ac.calculateDistance(acPath));
        System.out.println("validation result for ant: " + ac.validation(acPath, 156));
        System.out.println(acPath);


    }
}