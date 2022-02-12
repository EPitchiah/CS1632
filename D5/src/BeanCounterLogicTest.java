import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.nasa.jpf.vm.Verify;
import java.util.Random;
import jdk.internal.dynalink.support.AutoDiscovery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;


/**
 * Code by @author Wonsun Ahn
 * 
 * <p>Uses the Java Path Finder model checking tool to check BeanCounterLogic in
 * various modes of operation. It checks BeanCounterLogic in both "luck" and
 * "skill" modes for various numbers of slots and beans. It also goes down all
 * the possible random path taken by the beans during operation.
 */

public class BeanCounterLogicTest {
	private static BeanCounterLogic logic; // The core logic of the program
	private static Bean[] beans; // The beans in the machine
	private static String failString; // A descriptive fail string for assertions

	private static int slotCount; // The number of slots in the machine we want to test
	private static int beanCount; // The number of beans in the machine we want to test
	private static boolean isLuck; // Whether the machine we want to test is in "luck" or "skill" mode

	// private methods from GradeScopeTest
	private int getInFlightBeanCount(BeanCounterLogic logic, int slotCount) {
		int inFlight = 0;
		for (int yPos = 0; yPos < slotCount; yPos++) {
			int xPos = logic.getInFlightBeanXPos(yPos);
			if (xPos != BeanCounterLogic.NO_BEAN_IN_YPOS) {
				inFlight++;
			}
		}
		return inFlight;
	}

	private int getInSlotsBeanCount(BeanCounterLogic logic, int slotCount) {
		int inSlots = 0;
		for (int i = 0; i < slotCount; i++) {
			inSlots += logic.getSlotBeanCount(i);
		}
		return inSlots;
	}
	
