package edu.northeastern.data;

public class Edge {
    Node n1;
    Node n2;
    Double distance;
    public Edge(Node a,Node b){
        this.n1=a;
        this.n2=b;
        this.distance = Math.sqrt(Math.pow((n1.latitude-n2.latitude),2) + Math.pow((n1.longitude-n2.longitude),2));
    }

    public String toString(){
        return "Edge start at "+ Integer.toString(n1.unique_id) + " ends at " +Integer.toString(n2.unique_id) +" distance : " +Double.toString(distance);
    }
}
