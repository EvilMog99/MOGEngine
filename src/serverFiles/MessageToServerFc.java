package serverFiles;

import java.io.Serializable;

public class MessageToServerFc implements Serializable 
{
	public String message = "";
	
	public boolean fileSet;
	public SendObject fileObject;
	
	public boolean requestingMod;
	public String requestedModName;
	
	public boolean adminCommand;
	//admin values
	public boolean getModData;
	public String modRefName_change;
	public int modsInViewIndex0;
	public int commandID;

	public MessageToServerFc(String message, boolean adminCommand, AdminServerCommandsFc adminServerCommandsFc) {
		super();
		this.message = message;
		
		fileSet = false;
		fileObject = null;
		
		this.adminCommand = adminCommand;
		if (adminCommand)
		{
			this.getModData = adminServerCommandsFc.isGetModData();
			this.modRefName_change = adminServerCommandsFc.modRefName_change;
			this.modsInViewIndex0 = adminServerCommandsFc.modsInViewIndex0;
			this.commandID = adminServerCommandsFc.getCommandID();
		}
		
		requestingMod = false;
	} 

}
