package serverFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gameRunner.Dimension;

public class ServerGameData implements Serializable
{
	public ServerFileInfo serverFileInfo;
	public PlayerAccountsFile playerAccountsFile;
	public Dimension[] universe;
	
	public ServerGameData()
	{
	}
	
	public ServerGameData(int specs)
	{
		serverFileInfo = new ServerFileInfo();
		playerAccountsFile = new PlayerAccountsFile();
		
		//setup universe
		universe = new Dimension[1];
		
		for (int i = 0; i < universe.length; i++)
		{
			universe[i] = new Dimension(0, 0, i);
		}
	}
	
}
