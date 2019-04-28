import java.io.FileNotFoundException;
import java.util.Scanner;


/***
 * Usage:
 * Driver takes in 2 parameters.
 * The first is [-1] to test part one or [-2] to test part two of your solution
 * The second is the input file
 * Ex. "-1 4.in" tests part 1 of your solution with the file 4.in
 */
public class Driver {
    
    public static String filename;
    public static boolean willTestPartOne;
    public static boolean willTestPartTwo;

    private static int testPartOne(Program3 program, String filename) {
        
        PlanetPathScenario planetScenario = null;

        try {
            planetScenario = new PlanetPathScenario(filename);
        } catch(FileNotFoundException e) {
            System.err.printf("Could not find file: %s\n", filename);
            usage();
        }

        program.initialize(planetScenario);

        return program.computeMinimumTime();

    }

    private static int testPartTwo(Program3 program, String filename) {

        DamageCalculator calculator = null;

        try{
            calculator = new DamageCalculator(filename);
        } catch (FileNotFoundException e){
            System.err.printf("Could not find file : %s\n", filename);
            usage();
        }

        program.initialize(calculator);

        return program.computeDamage();
    }

    private static void usage() {
        System.err.println("usage: java Driver [-1] <filename>");
        System.err.println("usage: java Driver [-2] <filename>");
        System.err.println("\t-1\tTest part 1 of Program 3");
        System.err.println("\t-2\tTest part 2 of Program 3");
        System.exit(1);
    }

    public static void parseArgs(String[] args) {

        willTestPartOne = false;
        willTestPartTwo = false;
        filename = null;

        if (args.length != 2) {
            usage();
        }
        
        if (args[0].equals("-1")) {
            willTestPartOne = true;
        } else if (args[0].equals("-2")) {
            willTestPartTwo = true;
        } else {
            System.err.printf("Unknown option %s\n", args[0]);
            usage();
        }

        filename = args[1];
    }

    public static void main(String[] args) {

        Program3 program = new Program3();
        parseArgs(args);
        int result;
        
        if (willTestPartOne) {
            result = testPartOne(program, filename);
            System.out.printf("Result for %s: %s\n", "part 1", result);
        } else if (willTestPartTwo) {
            result = testPartTwo(program, filename);
            System.out.printf("Result for %s: %s\n", "part 2", result);
        }
    }
}
