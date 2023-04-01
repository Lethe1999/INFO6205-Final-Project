package edu.northeastern;

import edu.northeastern.algorithm.*;
import edu.northeastern.data.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creat graph
        Graph graph = new Graph();
        graph.createGraph("src/main/resources/crimeSampleTest.csv");
        graph.showGraph("Total graph");
        // Using Prim Algorithm to create Minimum Spanning Tree
        Prim prim = new Prim(graph);
        List<double[]>[] mst = prim.getMst();
        //test show graph
        Graph graphMst = new Graph();
        graphMst.setNodes(graph.getNodes());
        graphMst.setGraph(mst);
        graphMst.showGraph("Min Spanning tree");
        //build muti graph that includes only even degree nodes except solo node
        GreedyMinWeightMatching gmw = new GreedyMinWeightMatching(graphMst,graph);
        graphMst.showGraph("Muti Graph");




    }
}