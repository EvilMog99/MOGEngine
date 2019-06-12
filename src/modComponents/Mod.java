package modComponents;

public class Mod
{
	//private GameEntity[] allEntityStructures;//for creating objects
	private GameEntityData[] allEntityData;//for holding data about each type of object

	private String modName;
	private String publisherUserName;
	private long serverID;
	private String[] allCreators;
	private String[] allGameEntityFileNames;
	
	public Mod(String modName, String publisherUserName, String[] allCreators, String[] allGameEntityFileNames)
	{
		//this.allEntityStructures = new GameEntity[0];
		this.allEntityData = new GameEntityData[0];
		this.modName = modName;
		this.publisherUserName = publisherUserName;
		//this.serverID = serverID;
		this.allCreators = allCreators;
		this.allGameEntityFileNames = allGameEntityFileNames;
	}

	public Mod(GameEntityData[] allEntityData, String modName,
			String publisherUserName, long serverID, String[] allCreators, String[] allGameEntityFileNames) {
		super();
		//this.allEntityStructures = allEntityStructures;
		this.allEntityData = allEntityData;
		this.modName = modName;
		this.publisherUserName = publisherUserName;
		this.serverID = serverID;
		this.allCreators = allCreators;
		this.allGameEntityFileNames = allGameEntityFileNames;
	}

	//all getters and setters
	
//	public GameEntity getEntityStructure(int index) {
//		return allEntityStructures[index];
//	}
//
//	public void setEntityStructure(GameEntity entityStructure, int index) {
//		this.allEntityStructures[index] = entityStructure;
//	}
	
	public String[] getAllGameEntityFileNames() {
		return allGameEntityFileNames;
	}

	public void setAllGameEntityFileNames(String[] allGameEntityFileNames) {
		this.allGameEntityFileNames = allGameEntityFileNames;
	}
	
	public String[] getGameEntityFileNames() {
		return allGameEntityFileNames;
	}
	
	public String getGameEntityFileName(int index) {
		return allGameEntityFileNames[index];
	}

	public void setGameEntityFileName(String str, int index) {
		this.allGameEntityFileNames[index] = str;
	}

	public void setAllEntityData(GameEntityData[] allEntityData) {
		this.allEntityData = allEntityData;
	}
	
	public GameEntityData getEntityData(int index) {
		return allEntityData[index];
	}
	
	public GameEntityData[] getAllEntityData() {
		return allEntityData;
	}

	public void setEntityData(GameEntityData entityData, int index) {
		this.allEntityData[index] = entityData;
	}

	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getPublisherUserName() {
		return publisherUserName;
	}

	public void setPublisherUserName(String publisherUserName) {
		this.publisherUserName = publisherUserName;
	}

	public long getServerID() {
		return serverID;
	}

//	public void setServerID(long serverID) {
//		this.serverID = serverID;
//	}

	public String[] getAllCreators() {
		return allCreators;
	}

	public void setAllCreators(String[] allCreators) {
		this.allCreators = allCreators;
	}
	
}
