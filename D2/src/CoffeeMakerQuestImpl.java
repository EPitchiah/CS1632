import java.util.*;

public class CoffeeMakerQuestImpl implements CoffeeMakerQuest {

	// TODO: Add more member variables and methods as needed.
	List<Room> roomsList;
	List<String> adjectives; //to store all the adjectives of the rooms in the game
	List<String> furnishings; //to store all the furnishings in the rooms of the game
	Room currentRoom; //represents the current room the player is in
	Player player; //represents the player who is currently playing the game
	Room northernMostRoom; 
	Room firstRoom;
	boolean isGameOver; //true if the player has all the ingredients and plays the drink command

	CoffeeMakerQuestImpl() {
		super();
		roomsList = new ArrayList<>();
		adjectives = new ArrayList<>();
		furnishings = new ArrayList<>();
		isGameOver = false;
	}

	/**
	 * Whether the game is over. The game ends when the player drinks the coffee.
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Set the player to p.
	 * 
	 * @param p the player
	 */
	public void setPlayer(Player p) {
		player = p;
	}
	
	/**
	 * Add the first room in the game. If room is null or if this not the first room
	 * (there are pre-exiting rooms), the room is not added and false is returned.
	 * 
	 * @param room the room to add
	 * @return true if successful, false otherwise
	 */
	public boolean addFirstRoom(Room room) {
		if(room == null || !roomsList.isEmpty()){
			return false;
		}
		//at this point we can add the new room 
		northernMostRoom = room;
		roomsList.add(room);
		adjectives.add(room.getAdjective());
		furnishings.add(room.getDescription());
		firstRoom = room;
		return true;
	}

	/**
	 * Attach room to the northern-most room. If either room, northDoor, or
	 * southDoor are null, the room is not added. If there are no pre-exiting rooms,
	 * the room is not added. If room is not a unique room (a pre-exiting room has
	 * the same adjective or furnishing), the room is not added. If all these tests
	 * pass, the room is added. Also, the north door of the northern-most room is
	 * labeled northDoor and the south door of the added room is labeled southDoor.
	 * Of course, the north door of the new room is still null because there is
	 * no room to the north of the new room.
	 * 
	 * @param room      the room to add
	 * @param northDoor string to label the north door of the current northern-most room
	 * @param southDoor string to label the south door of the newly added room
	 * @return true if successful, false otherwise
	 */
	public boolean addRoomAtNorth(Room room, String northDoor, String southDoor) {
		if(room == null || northDoor == null || southDoor == null || roomsList.isEmpty()){
			return false;
		}
		if(adjectives.contains(room.getAdjective()) || furnishings.contains(room.getFurnishing())){
			// cannot add a room with a duplicate adjective and a furnishing so returning false
			return false;
		}

		//at this point we can add the new room
		roomsList.add(room);
		adjectives.add(room.getAdjective());
		furnishings.add(room.getFurnishing());

		northernMostRoom.setNorthDoor(northDoor);
		room.setSouthDoor(southDoor);
		northernMostRoom = room;
		return true;
	}

	/**
	 * Returns the room the player is currently in. If location of player has not
	 * yet been initialized with setCurrentRoom, returns null.
	 * 
	 * @return room player is in, or null if not yet initialized
	 */ 
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	/**
	 * Set the current location of the player. If room does not exist in the game,
	 * then the location of the player does not change and false is returned.
	 * 
	 * @param room the room to set as the player location
	 * @return true if successful, false otherwise
	 */
	public boolean setCurrentRoom(Room room) {
		//double checking that the room is a valid room in the game
		if(!roomsList.contains(room)){
			return false;
		}
		currentRoom = room;
		return true;
	}
	
	/**
	 * Get the instructions string command prompt. It returns the following prompt:
	 * " INSTRUCTIONS (N,S,L,I,D,H) > ".
	 * 
	 * @return comamnd prompt string
	 */
	public String getInstructionsString() {
		return " INSTRUCTIONS (N,S,L,I,D,H) > ";
	}
	
