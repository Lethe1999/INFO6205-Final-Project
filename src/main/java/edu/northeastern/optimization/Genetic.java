package edu.northeastern.optimization;
import edu.northeastern.data.Graph;
import edu.northeastern.data.Node;
import java.util.*;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Genetic {

    private List<double[]>[] totalGraph;
    private List<List<Node>> population;
    private PriorityQueue<List<Node>> pq;
    private int count;
    private double[][] edgesMatrix;
    List<Node> BestSolution;
    public Genetic(){}
    public Genetic(Graph g,int c,List<Node> bS){
        this.edgesMatrix=g.getEdgeMatrix();
        this.totalGraph =g.getGraph();
        this.count=c;
        this.pq = new PriorityQueue<>((a, b) -> {
            return calculateDistance(a) - calculateDistance(b) > 0 ? 1 : (calculateDistance(a)-calculateDistance(b) < 0 ? -1 : 0);
        });
        BestSolution=bS;
    }
    public double calculateDistance(List<Node> newTour) {
        double dis = 0.0;

        for (int i = 0; i < newTour.size() - 1; i++) {
            dis += findPath(newTour, i, i + 1);
        }
        // end -> first
        dis += findPath(newTour, newTour.size() - 1, 0);

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

    private void getResult(int size){
        int count_ = 0;
        population.clear();
        while(!pq.isEmpty()){
            List<Node> path = pq.poll();
            if(count_<size){
                population.add(path);
                if(calculateDistance(BestSolution)>calculateDistance(path)){
                    BestSolution=path;
                }
                count_++;
            }
            else{
                pq.clear();
            }

        }
    }
    public List<Node> start(List<List<Node>> p,int num){
        System.out.println("Genetic start");
        createPopulation(p);
        count = num;
        while(count>0){
            //add population in pq
            insertPq();
            //System.out.println("insertPq() "+ count);
            getCrossOver(population);
            //System.out.println(" getCrossOver(population)"+count);
            getMutation(population);
            //System.out.println(" getMutation(population)"+count);
            getNewParent();
            //System.out.println("getNewParent()"+count);
            getResult(100);
            //System.out.println(" getResult(100)"+count);
            System.out.println("Best solution is "+calculateDistance(BestSolution));
            count--;
        }
        return BestSolution;
        /*
        while(!pq.isEmpty()){
            List<Node> path = pq.poll();
            System.out.println(calculateDistance(path));
        }*/
    }

    private void createPopulation(List<List<Node>> p){
        population= p;



    }
    private void insertPq(){
        for(int i =0 ;i<population.size();i++){
            pq.offer(population.get(i));
        }
    }
    private void getMutation(List<List<Node>> parents){
        List<List<Node>> mutation = new LinkedList<>();
        for(int i = 0;i< parents.size();i++){
            Random rand = new Random();
            int size = parents.get(0).size();
            int rand_int1 = rand.nextInt(size);
            int rand_int2 = rand.nextInt(size-rand_int1)+rand_int1;
            List<Node> child = getMChild(parents.get(i),rand_int1,rand_int2);
            pq.offer(child);

        }
    }
    private List<Node> getMChild(List<Node> parent,int start,int end){

        List<Node>  child = new LinkedList<>();
        Random rand = new Random();
        //add after rand int


        int bound= (parent.size()-(end-start+1));
        int rand_int =0;
        if(bound==0){
            rand_int=bound;
        }
        else {
            rand_int = rand.nextInt(parent.size()-(end-start+1));
        }

        for(int i =0;i< parent.size();i++){
            if(i<start || i >end){
                child.add(parent.get(i));
            }
            if(i == rand_int){
                for(int j = start ;j<= end;j++){
                    child.add(parent.get(j));
                }
            }
        }
        //System.out.println(child.size());
        //System.out.println(validation(child,156));
        return child;
    }
    private void getCrossOver(List<List<Node>> parents){
        if(parents==null || parents.size()==0){
            return;
        }
        for(int i = 0;i< parents.size();i++){
            for(int j = i+1;j<parents.size();j++){
                Random rand = new Random();
                int size = parents.get(0).size();
                int rand_int1 = rand.nextInt(size);
                int rand_int2 = rand.nextInt(size-rand_int1)+rand_int1;
                List<Node> child = getChild(parents.get(i),parents.get(j),rand_int1,rand_int2);
                pq.offer(child);
            }
        }
    }
    private int findNode(List<Node> path,Node n){
        for (int i = 0;i< path.size();i++){
            if(n.getUnique_id()==path.get(i).getUnique_id()){
                return i;
            }
        }
        return -1;
    }
    private List<Node> getChild(List<Node> p1, List<Node> p2, int start, int end){
        // cut from start to end in p1 and take those from p2 , then add to p2
        List<Node> child = new LinkedList<>();
        child.addAll(p2);
        for(int i =0;i< p1.size();i++){
            if(i>=start && i <=end){
                child.remove(findNode(child,p1.get(i)));
                child.add(p1.get(i));
            }
        }
        //System.out.println(validation(child,156));
        //System.out.println(p1.size());
        //System.out.println(child.size());
        return child;
    }

    private void getNewParent(){
        for(int i = 0;i< population.size();i++){
            if(i%30==0){
                List<Node> path = population.get(i);
                List<Node> newPath = new LinkedList<>();
                newPath.addAll(path);
                Collections.shuffle(newPath);
                //System.out.println(validation(newPath,156));
                pq.offer(newPath);
            }
        }
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
