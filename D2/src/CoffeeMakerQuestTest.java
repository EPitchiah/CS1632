import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.*;
import org.mockito.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;

public class CoffeeMakerQuestTest {

	CoffeeMakerQuest cmq;
	Player player;
	Room room1;	// Small room, Quaint sofa, Cream
	Room room2;	// Funny room, Sad record player, None
	Room room3;	// Refinanced room, Tight pizza, Coffee
	Room room4;	// Dumb room, Flat energy drink, None
	Room room5;	// Bloodthirsty room, Beautiful bag of money, None
	Room room6;	// Rough room, Perfect air hockey table, Sugar
	public CoffeeMakerQuestTest(){
		super();
	}
	@Before
	public void setup() {
		// 0. Turn on bug injection for Player and Room.
		Config.setBuggyPlayer(true);
		Config.setBuggyRoom(true);
		
		// 1. Create the Coffee Maker Quest object and assign to cmq.
		cmq = CoffeeMakerQuest.createInstance();

		// TODO: 2. Create a mock Player and assign to player and call cmq.setPlayer(player). 
		// Player should not have any items (no coffee, no cream, no sugar)
		player = Mockito.mock(Player.class);
		Mockito.when(player.checkCoffee()).thenReturn(false);
		Mockito.when(player.checkCream()).thenReturn(false);
		Mockito.when(player.checkSugar()).thenReturn(false);
		cmq.setPlayer(player);
		// TODO: 3. Create mock Rooms and assign to room1, room2, ..., room6.
		// Mimic the furnishings / adjectives / items of the rooms in the original Coffee Maker Quest.
		room1 = Mockito.mock(Room.class);
		Mockito.when(room1.getAdjective()).thenReturn("Small");
		Mockito.when(room1.getFurnishing()).thenReturn("Quaint sofa");
		Mockito.when(room1.getItem()).thenReturn(Item.CREAM);

		room2 = Mockito.mock(Room.class);
		Mockito.when(room2.getAdjective()).thenReturn("Funny");
		Mockito.when(room2.getFurnishing()).thenReturn("Sad record player");
		Mockito.when(room2.getItem()).thenReturn(Item.NONE);

		room3 = Mockito.mock(Room.class);
		Mockito.when(room3.getAdjective()).thenReturn("Refinanced");
		Mockito.when(room3.getFurnishing()).thenReturn("Tight pizza");
		Mockito.when(room3.getItem()).thenReturn(Item.COFFEE);

		room4 = Mockito.mock(Room.class);
		Mockito.when(room4.getAdjective()).thenReturn("Dumb");
		Mockito.when(room4.getFurnishing()).thenReturn("Flat energy drink");
		Mockito.when(room4.getItem()).thenReturn(Item.NONE);

		room5 = Mockito.mock(Room.class);
		Mockito.when(room5.getAdjective()).thenReturn("Bloodthirsty");
		Mockito.when(room5.getFurnishing()).thenReturn("Beautiful bag of money");
		Mockito.when(room5.getItem()).thenReturn(Item.NONE);

		room6 = Mockito.mock(Room.class);
		Mockito.when(room6.getAdjective()).thenReturn("Rough");
		Mockito.when(room6.getFurnishing()).thenReturn("Perfect air hockey table");
		Mockito.when(room6.getItem()).thenReturn(Item.SUGAR);
		// TODO: 4. Add the rooms created above to mimic the layout of the original Coffee Maker Quest.
		cmq.addFirstRoom(room1);
		cmq.addRoomAtNorth(room2, "Magenta", "Massive");
		cmq.addRoomAtNorth(room3, "Beige", "Smart");
		cmq.addRoomAtNorth(room4, "Dead", "Slim");
		cmq.addRoomAtNorth(room5, "Vivacious", "Sandy");
		cmq.addRoomAtNorth(room6, "Purple", "Minimalist");
		
	}

	@After
	public void tearDown() {
	}
	
