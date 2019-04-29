import sun.jvm.hotspot.gc.shared.Space;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

//Name: Christian Gil
//EID: cmg4463


public class Program3 {

    DamageCalculator calculator;
    PlanetPathScenario planetScenario;

    public Program3() {
        this.calculator = null;
        this.planetScenario = null;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 2.
     */
    public void initialize(PlanetPathScenario ps) {
        this.planetScenario = ps;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 1.
     */
    public void initialize(DamageCalculator dc) {
        this.calculator = dc;
    }


    /*
     * This method returns an integer that is the minimum amount of time necessary to travel
     * from the start planet to the end planet in the PlanetPathScenario given the total
     * amout of fuel that Thanos has. If a path is not possible given the amount of fuel, return -1.
     */
     //TODO: Complete this method
     public int computeMinimumTime() {
        int start = planetScenario.getStartPlanet();
        int end = planetScenario.getEndPlanet();
        int totalFuel = planetScenario.getTotalFuel();

        // Use Dijestra's algo to find shortest path (time) from start to end
        SpaceFlight[][] connections = planetScenario.getAllFlights();
        // 1. initialize_single_source(G, s)
        MinHeap G = initialize_single_source(connections, start);      

        // 2. S <- Empty
        ArrayList<Integer> shortest_path_nodes = new ArrayList<Integer>();
        ArrayList<Integer> shortest_path_values = new ArrayList<Integer>();

        // 3. Q <- G.V
        MinHeap Q = G;

        // 4. while Q != Empty
        while (Q.size() != 0){
            // 5.   do u <- Extract-Min(Q) // If the min edge is reached, we are done
            Planet u = Q.getMin();
            // 6.     S <- S U {u}
            shortest_path_nodes.add(u.planet);
            shortest_path_values.add(u.distance);
            if (u.planet == end){
                break;
            }
            // 7.     for each vertex v within Adj[u]
            for (int i = 0; i < connections[u.planet].length; i++){
                // w is the weight from u to v
                int w = connections[u.planet][i].getTime();
                // get the destination planet reachable from the current planet
                int destination = connections[u.planet][i].getDestination();
                // 8.       do relax(u, v, w)
                relax(Q, u, destination, w);
            }
        }

        // Get the length of the path
        return shortest_path_values.get( shortest_path_values.size() - 1 );
     }

    // Create Initialization step
    public MinHeap initialize_single_source(SpaceFlight[][] G, int s){
        // Specify the number of planets (rows)
        int num_planets = G.length;
        // Save the SpaceFlight as a 2D array
        SpaceFlight[][] workingSpaceFlights = new SpaceFlight[num_planets][num_planets];
        
        // Create the min heap object
        MinHeap minheap = new MinHeap();

        // 1. for each vertex v within G.V
        for (int i = 0; i < num_planets; i++){
            Planet current;
            // The starting planet takes 0 minues to travel to
            if (s == i){
                // 4. s.d <- 0   
                current = new Planet(i, -1, 0);
            }
            else{
                // 2.  do v.d <- inf
                int distance = Integer.MAX_VALUE;
                // 3.    v.pi <- NIL
                int parent = -1;
                current = new Planet(i, parent, distance);
            }
            // Create the Min-Heap
            minheap.createNode(current);
        }

        return minheap;
    }


    // Create Relaxation step
    public void relax(MinHeap minheap, Planet u, int destination, int w){
        Planet destinationPlanet = minheap.getPlanet(destination); // THIS STEP WILL FAIL IF NO CONNECTION FROM 0 TO THIS PLANET
        // if v.d > u.d + w(u,v)
        if (destinationPlanet.distance > (u.distance + w)){
            //   then v.d <- u.d + w(u,v)
            destinationPlanet.distance = (u.distance + w);
            //     v.pi <- u
            destinationPlanet.pathParent = u;
        }
        // Reorganize the heap
        if (minheap.size() != 0) {
            minheap.HeapifyUp( minheap.getIndexOfPlanet( destinationPlanet ) );
        }
    }
    
    // // Create Extract-Min(Q)
    // public void extract_min(Q){

    // }
    
    // // Function to create Min-Heap 
    // public void create_min_heap( nodes, edges ){

    // }

    /*
     * This method returns an integer that is the maximum possible damage that can be dealt
     * given a certain amount of time.
     */
    //TODO: Complete this function
    public int computeDamage() {

        int totalTime = calculator.getTotalTime();
        int numAttacks = calculator.getNumAttacks();
        return 0;
    }

    /*                      *
    *                       *
    *                       *
    * EXTRA CLASSES SECTION *
    *                       *
    *                       *
    */

}
// This is the class we will use to create the min heap
class Planet{
    public int planet;
    public int parent;
    public int distance;
    public Planet pathParent;

    Planet(){
        this.planet = -1;
        this.parent = -1;
        this.distance = -1;
    }

    public Planet(int planet, int parent, int distance){
        this.planet = planet;
        this.parent = parent;
        this.distance = distance;
    }

    // Overriding the compare method to sort the age 
    public int compare(Planet P, Planet P1) {
        return P.planet - P1.planet;
    }
}

// This is the class we will use to construct the min heap
class MinHeap{
    // Declare the heap
    private ArrayList<Planet> node;

    // Initalize the nodes
    public MinHeap(){
        this.node = new ArrayList<Planet>();
    }

    // Create a node and insert it into the heap
    public void createNode(Planet P){
        if (this.node.size() != 0){
            this.node.add(P);
            HeapifyUp(this.node.size() - 1);
        }
        else{
            this.node.add(P);
        }
    }

    // Remove a node from the heap
    public Planet getMin(){
        Planet P = this.node.remove(0);
        if (this.node.size() > 1){
            HeapifyDown(0);
        }
        return P;
    }

    // Heapify-up (H. i):
    public void HeapifyUp(int i){
        // If i > 1 then
        if (i > 0){
            // let j = parent(i) = [i/2]
            this.node.get(i).parent = i / 2;
            int j = i / 2;
            // If key[H[i]] < key[H[j]] then
            if (this.node.get(i).distance < this.node.get(j).distance){
                // swap the array entries H[i] and H[j]
                Planet temp = this.node.get(i);
                this.node.set(i, this.node.get(j));
                this.node.set(j, temp);
                // HeapifyUp(H, j)
                HeapifyUp(j);
            }
        }
    }

    // Heapify-down(H,i):
    public void HeapifyDown(int i){
        // Let n = length(H)
        int n = this.node.size();
        // persist j
        int j = -1;
        // If 2i > n then
        if (2*i > n){
            // Terminate with H unchanged
            return;
        }
        // Else if 2i < n then
        else if (2*i < n){
            // Let left = 2i and right = 2i + 1
            int left = 2*i;
            int right = 2*i + 1;
            // Let j be the index that minimizes key[H[left]] and key[H[right]]
            int leftValue = this.node.get(left).distance;
            int rightValue = this.node.get(right).distance;
            if (leftValue < rightValue){
                j = left;
            }
            else if (rightValue < leftValue){
                j = right;
            }
            else{
                j = right;
            }
        }
        // Else if 2i = n then
        else if (2*i == n){
            j = 2*i;
        }
        // If key[H[j]] < key[H[i]] then
        if (this.node.get(j).distance < this.node.get(i).distance){
            // swap the array entreis H[i] and H[j]
            Planet temp = this.node.get(j);
            this.node.set(j, this.node.get(i)); 
            this.node.set(i, temp);
            // Heapify-down(H, j)
            HeapifyDown(j);
        }

    }

    // getter for the size of the array
    public int size(){
        return this.node.size();
    }

    // Search the list and return the planet
    public Planet getPlanet(int i){
        // // Clone the Array
        // List<Planet> nodes = (ArrayList<Planet>)this.node.clone();
        // // Sort the nodes
        // Arrays.sort(nodes, new Planet());
        // // Use binary search to find the planet
        // Arrays.binarySearch(nodes, i, new Planet());
        
        for (int j = 0; j < this.node.size(); j++){
            if (this.node.get(j).planet == i){
                return this.node.get(j);
            }
        }
        Planet emptyPlanet = new Planet();
        return emptyPlanet;
    }

    // Get the index of the planet
    public int getIndexOfPlanet(Planet P){
        return this.node.indexOf(P);
    }
}