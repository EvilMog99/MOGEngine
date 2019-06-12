package serverFiles;

import java.io.Serializable;

public class MessageToClientFs implements Serializable 
{
	public String message = "";
	
	public boolean fileSet;
	public boolean modFileForClient;
	public SendObject fileObject;
	
	public int confirmCommand;//confirm that the command ID has been processed
	
	public boolean isAdmin;
	
	
	//admin data
	public String[] bufferedModList = new String[8];
	public String[] bufferedModData;

	public MessageToClientFs(String message, boolean isAdmin, AdminClientData adminClientData, int confirmCommand) {
		super();
		this.message = message;
		
		fileSet = false;
		fileObject = null;
		
		modFileForClient = false;
		
		this.confirmCommand = confirmCommand;
		
		this.isAdmin = isAdmin;
		if (isAdmin)
		{
			for (int i = 0; i < bufferedModList.length; i++)
			{
				bufferedModList[i] = adminClientData.viewingModsBasicData[i];
			}
			
			bufferedModData = new String[adminClientData.selectedModData.length];
			for (int i = 0; i < bufferedModData.length; i++)
			{
				bufferedModData[i] = adminClientData.selectedModData[i];
			}
		}
//		this.adminClientData = adminClientData;
	} 

}
