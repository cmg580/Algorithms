/**
 * Created by tkalbar on 3/2/19.
 */

public class Program2 {

    public int constructIntensityGraph(int[][] image){
        int[][] leftEdges = new int[image.length][image[0].length];
        int[][] bottomEdges = new int[image.length][image[0].length];
        
        // For each row of the image
        for ( int i = 0; i < image.length; i++ ){
            // For each column in the image
            for ( int j = 0; j < image[i].length; j++ ){
                // Populate left edges
                if (j-1 >= 0){
                    leftEdges[i][j] = Math.abs( image[i][j-1] - image[i][j] );
                }
                // Populate the bottom edges
                if (i+1 < image.length){
                    bottomEdges[i][j] = Math.abs( image[i+1][j] - image[i][j] );
                }
            }
        }

        int sum = 0;
        // Sum all the edges
        for ( int i = 0; i < image.length; i++ ){
            // For each column in the image
            for ( int j = 0; j < image[i].length; j++ ){
                sum += leftEdges[i][j] + bottomEdges[i][j];
            }
        }

        return sum;
    }

    public int constructPrunedGraph(int[][] image){
        // Initalize constants
        int columnSize = image.length;
        int rowSize = image[0].length;
        int totalSize = columnSize * rowSize;

        // Create an array to hold the MST
        int[] mst = new int [totalSize];
        // Create an array to hold key values
        int[] keys = new int [totalSize];
        // Create an array to determine if the node is in the MST or not
        boolean[] isInMST = new boolean [totalSize];

        // Assign all the values in the graph to inf except for the first one
        for (int i = 1; i < totalSize; i++){
            keys[i] = Integer.MAX_VALUE;
            isInMST[i] = false;
        }
        int currentNode = 0;
        int[] mstWeights = new int[totalSize];

        // While the length of the min spanning tree is does not contain all the vertexes
        for (int nul = 0; nul < isInMST.length; nul++){
            int minValue = Integer.MAX_VALUE;
            // Pick a vertex that is not in the mst and has the minimum key value
            for (int i = 0; i < totalSize; i++){
                if ((!isInMST[i]) && (minValue > keys[i])){
                    currentNode = i;
                    minValue = keys[i];
                }
            }

            // Add the vertex to the mst
            isInMST[currentNode] = true;
            // Save the min value
            mstWeights[nul] = minValue;

            //// Determine the neighbors
            int [] bottomNeighbor = {-1, -1}; int bottomWeight = -1;
            int [] topNeighbor    = {-1, -1}; int topWeight    = -1;
            int [] leftNeighbor   = {-1, -1}; int leftWeight   = -1;
            int [] rightNeighbor  = {-1, -1}; int rightWeight  = -1;
            // Set the Current node in 2D
            int[] currentNode2D = to2Dindices(currentNode, image);

            // Above Edge
            if (currentNode2D[0] > 0){
                topNeighbor[0] = currentNode2D[0] - 1; topNeighbor[1] = currentNode2D[1];
                int diff = Math.abs( image[topNeighbor[0]][topNeighbor[1]] - image[currentNode2D[0]][currentNode2D[1]] );
                topWeight = diff;
            }
            // Left Edge
            if (currentNode2D[1] > 0){
                leftNeighbor[0] = currentNode2D[0]; leftNeighbor[1] = currentNode2D[1] - 1;
                int diff = Math.abs( image[ leftNeighbor[0] ][ leftNeighbor[1] ] - image[ currentNode2D[0] ][ currentNode2D[1] ]);
                leftWeight = diff;
            }
            // Right Edge
            if (currentNode2D[1] + 1 < rowSize) {
                rightNeighbor[0] = currentNode2D[0]; rightNeighbor[1] = currentNode2D[1] + 1;
                int diff = Math.abs( image[ rightNeighbor[0] ][ rightNeighbor[1] ] - image[ currentNode2D[0] ][ currentNode2D[1] ] );
                rightWeight = diff;
            }
            // Bottom Edge
            if (currentNode2D[0] + 1 < columnSize){
                bottomNeighbor[0] = currentNode2D[0] + 1; bottomNeighbor[1] = currentNode2D[1];
                int diff = Math.abs( image[ bottomNeighbor[0] ][ bottomNeighbor[1] ] - image[ currentNode2D[0] ][ currentNode2D[1] ]);
                bottomWeight = diff;
            }

            int[][] neighborArray = {topNeighbor, rightNeighbor, bottomNeighbor, leftNeighbor};
            int[] weightArray     = {topWeight,   rightWeight,   bottomWeight,   leftWeight};
            // For each of the neighbors of the vertex
            for (int i = 0; i < neighborArray.length; i++){
                // If there is a neighbor
                if ((neighborArray[i][0] != -1) && (neighborArray[i][1] != -1)){
                    // If the key value is less than the current key
                    int keysPosition = to1Dindices(neighborArray[i][0], neighborArray[i][1], image);
                    if (weightArray[i] < keys[keysPosition]){
                        // Update the key value to the lower key
                        keys[keysPosition] = weightArray[i];
                    }
                }  
            }
            mst[nul] = currentNode;
        }
        // Sum all the mstWeights
        int sum = 0;
        for (int i = 0; i < mstWeights.length; i++){
            sum += mstWeights[i];
        }

        return sum;
    }

    // Function to convert the indices of a 2D array to 1D array indicies
    public int to1Dindices(int i, int j, int[][] image){
        int indices =  i * image[0].length + j;
        return indices;
    }

    // Function to convert the indicies of a 1D array to 2D array indicies
    public int[] to2Dindices(int i, int[][] image){
        int j_index = i % image[0].length;
        i -= j_index;
        int i_index = i / image[0].length;
        int[] indices = {i_index, j_index};
        return indices;
    }
}
