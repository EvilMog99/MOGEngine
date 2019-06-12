package modComponents;

import java.util.ArrayList;
import java.util.List;

import renderEngine.Loader;
import serverFiles.ModStorage;
import serverFiles.SendObject;
import serverFiles.ServerDataOnIntegratedMods;
import serverFiles.ServerFileInfo;
import toolbox.Saving;

public class IntegratedModData 
{
	private boolean loadedModFromFile = false;
	private boolean intergratedModInServer = false;
	
	private String modRefName = "";
	private int maxNumberOfFiles = 0;
	
	private Mod mod;//without images

	private List<SendObject> rawFilesForClients;

	//for newly added mods
	public IntegratedModData(String modRefName, Saving saver
			, List<SendObject> allModFiles_clientUpload, ServerFileInfo serverFileInfo
			, List<ServerDataOnIntegratedMods> allModsBeingIntegratedInGame
			, int startingWldX, int startingWldY) 
	{
		super();
		
		this.modRefName = modRefName;
		this.maxNumberOfFiles = allModFiles_clientUpload.size();
		this.rawFilesForClients = new ArrayList<SendObject>();
		
		boolean successfullySavedAllFiles = true;
		for (int i = 0; i < allModFiles_clientUpload.size(); i++)
		{
			if (!saver.save_mod_rawFileOnServer(allModFiles_clientUpload.get(i)))
			{
				System.out.println("Failed to save file: " + allModFiles_clientUpload.get(i).modFileName_ref);
				successfullySavedAllFiles = false;
				break;
			}
		}
		
		if (successfullySavedAllFiles)
		{
			//load mod
			this.mod = saver.open_modFile_automaticForServer(modRefName, this, this.rawFilesForClients);
			
			ServerDataOnIntegratedMods tempSDOIM = new ServerDataOnIntegratedMods(modRefName, serverFileInfo.getServerDataOnIntegratedModsLength(), startingWldX, startingWldY);
			serverFileInfo.addServerDataOnIntegratedMods(
					tempSDOIM
					);//add data to server
			allModsBeingIntegratedInGame.add(tempSDOIM);//add to list of mods being implemented into the server world
			intergratedModInServer = true;
			System.out.println("Successfully added: " + modRefName + " to server");
		}
		else
		{
			
			System.out.println("Failed to add: " + modRefName + " to server");
		}
	}
	
	//for loading saved & integrated mods
	public IntegratedModData(String modRefName, Saving saver) 
	{
		super();
		this.modRefName = modRefName;
		this.rawFilesForClients = new ArrayList<SendObject>();
		

		//load mod
		this.mod = saver.open_modFile_automaticForServer(modRefName, this, this.rawFilesForClients);
		maxNumberOfFiles = this.rawFilesForClients.size();
		intergratedModInServer = true;
		
		//System.out.println("Successfully loaded: " + modRefName + " to server");
	}
	

	//for blank mods
	public IntegratedModData() 
	{
		super();
	}
	
	
	//all getters and setters
	public String getModRefName() {
		return modRefName;
	}

	public void setModRefName(String modRefName) {
		this.modRefName = modRefName;
	}

	public int getMaxNumberOfFiles() {
		return maxNumberOfFiles;
	}

	public void setMaxNumberOfFiles(int maxNumberOfFiles) {
		this.maxNumberOfFiles = maxNumberOfFiles;
	}

	public void setLoadedModFromFile(boolean loadedModFromFile) {
		this.loadedModFromFile = loadedModFromFile;
	}
	
	public boolean isLoadedModFromFile() {
		return loadedModFromFile;
	}

	public boolean isIntergratedModInServer() {
		return intergratedModInServer;
	}
	
	public Mod getMod() {
		return mod;
	}
	
	public List<SendObject> getRawFilesForClients() {
		return rawFilesForClients;
	}
}
