package edu.northeastern.data;

public class Node {
    private String id;
    private double longitude;
    private double latitude;
    //    public static int counter = 0;
    private int unique_id;

    public Node(String id, double longitude, double latitude, int unique_id) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.unique_id = unique_id;
    }

    public String toString() {
        return "Unique id: " + Integer.toString(unique_id) + " longitude :" + Double.toString(longitude) + " latitude :" + Double.toString(latitude);
    }

    // Getter
    public String getId() {
        return id;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public int getUnique_id() {
        return unique_id;
    }

}
