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

        // Prune the graph to remove any connections we cant reach at all
        pruneConnections( planetScenario.getAllFlights() );

        // Use Dijestras - Optimize for time -- if that fails, the optimize for fuel
        Planet destinationPlanet = dijestra(start, end, "time");
        boolean noFuel = destinationPlanet.fuel == -1;
        boolean noTime = destinationPlanet.time == -1;
        boolean noPathParent = destinationPlanet.pathParent == null;
        // If we couldn't get an answer when optimizing for time, optimize for fuel
        if (noFuel && noTime && noPathParent){
            destinationPlanet = dijestra(start, end, "fuel");
        }

        // If the destinationPlanet is the default, then Thanos cannot physically reach that planet
        noFuel = destinationPlanet.fuel == -1;
        noTime = destinationPlanet.time == -1;
        noPathParent = destinationPlanet.pathParent == null;
        if (noFuel && noTime && noPathParent){
            return -1;
        }

        // Get the time and fuel from the starting planet
        int time = sumTimeToPlanet( destinationPlanet );
        int fuel = sumFuelToPlanet( destinationPlanet );

        return time;
     }

     
     
     
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

    // Delete any edges from one planet to another that is greater than the fuel Thanos has
    public void pruneConnections(SpaceFlight[][] G){
        // All fuel Thanos has
        int fuel = planetScenario.getTotalFuel();
        // For each starting planet
        for (int i = 0; i < G.length; i++){
            SpaceFlight[] currentPlanet = G[i];
            ArrayList<SpaceFlight> cleanedPlanetPaths = new ArrayList<SpaceFlight>();
            // For each planet reachable by the current planet
            for (int j = 0; j < currentPlanet.length; j++){
                int fuelToPlanet = currentPlanet[j].getFuel();
                // If the fuel to that planet is not more than the fuel we have
                if (fuelToPlanet <= fuel){
                    cleanedPlanetPaths.add(G[i][j]);
                }
            }
            G[i] = cleanedPlanetPaths.toArray(new SpaceFlight[0]);
        }
    }

    // Create Dijestra's algo
    public Planet dijestra(int start, int end, String S){
        // Get all the fuel we have
        int totalFuel = planetScenario.getTotalFuel();
        // Use Dijestra's algo to find shortest path (time) from start to end
        SpaceFlight[][] connections = planetScenario.getAllFlights();
        // 1. initialize_single_source(G, s)
        MinHeap G = initialize_single_source(connections, start);      

        // 2. S <- Empty
        ArrayList<Integer> shortest_path_nodes = new ArrayList<Integer>();
        ArrayList<Integer> shortest_path_values = new ArrayList<Integer>();
        ArrayList<Planet> shortest_path_planets = new ArrayList<Planet>();

        // 3. Q <- G.V
        MinHeap Q = G;

        // Save the destination planet object when we reach it
        Planet destinationPlanet = new Planet();

        // 4. while Q != Empty
        while (Q.size() != 0){
            // 5.   do u <- Extract-Min(Q) // If the min edge is reached, we are done
            Planet u = Q.getMin();

            // Check that taking this planet wont use all our fuel
            int fuelUsed = sumFuelToPlanet( u );
            if (fuelUsed > totalFuel){
                // If getting to the planet takes more fuel than we have, the path we have taken is wrong.
                break;
            }
            // 6.     S <- S U {u}
            shortest_path_nodes.add(u.planet);
            shortest_path_values.add(u.weight);
            shortest_path_planets.add(u);

            if (u.planet == end){
                destinationPlanet = u;
                break;
            }
            // 7.     for each vertex v within Adj[u]
            for (int i = 0; i < connections[u.planet].length; i++){
                // w is the weight from u to v
                int w = connections[u.planet][i].getTime();
                // f is the fuel from u to v
                int f = connections[u.planet][i].getFuel();
                // get the destination planet reachable from the current planet
                int destination = connections[u.planet][i].getDestination();
                // if we have already traversed the planet, dont go back
                if ( !shortest_path_nodes.contains( destination ) ){
                    // 8.       do relax(u, v, w)
                    if (S.equals("time")){
                        relaxTime(Q, u, destination, w, f);
                    }
                    else if (S.equals("fuel")){
                        relaxFuel(Q, u, destination, w, f);
                    }
                    else{
                        relaxFuelTime(Q, u, destination, w, f);
                    }
                }
            }
        }

        return destinationPlanet;
    }
    
    // Recursively Traverse Planets and return the sum of the time
    public int sumTimeToPlanet(Planet P){
        return P.time;
    }

    // Recursively Traverse Planets and return the sum of the fuel
    public int sumFuelToPlanet(Planet P){
        return P.fuel;
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
                current = new Planet(i, -1, 0, 0, 0);
            }
            else{
                // 2.  do v.d <- inf
                int weight = Integer.MAX_VALUE;
                // 3.    v.pi <- NIL
                int parent = -1;
                current = new Planet(i, parent, weight, -1, -1);
            }
            // Create the Min-Heap
            minheap.createNode(current);
        }

        return minheap;
    }

    // Create Relaxation step
    public void relax(MinHeap minheap, Planet u, int destination, int w, int f){
        Planet destinationPlanet = minheap.getPlanet(destination); // Get the Planet object that is our destination
        // if v.d > u.d + w(u,v)
        if (destinationPlanet.weight > (u.weight + w + f)){
            // set the time and fuel to get to the planet from our origional starting planet
            destinationPlanet.fuel = u.fuel + f;
            destinationPlanet.time = u.time + w;
            //   then v.d <- u.d + w(u,v)
            destinationPlanet.weight = (u.weight + destinationPlanet.fuel + destinationPlanet.time);
            //     v.pi <- u
            destinationPlanet.pathParent = u;

        }
        // Regorganize the min heap with the new updated values
        minheap.update(destinationPlanet);
    }

    // Create Relaxation step
    public void relaxFuelTime(MinHeap minheap, Planet u, int destination, int w, int f){
        Planet destinationPlanet = minheap.getPlanet(destination); // Get the Planet object that is our destination
        // if v.d > u.d + w(u,v)
        if ((destinationPlanet.weight > (u.weight + w)) && ( (u.fuel + f) < planetScenario.getTotalFuel()) ){
            // set the time and fuel to get to the planet from our origional starting planet
            destinationPlanet.fuel = u.fuel + f;
            destinationPlanet.time = u.time + w;
            //   then v.d <- u.d + w(u,v)
            destinationPlanet.weight = (u.weight + destinationPlanet.time);
            //     v.pi <- u
            destinationPlanet.pathParent = u;

        }
        // Regorganize the min heap with the new updated values
        minheap.update(destinationPlanet);
    }

    // Create Relaxation step
    public void relaxTime(MinHeap minheap, Planet u, int destination, int w, int f){
        Planet destinationPlanet = minheap.getPlanet(destination); // Get the Planet object that is our destination
        // if v.d > u.d + w(u,v)
        if (destinationPlanet.weight > (u.weight + w)){
            // set the time and fuel to get to the planet from our origional starting planet
            destinationPlanet.fuel = u.fuel + f;
            destinationPlanet.time = u.time + w;
            //   then v.d <- u.d + w(u,v)
            destinationPlanet.weight = (u.weight + destinationPlanet.time);
            //     v.pi <- u
            destinationPlanet.pathParent = u;

        }
        // Regorganize the min heap with the new updated values
        minheap.update(destinationPlanet);
    }

    // Create Relaxation step
    public void relaxFuel(MinHeap minheap, Planet u, int destination, int w, int f){
        Planet destinationPlanet = minheap.getPlanet(destination); // Get the Planet object that is our destination
        // if v.d > u.d + w(u,v)
        if (destinationPlanet.weight > (u.weight + f)){
            // set the time and fuel to get to the planet from our origional starting planet
            destinationPlanet.fuel = u.fuel + f;
            destinationPlanet.time = u.time + w;
            //   then v.d <- u.d + w(u,v)
            destinationPlanet.weight = (u.weight + destinationPlanet.fuel);
            //     v.pi <- u
            destinationPlanet.pathParent = u;

        }
        // Regorganize the min heap with the new updated values
        minheap.update(destinationPlanet);
    }
}
// This is the class we will use to create the min heap
class Planet{
    public int planet;
    public int parent;
    public int weight;
    public Planet pathParent;
    public int time;
    public int fuel;

