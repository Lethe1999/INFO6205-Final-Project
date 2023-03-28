package edu.northeastern;

import edu.northeastern.algorithm.Prim;
import edu.northeastern.data.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creat graph
        Graph graph = new Graph();
        graph.createGraph("src/main/resources/crimeSample.csv");

        // Using Prim Algorithm to create Minimum Spanning Tree
        Prim prim = new Prim(graph);
        List<String> path = prim.getPath(graph);
        for (String p : path) {
            System.out.println(p);
        }
    }
}