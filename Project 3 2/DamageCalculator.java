import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//NOTE: DO NOT CHANGE THIS CODE
//

/***
 * NOTE: DO NOT CHANGE THIS CODE
 * Program 3 contains an DamageCalculator object that is initialized in the Driver.
 * Do not intialize a new DamageCalculator object. Use the calculateDamage function associated with the
 * DamageCalculator object that we have already intialized for you
 */
public class DamageCalculator {
    
    private int totalTime;
    private int numAttacks;
    private int[][] function;

    public DamageCalculator(String fileName) throws FileNotFoundException{

        Scanner sc = new Scanner(new File(fileName));
        String nextLine = sc.nextLine();
        String[] split = nextLine.split(" ");
        numAttacks = Integer.parseInt(split[0]);
        totalTime = Integer.parseInt(split[1]);
        function = new int[numAttacks][totalTime + 1];

        for(int i = 0; i < numAttacks; i++){
            function[i][0] = 0;
        }

        int i = 0;
        while(sc.hasNextLine()){
            nextLine = sc.nextLine();
            split = nextLine.split(" ");
            for(int j = 0; j < totalTime; j++){
                function[i][j + 1] = Integer.parseInt(split[j]);
            }
            i++;
        }

    }

    /***
     * @param attack: Attack number
     * @param time: Amount of time
     * @return damage inflicted
     */
    public int calculateDamage(int attack, int time) {
        return function[attack][time];
    }

    public int getNumAttacks() {
        return numAttacks;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
