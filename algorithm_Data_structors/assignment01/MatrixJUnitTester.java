/*
 * Here is a starting point for your Matrix tester. You will have to fill in the rest with
 * more code to sufficiently test your Matrix class. We will be using our own MatrixTester for grading.
 */
package assignment01;

//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixJUnitTester {

  Matrix threeByTwo, twoByThree, twoByTwoResult,
          threeByTwoPlusItself, testMatrix1, testMatrix2,
          resultTestMatrix1, multTestMatrix1, multTestMatrix2,
          multTestMatrix1Result1, multTestMatrix3, multTestMatrix4,
          multTestMatrix1Result2, badMatrix1, badMatrix2, nullResult;
  Boolean isTrue = false;
  /* Initialize some matrices we can play with for every test! */

  @BeforeEach
  public void setup() { //twoByThree.times(threeByTwo)
    threeByTwo = new Matrix(new int[][]{{1, 2, 3}, {2, 5, 6}});
    twoByThree = new Matrix(new int[][]{{4, 5}, {3, 2}, {1, 1}});
    // this is the known correct result of multiplying M1 by M2
    twoByTwoResult = new Matrix(new int[][]{{13, 12}, {29, 26}});
    threeByTwoPlusItself = new Matrix(new int[][] {{2, 4, 6}, {4, 10, 12}});

    testMatrix1 = new Matrix(new int[][]{{1, -2, 3}, {2, 5, 6}});
    testMatrix2 = new Matrix(new int[][]{{1, 50, -3}, {-75, 5, -30}});
    resultTestMatrix1 = new Matrix(new int[][]{{2, 48, 0}, {-73, 10, -24}});

    multTestMatrix1 = new Matrix(new int[][]{{1,2,3}, {4,5,6}});
    multTestMatrix2 = new Matrix(new int[][]{{7,8}, {9,10}, {11,12}});
    multTestMatrix1Result1 = new Matrix(new int[][]{{58,64}, {139, 154}});

    multTestMatrix3 = new Matrix(new int[][]{{3,4,2}});
    multTestMatrix4 = new Matrix(new int[][]{{13,9,7,15}, {8,7,4,6}, {6,4,0,3}});
    multTestMatrix1Result2 = new Matrix(new int[][]{{83,63,37,75}});

     // [3x4] * [2x3] -> invalid
    badMatrix1 = new Matrix(new int[][]{{3,4,2, 5}, {3,4,2, 7}, {3,4,2, 0}});
    badMatrix2 = new Matrix(new int[][]{{13,9}, {8,7}, {6,4}});
  }

  @AfterEach
  public void reset(){
    threeByTwo = null;
    twoByThree = null;
  }

  @Test
  public void additionTest1(){
    assertEquals(threeByTwoPlusItself, threeByTwo.plus(threeByTwo));
    assertEquals(null, threeByTwo.plus(twoByThree));
    assertEquals(resultTestMatrix1, testMatrix1.plus(testMatrix2));
    assertEquals(null, twoByThree.plus(threeByTwo));
  }

  @Test
  public void timesWithBalancedDimensions() {
    Matrix matrixProduct = threeByTwo.times(twoByThree);
    assertTrue(twoByTwoResult.equals(matrixProduct));

    Matrix matrixProduct1 = multTestMatrix1.times(multTestMatrix2);
    assertTrue(multTestMatrix1Result1.equals(matrixProduct1));

    Matrix matrixProduct2 = multTestMatrix3.times(multTestMatrix4);
    assertTrue(multTestMatrix1Result2.equals(matrixProduct2));
  }

  @Test
  public void timesWithUnbalancedDimensions() {
    assertEquals(null, badMatrix1.times(badMatrix2)); //incompatiable dimensions
    assertEquals(null, badMatrix2.times(badMatrix1)); //incompatiable dimensions
  }

  @Test
  public void twoByTwoToString() {
    String resultString = "13 12 \n29 26 \n";
    assertEquals(resultString, twoByTwoResult.toString());
  }

  @Test
  public void setTwoByThreeToString(){
    String resultString = "1 2 3 \n2 5 6 \n";
    assertEquals(resultString, threeByTwo.toString());
  }

  @Test
  public void equalDiffSize() { //does this equal itself?
    assertFalse(threeByTwo.equals(twoByThree));
    assertTrue(!threeByTwo.equals(twoByThree));
  }

  @Test
  public void sameSizeEquals(){
    assertTrue(threeByTwo.equals(threeByTwo));
  }

  @Test
  public void toStringTest(){
    String newString = threeByTwo.toString();
    //System.out.println(newString);
  }

}


