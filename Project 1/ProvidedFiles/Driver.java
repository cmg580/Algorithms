import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;   

public class Driver {
    public static String filename;
    public static boolean testBruteForce;
    public static boolean testGS;

    public static void main(String[] args) throws Exception {
        parseArgs(args);

        Matching problem = parseMatchingProblem(filename);
        testRun(problem);
    }

    private static void usage() {
        System.err.println("usage: java Driver [-b] [-g] <filename>");
        System.err.println("\t-b\tTest brute force implementation");
        System.err.println("\t-g\tTest Gale-Shapley server optimal implementation");
        System.exit(1);
    }

    public static void parseArgs(String[] args) {
        if (args.length == 0) {
            usage();
        }

        filename = "";
        testBruteForce = false;
        testGS = false;
        boolean flagsPresent = false;

        for (String s : args) {
            if (s.equals("-g")) {
                flagsPresent = true;
                testGS = true;
            } else if (s.equals("-b")) {
                flagsPresent = true;
                testBruteForce = true;
            } else if (!s.startsWith("-")) {
                filename = s;
            } else {
                System.err.printf("Unknown option: %s\n", s);
                usage();
            }
        }

        if (!flagsPresent) {
            testBruteForce = true;
            testGS = true;
        }
    }

    public static Matching parseMatchingProblem(String inputFile) throws Exception {
        int m = 0;
        int n = 0;
        ArrayList<ArrayList<Integer>> serverPrefs, userPrefs;
        ArrayList<Integer> serverSlots;
	ArrayList<Integer> userTimes;
	ArrayList<ArrayList<Integer>> proximities;
	ArrayList<ArrayList<Integer>> userJobs;

        Scanner sc = new Scanner(new File(inputFile));
        String[] inputSizes = sc.nextLine().split(" ");

        m = Integer.parseInt(inputSizes[0]);
        n = Integer.parseInt(inputSizes[1]);
        serverSlots = readSlotsList(sc, m);
	userTimes = readTimesList(sc, n);
        proximities = readProximities(sc, n);
        userJobs = readUserJobs(sc, n);

	userPrefs = new ArrayList<ArrayList<Integer>>(0);
	for (int i = 0; i < n; i++){
		ArrayList<Integer> tempProximity = new ArrayList<>(proximities.get(i));
		Collections.sort(tempProximity);
		ArrayList<Integer> prefList = new ArrayList<Integer>(0);
		for(int j = 0; j <m; j++){
			prefList.add( (proximities.get(i)).indexOf(tempProximity.get(j)) );
		}
		userPrefs.add(prefList);
	}

        serverPrefs = new ArrayList<ArrayList<Integer>>(0);
	for (int i = 0; i < m; i++){
		ArrayList<Integer> function_output = new ArrayList<Integer>(0);
		ArrayList<Integer> assoc_indx = new ArrayList<Integer>(0);
		for(int j = 0; j < n; j++){
			function_output.add( ( 30 * userJobs.get(j).get(i) ) + ( 70 * proximities.get(j).get(i) ) );
			assoc_indx.add(j);
		}
		
		ArrayList<Integer> tempFunc = new ArrayList<>(function_output);
		for (int j = 0; j < n-1; j++) 
        	{	 
            		int min_idx = j; 
            		for (int k = j+1; k < n; k++){ 
                		if (tempFunc.get(k) < tempFunc.get(min_idx)) 
                    			min_idx = k; 
				if (tempFunc.get(k) == tempFunc.get(min_idx)){
					if( userTimes.get(k) < userTimes.get(min_idx) )
						min_idx = k;
				}
			}
            		int temp = tempFunc.get(min_idx); 
			int temp_idx = assoc_indx.get(min_idx);
            		tempFunc.set(min_idx, tempFunc.get(j));
			assoc_indx.set(min_idx, assoc_indx.get(j)); 
            		tempFunc.set(j, temp); 
			assoc_indx.set(j,temp_idx);
        	}

                serverPrefs.add(assoc_indx);
	 
	}

	Matching problem = new Matching(m, n, serverPrefs, userPrefs, serverSlots);

        return problem;
    }

    private static ArrayList<Integer> readSlotsList(Scanner sc, int m) {
        ArrayList<Integer> serverSlots = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            serverSlots.add(Integer.parseInt(slots[i]));
        }

        return serverSlots;
    }

    private static ArrayList<Integer> readTimesList(Scanner sc, int m) {
        ArrayList<Integer> userTimes = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            userTimes.add(Integer.parseInt(slots[i]));
        }

        return userTimes;
    }

    private static ArrayList<ArrayList<Integer>> readProximities(Scanner sc, int m) {
        ArrayList<ArrayList<Integer>> proximityLists;
        proximityLists = new ArrayList<ArrayList<Integer>>(0);

        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] proximities = line.split(" ");
            ArrayList<Integer> proximityList = new ArrayList<Integer>(0);
            for (Integer j = 0; j < proximities.length; j++) {
                proximityList.add(Integer.parseInt(proximities[j]));
            }
            proximityLists.add(proximityList);
        }

        return proximityLists;
    }

    private static ArrayList<ArrayList<Integer>> readUserJobs(Scanner sc, int m) {
        ArrayList<ArrayList<Integer>> jobLists;
        jobLists = new ArrayList<ArrayList<Integer>>(0);

        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] jobs = line.split(" ");
            ArrayList<Integer> jobList = new ArrayList<Integer>(0);
            for (Integer j = 0; j < jobs.length; j++) {
                jobList.add(Integer.parseInt(jobs[j]));
            }
            jobLists.add(jobList);
        }

        return jobLists;
    }

    public static void testRun(Matching problem) {
        Program1 program = new Program1();
        boolean isStable;

        if (testGS) {
            Matching GSMatching = program.stableMarriageGaleShapley(problem);
            System.out.println(GSMatching);
            isStable = program.isStableMatching(GSMatching);
            System.out.printf("%s: stable? %s\n", "Gale-Shapley Server Optimal", isStable);
            System.out.println();
        }

        if (testBruteForce) {
            Matching BFMatching = program.stableMarriageBruteForce(problem);
            System.out.println(BFMatching);
            isStable = program.isStableMatching(BFMatching);
            System.out.printf("%s: stable? %s\n", "Brute Force", isStable);
            System.out.println();
        }
    }
}
