package edu.northeastern.optimization;

import edu.northeastern.algorithm.Tour;
import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TacticalOpt {
    private List<double[]>[] graph;     // Should be the Originally all connected graph
    private List<Node> bestTour;
    private double bestDistance;
    private List<List<Node>> solutions;
    private double[][] edgesMatrix;

    public TacticalOpt(Tour tour, Graph originalGraph) {
        this.edgesMatrix=originalGraph.getEdgeMatrix();
        this.graph = originalGraph.getGraph();
        this.bestTour = tour.getTour();
        this.bestDistance = calculateDistance(bestTour);
        this.solutions = new LinkedList<>();
    }


    public List<Node> randomSwapping() {
        System.out.println("Current Best Distance: " + bestDistance);
        Random random = new Random();
        boolean hasImprovement = true;
        int cnt = bestTour.size() * bestTour.size() / 10;

        // Loop until no improvements are made
        while (hasImprovement || cnt > 0) {
            hasImprovement = false;

            int i = random.nextInt(bestTour.size() - 1);
            int j = random.nextInt(i + 1, bestTour.size());

            // swap
            List<Node> newTour = randomSwap(i, j);
            double newDistance = calculateDistance(newTour);
            // Update Distance
            if (newDistance < bestDistance) {
                bestDistance = newDistance;
                bestTour = newTour;
            }

            cnt--;
        }

        return bestTour;
    }


    public List<Node> twoOpt() {
        int countSolution=0;
        System.out.println("Current Best Distance: " + bestDistance);
        boolean hasImprovement = true;

        // Loop until no improvements are made
        while (hasImprovement) {
            hasImprovement = false;

            // i, j are indices of tour list. NOT the unique_id of Node
            for (int i = 1; i < bestTour.size() - 2; i++) {
                for (int j = i + 1; j < bestTour.size() - 1; j++) {
                    //check distance of line A,B + line C,D against A,C + B,D if there is improvement, call swap method.
                    if (findPath(bestTour, i - 1, i) + findPath(bestTour, j, j + 1) > findPath(bestTour, i - 1, j) + findPath(bestTour, i, j + 1)) {
                        List<Node> newTour = twoOptSwap(i, j);
                        double newDistance = calculateDistance(newTour);
                        // Update Distance
                        if (newDistance < bestDistance) {
                            //System.out.println("find");
                            if(countSolution<100){
                                solutions.add(newTour);
                                countSolution++;
                            }

                            bestDistance = newDistance;
                            bestTour = newTour;
                            hasImprovement = true;
                        }
                    }
                }
            }
        }

        return bestTour;
    }

    // i < j
    private List<Node> randomSwap(int i, int j) {
        List<Node> newTour = new LinkedList<>();

        int k = 0;
        while (k < bestTour.size()) {
            if (k == i) {
                newTour.add(bestTour.get(j));
                k++;
            } else if (k == j) {
                newTour.add(bestTour.get(i));
                k++;
            } else {
                newTour.add(bestTour.get(k++));
            }
        }

        return newTour;
    }


    private List<Node> twoOptSwap(int i, int j) {
        List<Node> newTour = new LinkedList<>();

        // 0 -> i
        for (int k = 0; k < i; k++) {
            newTour.add(bestTour.get(k));
        }
        // j -> i
        for (int k = j; k >= i; k--) {
            newTour.add(bestTour.get(k));
        }
        // j -> end
        for (int k = j + 1; k < bestTour.size(); k++) {
            newTour.add(bestTour.get(k));
        }

        return newTour;
    }


    // Input bestTour indices
    private double findPath(List<Node> tour, int a, int b) {
        /*
        for (double[] edge : graph[tour.get(a).getUnique_id()]) {
            if (edge[1] == tour.get(b).getUnique_id()) return edge[2];
        }
        return Integer.MAX_VALUE;*/
        return this.edgesMatrix[tour.get(a).getUnique_id()][tour.get(b).getUnique_id()];
    }


    private double calculateDistance(List<Node> newTour) {
        double dis = 0.0;

        for (int i = 0; i < newTour.size() - 1; i++) {
            dis += findPath(newTour, i, i + 1);
        }
        // end -> first
        dis += findPath(newTour, newTour.size() - 1, 0);

        return dis;
    }


    /**
     * Getter
     */
    public List<Node> getBestTour() {
        return bestTour;
    }

    public double getBestDistance() {
        return bestDistance;
    }

    public List<List<Node>> getSolutions() {
        return solutions;
    }
}
