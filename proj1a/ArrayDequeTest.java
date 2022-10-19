/** Performs some basic linked list tests. */
public class ArrayDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	public static boolean checkValue(Integer expected, Integer actual) {
		if (expected != actual) {
			System.out.println("Expected: "+expected+" Acutal:"+actual);
			return false;
		}
		return true;
	}
	public static void getTest() {
		System.out.println("Running getTest..");
		ArrayDeque<Integer> lldq1 = new ArrayDeque();
		boolean passed = checkValue(null, lldq1.get(0));
		lldq1.addFirst(0);
		lldq1.addFirst(1);
		lldq1.addFirst(2);
		lldq1.addFirst(3);
		lldq1.addFirst(4);
		lldq1.addFirst(5);
		lldq1.addFirst(6);
		lldq1.addFirst(7);
		lldq1.addFirst(8);
		passed = checkValue(8,lldq1.get(0)) && passed;
		passed = checkValue(0, lldq1.get(8)) && passed;
		passed = checkValue(null, lldq1.get(9)) && passed;
		printTestStatus(passed);
	}
	public static void resizeTest() {
		System.out.println("Running resizeTest..");
/*
		ArrayDeque<Integer> lldq1 = new ArrayDeque();
		lldq1.addFirst(0);
		lldq1.addFirst(1);
		lldq1.addFirst(2);
		lldq1.addFirst(3);
		lldq1.addFirst(4);
		lldq1.addFirst(5);
		lldq1.addFirst(6);
		lldq1.addFirst(7);
		lldq1.addFirst(8);
		boolean passed = checkSize(14,lldq1.length());
		lldq1.removeLast();
		lldq1.removeLast();
		lldq1.removeLast();
		lldq1.removeLast();
		lldq1.removeLast();
		lldq1.removeLast();
		passed = checkSize(7,lldq1.length()) && passed;
		printTestStatus(passed);
 */
	}
	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		ArrayDeque<String> lld1 = new ArrayDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;

		System.out.println("Printing out deque: ");
		lld1.printDeque();

		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeFirst();
		// should be empty front为1，rear为0，虽然整个数组为0，但empty不true
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		// Test added according to gradescope
		lld1.addFirst(2);
		lld1.removeLast();
		lld1.addFirst(4);
		lld1.removeLast();
		lld1.addFirst(8);
		lld1.removeLast();
		lld1.addFirst(10);
		passed = checkValue(10,lld1.removeLast()) && passed;

	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		getTest();
		resizeTest();
	}
} 