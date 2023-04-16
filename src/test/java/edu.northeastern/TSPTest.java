package edu.northeastern;

import edu.northeastern.algorithm.GreedyMinWeightMatching;
import edu.northeastern.algorithm.Prim;
import edu.northeastern.algorithm.Tour;
import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;
import edu.northeastern.optimization.AntColony;
import edu.northeastern.optimization.Genetic;
import edu.northeastern.optimization.TacticalOpt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TSPTest {

    @Test
    public void testTSP() {
        // Creat graph
        System.out.println("--------------------- Create Graph ---------------------");
        Graph graph = new Graph();
        graph.createGraph("src/main/resources/info6205.spring2023.teamproject.csv");
        final int ROWS = 585;

        // Using Prim Algorithm to create Minimum Spanning Tree
        System.out.println("--------------------- Create MST with Prim ---------------------");
        Prim prim = new Prim(graph);
        List<double[]>[] mst = prim.getMst();

        // Build multi graph that includes only even degree nodes except solo node
        System.out.println("--------------------- Multi Graph ---------------------");
        Graph graphMst = new Graph();
        graphMst.setNodes(graph.getNodes());
        graphMst.setGraph(mst);
        GreedyMinWeightMatching gmw = new GreedyMinWeightMatching(graphMst, graph);
        Graph multiGraph = gmw.getGraph();

        // Generate Tour
        System.out.println("--------------------- Generate Tour ---------------------");
        Tour tour = new Tour(multiGraph);
        List<Node> path = tour.generateTSPTour(6);
        System.out.println("First path: " + path);

        // Random swapping
        System.out.println("--------------------- Random Swapping ---------------------");
        TacticalOpt randomSwapping = new TacticalOpt(tour, graph);
        path = randomSwapping.randomSwapping();
        double randomSwappingDis = randomSwapping.getBestDistance();
        System.out.println("Best result: " + randomSwappingDis);
        System.out.println("Path: " + path);

        // 2-opt
        System.out.println("--------------------- 2-OPT ---------------------");
        TacticalOpt twoOpt = new TacticalOpt(tour, graph);
        path = twoOpt.twoOpt();
        double twoOptDis = twoOpt.getBestDistance();
        System.out.println("Best result: " + twoOptDis);
        System.out.println("Path: " + path);
        assertTrue(twoOptDis <= randomSwappingDis);

        // Genetic
        System.out.println("--------------------- Genetic ---------------------");
        List<List<Node>> population = twoOpt.getSolutions();
        System.out.println("Population size: " + population.size());
        Genetic genetic = new Genetic(graph, 0, path);
        List<Node> geResult = genetic.start(population, 0);
        double geneticDis = genetic.calculateDistance(geResult);
        System.out.println("Best result: " + geneticDis);
        System.out.println("Path:" + geResult);
        assertTrue(genetic.validation(geResult, ROWS));
        assertTrue(geneticDis <= twoOptDis);

        // Ant
        System.out.println("--------------------- Ant Colony ---------------------");
        AntColony ac = new AntColony(graph);
        List<Node> acPath = ac.start(1, population);
        double acDis = ac.calculateDistance(acPath);
        System.out.println("Best result: " + acDis);
        System.out.println("Path: " + acPath);
        assertTrue(ac.validation(acPath, ROWS));
        assertTrue(acDis < geneticDis);
    }

}
