package serverFiles;

import java.io.Serializable;

public class ServerDataOnIntegratedMods implements Serializable
{
	private String integratedModName;
	
	private int integratedModIndex;
	
	//when integrating run through all worlds once to implement mod
	private boolean beingIntegrated = false;
	private int startingWldX, startingWldY;
	private int currentWldX, currentWldY;
	
	
	public ServerDataOnIntegratedMods(String integratedModName, int integratedModIndex, int startingWldX,
			int startingWldY) {
		super();
		this.integratedModIndex = integratedModIndex;
		this.integratedModName = integratedModName;
		this.beingIntegrated = true;
		this.startingWldX = startingWldX;
		this.startingWldY = startingWldY;
		this.currentWldX = startingWldX;
		this.currentWldY = startingWldY;
	}
	
	public int getIntegratedModIndex() {
		return integratedModIndex;
	}
	
	public boolean isBeingIntegrated() {
		return beingIntegrated;
	}


	public void setBeingIntegrated(boolean beingIntegrated) {
		this.beingIntegrated = beingIntegrated;
	}


	public int getStartingWldX() {
		return startingWldX;
	}


	public void setStartingWldX(int startingWldX) {
		this.startingWldX = startingWldX;
	}


	public int getStartingWldY() {
		return startingWldY;
	}


	public void setStartingWldY(int startingWldY) {
		this.startingWldY = startingWldY;
	}


	public int getCurrentWldX() {
		return currentWldX;
	}

	public int getCurrentWldY() {
		return currentWldY;
	}

	public void setCurrentWldX(int currentWldX) {
		this.currentWldX = currentWldX;
	}
	public void setCurrentWldY(int currentWldY) {
		this.currentWldY = currentWldY;
	}


	public String getIntegratedModName() {
		return integratedModName;
	}
}
