package serverFiles;

import java.io.Serializable;

public class AdminServerCommandsFc implements Serializable
{
	private boolean getModData = false;
	public String modRefName_change;
	public int modsInViewIndex0;
	private int commandID = 0;
	
	public AdminServerCommandsFc() {
		super();
		this.modRefName_change = "";
		this.modsInViewIndex0 = 0;
		this.commandID = 0;
	}

	public static final int ViewModList		= -2;
	public static final int ViewModInDetail	= -1;
	public static final int NoCommand		= 0;
	public static final int MOD_Add 		= 1;
	public static final int MOD_Delete 		= 2;
	public static final int SaveWorld		= 3;
	
	//all getters and setters
	public int getCommandID() {
		return commandID;
	}
	public void setCommandID(int commandID) {
		if (this.commandID == NoCommand)
		{
			this.commandID = commandID;
		}
	}
	public void resetCommandIdInServer() {
		this.commandID = NoCommand;
	}
	
	public void tryToResetCommandID(int serverAcknowledgement) {
		if (this.commandID == serverAcknowledgement)
		{
			this.commandID = NoCommand;
		}
	}
	
	public boolean isGetModData() {
		return getModData;
	}
	public void setGetModData(boolean getModData) {
		this.getModData = getModData;
	}
}
