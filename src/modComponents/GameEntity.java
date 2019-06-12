package modComponents;

import java.io.Serializable;
import java.util.Random;

import gameRunner.Dimension;
import gameRunner.GameData;

public class GameEntity implements Serializable
{
	
	//for all entity types
	private int entitysModID;
	private int entityID_inMod;
	private int entityMatterValue = 0;
	private float fillValue = 0;//100%

	private int entity_textureNo;
	private int entity_variationNo;
	private int entity_animationNo;
	
	private String entityNickName;
	
	private int entitySpawnType;

	private float positionX;
	private float positionY;
	
	private float velocityX;
	private float velocityY;
	
	private boolean hasCollisions;
	private boolean isLiquid;

	private float entityCurrentHealth = 1f;
	private float entityMaxHealth = 1f;
	private Random rnd;
	
	//for creatures
	private int currentDimensionID;
	
	
	//for blocks
	
	
//	private int[] entityInventory_inputSlots;
//	private int[] entityInventory_outputSlots;
//	private int[] entityInventory_progressRightArrows;
//	private int[] entityInventory_energyBars;
//	private int[] entityInventory_dangerBars;
	
	
	public GameEntity(int entitysModID, int entityID_inMod, int entityMatterValue, int entity_textureNo,
			int entity_variationNo, int entity_animationNo, String entityNickName, float positionX, float positionY,
			float entityCurrentHealth, float entityMaxHealth, int currentDimensionID, Random rnd, boolean hasCollisions) {
		super();
		this.entitysModID = entitysModID;
		this.entityID_inMod = entityID_inMod;
		this.entityMatterValue = entityMatterValue;
		this.entity_textureNo = entity_textureNo;
		this.entity_variationNo = entity_variationNo;
		this.entity_animationNo = entity_animationNo;
		this.entityNickName = entityNickName;
		this.positionX = positionX;
		this.positionY = positionY;
		this.entityCurrentHealth = entityCurrentHealth;
		this.entityMaxHealth = entityMaxHealth;
		this.currentDimensionID = currentDimensionID;
		this.rnd = rnd;
		entitySpawnType = SpawnType.NoSpawn;
		this.fillValue = 0;
		this.hasCollisions = hasCollisions;
	}
	
	public void setEntityID(int modIndex, Mod mod, Dimension wld, GameEntityData ged, int spawnType)
	{
		setEntityID(modIndex, ged.getEntityIndex(), mod, wld, ged.getMatterValue()
				, ged.getEntityMaxHealth(), ged.getUploadedTextures().length, ged.getEntityPriority(), spawnType);
	}
	
	public void setEntityID(int modID, int EntityID, Mod mod, Dimension wld
			
			, int matterValue, int entityMaxHealth
			, int maxTexturesLength, int entityPriority
			, int entitySpawnType)
	{
		wld.allFastActiveBlocks.remove(this);
		wld.allMediumActiveBlocks.remove(this);
		wld.allSlowActiveBlocks.remove(this);
		
		this.entitysModID = modID;
		this.entityID_inMod = EntityID;
		
		this.entityMatterValue = matterValue;
		
		this.entityMaxHealth = entityMaxHealth;
		this.entityCurrentHealth = entityMaxHealth;
		
		this.entitySpawnType = entitySpawnType;
		
		entity_animationNo = 0;
		entity_variationNo = 0;
		entity_textureNo = 0;
		
		if (entityID_inMod > -1)
		{
			entity_textureNo = rnd.nextInt(maxTexturesLength);
		}
		else
		{
			entity_textureNo = 0;
		}
		
		if (mod != null)
		{
			this.fillValue = GameData.calculateFillValue(mod.getAllEntityData()[entityID_inMod].getSpawnTypeID());
			GameData.setEntityCollisions(this, mod.getAllEntityData()[entityID_inMod].getSpawnTypeID());//with natural mod based spawn type
		}
		else
		{
			this.fillValue = 0f;
			GameData.setEntityCollisions(this, entitySpawnType);//with its own set spawn type
		}
		
		if (entityID_inMod > -1 && entityPriority != 0)//if this block is important to process
		{
			switch (entityPriority)
			{
			case 3://fast
				wld.allFastActiveBlocks.add(this);
				break;
			
			case 2://medium
				wld.allMediumActiveBlocks.add(this);
				System.out.println("add medium");
				break;
				
			case 1://slow
				wld.allSlowActiveBlocks.add(this);
				break;
				
				default:
			}
		}
	}