	/**
	 * Test case for String getInstructionsString().
	 * Preconditions: None.
	 * Execution steps: Call cmq.getInstructionsString().
	 * Postconditions: Return value is " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 */
	@Test
	public void testGetInstructionsString() {
		String instructions = cmq.getInstructionsString();
		assertEquals(" INSTRUCTIONS (N,S,L,I,D,H) > ", instructions);
	}
	
	/**
	 * Test case for boolean addFirstRoom(Room room).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock room and assign to myRoom.
	 * Execution steps: Call cmq.addFirstRoom(myRoom).
	 * Postconditions: Return value is false.
	 */
	@Test
	public void testAddFirstRoom() {
		Room myRoom = Mockito.mock(Room.class);
		assertFalse(cmq.addFirstRoom(myRoom));
	}
	
	/**
	 * Test case for boolean addRoomAtNorth(Room room, String northDoor, String southDoor).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock "Fake" room with "Fake bed" furnishing with no item, and assign to myRoom.
	 * Execution steps: Call cmq.addRoomAtNorth(myRoom, "North", "South").
	 * Postconditions: Return value is true.
	 *                 room6.setNorthDoor("North") is called.
	 *                 myRoom.setSouthDoor("South") is called.
	 */
	@Test
	public void testAddRoomAtNorthUnique() {
		Room myRoom = Mockito.mock(Room.class);
		when(myRoom.getFurnishing()).thenReturn("Fake bed");
		when(myRoom.getAdjective()).thenReturn("Fake");

		assertTrue(cmq.addRoomAtNorth(myRoom, "North", "South"));
		Mockito.verify(room6, times(1)).setNorthDoor("North");
		Mockito.verify(myRoom, times(1)).setSouthDoor("South");
	}
	
	/**
	 * Test case for boolean addRoomAtNorth(Room room, String northDoor, String southDoor).
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                Create a mock "Fake" room with "Flat energy drink" furnishing with no item, and assign to myRoom.
	 * Execution steps: Call cmq.addRoomAtNorth(myRoom, "North", "South").
	 * Postconditions: Return value is false.
	 *                 room6.setNorthDoor("North") is not called.
	 *                 myRoom.setSouthDoor("South") is not called.
	 */
	@Test
	public void testAddRoomAtNorthDuplicate() {
		Room myRoom = Mockito.mock(Room.class);
		when(myRoom.getFurnishing()).thenReturn("Flat energy drink");
		when(myRoom.getAdjective()).thenReturn("Fake");
		assertFalse(cmq.addRoomAtNorth(myRoom, "North", "South"));
		Mockito.verify(room6, times(0)).setNorthDoor("North");
		Mockito.verify(myRoom, times(0)).setSouthDoor("South");
	}
	
	/**
	 * Test case for Room getCurrentRoom().
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(Room) has not yet been called.
	 * Execution steps: Call cmq.getCurrentRoom().
	 * Postconditions: Return value is null.
	 */
	@Test
	public void testGetCurrentRoom() {
		Room currentRoom = cmq.getCurrentRoom();
		assertNull(currentRoom);
	}
	
	/**
	 * Test case for void setCurrentRoom(Room room) and Room getCurrentRoom().
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(Room room) has not yet been called.
	 * Execution steps: Call cmq.setCurrentRoom(room3).
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.setCurrentRoom(room3) is true. 
	 *                 Return value of cmq.getCurrentRoom() is room3.
	 */
	@Test
	public void testSetCurrentRoom() {
		boolean setCurrentRoomBoolean = cmq.setCurrentRoom(room3);
		Room currentRoom = cmq.getCurrentRoom();
		assertTrue(setCurrentRoomBoolean);
		assertEquals(room3, currentRoom);
	}
	
	
	/**
	 * Test case for String processCommand("I").
	 * Preconditions: Player does not have any items.
	 * Execution steps: Call cmq.processCommand("I").
	 * Postconditions: Return value is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n".
	 */
	@Test
	public void testProcessCommandI() {
		cmq.setPlayer(player);
		Mockito.when(player.getInventoryString()).thenReturn("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n");
		String inventory = cmq.processCommand("I");
		assertEquals("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n", inventory);	
	}
	
