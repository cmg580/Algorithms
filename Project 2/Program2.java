/**
 * Created by tkalbar on 3/2/19.
 */

public class Program2 {

    public int constructIntensityGraph(int[][] image){
        // Create the edge graph
        int[][] leftEdges = new int[image.length][image.length];
        int[][] bottomEdges = new int[image.length][image.length];
        
        // For each row of the image
        for ( i = 0; i < image.length; i++ ){
            // For each column in the image
            for ( j = 0; j < image[i].length; j++ ){
                // Populate left edges
                if (j-1 >= 0){
                    leftEdges[i][j] = abs( image[i][j-1] - image[i][j] );
                }
                // Populate the bottom edges
                if (i+1 <= image.length){
                    bottomEdges[i][j] = abs( image[i+1][j] - image[i][j] );
                }
            }
        }

        int sum = 0;
        // Sum all the edges
        for ( i = 0; i < image.length; i++ ){
            // For each column in the image
            for ( j = 0; j < image[i].length; j++ ){
                sum += leftEdges[i][j] + bottomEdges[i][j];
            }
        }

        return sum;
    }

    public int constructPrunedGraph(int[][] image){
        // TODO
        return 0;
    }

}
