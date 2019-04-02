/**
 * Created by tkalbar on 3/2/19.
 */
import java.util.Arrays;

public class Program2 {

    public int constructIntensityGraph(int[][] image){
        // Get the adjacency matrix
        int[][] adjMatrix = constructAdjMatrix(image);

        // Get the sum of bottom half of the adjacency matrix
        int sum = sumAdjacencyMatrix(adjMatrix);

        return sum;
    }

    public int constructPrunedGraph(int[][] image){
        // Sort Edges in ascending order

        // For each edge
            // Pick the next smallest edge
            // Check if adding this edge to a list will create a cycle
            // If a cycle is formed
                // Do not include the edge
            // else
                // Include the edge

        
        // Sum the lower edges
        // int sum = sumAdjacencyMatrix(minspanningMatrix);
        //
        // return sum;
        return 0;
    }

    // Helper Function to return an edge graph
    public int[][] constructAdjMatrix(int[][] image){
        // We will construct an adjacency matrix that is size MxN long
        int[][] adjMatrix = new int[image.length * image[0].length][image.length * image[0].length];
        // Image list
        int[] imageList = new int[image.length * image[0].length];

        // Create a list representation of the image
        int counter = 0;
        for ( int i = 0; i < image.length; i++ ){
            // For each column in the image
            for ( int j = 0; j < image[i].length; j++ ){
                imageList[counter] = image[i][j];
                counter++;
            }
        }

        // Create the adjacency matrix
        for ( int i = 0; i < adjMatrix.length; i++ ){
            // Above Edge
            if (i > 4){
                int j = i - image[0].length;
                int diff = Math.abs( imageList[j] - imageList[i] );
                adjMatrix[i][j] = diff;
                adjMatrix[j][i] = diff;
            }
            // Left Edge
            boolean atBeginningOfLine = (i % image[0].length == 0);
            if ((i > 0) && !(atBeginningOfLine)){
                int j = i - 1;
                int diff = Math.abs( imageList[j] - imageList[i] );
                adjMatrix[i][j] = diff;
                adjMatrix[j][i] = diff;
            }
            // Right Edge
            boolean atEndOfLine = ((i+1) % image[0].length == 0);
            if ((i + 1 < imageList.length) && !(atEndOfLine)) {
                int j = i + 1;
                int diff = Math.abs( imageList[j] - imageList[i] );
                adjMatrix[i][j] = diff;
                adjMatrix[j][i] = diff;
            }
            // Bottom Edge
            if (i + 5 < imageList.length){
                int j = i + image[0].length;
                int diff = Math.abs( imageList[j] - imageList[i] );
                adjMatrix[i][j] = diff;
                adjMatrix[j][i] = diff;
            }
        }

        return adjMatrix;
    }

    // Helper function to return all the unrepeated edges of an adjacency matrix
    public int sumAdjacencyMatrix(int[][] matrix){
        int sum = 0;
        // Sum all the edges
        for ( int i = 0; i < matrix.length; i++ ){
            // For each column in the image
            for ( int j = 0; j < matrix.length; j++ ){
                if (j <= i) {
                    sum += matrix[i][j];
                }
            }
        }
        return sum;
    }

}
