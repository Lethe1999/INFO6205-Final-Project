package edu.northeastern.algorithm;

import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.*;

public class Prim {

    private List<double[]>[] graph;
    private Queue<double[]> pq;     // Store cut array
    private boolean[] visited;      // Already used nodes
    private List<Integer> path;

    // Create sequence of nodes (path)
    public Prim(Graph map) {
        // Init
        this.graph = map.getGraph();
        this.pq = new PriorityQueue<>((a, b) -> {
            return a[2] - b[2] > 0 ? 1 : (a[2] - b[2] < 0 ? -1 : 0);
        });
        int n = graph.length;
        this.visited = new boolean[n];
        this.path = new LinkedList<>();

        // Start from the first node (Or any node is available)
        int start = 0;
        visited[start] = true;
        path.add(start);
        cut(start);

        while (!pq.isEmpty()) {
            double[] edge = pq.poll();
            int to = (int) edge[1];
            if (!visited[to]) {
                visited[to] = true;
                path.add(to);
                cut(to);        // More edges
            }
        }
    }

    // Add all edges of 'from' node to pq
    private void cut(int from) {
        for (double[] edge : graph[from]) {
            int to = (int) edge[1];
            if (!visited[to]) {     // Add if not visited
                pq.offer(edge);
            }
        }
    }

    public List<Integer> getPath() {
        return path;
    }

    public List<String> getPath(Graph map) {
        List<String> nodeList = new LinkedList<>();
        // Find id from Node class
        ArrayList<Node> nodes = map.getNodes();
        for (int p : path) {
            nodeList.add(nodes.get(p).getId());
        }
        return nodeList;
    }
}
