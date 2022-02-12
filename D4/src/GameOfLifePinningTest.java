import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameOfLifePinningTest {
	/*
	 * READ ME: You may need to write pinning tests for methods from multiple
	 * classes, if you decide to refactor methods from multiple classes.
	 * 
	 * In general, a pinning test doesn't necessarily have to be a unit test; it can
	 * be an end-to-end test that spans multiple classes that you slap on quickly
	 * for the purposes of refactoring. The end-to-end pinning test is gradually
	 * refined into more high quality unit tests. Sometimes this is necessary
	 * because writing unit tests itself requires refactoring to make the code more
	 * testable (e.g. dependency injection), and you need a temporary end-to-end
	 * pinning test to protect the code base meanwhile.
	 * 
	 * For this deliverable, there is no reason you cannot write unit tests for
	 * pinning tests as the dependency injection(s) has already been done for you.
	 * You are required to localize each pinning unit test within the tested class
	 * as we did for Deliverable 2 (meaning it should not exercise any code from
	 * external classes). You will have to use Mockito mock objects to achieve this.
	 * 
	 * Also, you may have to use behavior verification instead of state verification
	 * to test some methods because the state change happens within a mocked
	 * external object. Remember that you can use behavior verification only on
	 * mocked objects (technically, you can use Mockito.verify on real objects too
	 * using something called a Spy, but you wouldn't need to go to that length for
	 * this deliverable).
	 */

	/* TODO: Declare all variables required for the test fixture. */
		
	MainPanel mainPanel;
	Cell aliveCell;
	Cell deadCell;
	
	@Before
	public void setUp() {
		/*
		 * TODO: initialize the text fixture. For the initial pattern, use the "blinker"
		 * pattern shown in:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Examples_of_patterns
		 * The actual pattern GIF is at:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#/media/File:Game_of_life_blinker.gif
		 * Start from the vertical bar on a 5X5 matrix as shown in the GIF.
		 */

		Cell[][] cells = new Cell[5][5];
		for(int r = 0; r < 5; r++)
		{
			for(int c = 0; c < 5; c++)
			{
				if(c==2) 
				{
					if(r == 1 || r == 2 || r == 3) 
					{
						aliveCell = Mockito.mock(Cell.class);
						Mockito.when(aliveCell.getAlive()).thenReturn(true);
						cells[r][c] = aliveCell;
					}
					else{
						deadCell = Mockito.mock(Cell.class);
						Mockito.when(deadCell.getAlive()).thenReturn(false);
						cells[r][c] = deadCell;
					}
				}
				else 
				{
					deadCell = Mockito.mock(Cell.class);
					Mockito.when(deadCell.getAlive()).thenReturn(false);
					cells[r][c] = deadCell;
				}
				//System.out.print(cells[r][c].getAlive() + " ");
			}
			//System.out.println();
		}
		mainPanel = new MainPanel(cells);
	}

	/* TODO: Write the three pinning unit tests for the three optimized methods */
	//write pinning tests for calculateNextIternation() and iterateCell() from MainPanel.java
	@Test
	public void pinningTestCalculateNextIteration() {
		mainPanel.calculateNextIteration();

		//visiting all the cells and using behavior verification to check
		//if the cells changed correctly
		for(int r = 0; r < 5; r++){
			for(int c = 0; c < 5; c++){
				Cell currentCell = mainPanel.getCells()[r][c];
				if(c==2)
				{ //checking if the vertical bar changed to a horizontal bar
					if(r == 2)
					{
						Mockito.verify(currentCell).setAlive(true);
					}
					else{
						Mockito.verify(currentCell).setAlive(false);
					}
				}
				else if(r==2){
					if(c == 1 ||  c == 3)
					{
						Mockito.verify(currentCell).setAlive(true);
					}
					else{
						Mockito.verify(currentCell).setAlive(false);
					}
				}
				else{ //all the other cells should be dead
					Mockito.verify(currentCell).setAlive(false);
				}

			}
		}
		//mainPanel.calculateNextIteration();
		//Mockito.verify(mainPanel).iterateCell(); //we need some sort of numbers in the iterate cell call.
	}
	@Test
	public void pinningTestIterateCell() {

		//visiting all the cells to see if they are alive/dead correctly
		for (int r = 0; r < 5; r++) {
			for (int c = 0; c < 5; c++) {
				//checking if the horizontal bar will change to a vertical bar
				if (c == 2) {
					if (r == 2) {
						assertTrue(mainPanel.iterateCell(r, c));
					} else {
						assertFalse(mainPanel.iterateCell(r, c));
					}

				} else if (r == 2) {
					if (c == 1 || c == 3) {
						assertTrue(mainPanel.iterateCell(r, c));
					} else {
						assertFalse(mainPanel.iterateCell(r, c));
					}
				} else { //all the other cells should be dead
					assertFalse(mainPanel.iterateCell(r, c));
				}


			}
		}
	}

	@Test
	public void pinningTestToString() {
		Cell c = new Cell();
		c.setAlive(true);
		String alive = c.toString();
		c.setAlive(false);
		String dead = c.toString();
		
		assertEquals(alive,"X");
		assertEquals(dead, ".");
		
	}
	

}
 