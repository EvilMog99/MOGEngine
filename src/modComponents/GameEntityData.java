package modComponents;

import java.io.Serializable;

import org.newdawn.slick.opengl.Texture;

import toolbox.BasicFunctions;

public class GameEntityData implements Serializable
{
	private boolean allDataSet = false;
	
	private String entityName = "";
	private int entityIndex = -1;
	private boolean enabled = true;
	private int entityPriority = 0;
	private int entityMaxHealth = 1;
	private int matterValue = 0;
	
	private int spawnTypeID = 0;
	private int spawnSize = 0;
	private int spawnAbundance = 0;
	
	private UploadedTexture[][][][] uploadedTextures;
	
	
	private boolean entityHasGUI = false;
	private EntityGUIObject[] entityGUIObjects;
	
	
	public GameEntityData() {}
	public GameEntityData(String entityName, int entityIndex
			, int entityPriority, int entityMaxHealth, int matterValue
			, int spawnTypeID, int spawnSize, int spawnAbundance) 
	{ 
		this.entityName = entityName;
		this.entityIndex = entityIndex;
		this.uploadedTextures = new UploadedTexture[1][][][];
		this.uploadedTextures[0] = new UploadedTexture[1][][];
		this.uploadedTextures[0][0] = new UploadedTexture[1][];
		this.uploadedTextures[0][0][0] = new UploadedTexture[1];
		this.uploadedTextures[0][0][0][0] = new UploadedTexture();
		
		this.entityGUIObjects = new EntityGUIObject[0];
		
		this.entityPriority = entityPriority;
		this.entityMaxHealth = entityMaxHealth;
		this.matterValue = matterValue;
		
		this.spawnTypeID = spawnTypeID;
		this.spawnSize = spawnSize;
		this.spawnAbundance = spawnAbundance;
	}
	
	public GameEntityData(String entityName, int entityIndex, int[] utLoc, UploadedTexture uploadedTexture
			, boolean entityHasGUI, EntityGUIObject[] entityGUIObjects, int entityPriority, int entityMaxHealth, int matterValue
			, int spawnTypeID, int spawnSize, int spawnAbundance) 
	{ 
		this.entityName = entityName;
		this.entityIndex = entityIndex;
		this.uploadedTextures = new UploadedTexture[1][][][];
		this.uploadedTextures[0] = new UploadedTexture[1][][];
		this.uploadedTextures[0][0] = new UploadedTexture[1][];
		this.uploadedTextures[0][0][0] = new UploadedTexture[1];
		this.uploadedTextures[0][0][0][0] = uploadedTexture;
		
		this.entityHasGUI = entityHasGUI;
		this.entityGUIObjects = entityGUIObjects;
		
		this.entityPriority = entityPriority;
		this.entityMaxHealth = entityMaxHealth;
		this.matterValue = matterValue;
		
		this.spawnTypeID = spawnTypeID;
		this.spawnSize = spawnSize;
		this.spawnAbundance = spawnAbundance;
		//BasicFunctions.addToFrameArray(uploadedTexture, this.uploadedTextures, utLoc[0], utLoc[1], utLoc[2]);
	}
	
	//All getters and setters
	
	public int getMatterValue() {
		return matterValue;
	}
	public void setMatterValue(int matterValue) {
		this.matterValue = matterValue;
	}
	
	public boolean isAllDataSet() {
		return allDataSet;
	}

	public void setAllDataSet(boolean allDataSet) {
		this.allDataSet = allDataSet;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public int getEntityIndex() {
		return entityIndex;
	}
	public void setEntityIndex(int entityIndex) {
		this.entityIndex = entityIndex;
	}
	
	public UploadedTexture[][][][] getUploadedTextures() {
		return uploadedTextures;
	}
	public UploadedTexture getUploadedTexture(int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		return uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo];
	}
	public int getUploadedTexture_id(int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		return uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo].getTextureID();
	}
	public Texture getUploadedTexture_texture(int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		return uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo].getTextureData();
	}
	
	public void setUploadedTextures(UploadedTexture[][][][] uploadedTextures) {
		this.uploadedTextures = uploadedTextures;
	}
	public void setUploadedTexture(UploadedTexture uploadedTexture, int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		this.uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo] = uploadedTexture;
	}
	public void setUploadedTexture_id(int id, int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		this.uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo].setTextureID(id);
	}
	public void setUploadedTexture_data(Texture texture, int TextureNo, int VarietyNo, int AnimationNo, int FrameNo) {
		this.uploadedTextures[TextureNo][VarietyNo][AnimationNo][FrameNo].setTextureData(texture);
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
	public void setEntityHasGUI(boolean entityHasGUI) {
		this.entityHasGUI = entityHasGUI;
	}
	public EntityGUIObject[] getEntityGUIObjects() {
		return entityGUIObjects;
	}
	public void setEntityGUIObjects(EntityGUIObject[] entityGUIObjects) {
		this.entityGUIObjects = entityGUIObjects;
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
