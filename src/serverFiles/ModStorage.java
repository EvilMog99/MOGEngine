package serverFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModStorage implements Serializable
{
	private boolean finishedLoadingMod;
	private String modRefName = "";
	private int maxNumberOfFiles = 0;
	private boolean beingAddedToGame = false;

	//mod uploading variables
	private List<SendObject> allModFiles_clientUpload;
	private List<SendObject> allModFiles_clientDownload;

	public ModStorage(String modRefName, int maxNumberOfFiles)
	{
		allModFiles_clientUpload = new ArrayList<SendObject>();
		allModFiles_clientDownload = new ArrayList<SendObject>();

		this.modRefName = modRefName;
		this.maxNumberOfFiles = maxNumberOfFiles;
		finishedLoadingMod = false;
	}
	
	public ModStorage(String modRefName, int maxNumberOfFiles, SendObject modFile_clientUpload) {
		super();
		this.allModFiles_clientUpload = new ArrayList<SendObject>();
		this.allModFiles_clientUpload.add(modFile_clientUpload);
		
		this.modRefName = modRefName;
		this.maxNumberOfFiles = maxNumberOfFiles;
		finishedLoadingMod = false;
	}


	//function to add, test and save objects
	public void clearFiles()
	{
		allModFiles_clientUpload.clear();
		allModFiles_clientDownload.clear();
		finishedLoadingMod = false;
	}
	
	public void addFile_clientUpload(SendObject fileObject)
	{
		if (!finishedLoadingMod)
		{
			allModFiles_clientUpload.add(fileObject);
		}
		
		if (allModFiles_clientUpload.size() == maxNumberOfFiles)
		{
			finishedLoadingMod = true;
			System.out.println("Finished loading mod: " + modRefName);
		}
		System.out.println("CUp Received " + allModFiles_clientUpload.size() + " / " + maxNumberOfFiles);
	}
	
	public void addFile_clientDownload(SendObject fileObject)
	{
		if (!finishedLoadingMod)
		{
			allModFiles_clientDownload.add(fileObject);
		}
		
		if (allModFiles_clientDownload.size() == maxNumberOfFiles)
		{
			finishedLoadingMod = true;
			System.out.println("Finished downloading mod: " + modRefName);
		}
		System.out.println("CDn Received " + allModFiles_clientDownload.size() + " / " + maxNumberOfFiles);
	}
	
	
	
	//all getters and setters
	public boolean isFinishedLoadingMod() {
		return finishedLoadingMod;
	}

	public void setFinishedLoadingMod(boolean finishedLoadingMod) {
		this.finishedLoadingMod = finishedLoadingMod;
	}
	
	public String getModRefName() {
		return modRefName;
	}
	
	public int getMaxNumberOfFiles() {
		return maxNumberOfFiles;
	}
	
	public List<SendObject> getAllModFiles_clientUpload() {
		return allModFiles_clientUpload;
	}
	
	public List<SendObject> getAllModFiles_clientDownload() {
		return allModFiles_clientDownload;
	}
	
	public boolean isBeingAddedToGame() {
		return beingAddedToGame;
	}

	public void setBeingAddedToGame(boolean beingAddedToGame) {
		this.beingAddedToGame = beingAddedToGame;
	}
}
