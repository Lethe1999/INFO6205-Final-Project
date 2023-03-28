package edu.northeastern.data;

import java.util.*;
import java.io.*;

public class Graph {
    private ArrayList<Node> nodes;
    private List<double[]>[] graph;     // double[]{a, b, dis}

    public Graph() {
        nodes = new ArrayList<>();
    }

    public void createGraph(String name) {
        int n = 0;      // Number of nodes

        // Input csv file
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(name));
            //get rid of fst line
            br.readLine();
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] node = line.split(splitBy);    // use comma as separator
                Node newNode = new Node(
                        node[0].substring(node[0].length() - 5),    // Identify points with last five hex characters
                        Double.parseDouble(node[1]), Double.parseDouble(node[2]), n++);
                nodes.add(newNode);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create graph
        this.graph = new LinkedList[n];
        for (int i = 0; i < nodes.size(); i++) {
            graph[i] = new LinkedList<>();
            for (int j = 0; j < nodes.size(); j++) {
                if (i == j) continue;   // Skip self loop
                Node a = nodes.get(i);
                Node b = nodes.get(j);
                graph[i].add(new double[]{
                        a.getUnique_id(), b.getUnique_id(),
                        Math.sqrt(Math.pow((a.getLatitude() - b.getLatitude()), 2) + Math.pow((a.getLongitude() - b.getLongitude()), 2))}
                );
            }
        }
    }

    // Getter
    public List<double[]>[] getGraph() {
        return graph;
    }
    public ArrayList<Node> getNodes() {
        return nodes;
    }
}

