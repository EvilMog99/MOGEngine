package serverFiles;

import java.io.Serializable;

public class AdminClientData implements Serializable
{
	public String[] viewingModsBasicData;//[index][which data 0=mod ref name, 1=]
	public String[] selectedModData;
	
	public AdminClientData() {
		super();
		this.viewingModsBasicData = new String[8];
		
		for (int i = 0; i < viewingModsBasicData.length; i++)
		{
			viewingModsBasicData[i] = "";
		}
		
		this.selectedModData = new String[4];
		for (int i = 0; i < selectedModData.length; i++)
		{
			selectedModData[i] = "";
		}
	}
	
	
	
}
