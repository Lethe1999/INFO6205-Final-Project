package edu.northeastern.algorithm;

import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.*;

public class Tour {
    private int n = 0;
    private ArrayList<Node> nodes;
    private List<double[]>[] graph;
    private Queue<double[]> pq;
    private boolean[] visited;
    // Recordings
    private List<Node> tour;    // Record tour path
    private double distance;    // Record tour distance

    public Tour(Graph graph) {
        this.nodes = graph.getNodes();
        this.graph = graph.getGraph();
        this.n = nodes.size();
    }

    public List<Node> generateTSPTour(int start) {
        // Init
        this.pq = new PriorityQueue<>((a, b) -> {
            return a[2] - b[2] > 0 ? 1 : (a[2] - b[2] < 0 ? -1 : 0);
        });
        this.visited = new boolean[n];
        this.tour = new LinkedList<>();
        this.distance = 0.0;

        // Start from 0 or any other nodes
        visited[start] = true;
        tour.add(nodes.get(start));
        cut(start);

        while (!pq.isEmpty()) {
            double[] edge = pq.poll();
            int to = (int) edge[1];
            double dis = edge[2];
            if (!visited[to]) {         // Already checked in cut(). Could be removed
                visited[to] = true;
                tour.add(nodes.get(to));
                distance += dis;
                cut(to);
            }
        }

        // Return to start node
        Node startNode = tour.get(start);
        Node endNode = tour.get(n - 1);
        distance += Math.sqrt(Math.pow((startNode.getLatitude() - endNode.getLatitude()), 2) + Math.pow((startNode.getLongitude() - endNode.getLongitude()), 2));

        return tour;
    }

    private void cut(int from) {
        for (double[] edge : graph[from]) {
            int to = (int) edge[1];
            if (!visited[to]) {     // Add if not visited
                pq.offer(edge);
            }
        }
    }

    public List<Node> getTour() {return tour;}
    public double getDistance(){return distance;}
}
