
public class SpaceFlight {

    private int destination;
    private int fuel;
    private int time;

    public SpaceFlight(int destination, int fuel, int time) {
        this.destination = destination;
        this.fuel = fuel;
        this.time = time;
    }

    public int getFuel() {
        return this.fuel;
    }

    public int getTime() {
        return this.time;
    }

    public int getDestination() {
        return this.destination;
    }

    public String toString() {
        return "Destination: " + destination + ", Fuel: " + fuel + ", Time: " + time;
    }

}
