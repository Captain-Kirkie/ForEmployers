package Lab01JUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.CharArrayReader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import com.sun.tools.classfile.CompilationID_attribute;

class TestFindSmallestDiff {
	
	private int[] arr1, arr2, arr3, arr4, sorted, negArr, charArray;

	@BeforeEach
	void setUp() throws Exception {
		//called before every test method
		arr1 = new int[0];
		arr2 = new int[] { 3, 3, 3 };
		arr3 = new int[] { 52, 4, -8, 0, -17 };
		arr4 = new int[] { 4, 8, 8, 8 };
		sorted = new int[] {-17, -8, 0, 4, 52};
		negArr = new int[] {-17, -8, 0, -4, -52};
		charArray = new int[] {'a', 'b', 'Y', 'f', 'z'}; //passed as ints
	}

	@AfterEach
	void tearDown() throws Exception {
		//called after every test method
		arr1 = null;
		arr2 = null;
		arr3 = null;
		arr4 = null;
	}

	@Test
	public void emptyArray() {
		assertEquals(-1, DiffUtil.findSmallestDiff(arr1));
	}
	
	@Test
	public void allArrayElementsEqual() {
		assertEquals(0,  DiffUtil.findSmallestDiff(arr2));
	}
	
	@Test
	public void smallRandomArray() {
		assertEquals(4, DiffUtil.findSmallestDiff(arr3));
	}
	
	@Test
	public void SortedTest() {
		assertEquals(DiffUtil.findSmallestDiff(arr3), DiffUtil.findSmallestDiff(sorted));
	}
	
	@Test
	public void charTest() {
	
		for(int i : charArray) {
			System.out.println(i);
		}
		assertEquals(1, DiffUtil.findSmallestDiff(charArray));
		
	}
	
	

}
