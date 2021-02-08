package assignment01;

public class Matrix {
    int numRows;
    int numColumns;
    int data[][];

    // Constructor with data for new matrix (automatically determines dimensions)
    public Matrix(int data[][]) {
        numRows = data.length; // d.length is the number of 1D arrays in the 2D array
        if (numRows == 0) {
            numColumns = 0;
        } else {
            numColumns = data[0].length; // d[0] is the first 1D array
        }
        this.data = new int[numRows][numColumns]; // create a new matrix to hold the data
        // copy the data over
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }
  /*
  [][][][]
  [][][][]
  */

    @Override // instruct the compiler that we do indeed intend for this method to override
    // the superclass' (Object) version
    public boolean equals(Object other) {
        if (!(other instanceof Matrix)) { // make sure the Object we're comparing to is a Matrix
            return false;
        }
        Matrix matrix = (Matrix) other; // if the above was not true, we know it's safe to treat 'o' as a Matrix

        /*
         * replace the below return statement with the correct code, you must
         * return the correct value after determining if this matrix is equal to the
         * input matrix
         */
        if (this.numRows != matrix.numRows || this.numColumns != matrix.numColumns) {
            return false;
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (this.data[i][j] != matrix.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // instruct the compiler that we do indeed intend for this method to override
    // the superclass' (Object) version
    public String toString() {
        /*
         * TODO: replace the below return statement with the correct code, you must
         * return a String that represents this matrix, as specified on the assignment
         * page
         */
        //TODO:Need to use StringBuilder
        StringBuilder newString = new StringBuilder("");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                String tmp = Integer.toString(this.data[i][j]);
                newString.append(tmp + " ");
                if (j == numColumns - 1) {
                    newString.append("\n");
                }
            }
        }
        String toReturn = newString.toString();
        return toReturn;
    }

    public Matrix times(Matrix matrix) {
        /*
        https://www.programiz.com/java-programming/examples/multiply-matrix-function
         * TODO: replace the below return statement with the correct code, This function
         * must check if the two matrices are compatible for multiplication, if not,
         * return null. If they are compatible, determine the dimensions of the
         * resulting matrix, and fill it in with the correct values for matrix
         * multiplication
         */
        Matrix returnMatrix = null;
        if (this.numColumns != matrix.numRows) {
            return null;
        } else if (this.numColumns == matrix.numRows) {
            int arr[][] = new int[this.numRows][matrix.numColumns]; //new matrix with lhs rows, and rhs collumns
            returnMatrix = new Matrix(arr);
            System.out.println("num rows: " + returnMatrix.numRows);
            System.out.println("num collumns: " + returnMatrix.numColumns);
            //i = rows
            //j = collumns
            for (int i = 0; i < this.numRows; i++) {
                for (int j = 0; j < matrix.numColumns; j++) {
                    for(int k = 0; k < this.numColumns; k++){
                        returnMatrix.data [i][j] += this.data[i][k] * matrix.data[k][j];
                    }
                    System.out.println(returnMatrix.data[i][j]);
                }
            }
        }
        return returnMatrix;
         // placeholder
    }

    public Matrix plus(Matrix matrix) { //TODO:Write test for this verify what you did is right
        /*
         * TODO: replace the below return statement with the correct code, This function
         * must check if the two matrices are compatible for addition, if not, return
         * null. If they are compatible, create a new matrix and fill it in with the
         * correct values for matrix addition
         */
        //can i use == and !=?? or .equals
        int arr[][] = new int[numRows][numColumns];
        if (this.numColumns != matrix.numColumns || this.numRows != matrix.numRows) {
            return null;
        } else { //TODO:is this the right constructor? need an empty matrix to fill???
            Matrix sumMatrix = new Matrix(arr); //this matrix is full and gets overwritten
            //TODO:Do i have to create a matrix of a specific size?
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    sumMatrix.data[i][j] = this.data[i][j] + matrix.data[i][j];
                    //System.out.print(sumMatrix.data[i][j]);
                }
            }
            return sumMatrix;
        }
    }
}


//TODO:EXTRA CODE THIS ONE ALSO WORKS ??
  /*
public Matrix plus(Matrix matrix) {
    Matrix sumMatrix;
    if(this.numColumns != matrix.numColumns || this.numRows != matrix.numRows){
        return null;
    }else{
        sumMatrix = new Matrix(sumMatrix.data[numRows][numColumns]); //this matrix is full and gets overwritten
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numColumns; j++){
                sumMatrix.data[i][j] = this.data[i][j] + matrix.data[i][j];
                System.out.print(sumMatrix.data[i][j]);
            }
        }
        return sumMatrix;
    }
}
}
*/
