import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GradeScopeTest {

	private final int[] beanCounts = { 0, 2, 20, 200 };
	private final int[] logicSlotCounts = { 1, 10, 20 };
	private BeanCounterLogic[] logics;
	private Random rand;

	ByteArrayOutputStream out;
	PrintStream stdout;

	private String getFailString(int logicIndex, int beanCount) {
		return "[Slot Count = " + logicSlotCounts[logicIndex] + "] Test case with " + beanCount
				+ " initial beans failed";
	}

	private Bean[] createBeans(int slotCount, int beanCount, boolean luck) {
		Bean[] beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = Bean.createInstance(slotCount, luck, rand);
		}
		return beans;
	}

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
	 * The test fixture. Creates multiple machines (logics) with different slot
	 * counts. It also creates a real random object. But the random object is seeded
	 * with 42 so the tests will be reproducible.
	 */
	@Before
	public void setUp() {
		logics = new BeanCounterLogic[logicSlotCounts.length];
		for (int i = 0; i < logics.length; i++) {
			logics[i] = BeanCounterLogic.createInstance(logicSlotCounts[i]);
		}
		rand = new Random(42);

		out = new ByteArrayOutputStream();
		stdout = System.out;
		try {
			System.setOut(new PrintStream(out, false, Charset.defaultCharset().toString()));
		} catch (UnsupportedEncodingException uex) {
			fail();
		}
	}

	@After
	public void tearDown() {
		logics = null;
		rand = null;
		out = null;

		System.setOut(stdout);
	}

	/**
	 * Test reset(Bean[]). Preconditions: logics for each slot count in
	 * logicSlotCounts are created. Execution steps: For each logic, and for each
	 * bean count in beanCounts, Call createBeans to create lucky beans for the slot
	 * count and bean count Call logic.reset(beans). Invariants: If initial bean
	 * count is greater than 0, remaining bean count is beanCount - 1 in-flight bean
	 * count is 1 (the bean initially at the top) in-slot bean count is 0.
	 * <p>
	 * If initial bean count is 0, remaining bean count is 0 in-flight bean count is
	 * 0 in-slot bean count is 0.
	 */
	@Test
	public void testReset() {
		for (int i = 0; i < logics.length; i++) // for each logic
		{
			for (int beanCount : beanCounts) // for each bean
			{
				String failString = getFailString(i, beanCount);
				Bean[] beans = createBeans(logicSlotCounts[i], beanCount, true); // creates a lucky bean
				logics[i].reset(beans);

				int remainingObserved = logics[i].getRemainingBeanCount();
				int inFlightObserved = getInFlightBeanCount(logics[i], logicSlotCounts[i]);
				int inSlotsObserved = getInSlotsBeanCount(logics[i], logicSlotCounts[i]);

				int remainingExpected = (beanCount > 0) ? beanCount - 1 : 0;
				int inFlightExpected = (beanCount > 0) ? 1 : 0;
				int inSlotsExpected = 0;

				assertEquals(failString + ". Check on remaining bean count", remainingExpected, remainingObserved);
				assertEquals(failString + ". Check on in-flight bean count", inFlightExpected, inFlightObserved);
				assertEquals(failString + ". Check on in-slot bean count", inSlotsExpected, inSlotsObserved);
			}
		}
	}

	/**
	 * Test advanceStep() in luck mode. Preconditions: logics for each slot count in
	 * logicSlotCounts are created. Execution steps: For each logic, and for each
	 * bean count in beanCounts, Call createBeans to create lucky beans for the slot
	 * count and bean count Call logic.reset(beans). Call logic.advanceStep() in a
	 * loop until it returns false (the machine terminates). Invariants: After each
	 * advanceStep(), 1) The remaining bean count, 2) the in-flight bean count, and
	 * 3) the in-slot bean count all reflect the correct values at that step of the
	 * simulation.
	 */
	@Test
	public void testAdvanceStepLuckMode() {
		// TODO: Implement
		for (int i = 0; i < logics.length; i++) /// for each logic
		{
			for (int beanCount : beanCounts) // for each bean
			{
				String failString = getFailString(i, beanCount);
				Bean[] beans = createBeans(logicSlotCounts[i], beanCount, true); // creating lucky beans
				logics[i].reset(beans);

				int remainingExpected, inFlightExpected, inSlotsExpected;
				remainingExpected = beanCount;// initially all the beans are remaining
				inFlightExpected = 0;// initally there are 0 beans in flight
				inSlotsExpected = 0;// initially there are 0 beans in the slots
				int inFlightObserved = 0, remainingObserved = 0, inSlotsObserved = 0;

				boolean run = true;
				while (run) {
					run = logics[i].advanceStep();

					remainingObserved = logics[i].getRemainingBeanCount();
					inFlightObserved = getInFlightBeanCount(logics[i], logicSlotCounts[i]);
					inSlotsObserved = getInSlotsBeanCount(logics[i], logicSlotCounts[i]);

					// after a call to advance step, there is 1 less bean that is remaining
					// and 1 more is in flight
					remainingExpected = remainingExpected - 1;
					inFlightExpected = inFlightExpected + 1;

					// TODO: Calculating inSlots count
					if (inFlightExpected > logicSlotCounts[i]) {
						inFlightExpected = inFlightExpected - 1;
						inSlotsExpected = inSlotsExpected + 1;
					}

					assertEquals(failString + ". Check on remaining bean count", remainingExpected, remainingObserved);
					assertEquals(failString + ". Check on in-flight bean count", inFlightExpected, inFlightObserved);
					assertEquals(failString + ". Check on in-slot bean count", inSlotsExpected, inSlotsObserved);
				}

			}
		}
	}

	/**
	 * Test advanceStep() in skill mode. Preconditions: logics for each slot count
	 * in logicSlotCounts are created. rand.nextGaussian() always returns 0 (to fix
	 * skill level to 5). 
	 * Execution steps: For the logic with 10 slot counts, Call
	 * createBeans to create 200 skilled beans Call logic.reset(beans). Call
	 * logic.advanceStep() in a loop until it returns false (the machine
	 * terminates). Invariants: After the machine terminates,
	 * logics.getSlotBeanCount(5) returns 200, logics.getSlotBeanCount(i) returns 0,
	 * where i != 5.
	 */
	@Test
	public void testAdvanceStepSkillMode() {
		// TODO: Implement

		// we know that the 2nd logic in the logics array has 10 slot counts
		BeanCounterLogic logic = logics[1];
		Bean[] beans = createBeans(10, 200, false); // creating 200 skilled beans

		logic.reset(beans);
		boolean run = true;
		while (run) {
			run = logic.advanceStep();
		}
		// checking invariants after the loop terminates
		// checking that all 200 skilled beans fall in slot 5
		int observedSlotBeanCount = logic.getSlotBeanCount(5);
		int expectedSlotBeanCount = 200;
		assertEquals(expectedSlotBeanCount, observedSlotBeanCount);

		// checking that all other slots have 0 beans
		for (int i = 0; i < 5; i++) {
			observedSlotBeanCount = logic.getSlotBeanCount(i);
			expectedSlotBeanCount = 0;
			assertEquals(expectedSlotBeanCount, observedSlotBeanCount);
		}
		for (int i = 6; i < 10; i++) {
			observedSlotBeanCount = logic.getSlotBeanCount(i);
			expectedSlotBeanCount = 0;
			assertEquals(expectedSlotBeanCount, observedSlotBeanCount);
		}

	}

	/**
	 * Test lowerHalf() in luck mode. Preconditions: logics for each slot count in
	 * logicSlotCounts are created. Execution steps: For the logic with 10 slot
	 * counts, and for each bean count in beanCounts, Call createBeans to create
	 * lucky beans for the slot count and bean count Call logic.reset(beans). Call
	 * logic.advanceStep() in a loop until it returns false (the machine
	 * terminates). Construct an expectedSlotCounts array that stores expected bean
	 * counts for each slot, after having called logic.lowerHalf(). Call
	 * logic.lowerHalf(). Construct an observedSlotCounts array that stores current
	 * bean counts for each slot. Invariants: expectedSlotCounts matches
	 * observedSlotCounts exactly.
	 */
	@Test
	public void testLowerHalf() {
		// TODO: Implement
		BeanCounterLogic logic = logics[1]; // the logic with 10 slot counts
		for (int beanCount : beanCounts) { // for each bean count
			Bean[] beans = createBeans(10, beanCount, true); // creates lucky beans

			logic.reset(beans);
			boolean run = true;
			while (run) {
				run = logic.advanceStep();
			}
			int[] expectedSlotCounts = new int[10];

			int halfBeans;
			if (beanCount % 2 == 0) {
				halfBeans = beanCount / 2;
			} else {
				halfBeans = (beanCount + 1) / 2;
			}

			int currSum = 0;
			for (int i = 0; i < 10; i++) { // looping through all the slots
				int newSum = logic.getSlotBeanCount(i) + currSum; // calculating the new sum based on the current
																	// slots's count
				if (newSum > halfBeans) {// if we go over
					expectedSlotCounts[i] = halfBeans - currSum; // just inserting the number that is needed to reach
																	// halfBeans
					for (int j = i + 1; j < 10; j++) {
						expectedSlotCounts[j] = 0; // and then filling in the rest of the array with 0s
					}
					break;
				} else if (newSum == halfBeans) { // if we have the perfect correct number
					expectedSlotCounts[i] = logic.getSlotBeanCount(i);
					for (int j = i + 1; j < 10; j++) {
						expectedSlotCounts[j] = 0; // just filling in the rest of the array with 0s
					}
					break;
				} else {
					expectedSlotCounts[i] = logic.getSlotBeanCount(i); // inserting current slots's count to expected
																		// array
					currSum += logic.getSlotBeanCount(i); // incrementing running sum of beans
				}
			}

			logic.lowerHalf();

			int[] observedSlotCounts = new int[10];
			for (int i = 0; i < 10; i++) {// looping through all the slots and getting the observed bean counts in each
											// slot
				observedSlotCounts[i] = logic.getSlotBeanCount(i);
			}
			for (int i = 0; i < 10; i++) {// invariant checks for all the slots counts
				assertEquals(expectedSlotCounts[i], observedSlotCounts[i]);
			}

		}
	}

	/**
	 * Test upperHalf() in luck mode. Preconditions: logics for each slot count in
	 * logicSlotCounts are created. Execution steps: For the logic with 10 slot
	 * counts, and for each bean count in beanCounts, Call createBeans to create
	 * lucky beans for the slot count and bean count Call logic.reset(beans). Call
	 * logic.advanceStep() in a loop until it returns false (the machine
	 * terminates). Construct an expectedSlotCounts array that stores expected bean
	 * counts for each slot, after having called logic.upperHalf(). Call
	 * logic.upperHalf(). Construct an observedSlotCounts array that stores current
	 * bean counts for each slot. Invariants: expectedSlotCounts matches
	 * observedSlotCounts exactly.
	 */
	@Test
	public void testUpperHalf() {
		// TODO: Implement
		BeanCounterLogic logic = logics[1]; // the logic with 10 slot counts
		for (int beanCount : beanCounts) { // for each bean count
			Bean[] beans = createBeans(10, beanCount, true); // creating lucky beans
			logic.reset(beans);

			boolean run = true;
			while (run) {// calling advanceStep in a loop
				run = logic.advanceStep();
			}

			int[] expectedSlotCounts = new int[10];
			int halfBeans;
			if (beanCount % 2 == 0) {
				halfBeans = beanCount / 2;
			} else {
				halfBeans = (beanCount + 1) / 2;
			}

			int currSum = 0;
			for (int i = 9; i >= 0; i--) { // looping through all the slots
				int newSum = currSum + logic.getSlotBeanCount(i); // calculating the new sum with the current slot count
				if (newSum > halfBeans) { // if we go over
					expectedSlotCounts[i] = halfBeans - currSum;
					for (int j = i - 1; j >= 0; j--) {
						expectedSlotCounts[j] = 0; // filling in the rest of the array with 0s
					}
					break;
				} else if (newSum == halfBeans) { // if we have the correct number of beans
					expectedSlotCounts[i] = logic.getSlotBeanCount(i);
					for (int j = i - 1; j >= 0; j--) {
						expectedSlotCounts[j] = 0; // filling in the rest of the array with 0s
					}
					break;
				} else {
					expectedSlotCounts[i] = logic.getSlotBeanCount(i);
					currSum += logic.getSlotBeanCount(i); // adding the current slot count to the running sum
				}
			}

			logic.upperHalf();

			// now need to create the observed slot counts array
			int[] observedSlotCounts = new int[10];
			for (int i = 0; i < 10; i++) {// looping through all the slots and getting the observed bean counts in each
											// slot
				observedSlotCounts[i] = logic.getSlotBeanCount(i);
			}

			for (int i = 0; i < 10; i++) {// invariant checks for all the slots
				assertEquals(expectedSlotCounts[i], observedSlotCounts[i]);
			}
		}
	}

	/**
	 * Test repeat() in skill mode. Preconditions: logics for each slot count in
	 * logicSlotCounts are created. Execution steps: For the logic with 10 slot
	 * counts, and for each bean count in beanCounts, Call createBeans to create
	 * skilled beans for the slot count and bean count Call logic.reset(beans). Call
	 * logic.advanceStep() in a loop until it returns false (the machine
	 * terminates). Construct an expectedSlotCounts array that stores current bean
	 * counts for each slot. Call logic.repeat(). Call logic.advanceStep() in a loop
	 * until it returns false (the machine terminates). Construct an
	 * observedSlotCounts array that stores current bean counts for each slot.
	 * Invariants: expectedSlotCounts matches observedSlotCounts exactly.
	 */
	@Test
	public void testRepeat() {
		// TODO: Implement

		BeanCounterLogic logic = logics[1]; // the logic with 10 slot counts
		for (int beanCount : beanCounts) { // for each bean count
			Bean[] beans = createBeans(10, beanCount, false); // creating skilled beans
			logic.reset(beans);

			boolean run = true;
			while (run) {// calling advanceStep in a loop
				run = logic.advanceStep();
			}

			int[] expectedSlotCounts = new int[10];
			int observedInFlight = logic.getRemainingBeanCount();

			logic.repeat();

			run = true;
			while (run) {// calling advanceStep in a loop
				run = logic.advanceStep();
			}
			
			int[] observedSlotCounts = new int[10];
			for (int i = 0; i < 10; i++) {// looping through all the slots and getting the observed bean counts in each
											// slot
				observedSlotCounts[i] = logic.getSlotBeanCount(i);
			}

			for (int i = 0; i < 10; i++) { // invariant check
				assertEquals(expectedSlotCounts[i], observedSlotCounts[i]);
			}
			//extra Invariant
			//after the repeat method, 
			//we know remainging bean count will be the total bean count
			int observedRemain = logic.getRemainingBeanCount();
			assertEquals(beanCount, observedRemain);

		}
	}

	/**
	 * Test getAverageSlotBeanCount() in luck mode. Preconditions: logics for each
	 * slot count in logicSlotCounts are created. Execution steps: For the logic
	 * with 10 slot counts, Call createBeans to create 200 lucky beans Call
	 * logic.reset(beans). Call logic.advanceStep() in a loop until it returns false
	 * (the machine terminates). Store an expectedAverage, an average of the slot
	 * number for each bean. Store an observedAverage, the result of
	 * logic.getAverageSlotBeanCount(). Store an idealAverage, which is 4.5.
	 * Invariants: Math.abs(expectedAverage - observedAverage) < 0.01.
	 * Math.abs(idealAverage - observedAverage) < 0.5.
	 */
	@Test
	public void testGetAverageSlotBeanCount() {
		// TODO: Implement
		BeanCounterLogic logic = logics[1]; // we know that this logic has 10 slot counts

		Bean[] beans = createBeans(10, 200, true); // creates 200 lucky beans
		logic.reset(beans);

		boolean run = true;
		while (run) {// calling advanceStep in a loop
			run = logic.advanceStep();
		}

		double expectedAverage;
		double sum = 0.0;
		for (int i = 0; i < 10; i++) {
			int beanCount = logic.getSlotBeanCount(i);
			sum += (beanCount * i);
		}
		expectedAverage = sum / 200;

		double observedAverage = logic.getAverageSlotBeanCount();
		double idealAverage = 4.5;

		// checking invariants
		assertTrue(Math.abs(expectedAverage - observedAverage) < 0.01);
		assertTrue(Math.abs(idealAverage - observedAverage) < 0.5);

	}

	/**
	 * Test main(String[] args). Preconditions: None. Execution steps: Check
	 * invariants after either calling BeanCounterLogicImpl.main("10", "500",
	 * "luck"), or BeanCounterLogicImpl.main("10", "500", "skill"). Invariants:
	 * There are two lines of output. There are 10 slot counts on the second line of
	 * output. The sum of the 10 slot counts is equal to 500.
	 */
	@Test
	public void testMain() {
		// TODO: Implement using out.toString() to get output stream

		String[] arguments1 = { "10", "500", "luck" };
		BeanCounterLogicImpl.main(arguments1);
		String output = out.toString();
		String[] splitOutput = output.split("\n");
		assertEquals(splitOutput.length, 2);// There are two lines of output.
		String[] slots = splitOutput[1].split(" "); // splits the second line into slots.
		assertEquals(slots.length, 10);

		int sum = 0;
		int num;
		for (String s : slots) {
			num = Integer.parseInt(s);
			s += num;
		}
		assertEquals(sum, 500);

		// TODO: The last 2 assertions
		// String[] slots = splitOutput[1].split(" ");
		// assertTrue(slots.length == 1);

	}

}
