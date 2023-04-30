# INFO6205 Final Project
## THE TRAVELING SALESMAN PROBLEM

### Team: Changyu Wu, Ruizhe Zeng

#### Description
The travelling salesman problem (also called the travelling salesperson problem or TSP) asks the following question: "Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city?"

#### Data Structure
Graph contains nodes and original graph, which is generated by input csv file.

#### Algorithm Class
In Prim class, we used Prim algorithm to generate a minimum spanning tree from existing graph.

To run the Project please import to your IDE go to src/main/java/edu/northeastern/Main.java and hit run as java application. 

In the main function it will create a map base on the input CSV file (in this case 585 data set,but you can change the file name if needed),use Prim to create MST, trans to Muti graph with only even degree node, generate the tour , and finally do 2-opt,random swapping,genetic alg and ant colony to get the best result. More details please see the comments , thank you .

