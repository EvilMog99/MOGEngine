package modComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameEntityDataFile  implements Serializable
{
	private boolean allDataSet = false;
	
	private String entityName = "";
	private int entityIndex = -1;
	private boolean enabled;
	
	private int entityPriority = 0;
	private int entityMaxHealth = 1;
	private int matterValue = 0;
	
	private int spawnTypeID = 0;
	private int spawnSize = 0;
	private int spawnAbundance = 0;

	private boolean entityHasGUI = false;
	private EntityGUIObject[] entityGUIObjects;

	//private GameEntity gameEntity;
	private List<List<List<List<String>>>> allImageNames = new ArrayList<List<List<List<String>>>>();

	
	public GameEntityDataFile(boolean allDataSet, boolean enabled, String entityName, int entityIndex, 
			List<List<List<List<String>>>> allImageNames
			, boolean entityHasGUI, EntityGUIObject[] entityGUIObjects
			, int entityPriority, int entityMaxHealth, int matterValue
			, int spawnTypeID, int spawnSize, int spawnAbundance) {
		super();
		this.allDataSet = allDataSet;
		this.setEnabled(enabled);
		this.entityName = entityName;
		this.entityIndex = entityIndex;
		//this.gameEntity = gameEntity;
		this.allImageNames = allImageNames;
		
		this.entityHasGUI = entityHasGUI;
		this.entityGUIObjects = entityGUIObjects;
		this.entityPriority = entityPriority;
		this.entityMaxHealth = entityMaxHealth;
		this.matterValue = matterValue;
		
		this.spawnTypeID = spawnTypeID;
		this.spawnSize = spawnSize;
		this.spawnAbundance = spawnAbundance;
	}
	
	
	//getters and setters

	public int getMatterValue() {
		return matterValue;
	}

	public void setMatterValue(int matterValue) {
		this.matterValue = matterValue;
	}
	
	public boolean isAllDataSet() {
		return allDataSet;
	}

	public String getEntityName() {
		return entityName;
	}

	public int getEntityIndex() {
		return entityIndex;
	}

//	public GameEntity getGameEntity() {
//		return gameEntity;
//	}

	public List<List<List<List<String>>>> getAllImageNames() {
		return allImageNames;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEntityHasGUI() {
		return entityHasGUI;
	}

	public EntityGUIObject[] getEntityGUIObjects() {
		return entityGUIObjects;
	}
	
	public int getEntityPriority() {
		return entityPriority;
	}


	public void setEntityPriority(int entityPriority) {
		this.entityPriority = entityPriority;
	}
	public int getEntityMaxHealth() {
		return entityMaxHealth;
	}


	public void setEntityMaxHealth(int entityMaxHealth) {
		this.entityMaxHealth = entityMaxHealth;
	}
	
	public int getSpawnTypeID() {
		return spawnTypeID;
	}


	public void setSpawnTypeID(int spawnTypeID) {
		this.spawnTypeID = spawnTypeID;
	}


	public int getSpawnSize() {
		return spawnSize;
	}


	public void setSpawnSize(int spawnSize) {
		this.spawnSize = spawnSize;
	}


	public int getSpawnAbundance() {
		return spawnAbundance;
	}


	public void setSpawnAbundance(int spawnAbundance) {
		this.spawnAbundance = spawnAbundance;
	}
}
