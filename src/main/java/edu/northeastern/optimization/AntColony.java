package edu.northeastern.optimization;

import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
public class AntColony {

    private List<double[]>[] totalGraph;
    private List<Node> nodes;
    private List<List<Node>> ants;
    List<List<Double>> phMatrix;
    List<Node> BestPath=null;
    HashMap<List<Node>,Double> dismap= new HashMap<>();
    private double[][] edgesMatrix;

    public AntColony(Graph totalG){
        this.edgesMatrix=totalG.getEdgeMatrix();
        totalGraph=totalG.getGraph();
        nodes=totalG.getNodes();
        Collections.sort(nodes, new Comparator<Node>(){
            public int compare(Node o1, Node o2){
                return o1.getUnique_id() - o2.getUnique_id();
            }
        });

        int size = totalG.getNodes().size();
        phMatrix=new ArrayList<>();
        ants=new LinkedList<>();
        for(int i = 0;i<size;i++){
            //initial ants
            List<Node> path = new LinkedList<>();
            ants.add(path);
            //initial matrix to 1
            List<Double> tmp = new ArrayList<>();
            for(int j = 0;j<size;j++){
               tmp.add(1.0);
            }
            phMatrix.add(tmp);
        }


    }
    public double calculateDistance(List<Node> newTour) {
        double dis = 0.0;

        if(dismap.containsKey(newTour)){
            return dismap.get(newTour);
        }

        for (int i = 0; i < newTour.size() - 1; i++) {
            dis += findPath(newTour, i, i + 1);
        }
        // end -> first
        dis += findPath(newTour, newTour.size() - 1, 0);
        dismap.put(newTour,dis);
        return dis;
    }
    private double findPath(List<Node> tour, int a, int b) {
        /*
        for (double[] edge : graph[tour.get(a).getUnique_id()]) {
            if (edge[1] == tour.get(b).getUnique_id()) return edge[2];
        }
        return Integer.MAX_VALUE;*/
        return this.edgesMatrix[tour.get(a).getUnique_id()][tour.get(b).getUnique_id()];
    }
    private Node findNode(int id){
        return nodes.get(id);
    }

    public List<Node> start(int count ,List<List<Node>> population){
        //initial matrix by population
        for(int i = 0;i< population.size();i++){
            updateMatrix(population.get(i),calculateDistance(population.get(i)));
        }
        while(count>0){
            System.out.println("loop "+count +"starts");
            for (int i=0;i<totalGraph.length;i++){
                //System.out.println("ant "+i+" moves "+count);
                List<Node> path = moveAnt(i);
                ants.set(i,path);
                if(BestPath==null || calculateDistance(path)<calculateDistance(BestPath)){
                    BestPath=path;
                }
            }
            System.out.println("Updateing Matrix");
            updateMatrix();
            System.out.println("Clearing ants");
            ants.clear();
            ants= new LinkedList<>();
            for(int i = 0;i< totalGraph.length;i++){
                ants.add(new LinkedList<>());
            }
            count--;
        }
        return BestPath;
    }
    void updateMatrix(){
        for (int i =0;i<phMatrix.size();i++){
            for(int j = 0;j<phMatrix.get(i).size();j++){
                double tij = phMatrix.get(i).get(j);
                phMatrix.get(i).set(j,tij*0.5);
            }
        }
        for(int i = 0;i< ants.size();i++){
            updateMatrix(ants.get(i),calculateDistance(ants.get(i)));
        }
    }
    void updateMatrix(List<Node> path,double dis){
        for(int i = 0;i<path.size();i++){
            int j = i+1;
            if(j>path.size()-1){
                break;
            }
            double tij = phMatrix.get(i).get(j);
            Node from = findNode(i);
            Node to = findNode(j);
            //double dis = Math.sqrt(Math.pow((from.getLatitude() - to.getLatitude()), 2) + Math.pow((from.getLongitude() - to.getLongitude()), 2));
            //double dis = calculateDistance(path);
            double newtij = tij + 1/dis;
            phMatrix.get(i).set(j,newtij);
        }
    }
    private List<Node> moveAnt(int index){
        int size = totalGraph.length;
        Node curNode = findNode(index);
        List<Node> ant = new LinkedList<>();
        while (ant.size()<size){
            Node node = chooseNode(curNode,ant);
            ant.add(node);
            curNode=node;
        }
        return ant;

    }

    private Node chooseNode(Node curNode,List<Node> ant){
        //{p,to}
        ArrayList<double[]> possibilities= new ArrayList<>();

        List<double[]> edges = totalGraph[curNode.getUnique_id()];
        for (int i = 0;i< edges.size();i++){
            double from =  edges.get(i)[0];
            double to =  edges.get(i)[1];
            if(visited(ant,(int)to)){
               continue;
            }
            double dis =  edges.get(i)[2];
            double tij = phMatrix.get((int)from).get((int)to);
            double partP = tij*(1.0/dis);
            possibilities.add(new double[]{partP,to});

        }
        if(possibilities.size()==0){
            return null;
        }

        //sum possibilities and devide all element in p
        double sum = 0.0;
        for(int i = 0;i<possibilities.size();i++){
            sum += possibilities.get(i)[0];
        }
        for(int i = 0;i<possibilities.size();i++){
            double[] old = possibilities.get(i);
            possibilities.set(i,new double[]{old[0]/sum,old[1]});
        }
        Comparator<double[]> comp = (double[] a, double[] b) -> {
            if(a[0]>b[0]){
                return -1;
            }
            else{
                return 1;
            }
        };
        Collections.sort(possibilities,comp);
        //trans to pos level
        double preP = 1.0;
        for(int i = 0;i< possibilities.size();i++){
            double r1 = possibilities.get(i)[0];
            double r2 = possibilities.get(i)[1];
            possibilities.set(i,new double[]{preP,r2});
            preP = preP-r1;
        }
        //choose a node
        Random rand = new Random();
        double randD= rand.nextDouble();

        int pickNodeId = -1;
        for(int i = 0;i<possibilities.size();i++){
            if(randD >= possibilities.get(i)[0]){
                int to = -1;
                if(i-1 >0){
                    to = (int)possibilities.get(i-1)[1];
                }
                else{
                    to = (int)possibilities.get(i)[1];
                }
                pickNodeId=to;
                break;
            }
        }
        // if it is the last index
        if(pickNodeId==-1 && possibilities.size()!=0){
            pickNodeId=(int)possibilities.get(possibilities.size()-1)[1];
        }
        //System.out.println(possibilities.get(0)[0]+"   "+possibilities.get(1)[0]+"   "+possibilities.get(2)[0]);
        //System.out.println(pickNodeId);
        //System.out.println(randD);
        return findNode(pickNodeId);



    }

    private boolean visited(List<Node> ant,int id){

        for (int i = 0;i< ant.size();i++){
            if(ant.get(i).getUnique_id()==id){
                return true;
            }
        }
        return false;
    }
    public boolean validation(List<Node> path,int size ){
        if(path.size()!=size){
            //return false;
        }
        int[] r = new int[path.size()];
        for(int i = 0;i< path.size();i++){
            Node n=path.get(i);
            r[n.getUnique_id()]+=1;
        }
        for(int i = 0;i< r.length;i++){
            //
            if(r[i]!=1){
                System.out.println("Node "+ i + "appear" + r[i]+" times");
                return false;
            }
        }

        return true;
    }



}
