package edu.northeastern.data;
import java.util.*;
import java.io.*;
public class Graph {

    //HashMap<Node, ArrayList<Edge>> Gmap;
    ArrayList<Edge> Edges;
    ArrayList<Node> Nodes;
    public Graph(){
        // ignore for now
        //Gmap = new HashMap<Node, ArrayList<Edge>>();
        Nodes= new ArrayList<>();
        Edges= new ArrayList<>();
    }
    public void inputCSV(String name){
        String line = "";
        String splitBy = ",";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("src/crimeSample.csv"));
            //get rid of fst line
            br.readLine();
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] node = line.split(splitBy);    // use comma as separator
                Node newNode = new Node(node[0],Double.parseDouble(node[1]),Double.parseDouble(node[2]) );
                Nodes.add(newNode);
                //System.out.println(newNode);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for(int i = 0; i< Nodes.size();i++){
            for(int j = i+1;j<Nodes.size();j++){
                Edge e = new Edge(Nodes.get(i),Nodes.get(j));
                System.out.println(e);
                Edges.add(e);
            }
        }

    }
}

