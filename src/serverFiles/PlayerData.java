package serverFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gameRunner.GameData;


public class PlayerData implements Serializable
{
	public int playerServerID = -1;

	public int currentDimensionID = 0;
	
	//current position
	public float positionX = 0;//grid . position in block
	public float positionY = 0;
	public float velocityX = 0;
	public float velocityY = 0;
	public float previousFinalVelocityY = 0;
	private boolean onGround = false;
	public int mouseX = 0;//mouse grid . position in block
	public int mouseY = 0;
	//grid target - where this player is/moving to
	public int targetX = 0;
	public int targetY = 0;
	
	//visuals
	public int creatureID = 0;//human player
	public int TextureNo = 0;
	public int AnimationNo = 0;
	public int FrameNo = 0;
	
	//playing data
	private float health;
	private float health_Max;

//	private int[][] inventoryForShow;//lists IDs to reference [mod (-1=not mod) , blocks]
//	private final int INV_LENGHT = 20;

	private List<List<Integer>> inventory;
	
	private int invView_modId = -1;
	private int invView_entityScroll = -1;
	
	private int heldItem_modIndex = -1;
	private int heldItem_entityIndex = -1;
	
	private int gameMode = 0;//0 = normal, 1 = create

	private boolean isAdmin;
	
	//unique data
	private String username = "";
	private int[] playerRndIDs;
	
	private boolean sendWorldData = false;
	
	
	public PlayerData(int[] pastedPlayerRndIDs, String username, int startX, int startY, int numberOfPlayerAccounts)
	{
		playerRndIDs = new int[pastedPlayerRndIDs.length];
		
		for (int i = 0; i < playerRndIDs.length; i++) {
			this.playerRndIDs[i] = pastedPlayerRndIDs[i];
		}
		
		this.username = username;
		
		this.positionX = startX;
		this.positionY = startY;
		
		this.playerServerID = numberOfPlayerAccounts;
		
//		inventoryForShow = new int[INV_LENGHT][];
//		for (int i = 0; i < INV_LENGHT; i++)
//		{
//			inventoryForShow[i] = new int[] { -1, 0 };
//		}
		
		health = 20f;
		health_Max = 20f;
		
		isAdmin = false;
		
		inventory = new ArrayList<List<Integer>>();
	}
	
	
	
	
	//all getters and setters
	
	public int getPlayerServerID() {
		return playerServerID;
	}

	public String getUsername() {
		return username;
	}

	public int[] getPlayerRndIDs() {
		return playerRndIDs;
	}
	
//	public int getInventoryItem_Count(int index) 
//	{
//		if (index > 0 && index < inventory.length)
//		{
//			return inventory[index];
//		}
//		else
//		{
//			return -1;
//		}
//	}
//	
//	public boolean addInventoryItem(int modID, int index, int numberOfItem) 
//	{
//		if (index > 0 && index < inventory.length)
//		{
//			inventory[index] += numberOfItem;
//			
//			if (inventory[index][] > 100000)
//			{
//				inventory[index] = 100000;
//			}
//			
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	public boolean removeInventoryItem(int index, int numberOfItem) 
//	{
//		if (index > 0 && index < inventory.length)
//		{
//			if (inventory[index] - numberOfItem > -1)
//			{
//				inventory[index] -= numberOfItem;
//				return true;
//			}
//			else
//			{
//				return false;
//			}
//		}
//		else
//		{
//			return false;
//		}
//	}
	
//	public int getInventoryForShow_block(int slotIndex) 
//	{
//		return inventoryForShow[slotIndex][1];
//	}
//	public int getInventoryForShow_mod(int modIndex) 
//	{
//		return inventoryForShow[modIndex][0];
//	}
//	public void setInventoryForShow(int slotIndex, int modID, int blockID) 
//	{
//		this.inventoryForShow[slotIndex][0] = modID;
//		this.inventoryForShow[slotIndex][1] = blockID;
//	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getHealth_Max() {
		return health_Max;
	}

	public void setHealth_Max(float health_Max) {
		this.health_Max = health_Max;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public boolean isSendWorldData() {
		return sendWorldData;
	}

	public void setSendWorldData(boolean sendWorldData) {
		this.sendWorldData = sendWorldData;
	}
	
	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
	public void updateVelocityY(float gravity) {
		this.velocityY += gravity;
		if (this.velocityY < GameData.playerMaxFallSpeed_horizontal)
		{
			this.velocityY = GameData.playerMaxFallSpeed_horizontal;
		}
		//minusing minus values
		else if (this.velocityY > -GameData.playerMaxFallSpeed_horizontal)
		{
			this.velocityY = -GameData.playerMaxFallSpeed_horizontal;
		}
	}
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}
	
	public void updateVelocityX(float force) {
		//System.out.println("up0 X: " + this.velocityX + " force: " + force);
		this.velocityX += force;
		if (this.velocityX > GameData.playerMaxWalkSpeed_verticle)
		{
			this.velocityX = GameData.playerMaxWalkSpeed_verticle;
		}
		else if (this.velocityX < -GameData.playerMaxWalkSpeed_verticle)
		{
			this.velocityX = -GameData.playerMaxWalkSpeed_verticle;
		}
	}
	
	public int getGameMode() {
		return gameMode;
	}
	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}
	
	public int getHeldItem_modIndex() {
		return heldItem_modIndex;
	}
	public void setHeldItem_modIndex(int heldItem_modIndex) {
		this.heldItem_modIndex = heldItem_modIndex;
	}

	public int getHeldItem_entityIndex() {
		return heldItem_entityIndex;
	}
	public void setHeldItem_entityIndex(int heldItem_entityIndex) {
		this.heldItem_entityIndex = heldItem_entityIndex;
	}
	
	public int getInvView_modId() {
		return invView_modId;
	}
	public void setInvView_modId(int invView_modId) {
		this.invView_modId = invView_modId;
	}

	public int getInvView_entityScroll() {
		return invView_entityScroll;
	}
	public void setInvView_entityScroll(int invView_entityScroll) {
		this.invView_entityScroll = invView_entityScroll;
	}
	
	public int getInventoryItem(int modIndex, int entityIndex) 
	{	
		if (modIndex > -1 && modIndex < inventory.size())
		{
			if (entityIndex > -1 && entityIndex < inventory.get(modIndex).size())
			{
				return inventory.get(modIndex).get(entityIndex);
			}
		}
		return -1;
	}
	public void setInventoryItem(int inventoryItemAmount, int modIndex, int entityIndex) 
	{	
		if (modIndex > -1 && modIndex < inventory.size())
		{
			if (entityIndex > -1 && entityIndex < inventory.get(modIndex).size())
			{
				this.inventory.get(modIndex).set(entityIndex, inventoryItemAmount);
			}
		}
	}
	
	public void incrementInventory(int addVal, int modIndex, int entityIndex) 
	{		
		if (modIndex > -1 && modIndex < inventory.size())
		{
			if (entityIndex > -1 && entityIndex < inventory.get(modIndex).size())
			{
				this.inventory.get(modIndex).set(entityIndex, this.inventory.get(modIndex).get(entityIndex) + addVal);
			}
		}
	}
	
	public void decrementInventory(int subVal, int modIndex, int entityIndex) 
	{		
		if (modIndex > -1 && modIndex < inventory.size())
		{
			if (entityIndex > -1 && entityIndex < inventory.get(modIndex).size())
			{
				this.inventory.get(modIndex).set(entityIndex, this.inventory.get(modIndex).get(entityIndex) - subVal);
			}
		}
	}
	
	public void addNewInventoryList(List<Integer> addList) 
	{	
		this.inventory.add(addList);
	}
}
