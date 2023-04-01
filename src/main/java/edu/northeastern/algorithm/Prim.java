package edu.northeastern.algorithm;

import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.*;

public class Prim {
    private List<double[]>[] graph;
    private Queue<double[]> pq;     // Store cut array
    private boolean[] visited;      // Already used nodes
    private List<double[]>[] mst;   // Minimum Spanning Tree

    // Create sequence of nodes (path)
    public Prim(Graph map) {
        // Init
        this.graph = map.getGraph();
        this.pq = new PriorityQueue<>((a, b) -> {
            return a[2] - b[2] > 0 ? 1 : (a[2] - b[2] < 0 ? -1 : 0);
        });
        int n = graph.length;
        this.visited = new boolean[n];
        this.mst = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            mst[i] = new LinkedList<>();
        }

        // Start from the first node (Or any node is available)
        int start = 0;
        visited[start] = true;
        cut(start);

        while (!pq.isEmpty()) {
            double[] edge = pq.poll();
            int from = (int) edge[0];
            int to = (int) edge[1];
            if (!visited[to]) {
                visited[to] = true;
                mst[from].add(edge);    // Add edges in mst
                //Also add the opposite direction  edge !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!changed
                double[] edgeOpposite=new double[]{edge[1],edge[0],edge[2]};
                mst[to].add(edgeOpposite);
                cut(to);        // Add edges to pq
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

    public List<double[]>[] getMst() {
        return mst;
    }
}
