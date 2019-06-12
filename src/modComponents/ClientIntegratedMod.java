package modComponents;

import renderEngine.Loader;
import toolbox.Saving;

public class ClientIntegratedMod 
{
	private boolean askServerForMod = false;
	private boolean loadedModFromFile = false;

	private String modRefName = "";
	private int maxNumberOfFiles = 0;
	
	private Mod mod;//without images

	public ClientIntegratedMod(String modRefName, Saving saver, Loader loader) {
		super();
		this.modRefName = modRefName;

		mod = saver.open_modFile_automaticForClient(this.modRefName, this, loader);
	}
	
	public void retryModFiles(Saving saver, Loader loader)
	{
		mod = saver.open_modFile_automaticForClient(this.modRefName, this, loader);
	}
	
	
	//all getters and setters
	public boolean isAskServerForMod() {
		return askServerForMod;
	}

	public void setAskServerForMod(boolean askServerForMod) {
		this.askServerForMod = askServerForMod;
	}

	public boolean isLoadedModFromFile() {
		return loadedModFromFile;
	}

	public void setLoadedModFromFile(boolean loadedModFromFile) {
		this.loadedModFromFile = loadedModFromFile;
	}

	public String getModRefName() {
		return modRefName;
	}

	public Mod getMod() {
		return mod;
	}
	
}
