import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlanetPathScenario {

    private int totalFuel, numPlanets;
    private int start, end;
    private SpaceFlight[][] connections;

    public PlanetPathScenario(String fileName) throws FileNotFoundException {

        Scanner sc = new Scanner(new File(fileName));
        String nextLine = sc.nextLine();
        String[] split = nextLine.split(" ");
        numPlanets = Integer.parseInt(split[0]);
        totalFuel = Integer.parseInt(split[1]);
        nextLine = sc.nextLine();
        split = nextLine.split(" ");
        start = Integer.parseInt(split[0]);
        end = Integer.parseInt(split[1]);
        connections = new SpaceFlight[numPlanets][];
        ArrayList<ArrayList<SpaceFlight>> temp = new ArrayList<>();
        for (int i = 0; i < numPlanets; i++) {
            temp.add(new ArrayList<>());
        }
        int p1, p2, fuel, time;
        while (sc.hasNextLine()) {
            nextLine = sc.nextLine();
            split = nextLine.split(" ");
            p1 = Integer.parseInt(split[0]);
            p2 = Integer.parseInt(split[1]);
            fuel = Integer.parseInt(split[2]);
            time = Integer.parseInt(split[3]);
            temp.get(p1).add(new SpaceFlight(p2, fuel, time));
            temp.get(p2).add(new SpaceFlight(p1, fuel, time));
        }
        for (int i = 0; i < numPlanets; i++) {
            connections[i] = temp.get(i).toArray(new SpaceFlight[0]);
        }

    }

    public SpaceFlight[][] getAllFlights() {
        return connections;
    }

    public SpaceFlight[] getFlightsFromPlanet(int planet) {
        return connections[planet];
    }

    public int getTotalFuel() {
        return this.totalFuel;
    }

    public int getNumPlanets() {
        return this.numPlanets;
    }

    public int getStartPlanet() {
        return this.start;
    }

    public int getEndPlanet() {
        return this.end;
    }
}