	/**
	 * Test case for String processCommand("l").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "There might be something here...\nYou found some creamy cream!\n".
	 *                 player.addItem(Item.CREAM) is called.
	 */
	@Test
	public void testProcessCommandLCream() {
		cmq.setCurrentRoom(room1);
		String creamFoundMessage = cmq.processCommand("l");
		assertEquals("There might be something here...\nYou found some creamy cream!\n", creamFoundMessage);
		Mockito.verify(player, times(1)).addItem(Item.CREAM);	
	}
	
	/**
	 * Test case for String processCommand("l").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "There might be something here...\nYou found some caffeinated coffee!\n."
	 *                 player.addItem(Item.COFFEE) is called.
	 */
	@Test
	public void testProcessCommandLCoffee() {
		cmq.setCurrentRoom(room3);
		String coffeeFoundMessage = cmq.processCommand("l");
		assertEquals("There might be something here...\nYou found some caffeinated coffee!\n", coffeeFoundMessage);
		Mockito.verify(player, times(1)).addItem(Item.COFFEE);	
	}
	
	/**
	 * Test case for String processCommand("l").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "There might be something here...\nYou found some sweet sugar!\n"
	 *                 player.addItem(Item.SUGAR) is called.
	 */
	@Test
	public void testProcessCommandLSugar() {
		cmq.setCurrentRoom(room6);
		String sugarFoundMessage = cmq.processCommand("l");
		assertEquals("There might be something here...\nYou found some sweet sugar!\n", sugarFoundMessage);
		Mockito.verify(player, times(1)).addItem(Item.SUGAR);	
	}
	
	/**
	 * Test case for String processCommand("l").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("l").
	 * Postconditions: Return value is "You don't see anything out of the ordinary.\n"
	 *                 player.addItem(Item.NONE) is called.
	 */
	@Test
	public void testProcessCommandLNone() {
		cmq.setCurrentRoom(room2);
		String noneFoundMessage = cmq.processCommand("l");
		assertEquals("You don't see anything out of the ordinary.\n", noneFoundMessage);
		Mockito.verify(player, times(1)).addItem(Item.NONE);	
	}
	
	
	
	/**
	 * Test case for String processCommand("n").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room4) has been called.
	 * Execution steps: Call cmq.processCommand("n").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("n") is "".
	 *                 Return value of cmq.getCurrentRoom() is room5.
	 */
	@Test
	public void testProcessCommandN() {	
		cmq.setCurrentRoom(room4);
		String moveNorthMessage = cmq.processCommand("n");
		Room currentRoom = cmq.getCurrentRoom();
		assertEquals(moveNorthMessage, "");
		assertEquals(room5, currentRoom);
	}
	
	
	/**
	 * Test case for String processCommand("s").
	 * Preconditions: room1 ~ room6 have been added to cmq.
	 *                cmq.setCurrentRoom(room1) has been called.
	 * Execution steps: Call cmq.processCommand("s").
	 *                  Call cmq.getCurrentRoom().
	 * Postconditions: Return value of cmq.processCommand("s") is "A door in that direction does not exist.\n".
	 *                 Return value of cmq.getCurrentRoom() is room1.
	 */
	@Test
	public void testProcessCommandS() {
		cmq.setCurrentRoom(room1);
		String noDoorMessage = cmq.processCommand("s");
		Room currentRoom = cmq.getCurrentRoom();
		assertEquals("A door in that direction does not exist.\n", noDoorMessage);
		assertEquals(room1, currentRoom);
	}
	
