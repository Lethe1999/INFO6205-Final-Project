package edu.northeastern;
import edu.northeastern.data.*;
public class Main {
    public static void main(String[] args) {

        //test node
        //System.out.println("Hello world!");
        //Node n1 = new Node("cid1",10,10);
        //Node n2 = new Node("cid2",11,11);
        //System.out.println(n1);
        //System.out.println(n2);
        //test edges
        //Edge e1 = new Edge(n1,n2);
        //System.out.println(e1);
        //test Graph
        Graph g1= new Graph();
        g1.inputCSV("src/crimeSample.csv");
    }
}