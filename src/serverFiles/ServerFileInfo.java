package serverFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//stores general data about game 
public class ServerFileInfo implements Serializable
{
	public int numberOfUniverses = 0;
	
	public int lastFastBlockProcessed = 0;
	public int lastMediumBlockProcessed = 0;
	public int lastSlowBlockProcessed = 0;
	
	public float lastFastBlockProcessed_incVal = 0;
	public float lastMediumBlockProcessed_incVal = 0;
	public float lastSlowBlockProcessed_incVal = 0;
	
	public boolean lastFastBlockProcessed_newVal = false;
	public boolean lastMediumBlockProcessed_newVal = false;
	public boolean lastSlowBlockProcessed_newVal = false;
	
	public int serverState = -1;
	
	private List<ServerDataOnIntegratedMods> allServerDataOnIntegratedMods;
	

	public ServerFileInfo() {
		super();
		// TODO Auto-generated constructor stub
		allServerDataOnIntegratedMods = new ArrayList<ServerDataOnIntegratedMods>();
	}
	
	public ServerDataOnIntegratedMods getServerDataOnIntegratedMod(int index) {
		if (index > -1 && index < allServerDataOnIntegratedMods.size())
		{
			return allServerDataOnIntegratedMods.get(index);
		}
		return null;
	}
	
	public int getServerDataOnIntegratedModsLength() {
		return allServerDataOnIntegratedMods.size();
	}

	public void addServerDataOnIntegratedMods(ServerDataOnIntegratedMods sdoim) {
		this.allServerDataOnIntegratedMods.add(sdoim);
	}
	public void deleteServerDataOnIntegratedMods(int index) {
		if (index > -1 && index < allServerDataOnIntegratedMods.size())
		{
			allServerDataOnIntegratedMods.remove(index);
		}
	}
	public void clearServerDataOnIntegratedMods() {
		this.allServerDataOnIntegratedMods.clear();
	}
}
