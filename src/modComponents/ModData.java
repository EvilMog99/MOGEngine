package modComponents;

import java.io.Serializable;

public class ModData implements Serializable
{
	private String modName;
	private String modDevDate = "";
	private String publisherUserName;
	private String[] allCreators;
	
	private String[] allGameEntityFileNames;

	public ModData(String[] allGameEntityFileNames) 
	{
		super();
		this.allGameEntityFileNames = allGameEntityFileNames;
	}
	
	public ModData(Mod mod) 
	{
		super();
		this.allGameEntityFileNames = new String[0];
		
		this.modName = mod.getModName();
		this.publisherUserName = mod.getPublisherUserName();
		this.allCreators = mod.getAllCreators();
	}
	
//	public void setupMod(Mod mod)
//	{
//		mod.setModName(this.modName);
//		mod.setPublisherUserName(this.publisherUserName);
//		mod.setAllCreators(this.allCreators);
//	}

	
	//all getters and setters
	
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

	public String[] getAllCreators() {
		return allCreators;
	}

	public void setAllCreators(String[] allCreators) {
		this.allCreators = allCreators;
	}
	
	public String getModDevDate() {
		return modDevDate;
	}

	public void setModDevDate(String modDevDate) {
		this.modDevDate = modDevDate;
	}
}