	/**
	 * Processes the user command given in String cmd and returns the response
	 * string. For the list of commands, please see the Coffee Maker Quest
	 * requirements documentation (note that commands can be both upper-case and
	 * lower-case). For the response strings, observe the response strings printed
	 * by coffeemaker.jar. The "N" and "S" commands potentially change the location
	 * of the player. The "L" command potentially adds an item to the player
	 * inventory. The "D" command drinks the coffee and ends the game. Make
     * sure you use Player.getInventoryString() whenever you need to display
     * the inventory.
	 * 
	 * @param cmd the user command
	 * @return response string for the command
	 */
	public String processCommand(String cmd) {
		// TODO
		if(cmd.equalsIgnoreCase("I")) { // inventory command
			return player.getInventoryString();
		}
		else if(cmd.equalsIgnoreCase("L")){ //look command
			Item item = currentRoom.getItem(); //getting the item that is in the current room
			String foundString = "";
			switch(item) { //checking what item (if any) was found
				case COFFEE:
					foundString = "There might be something here...\nYou found some caffeinated coffee!\n";
					break;
				case CREAM:
					foundString = "There might be something here...\nYou found some creamy cream!\n";
					break;
				case SUGAR:
					foundString = "There might be something here...\nYou found some sweet sugar!\n";
					break;
				case NONE:
					foundString = "You don't see anything out of the ordinary.\n";
					break;
				}
			player.addItem(item); //adding the item to the player's inventory
			return foundString;
		}
		else if(cmd.equalsIgnoreCase("D")){//drink command
			String result = "";
			if(player.checkCoffee() && player.checkCream() && player.checkSugar()){
				//the player wins the game if they have all the ingredients
				result = player.getInventoryString();
				result += "\nYou drink the beverage and are ready to study!\nYou win!\n";
			}
			else{
				//they lose the game if they do not have all the ingredients
				result = player.getInventoryString() + "\n";
				result += getLoseMessage();
			}
			isGameOver = true;
			return result;
		}
		else if(cmd.equalsIgnoreCase("N")){ //command to move north in the game
			//double checking that there is a room to the north of the current room
			if(currentRoom != northernMostRoom){
				currentRoom = roomsList.get(roomsList.indexOf(currentRoom) + 1);
				return "";
			}
			else{
				return "A door in that direction does not exist.\n";
			}
		}
		else if(cmd.equalsIgnoreCase("S")){
			//checking that there is a room to the south of the current room
			if(currentRoom != firstRoom){
				currentRoom = roomsList.get(roomsList.indexOf(currentRoom) - 1);
				return "";
			}
			else{
				return "A door in that direction does not exist.\n";
			}
		}	
		else if(cmd.equalsIgnoreCase("H")){//help command
			return "N - Go north\nS - Go south\nL - Look and collect any items in the room\nI - Show inventory of items collected\nD - Drink coffee made from items in inventory\n";
		}
		return "";
	}

	//private method that determines the message that displays to tell the user they have lost the game
	//the message is based on what ingredients the user has and does not have
	private String getLoseMessage(){
		String loseMessage = "";
		if(player.checkCoffee() && player.checkCream() && !player.checkSugar()){
			loseMessage = "Without sugar, the coffee is too bitter. You cannot study.\nYou lose!\n";	
		}
		else if(player.checkCoffee() && !player.checkCream() && player.checkSugar()){
			loseMessage = "Without cream, you get an ulcer and cannot study.\nYou lose!\n";
		}
		else if(!player.checkCoffee() && player.checkCream() && player.checkSugar()){
			loseMessage = "You drink the sweetened cream, but without caffeine you cannot study.\nYou lose!\n";
		}
		else if(!player.checkCoffee() && !player.checkCream() && player.checkSugar()){
			loseMessage = "You eat the sugar, but without caffeine, you cannot study.\nYou lose!\n";
		}
		else if(player.checkCoffee() && !player.checkCream() && !player.checkSugar()){
			loseMessage = "Without cream, you get an ulcer and cannot study.\nYou lose!\n";
		}
		else if(!player.checkCoffee() && player.checkCream() && !player.checkSugar()){
			loseMessage = "You drink the cream, but without caffeine, you cannot study.\nYou lose!\n";
		}
		else if(!player.checkCoffee() && !player.checkCream() && !player.checkSugar()){
			loseMessage = "You drink the air, as you have no coffee, sugar, or cream.\nThe air is invigorating, but not invigorating enough. You cannot study.\nYou lose!\n";
		}
		return loseMessage;
	}
	
}