	/**
	 * Test case for String processCommand("D").
	 * Preconditions: Player has no items.
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 */
	@Test
	public void testProcessCommandDLose() {
		Mockito.when(player.getInventoryString()).thenReturn("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n");
		String drinkMessage = cmq.processCommand("D");
		boolean isGameOver = cmq.isGameOver();
		assertEquals("YOU HAVE NO COFFEE!\nYOU HAVE NO CREAM!\nYOU HAVE NO SUGAR!\n\nYou drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n", drinkMessage);
		assertTrue(isGameOver);
	}
	
	/**
	 * Test case for String processCommand("D").
	 * Preconditions: Player has all 3 items (coffee, cream, sugar).
	 * Execution steps: Call cmq.processCommand("D").
	 *                  Call cmq.isGameOver().
	 * Postconditions: Return value of cmq.processCommand("D") is "You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n".
	 *                 Return value of cmq.isGameOver() is true.
	 */
	@Test
	public void testProcessCommandDWin() {
		Mockito.when(player.checkCoffee()).thenReturn(true);		
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);
		Mockito.when(player.getInventoryString()).thenReturn("You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n");
		String drinkMessage= cmq.processCommand("D");
		boolean isGameOver = cmq.isGameOver();
		assertEquals("You have a cup of delicious coffee.\nYou have some fresh cream.\nYou have some tasty sugar.\n\nYou drink the beverage and are ready to study!\nYou win!\n", drinkMessage);
		assertTrue(isGameOver);
	}
	
	// TODO: Put in more unit tests of your own making to improve coverage!
	/**
	 * Test case for String processCommand("H")
	 * Preconditions: None.
	 * Execution steps: Call cmq.processCommand("H").
	 * Postcondition: Return value of cmq.processCommand("H") is "N - Go north\nS - Go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n".
	 */
	@Test
	public void testProcessCommandH() {
		String helpMessage = cmq.processCommand("H");
		assertEquals("N - Go north\nS - Go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n", helpMessage);
	}

	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has Coffee and Cream
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "Without sugar, the coffee is too bitter. You cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessage() throws Exception{
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkCream()).thenReturn(true);

		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("Without sugar, the coffee is too bitter. You cannot study.\nYou lose!\n", loseMessage);
	}
	
	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has Coffee and Sugar
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "Without cream, you get an ulcer and cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessageCoSu() throws Exception{
		Mockito.when(player.checkCoffee()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);

		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("Without cream, you get an ulcer and cannot study.\nYou lose!\n", loseMessage);
	}
	
	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has Cream and Sugar
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "You drink the sweetened cream, but without caffeine you cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessageCrSu() throws Exception{
		Mockito.when(player.checkCream()).thenReturn(true);
		Mockito.when(player.checkSugar()).thenReturn(true);

		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("You drink the sweetened cream, but without caffeine you cannot study.\nYou lose!\n", loseMessage);
	}
	
	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has 
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "You drink the cream, but without caffeine, you cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessageCr() throws Exception{
		Mockito.when(player.checkCream()).thenReturn(true);


		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("You drink the cream, but without caffeine, you cannot study.\nYou lose!\n", loseMessage);
	}
	
	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has Sugar
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "You eat the sugar, but without caffeine, you cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessageSu() throws Exception{
		Mockito.when(player.checkSugar()).thenReturn(true);

		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("You eat the sugar, but without caffeine, you cannot study.\nYou lose!\n", loseMessage);
	}
	
	/**
	 * Test case for String getLoseMessage()
	 * Preconditions: Player has Coffee
	 * Execution steps: Call getLoseMessage() through reflection
	 * Postconditions: Return value of calling getLoseMessage() through reflection is "Without cream, you get an ulcer and cannot study.\nYou lose!\n".
	 */
	@Test
	public void testGetLoseMessageCo() throws Exception{
		Mockito.when(player.checkCoffee()).thenReturn(true);

		Method m = CoffeeMakerQuestImpl.class.getDeclaredMethod("getLoseMessage");
		m.setAccessible(true);
		Object loseMessage = m.invoke(cmq);

		assertEquals("Without cream, you get an ulcer and cannot study.\nYou lose!\n", loseMessage);
	}
	
}
