package edu.northeastern.algorithm;

import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.*;

public class GreedyMinWeightMatching {
    private Graph graph;
    private Graph totalGraph;
    private ArrayList<Integer> oddDegreeNodes;
    private Queue<double[]> pq;
    private List<double[]>[] matching;
    private boolean[] paired;

    public GreedyMinWeightMatching(Graph G, Graph total) {
        //mst
        this.graph = G;
        //graph contains all possible edges
        this.totalGraph = total;
        //isolate odd degree edges in mst
        this.isolateOddDegreeNode();
        // find match
        matching = this.findMinWeightMatching();
        //merge two graphs, mst has changed to muti graph and ready for the next step
        this.mergeEdges();
    }

    public void isolateOddDegreeNode() {
        // find the node with odd edges
        oddDegreeNodes = new ArrayList<>();
        List<double[]>[] edges = this.graph.getGraph();
        for (int i = 0; i < edges.length; i++) {
            int len = edges[i].size();
            if (len % 2 != 0) {
                oddDegreeNodes.add(i);
            }
        }
    }

    public List<double[]>[] findMinWeightMatching() {
        int len = graph.getNodes().size();
        List<double[]>[] matching = new LinkedList[len];
        for (int i = 0; i < len; i++) {
            matching[i] = new LinkedList<>();
        }
        if (oddDegreeNodes.size() == 0) {
            return matching;
        }
        //for each vertex v in G', find the edge incident to v with the minimum weight, and add
        // //it to the matching M if the other endpoint of the edge is not already in M.

        paired = new boolean[len];
        this.pq = new PriorityQueue<>((a, b) -> {
            return a[2] - b[2] > 0 ? 1 : (a[2] - b[2] < 0 ? -1 : 0);
        });
        for (int i = 0; i < oddDegreeNodes.size(); i++) {
            int start = oddDegreeNodes.get(i);
            cut(start);
            while (!pq.isEmpty()) {
                double[] minEdge = pq.poll();
                int from = (int) minEdge[0];
                int to = (int) minEdge[1];
                double distance = minEdge[2];
                //if paired skip, and we only care about odd degree nodes
                if (!paired[from] && !paired[to] && oddDegreeNodes.contains(from) && oddDegreeNodes.contains(to)) {
                    matching[from].add(minEdge);
                    matching[to].add(new double[]{to, from, distance});
                    paired[from] = true;
                    paired[to] = true;
                }
            }
        }


        return matching;
    }

    private void cut(int from) {
        // Add the odd degree node if not paired
        for (double[] edge : totalGraph.getGraph()[from]) {
            int to = (int) edge[1];
            if (!paired[to]) {
                pq.offer(edge);
            }
        }
    }

    private boolean containsE(List<double[]> mstI, double[] edge) {
        // check for existence before merge
        boolean r = false;
        for (double[] e : mstI) {
            if (e[0] == edge[0] && e[1] == edge[1]) {
                r = true;
            }
        }
        return r;
    }

    private void mergeEdges() {
        // merge matching to the mst 
        List<double[]>[] mst = graph.getGraph();
        for (int i = 0; i < matching.length; i++) {
            for (double[] edge : matching[i]) {
                if (!containsE(mst[i], edge)) {
                    mst[i].add(edge);
                }
            }
        }
        graph.setGraph(mst);

    }

    public Graph getGraph() {
        return this.graph;
    }

    public void show() {
        for (int i = 0; i < oddDegreeNodes.size(); i++) {
            System.out.println(oddDegreeNodes.get(i));
        }
    }

}
