package serverFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerAccountsFile implements Serializable
{
	public List<PlayerData> allAccounts;
	
	public PlayerAccountsFile() {
		// TODO Auto-generated constructor stub
		
		allAccounts = new ArrayList<PlayerData>();
	}
}
