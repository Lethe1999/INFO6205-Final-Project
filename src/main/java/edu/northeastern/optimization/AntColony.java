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
    double[][] phMatrix;
    //tij = [from][to]
    boolean[][] vistedMatrix;
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
        phMatrix=new double[size][size];
        ants=new LinkedList<>();
        for(int i = 0;i<size;i++){
            //initial ants
            List<Node> path = new LinkedList<>();
            ants.add(path);

            //initial matrix to 1
            for(int j = 0;j<size;j++){
               phMatrix[i][j]=1;
            }

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
            //initial vistedMatrix every loop
            this.initVistedM();
            for (int i=0;i<totalGraph.length;i++){
                List<Node> path = moveAnt(i);
                ants.set(i,path);
                if(BestPath==null || calculateDistance(path)<calculateDistance(BestPath)){
                    BestPath=path;
                }
            }
            //System.out.println("Updateing Matrix");
            updateMatrix();
            //System.out.println("Clearing ants");
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
        for (int i =0;i<phMatrix.length;i++){
            for(int j = 0;j<phMatrix[i].length;j++){
                double tij = phMatrix[i][j];
                phMatrix[i][j] = tij*0.5;
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

            int from = path.get(i).getUnique_id();
            int to = path.get(j).getUnique_id();
            double tij = phMatrix[from][to];
            //double dis = Math.sqrt(Math.pow((from.getLatitude() - to.getLatitude()), 2) + Math.pow((from.getLongitude() - to.getLongitude()), 2));
            //double dis = calculateDistance(path);
            double newtij = tij + 1/dis;
            phMatrix[from][to] = newtij;
            phMatrix[to][from] = newtij;
        }
        int from =path.get(0).getUnique_id();
        int to = path.get(path.size()-1).getUnique_id();
        double tij = phMatrix[from][to];
        double newtij = tij + 1/dis;
        phMatrix[from][to] = newtij;
        phMatrix[to][from] = newtij;

    }
    private List<Node> moveAnt(int index){
        int size = totalGraph.length;
        Node curNode = findNode(index);
        //insert first node(start point)
        List<Node> ant = new LinkedList<>();
        ant.add(curNode);
        vistedMatrix[index][curNode.getUnique_id()]=true;

        while (ant.size()<size){
            Node node = chooseNode(curNode,ant,index);
            ant.add(node);
            //update visited matrix
            vistedMatrix[index][node.getUnique_id()]=true;
            curNode=node;
        }
        return ant;

    }

    private Node chooseNode(Node curNode,List<Node> ant,int index){
        //{p,to}
        ArrayList<double[]> possibilities= new ArrayList<>();

        List<double[]> edges = totalGraph[curNode.getUnique_id()];
        for (int i = 0;i< edges.size();i++){
            double from =  edges.get(i)[0];
            double to =  edges.get(i)[1];
            if(visited((int)to,index)){
               continue;
            }
            double dis =  edges.get(i)[2];
            double tij = phMatrix[(int)from][(int)to];
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
            else if(a[0]==b[0]){
                return 0;
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

    private boolean visited(int id,int index){

        return this.vistedMatrix[index][id];
        /*
        for (int i = 0;i< ant.size();i++){
            if(ant.get(i).getUnique_id()==id){
                return true;
            }
        }
        return false;*/
    }
    public boolean validation(List<Node> path,int size ){
        if(path==null||path.size()!=size||path.size()<2){
            return false;
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
        double dis =0.0;
        for(int i = 0;i<path.size();i++){
            int j = i+1;
            if(j>path.size()-1){
                break;
            }
            Node a = path.get(i);
            Node b=path.get(j);
            dis+=Math.sqrt(Math.pow((a.getLatitude() - b.getLatitude()), 2) + Math.pow((a.getLongitude() - b.getLongitude()), 2));
        }
        Node a = path.get(0);
        Node b = path.get(path.size()-1);
        dis+=Math.sqrt(Math.pow((a.getLatitude() - b.getLatitude()), 2) + Math.pow((a.getLongitude() - b.getLongitude()), 2));
        System.out.println("Validation complete, with valid tour dis = "+dis);
        return true;
    }
    private void initVistedM(){
        this.vistedMatrix=new boolean[nodes.size()][nodes.size()];
        for(int i = 0;i<this.vistedMatrix.length;i++){
            for(int j = 0;j<this.vistedMatrix[i].length;j++){
                vistedMatrix[i][j]=false;
            }
        }

    }



}
