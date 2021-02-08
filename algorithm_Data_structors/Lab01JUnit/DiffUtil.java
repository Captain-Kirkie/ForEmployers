package Lab01JUnit;

public class DiffUtil {
	
	
	/**
	 // * @param arr -- input array of integers
	  * @return The smallest difference (absolute value of subtraction) among every
	  *         pair of integers in the input array. If the array contains less
	  *         than two items, returns -1.
	  */
	public static int findSmallestDiff(int[] a) {
		
		if (a.length < 2) {
		    return -1;
		  }

		  int diff = Math.abs(a[0] - a[1]); //starting diff

		  for (int i = 0; i < a.length; i++) {  
		    for (int j = i + 1; j < a.length; j++) {
		      int tmp_diff = Math.abs(a[i] - a[j]);; //get a temp diff 
		      
		      if (tmp_diff < diff) //compare the two
		        diff = tmp_diff;
		    }
		  }

		  return diff;
	}
	
	
	
	
	
	
}