	/**
	 * Sets up the test fixture.
	 */
	@BeforeClass
	public static void setUp() {
		if (Config.getTestType() == TestType.JUNIT) {
			slotCount = 5;

			beanCount = 3;
			isLuck = true;
		} else if (Config.getTestType() == TestType.JPF_ON_JUNIT) {
			/*
			 * TODO: Use the Java Path Finder Verify API to generate choices for slotCount,
			 * beanCount, and isLuck: slotCount should take values 1-5, beanCount should
			 * take values 0-3, and isLucky should be either true or false. For reference on
			 * how to use the Verify API, look at:
			 * https://github.com/javapathfinder/jpf-core/wiki/Verify-API-of-JPF
			 */
			slotCount = Verify.getInt(1, 5);
			beanCount = Verify.getInt(0, 3);
			isLuck = Verify.getBoolean();

		} else {
			assert (false);
		}
		// Create the internal logic
		logic = BeanCounterLogic.createInstance(slotCount);
		// Create the beans
		beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = Bean.createInstance(slotCount, isLuck, new Random(42));
		}
		failString = "Failure in (slotCount=" + slotCount 
				+ ", beanCount=" + beanCount + ", isLucky=" + isLuck + "):";
	}
	
	@AfterClass
	public static void tearDown() {
		logic = null;
		beans = null;
	}
	
	/**
	 * Test case for void reset(Bean[] beans). Preconditions: None. Execution steps:
	 * Call logic.reset(beans). Invariants: If beanCount is greater than 0,
	 * remaining bean count is beanCount - 1 in-flight bean count is 1 (the bean
	 * initially at the top) in-slot bean count is 0. If beanCount is 0, remaining
	 * bean count is 0 in-flight bean count is 0 in-slot bean count is 0.
	 */
	@Test
	public void testReset() {
		// TODO: Implement
		/*
		 * Currently, it just prints out the failString to demonstrate to you all the
		 * cases considered by Java Path Finder. If you called the Verify API correctly
		 * in setUp(), you should see all combinations of machines
		 * (slotCount/beanCount/isLucky) printed here:
		 * 
		 * Failure in (slotCount=1, beanCount=0, isLucky=false): Failure in
		 * (slotCount=1, beanCount=0, isLucky=true): Failure in (slotCount=1,
		 * beanCount=1, isLucky=false): Failure in (slotCount=1, beanCount=1,
		 * isLucky=true): ...
		 * 
		 * PLEASE REMOVE when you are done implementing.
		 */
		// System.out.println(failString);

		logic.reset(beans);

		// checking the invariants
		if (beanCount > 0) {
			assertEquals(beanCount - 1, logic.getRemainingBeanCount());
			assertEquals(1, getInFlightBeanCount(logic, slotCount));
			assertEquals(0, getInSlotsBeanCount(logic, slotCount));

		} else {
			assertEquals(0, logic.getRemainingBeanCount());
			assertEquals(0, getInFlightBeanCount(logic, slotCount));
			assertEquals(0, getInSlotsBeanCount(logic, slotCount));

		}

	}

	/**
	 * Test case for boolean advanceStep(). Preconditions: None. Execution steps:
	 * Call logic.reset(beans). Call logic.advanceStep() in a loop until it returns
	 * false (the machine terminates). Invariants: After each advanceStep(), all
	 * positions of in-flight beans are legal positions in the logical coordinate
	 * system.
	 */
	@Test
	public void testAdvanceStepCoordinates() {
		// TODO: Implement

		// Execution Steps:
		logic.reset(beans);
		boolean run = true;
		while (run) {
			run = logic.advanceStep();
			// TODO: CHECK INVARIANT
			// use getInFlightXPos
			for (int i = 0; i < slotCount; i++) { // just check up until slotCount
				int xpos = logic.getInFlightBeanXPos(i);
				if (xpos == BeanCounterLogic.NO_BEAN_IN_YPOS) {
					assertEquals(-1, xpos);
				} else {
					assertTrue(xpos >= 0);
					assertTrue(xpos < slotCount);
				}
			}
		}
	}

	/**
	 * Test case for boolean advanceStep(). Preconditions: None. Execution steps:
	 * Call logic.reset(beans). Call logic.advanceStep() in a loop until it returns
	 * false (the machine terminates). Invariants: After each advanceStep(), the sum
	 * of remaining, in-flight, and in-slot beans is equal to beanCount.
	 */
	@Test
	public void testAdvanceStepBeanCount() {
		// TODO: Implement
		logic.reset(beans);
		boolean run = true;
		int sum = 0;
		while (run) {
			run = logic.advanceStep();
			sum = getInFlightBeanCount(logic, slotCount) + getInSlotsBeanCount(logic, slotCount)
					+ logic.getRemainingBeanCount();
			assertEquals(sum, beanCount);
		}
	}
	
	/**
	 * Test case for boolean advanceStep(). Preconditions: None. Execution steps:
	 * Call logic.reset(beans). Call logic.advanceStep() in a loop until it returns
	 * false (the machine terminates). Invariants: After the machine terminates,
	 * remaining bean count is 0 in-flight bean count is 0 in-slot bean count is
	 * beanCount.
	 */
	@Test
	public void testAdvanceStepPostCondition() {
		// TODO: Implement
		logic.reset(beans);
		boolean run = true;
		while (run) {
			run = logic.advanceStep();
		}

		assertEquals(0, logic.getRemainingBeanCount());
		assertEquals(0, getInFlightBeanCount(logic, slotCount));
		assertEquals(beanCount, getInSlotsBeanCount(logic, slotCount));
	}
	
	/**
	 * Test case for void lowerHalf()(). Preconditions: None. Execution steps: Call
	 * logic.reset(beans). Call logic.advanceStep() in a loop until it returns false
	 * (the machine terminates). Call logic.lowerHalf(). Invariants: After the
	 * machine terminates, remaining bean count is 0 in-flight bean count is 0
	 * in-slot bean count is beanCount. After calling logic.lowerHalf(), slots in
	 * the machine contain only the lower half of the original beans. Remember, if
	 * there were an odd number of beans, (N+1)/2 beans should remain. Check each
	 * slot for the expected number of beans after having called logic.lowerHalf().
	 */
	@Test
	public void testLowerHalf() {
		// TODO: Implement
		logic.reset(beans);
		boolean run = true;
		while (run) {
			run = logic.advanceStep();
		}
		assertEquals(0, logic.getRemainingBeanCount());
		assertEquals(0, getInFlightBeanCount(logic, slotCount));
		assertEquals(beanCount, getInSlotsBeanCount(logic, slotCount));
		int[] expectedSlotCounts = new int[slotCount];
		int half;
		if (beans.length % 2 == 0) {
			half = beans.length / 2;
		} else {
			half = (beanCount + 1) / 2;
		}
		int currSum = 0;
		for (int i = 0; i < slotCount; i++) { // looping through all the slots
			int newSum = logic.getSlotBeanCount(i) + currSum;
			if (newSum > half) {
				expectedSlotCounts[i] = half - currSum;
				for (int j = i + 1; j < slotCount; j++) {
					expectedSlotCounts[j] = 0;
				}
				break;
			} else if (newSum == half) {
				expectedSlotCounts[i] = logic.getSlotBeanCount(i); 
				for (int j = i + 1; j < slotCount; j++) {
					expectedSlotCounts[j] = 0; 
				}
				break;
			} else {
				expectedSlotCounts[i] = logic.getSlotBeanCount(i);
				currSum += logic.getSlotBeanCount(i);
			}
		}
		logic.lowerHalf();

		int[] observedSlotCounts = new int[slotCount];
		for (int i = 0; i < slotCount; i++) {
			observedSlotCounts[i] = logic.getSlotBeanCount(i);
		}
		for (int i = 0; i < slotCount; i++) {
			assertEquals(expectedSlotCounts[i], observedSlotCounts[i]);
		}
	}
	
	/**
	 * Test case for void upperHalf(). Preconditions: None. Execution steps: Call
	 * logic.reset(beans). Call logic.advanceStep() in a loop until it returns false
	 * (the machine terminates). Call logic.upperHalf(). Invariants: After the
	 * machine terminates, remaining bean count is 0 in-flight bean count is 0
	 * in-slot bean count is beanCount. After calling logic.upperHalf(), slots in
	 * the machine contain only the upper half of the original beans. Remember, if
	 * there were an odd number of beans, (N+1)/2 beans should remain. Check each
	 * slot for the expected number of beans after having called logic.upperHalf().
	 */
	@Test
	public void testUpperHalf() {
		// TODO: Implement
		logic.reset(beans);
		boolean run = true;
		while (run) {
			run = logic.advanceStep();
		}
		// checking invariants
		assertEquals(0, logic.getRemainingBeanCount());
		assertEquals(0, getInFlightBeanCount(logic, slotCount));
		assertEquals(beanCount, getInSlotsBeanCount(logic, slotCount));

		int[] expectedSlotCounts = new int[slotCount]; // creating an array for the expected slot counts

		// calculating half the number of beans
		int half = 0;
		if (beanCount % 2 == 0) {
			half = beanCount / 2;
		} else {
			half = (beanCount + 1) / 2;
		}
		int total = 0;
		for (int i = slotCount - 1; i >= 0; i--) { 
			int newSum = total + logic.getSlotBeanCount(i); 
			if (newSum > half) {
				expectedSlotCounts[i] = half - total;
				for (int j = i - 1; j >= 0; j--) {
					expectedSlotCounts[j] = 0;
				}
				break;
			} else if (newSum == half) {
				expectedSlotCounts[i] = logic.getSlotBeanCount(i);
				for (int j = i - 1; j >= 0; j--) {
					expectedSlotCounts[j] = 0;
				}
				break;

			} else {
				expectedSlotCounts[i] = logic.getSlotBeanCount(i);
				total += expectedSlotCounts[i];
			}
		}
		logic.upperHalf();

		int[] observedSlotCounts = new int[slotCount];
		for (int i = 0; i < slotCount; i++) {
			observedSlotCounts[i] = logic.getSlotBeanCount(i);
		}
		for (int i = 0; i < slotCount; i++) {
			assertEquals(expectedSlotCounts[i], observedSlotCounts[i]);
		}
	}
	
	/**
	 * Test case for void repeat(). Preconditions: The machine is operating in skill
	 * mode. Execution steps: Call logic.reset(beans). Call logic.advanceStep() in a
	 * loop until it returns false (the machine terminates). Call logic.repeat();
	 * Call logic.advanceStep() in a loop until it returns false (the machine
	 * terminates). Invariants: Bean count in each slot is identical after the first
	 * run and second run of the machine.
	 */
	@Test
	public void testRepeat() {
		if (!isLuck) {
			// TODO: Implement
			logic.reset(beans);

			boolean run = true;
			while (run) {
				run = logic.advanceStep();
			}

			int[] firstIterationSlotCounts = new int[slotCount];
			for (int i = 0; i < slotCount; i++) {
				firstIterationSlotCounts[i] = logic.getSlotBeanCount(i);
			}
			logic.repeat();
			run = true;
			while (run) {
				run = logic.advanceStep();
			}

			int[] secondIterationSlotCounts = new int[slotCount];
			for (int i = 0; i < slotCount; i++) {
				secondIterationSlotCounts[i] = logic.getSlotBeanCount(i);
			}

			for (int i = 0; i < slotCount; i++) {
				assertEquals(firstIterationSlotCounts[i], secondIterationSlotCounts[i]);
			}
		}
	}
}