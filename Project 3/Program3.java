import java.util.ArrayList;

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
        SpaceFlight[][] connections = planetScenario.getAllFlights();

        // If the flight to a planet takes more fuel than we have, we cannot go to that planet
        // cleanConnections(connections);

        // This problem is very similiar to the knapsack problem


        int bestTime = Integer.MAX_VALUE;
        int bestFuel = Integer.MAX_VALUE;
        // Start slowly incrementing the total fuel
        for (int i = 0; i <= totalFuel; i++){
            // Since we know the starting planet, we know which row to get first
            SpaceFlight[] G = connections[start];
            int[] alreadyExplored = new int[connections.length];
            alreadyExplored = initalizeArray(alreadyExplored);
            alreadyExplored[0] = start;
            int fuel = i;
            int time_consumed = 0;
            int fuel_consumed = 0;
            int[] results = rabbitHole(G, alreadyExplored, fuel, time_consumed, fuel_consumed);

            // Extract the results
            time_consumed = results[0];
            fuel_consumed = results[1];

            // If they both do not equal -1, then we have found a cheap way to the planet!
            if ((time_consumed != -1) && (fuel_consumed != -1) && (time_consumed < bestTime)){
                bestTime = time_consumed;
                bestFuel = fuel_consumed;
            }
        }
        if ((bestTime != Integer.MAX_VALUE) && (bestFuel != Integer.MAX_VALUE)){
            return bestTime;
        }
        else{
            // Since we could not reach the desired planet with all of the fuel, we return -1
            return -1;
        }
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

    // Function to remove any edges greater than the fuel we have
    public void cleanConnections(SpaceFlight[][] G){
        // Get the total amount of fuel Thanos has
        int totalFuel = planetScenario.getTotalFuel();

        // Go through and clean the connections
        for (int i = 0; i < G.length; i++){
            ArrayList<SpaceFlight> rowOfPlanets = new ArrayList<SpaceFlight>();
            for (int j = 0; j < G[i].length; j++){
                if (G[i][j].getFuel() < totalFuel){
                    rowOfPlanets.add(G[i][j]);
                }
            }
            G[i] = rowOfPlanets.toArray(new SpaceFlight[0]);
        }
    }

    // Recursive Funciton to go down and see what values we can reach
    public int[] rabbitHole(SpaceFlight[] G, int[] alreadyExplored, int fuel, int time_consumed, int fuel_consumed){
        // Get the desired landing planet
        int end = planetScenario.getEndPlanet();
        // If we have reached our desired planet, go ahead and return
        SpaceFlight[][] connections = planetScenario.getAllFlights();
        if (connections[end] == G){
            int[] returnValue = {time_consumed, fuel_consumed};
            return returnValue;
        }
        // Since for one starting planet, we may have multiple routes, create an array to hold those values
        ArrayList<int[]> results = new ArrayList<int[]>();
        // Go through the planets and see which ones we can go to
        for (int i = 0; i < G.length; i++){
            // If the fuel it takes to get to the planet is less than the fuel we have and we havent visited the planet before
            SpaceFlight currentPlanet = G[i];
            if ( (currentPlanet.getFuel() <= fuel) && ( !isIn(alreadyExplored, currentPlanet.getDestination()) ) ){
                // Mark the planet as explored
                add(alreadyExplored, currentPlanet.getDestination());
                // Then check the rest of the planets
                int[] result = rabbitHole( connections[currentPlanet.getDestination()], copy(alreadyExplored), fuel - currentPlanet.getFuel(), time_consumed + currentPlanet.getTime(), fuel_consumed + currentPlanet.getFuel());
                // Remove the planet so that the next one can go into
                remove(alreadyExplored, currentPlanet.getDestination());
                results.add(result);
            }
        }
        // If we have a result
        if ( !(results.size() == 0) ){
            // Go through and get the min time and min fuel consumption
            int[] bestResult = {Integer.MAX_VALUE, Integer.MAX_VALUE};
            for (int i = 0; i < results.size(); i++){
                // If the result is not -1 for time and fuel
                time_consumed = results.get(i)[0];
                fuel_consumed = results.get(i)[1];
                boolean badTime = (time_consumed == -1);
                boolean badFuel = (fuel_consumed == -1);
                if ((!badTime && !badFuel) && (time_consumed < bestResult[0])){
                    bestResult[0] = time_consumed;
                    bestResult[1] = fuel_consumed;
                }
            }
            
            if ( !((bestResult[0] == Integer.MAX_VALUE) && (bestResult[1] == Integer.MAX_VALUE)) ){
                return bestResult;
            }
            // If all the results were really bad, return the bad array (fall through)
        }

        // If the length of the results array is zero, return a bad array
        // If there is not enough fuel to get to the desired planet, return -1 for everything
        int[] badReturn = {-1, -1};
        return badReturn;
    }

    // Function to add item to end of list
    public void add(int[] array, int num){
        for (int i = 0; i < array.length; i++){
            // If there is no number at the area
            if( array[i] == -1 ){
                array[i] = num;
            }
        }
    }
    // Function to remove the item from a list
    public void remove(int[] array, int num){
        for (int i = 0; i < array.length; i++){
            // If there is no number at the area
            if( array[i] == num ){
                array[i] = -1;
            }
        }
    }
    // Function to check if a number exists in a list
    public boolean isIn(int[] array, int num){
        for (int i = 0; i < array.length; i++){
            // If there is no number at the area
            if( array[i] == num ){
                return true;
            }
        }
        return false;
    }
    // Function to turn all the planets to -1
    public int[] initalizeArray(int[] array){
        for (int i = 0; i < array.length; i++){
            array[i] = -1;
        }
        return array;
    }
    // Function to create anther array
    public int[] copy(int[] array){
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++){
            newArray[i] = array[i];
        }
        return newArray;
    }
}