	public void addToEntityCurrentHealth(float add) {
		
		if (entityCurrentHealth + add > entityMaxHealth)
		{
			entityCurrentHealth = entityMaxHealth;
		}
		else
		{
			entityCurrentHealth += add;
		}
	}
	
	public void removeFromEntityCurrentHealth(float minus) {
		
		if (entityCurrentHealth - minus < 0)
		{
			entityCurrentHealth = 0;
		}
		else
		{
			entityCurrentHealth -= minus;
		}
	}
	
	//all getters and setters
	
	public int getEntitysModID() {
		return entitysModID;
	}


	public void setEntitysModID(int entitysModID) {
		this.entitysModID = entitysModID;
	}


	public int getEntityID_inMod() {
		return entityID_inMod;
	}


	public void setEntityID_inMod(int entityID_inMod) {
		this.entityID_inMod = entityID_inMod;
	}


	public int getEntityMatterValue() {
		return entityMatterValue;
	}


	public void setEntityMatterValue(int entityMatterValue) {
		this.entityMatterValue = entityMatterValue;
	}


	public int getEntity_textureNo() {
		return entity_textureNo;
	}


	public void setEntity_textureNo(int entity_textureNo) {
		this.entity_textureNo = entity_textureNo;
	}


	public int getEntity_variationNo() {
		return entity_variationNo;
	}


	public void setEntity_variationNo(int entity_variationNo) {
		this.entity_variationNo = entity_variationNo;
	}


	public int getEntity_animationNo() {
		return entity_animationNo;
	}


	public void setEntity_animationNo(int entity_animationNo) {
		this.entity_animationNo = entity_animationNo;
	}


	public String getEntityNickName() {
		return entityNickName;
	}


	public void setEntityNickName(String entityNickName) {
		this.entityNickName = entityNickName;
	}


	public float getPositionX() {
		return positionX;
	}


	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}


	public float getPositionY() {
		return positionY;
	}


	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
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
	
	public void updateVelocityX(float force) {
		System.out.println("up0 X: " + this.velocityX + " force: " + force);
		this.velocityX += force;
		System.out.println("up1 X: " + this.velocityX);
		if (this.velocityX > GameData.playerMaxWalkSpeed_verticle)
		{
			this.velocityX = GameData.playerMaxWalkSpeed_verticle;
			System.out.println("up2 X: " + this.velocityX);
		}
		else if (this.velocityX < -GameData.playerMaxWalkSpeed_verticle)
		{
			this.velocityX = -GameData.playerMaxWalkSpeed_verticle;
			System.out.println("up3 X: " + this.velocityX);
		}
	}

	public int getCurrentDimensionID() {
		return currentDimensionID;
	}


	public void setCurrentDimensionID(int currentDimensionID) {
		this.currentDimensionID = currentDimensionID;
	}

	public float getEntityCurrentHealth() {
		return entityCurrentHealth;
	}

	public void setEntityCurrentHealth(float entityCurrentHealth) {
		this.entityCurrentHealth = entityCurrentHealth;
	}

	public float getEntityMaxHealth() {
		return entityMaxHealth;
	}

	public void setEntityMaxHealth(float entityMaxHealth) {
		this.entityMaxHealth = entityMaxHealth;
	}
	
	public int getEntitySpawnType() {
		return entitySpawnType;
	}
	
	public float getFillValue() {
		return fillValue;
	}

	public void setFillValue(float fillValue) {
		this.fillValue = fillValue;
	}
	
	public boolean isHasCollisions() {
		return hasCollisions;
	}

	public void setHasCollisions(boolean hasCollisions) {
		this.hasCollisions = hasCollisions;
	}
	
	public boolean isLiquid() {
		return isLiquid;
	}

	public void setLiquid(boolean isLiquid) {
		this.isLiquid = isLiquid;
	}
}