    Planet(){
        this.planet = -1;
        this.parent = -1;
        this.weight = -1;
        this.time = -1;
        this.fuel = -1;
    }

    public Planet(int planet, int parent, int weight, int time, int fuel){
        this.planet = planet;
        this.parent = parent;
        this.weight = weight;
        this.time = time;
        this.fuel = fuel;
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

    // quick function to get parent
    private int getParent(int i){
        return (i / 2);
    }

    // function to get Left
    private int left(int i){
        return (2*i);
    }

    // function to get right
    private int right(int i){
        return ( (2*i) + 1 );
    }

    // function to determine if we are on a leaf
    private boolean leaf(int i){
        int heapSize = this.node.size();
        if ((i >= (heapSize / 2)) && (i <= heapSize)){
            return true;
        }
        return false;
    }

    // function to do a swap
    private void swap(int firstPostition, int secondPosition){
        Planet temp = this.node.get(firstPostition);
        this.node.set( firstPostition, this.node.get(secondPosition) );
        this.node.set( secondPosition, temp);
    }

    // Function that maintains the heap
    private void heapify(int i){
        if (!leaf(i)){
            int lDist = this.node.get(left(i)).weight;
            int rDist = this.node.get(right(i)).weight;
            int iDist = this.node.get(i).weight;
            // If any of the children have a smaller weight than the parent
            if ( (lDist < iDist) || (rDist < iDist) ) {
                // Move up the left child if it is smaller than the right child
                if (lDist < rDist){
                    swap(i, left(i));
                    heapify(left(i));
                }

                // Move up right right child if it is smaller or equal top the left child
                else{
                    swap(i, right(i));
                    heapify(right(i));
                }
            }
        }
    }

    // Create a node and insert it into the heap
    public void createNode(Planet P){
        if (this.node.size() != 0){
            this.node.add(P);
            int currPlanetIndex = size() - 1;
            while( this.node.get( currPlanetIndex ).weight < this.node.get( getParent(currPlanetIndex) ).weight ){
                swap(currPlanetIndex, getParent(currPlanetIndex));
                currPlanetIndex = getParent( currPlanetIndex );
            }
        }
        else{
            this.node.add(P);
        }
    }

    // Create a function that will 'update' the heap
    public void update(Planet P){
        // Move everything around
        int currPlanetIndex = getIndexOfPlanet(P);
        while( this.node.get( currPlanetIndex ).weight < this.node.get( getParent(currPlanetIndex) ).weight ){
            swap(currPlanetIndex, getParent(currPlanetIndex));
            currPlanetIndex = getParent( currPlanetIndex );
        }
    }

    // Remove a node from the heap
    public Planet getMin(){
        Planet P = this.node.remove(0);
        if (this.node.size() > 1){
            heapify(0);
        }
        return P;
    }

    // getter for the size of the array
    public int size(){
        return this.node.size();
    }

    // Search the list and return the planet
    public Planet getPlanet(int i){
        // Use a basic loop to get the desired planet
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