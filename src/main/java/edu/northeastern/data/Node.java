package edu.northeastern.data;

public class Node {
    //crimeID,longitude,latitude
    //f819dabb36b5c1b081c8caa6a02764f7c122f7d8774df1ebb0a095fe454576,-0.016542,51.515192
    public String id;
    public double longitude;
    public double latitude;
    public static int counter = 0;
    public int unique_id;
    public Node(String id,double longitude,double latitude){
        this.id = id;
        this.longitude=longitude;
        this.latitude=latitude;
        this.unique_id = counter++;
    }

    public String toString(){
        return "Unique id: " +Integer.toString(unique_id)+" longitude :"+Double.toString(longitude)+" latitude :"+Double.toString(latitude);
    }

}
