package serverFiles;

import java.util.ArrayList;
import java.util.List;

import gameRunner.GameData;

public class DataPacket 
{
	public boolean beingRead = false;
	public boolean beingUpdated = false;
	
	private String playerUsername = "";
	private int[] playerRndIDs = { -1, -1, -1, -1 };
	
	private boolean exceptionThrown = false;

	private int serverState = -1;

	private float cliPositionX, cliPositionY;
	private int cliWorldPositionX, cliWorldPositionY;
	public int cliWorldID;
	private int playerGameMode = 0;

	// 0 = up, 1 = down, 2 = left, 3 = right, 4 = left click, 5 = right click, 6 = X coordinate, 7 = Y coordinate, 
	// 8 = guiClicked, 9 = guiCommand, 10 = heldItem_modIndex, 11 = heldItem_entityIndex, 12 = invView_modId, 13 = invView_entityScroll
	public int[] inputCommands = { 0, 0, 0, 0
			, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	//client side data
	public int[][] viewedTiles = new int[4000][];
	
	int otherPlayersLength = 100;
	public int [][] otherPlayers_Int = new int[otherPlayersLength][];
	public String [] otherPlayers_Str = new String[otherPlayersLength];
	
	int otherNPCsLength = 100;
	public int [][] otherNPCs_Int = new int[otherNPCsLength][];
	public String [] otherNPCs_Str = new String[otherNPCsLength];
	
	public int playersCreatureID = 0;
	public int playersTextureNo = 0;
	public int playersVariationNo = 0;
	public int playersAnimationNo = 0;
	//public int playersFrameNo = 0;
	
	public String[] client_visibleMods;
	
	public int playersHP_Percent = 0;
	
	private int invSetToShow_modId = -1;
	private int invSetToShow_entityScroll = -1;
	private int[] invDisplay = new int[40];
	//\\\\\\
	
	public List<String> allRequiredModNames = new ArrayList<String>(); 
	public List<Integer> allRequiredModFileLengths = new ArrayList<Integer>(); 

	public int bufferedNumber;//for numbers which can't be returned
	
	private boolean finishedUploadingMod_clientUpload = false;
	public boolean idFinishedUploadingMod_clientUpload() {
		return finishedUploadingMod_clientUpload;
	}
	
	private boolean existingAdminClientData = false;
	private boolean existingAdminServerCommandsFc = false;
	private AdminClientData adminClientData = null;
	private AdminServerCommandsFc adminServerCommandsFc = null;
	private int serverConfirmCommand = AdminServerCommandsFc.NoCommand;
	
	private int worldLengthX = -1;
	private int worldLengthY = -1;
//
//	private boolean nextSendMessageWillBeFile = false;
//	public boolean isNextSendMessageWillBeFile() {
//		if (nextSendMessageWillBeFile)
//		{
//			nextSendMessageWillBeFile = false;
//			return true;
//		}
//		return false;
//	}

	
	public DataPacket()
	{
		for (int i = 0; i < viewedTiles.length; i++)
		{
			viewedTiles[i] = new int[] { -1, -1, -1, -1, -1, -1, -1 };
		}
		
		for (int i = 0; i < otherPlayers_Int.length; i++)
		{
			otherPlayers_Int[i] = new int[] { -1, -1, -1, -1, -1, -1, -1, -1 };
			otherPlayers_Str[i] = "";
		}
		
		for (int i = 0; i < otherNPCs_Int.length; i++)
		{
			otherNPCs_Int[i] = new int[] { -1, -1, -1, -1, -1, -1, -1, -1 };
			otherNPCs_Str[i] = "";
		}
		
		for (int i = 0; i < invDisplay.length; i++)
		{
			invDisplay[i] = 0;
		}
		
		client_visibleMods = new String[] {"", "", "", "", "", "", "", ""};
		adminClientData = new AdminClientData();
		adminServerCommandsFc = new AdminServerCommandsFc();
	}
	
	//translate data from String to player data
	public void readMessage(char[] message)
	{
		
		bufferedNumber = 0;
		
		try {
			int tileIndex = 0;
			int playersIndex = 0;
			int npcsIndex = 0;
			
			
			for (int i = 0; i < message.length; i++)
			{
				//System.out.println("Command: " + message[i]);
				switch(message[i++])
				{
				case 'p'://player ID
					if (message[i] == 'n')
					{
						playerRndIDs[0] = readNumber(message, ++i);//no more than 9 digits
						i = bufferedNumber;
						playerRndIDs[1] = readNumber(message, ++i);//no more than 9 digits
						i = bufferedNumber;
						playerRndIDs[2] = readNumber(message, ++i);//no more than 9 digits
						i = bufferedNumber;
						playerRndIDs[3] = readNumber(message, ++i);//no more than 9 digits
						i = bufferedNumber;
						playerUsername = readUsername(message, ++i);
						i = bufferedNumber;
						
//						System.out.println("received name: " + playerUsername);
//						System.out.println("received rnd1: " + playerRndIDs[0]);
//						System.out.println("received rnd2: " + playerRndIDs[1]);
//						System.out.println("received rnd3: " + playerRndIDs[2]);
//						System.out.println("received rnd4: " + playerRndIDs[3]);
					}
					break;
					
				case 'B'://block
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							viewedTiles[tileIndex][0] = readNumber(message, ++i);//mod
							i = bufferedNumber;

							viewedTiles[tileIndex][1] = readNumber(message, ++i);//block
							i = bufferedNumber;
							
							viewedTiles[tileIndex][2] = readNumber(message, ++i);//texture
							i = bufferedNumber;
							
							viewedTiles[tileIndex][3] = readNumber(message, ++i);//variation
							i = bufferedNumber;
							
							viewedTiles[tileIndex][4] = readNumber(message, ++i);//animation
							i = bufferedNumber;
							
							viewedTiles[tileIndex][5] = readNumber(message, ++i);//hp
							i = bufferedNumber;
							
							viewedTiles[tileIndex][6] = readNumber(message, ++i);//fill value
							i = bufferedNumber;
							
							tileIndex++;
						}
						else
						{
							//i++;
						}
					}
					break;
					
				case 'D'://data to client
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							//get server state
							serverState = readNumber(message, ++i);
							i = bufferedNumber;
							
							//game mode
							playerGameMode = readNumber(message, ++i);
							i = bufferedNumber;
							
							//world id
							cliWorldID = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player X position
							cliPositionX = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player Y position
							cliPositionY = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player X position
							cliWorldPositionX = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player Y position
							cliWorldPositionY = readNumber(message, ++i);
							i = bufferedNumber;
							
							//player img 
							playersCreatureID = readNumber(message, ++i);
							i = bufferedNumber;
							playersTextureNo = readNumber(message, ++i);
							i = bufferedNumber;
							playersVariationNo = readNumber(message, ++i);
							i = bufferedNumber;
							playersAnimationNo = readNumber(message, ++i);
							i = bufferedNumber;
							
							playersHP_Percent = readNumber(message, ++i);
							i = bufferedNumber;
							
							//inventory update
							for (int j = 0; j < GameData.EntitiesDisplayedInInv; j++)
							{
								invDisplay[j] = readNumber(message, ++i);
								i = bufferedNumber;
							}
							invSetToShow_modId = readNumber(message, ++i);
							i = bufferedNumber;
							invSetToShow_entityScroll = readNumber(message, ++i);
							i = bufferedNumber;
						}
						else
						{
							//i++;
						}
					}
					
					break;
					
					
				case 'W'://one off world data to client
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							worldLengthX = readNumber(message, ++i);
							i = bufferedNumber;
							
							worldLengthY = readNumber(message, ++i);
							i = bufferedNumber;
						}
						else
						{
							//i++;
						}
					}
					
					break;
					
				case 'O'://other players
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							//Player X position
							otherPlayers_Int[playersIndex][0] = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player Y position
							otherPlayers_Int[playersIndex][1] = readNumber(message, ++i);
							i = bufferedNumber;
							
							otherPlayers_Int[playersIndex][2] = readNumber(message, ++i);//mod
							i = bufferedNumber;
							otherPlayers_Int[playersIndex][3] = readNumber(message, ++i);//img
							i = bufferedNumber;
							otherPlayers_Int[playersIndex][4] = readNumber(message, ++i);//texture
							i = bufferedNumber;
							otherPlayers_Int[playersIndex][5] = readNumber(message, ++i);//variety
							i = bufferedNumber;
							otherPlayers_Int[playersIndex][6] = readNumber(message, ++i);//animation
							i = bufferedNumber;
							
							otherPlayers_Int[playersIndex][7] = readNumber(message, ++i);//hp
							i = bufferedNumber;
							
							otherPlayers_Str[playersIndex] = readUsername(message, ++i);
							i = bufferedNumber;
							
							playersIndex++;
						}
						else
						{
							//i++;
						}
					}
					
					break;
					
				case 'o'://other creatures
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							//Player/Creature X position
							otherNPCs_Int[npcsIndex][0] = readNumber(message, ++i);
							i = bufferedNumber;
							
							//Player/Creature Y position
							otherNPCs_Int[npcsIndex][1] = readNumber(message, ++i);
							i = bufferedNumber;
							
							otherNPCs_Int[npcsIndex][2] = readNumber(message, ++i);//mod id
							i = bufferedNumber;
							otherNPCs_Int[npcsIndex][3] = readNumber(message, ++i);//player/creature img 
							i = bufferedNumber;
							otherNPCs_Int[npcsIndex][4] = readNumber(message, ++i);//texture
							i = bufferedNumber;
							otherNPCs_Int[npcsIndex][5] = readNumber(message, ++i);//variety
							i = bufferedNumber;
							otherNPCs_Int[npcsIndex][6] = readNumber(message, ++i);//animation
							i = bufferedNumber;
							
							otherNPCs_Int[npcsIndex][7] = readNumber(message, ++i);//hp %
							i = bufferedNumber;
							
							otherNPCs_Str[npcsIndex] = readUsername(message, ++i);//name
							i = bufferedNumber;

							npcsIndex++;
						}
						else
						{
							//i++;
						}
					}
					
					break;
					
				case 'C'://data from client
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							//get commands from client
							

							inputCommands[0] += readNumber(message, ++i);//up key
							i = bufferedNumber;
							inputCommands[1] += readNumber(message, ++i);//down key
							i = bufferedNumber;
							inputCommands[2] += readNumber(message, ++i);//left key
							i = bufferedNumber;
							inputCommands[3] += readNumber(message, ++i);//right key
							i = bufferedNumber;
							
							inputCommands[4] += readNumber(message, ++i);//left mouse
							inputCommands[4] = (inputCommands[4] != 0)? 1 : 0;
							i = bufferedNumber;
							inputCommands[5] += readNumber(message, ++i);//right mouse
							inputCommands[5] = (inputCommands[5] != 0)? 1 : 0;
							i = bufferedNumber;
							inputCommands[6] = readNumber(message, ++i);//mouse X - overwrite
							i = bufferedNumber;
							inputCommands[7] = readNumber(message, ++i);//mouse Y - overwrite
							i = bufferedNumber;
							
							inputCommands[8] += readNumber(message, ++i);//if there's a gui command - overwrite
							inputCommands[8] = (inputCommands[8] != 0)? 1 : 0;
							i = bufferedNumber;
							inputCommands[9] = readNumber(message, ++i);//gui command id - overwrite
							i = bufferedNumber;
							
							//new selected held item
							inputCommands[10] = readNumber(message, ++i);
							i = bufferedNumber;
							inputCommands[11] = readNumber(message, ++i);
							i = bufferedNumber;
							
							inputCommands[12] = readNumber(message, ++i);
							i = bufferedNumber;
							inputCommands[13] = readNumber(message, ++i);
							i = bufferedNumber;

						}
						else
						{
							//i++;
						}
					}
					
					break;
				
				case 'F'://sending file name for next file upload
					
					//nextFileUploadName_clientUpload = readUsername(message, ++i);//name
					//i = bufferedNumber;
					
					finishedUploadingMod_clientUpload = false;
					break;
					
				case 'V'://command to end mod upload
					finishedUploadingMod_clientUpload = true;
					break;
					
				case 'M'://Name, Index and file length of mod on server
					while(message[i] != 'e')
					{
						if (message[i] == 'n')
						{
							//Mod index
							int tempSetIndex = readNumber(message, ++i);
							i = bufferedNumber;
							
							String tempName = readUsername(message, ++i);//mod name
							i = bufferedNumber;
							
							int tempSetFileLength = readNumber(message, ++i);
							i = bufferedNumber;
							
							//make sure this name goes in the right location in the list
							for (int j = allRequiredModNames.size(); j <= tempSetIndex; j++)
							{
								if (allRequiredModNames.size() == tempSetIndex)
								{
									allRequiredModNames.add(tempName);
									allRequiredModFileLengths.add(tempSetFileLength);
								}
								else
								{
									allRequiredModNames.add("");
									allRequiredModFileLengths.add(0);
								}
							}
							
							
							
//							String prnt = "";
//							for (int j = 0; j < allRequiredModNames.size(); j++)
//							{
//								prnt += allRequiredModNames.get(j) + ", ";
//							}
//							System.out.println("List of required mods size: " +  allRequiredModNames.size() + " contains: " + prnt);
						}
						else
						{
							i++;
						}
					}
					
					break;
					
					
				default:
				}
			}
			
			for (playersIndex = playersIndex; playersIndex < otherPlayers_Int.length; playersIndex++)
			{
				otherPlayers_Int[playersIndex][0] = -1;
			}
			
			for (npcsIndex = npcsIndex; npcsIndex < otherNPCs_Int.length; npcsIndex++)
			{
				otherNPCs_Int[npcsIndex][0] = -1;
			}
			
			exceptionThrown = false;
		}
		catch (Exception e)
		{
			exceptionThrown = true;
			
			e.printStackTrace();
			
			/*for (int i = 0; i < inputCommands.length; i++)
			{
				inputCommands[i] = 0;
			}*/
		}
			
	}
	

	private int readNumber(char[] message, int index)
	{
		int ret = 0;
		int multiplier = 1;
		
		if (message[index] == '-')
		{
			index++;
			multiplier = -1;
		}
		//System.out.println("Multi -> Str: " + message[index - 1] + " Act: " + multiplier);
		
		while (message[index] != 'n' && message[index] != 'e')
		{
			ret *= 10;
			ret += Integer.parseInt("" + message[index++]);
		}
		
		//System.out.println("ID -> Str: " + ret + " Act: " + message[index - 1]);
		
		bufferedNumber = index;
		
		ret *= multiplier;
		
		return ret;
	}
	
	private String readUsername(char[] message, int index)//always at the end of the message with 'e' on the end
	{
		String ret = "";
		
		while (index < message.length - 1 && message[index] != '\\')
		{
			ret += message[index++];
		}
		
		index++;
		//System.out.println("ID -> Str: " + ret + " Act: " + message[index - 1]);
		
		bufferedNumber = index;
		
		return ret;
	}
	
	
	//prepare message to send
	public String prepareMessage()
	{
		String ret = "";
		
		ret += "p" + "n" + playerRndIDs + "e";
		
		
		return ret;
	}
	
	
	//all getters and setters
	
	public String getPlayerUsername() {
		return playerUsername;
	}

	public int[] getPlayerRndIDs() {
		return playerRndIDs;
	}
	
	public boolean isExceptionThrown() {
		return exceptionThrown;
	}
	
	public int getServerState() {
		return serverState;
	}
	
	public void setAdminClientData(MessageToClientFs messageToClientFs) 
	{
		if (messageToClientFs.isAdmin)
		{
			for (int i = 0; i < messageToClientFs.bufferedModList.length; i++)
			{
				this.adminClientData.viewingModsBasicData[i] = messageToClientFs.bufferedModList[i];
			}
			
			for (int i = 0; i < messageToClientFs.bufferedModData.length; i++)
			{
				this.adminClientData.selectedModData[i] = messageToClientFs.bufferedModData[i];
			}
		}
		existingAdminClientData = messageToClientFs.isAdmin;
	}

	public void setAdminServerCommandsFc(MessageToServerFc messageToServerFc) 
	{
		if (messageToServerFc.adminCommand)
		{
			this.adminServerCommandsFc.setGetModData(messageToServerFc.getModData);
			this.adminServerCommandsFc.modRefName_change = messageToServerFc.modRefName_change;
			this.adminServerCommandsFc.modsInViewIndex0 = messageToServerFc.modsInViewIndex0;
			this.adminServerCommandsFc.setCommandID(messageToServerFc.commandID);
		}
		existingAdminServerCommandsFc = messageToServerFc.adminCommand;
	}
	
	public AdminClientData getAdminClientData() {
		return adminClientData;
	}

	public AdminServerCommandsFc getAdminServerCommandsFc() {
		return adminServerCommandsFc;
	}
	
	public boolean isExistingAdminClientData() {
		return existingAdminClientData;
	}

	public boolean isExistingAdminServerCommandsFc() {
		return existingAdminServerCommandsFc;
	}
	
	public int getServerConfirmCommand() {
		return serverConfirmCommand;
	}

	public void setServerConfirmCommand(int serverConfirmCommand) {
		this.serverConfirmCommand = serverConfirmCommand;
	}
	
	public void setAllRequiredModNames(List<String> allRequiredModNames) //for setting reference
	{
		this.allRequiredModNames = allRequiredModNames;
	}
	public void setAllRequiredModFileLengths(List<Integer> allRequiredModFileLengths) //for setting reference
	{
		this.allRequiredModFileLengths = allRequiredModFileLengths;
	}
	
	public int getWorldLengthX() {
		return worldLengthX;
	}

	public int getWorldLengthY() {
		return worldLengthY;
	}
	
	public float getCliPositionY() {
		return cliPositionY;
	}

	public float getCliPositionX() {
		return cliPositionX;
	}
	
	public int getCliWorldPositionY() {
		return cliWorldPositionY;
	}

	public int getCliWorldPositionX() {
		return cliWorldPositionX;
	}
	
	public int getPlayerGameMode() {
		return playerGameMode;
	}
	
	public int getInvSetToShow_modId() {
		return invSetToShow_modId;
	}

	public void setInvSetToShow_modId(int invSetToShow_modId) {
		this.invSetToShow_modId = invSetToShow_modId;
	}

	public int getInvSetToShow_entityId() {
		return invSetToShow_entityScroll;
	}

	public void setInvSetToShow_entityId(int invSetToShow_entityId) {
		this.invSetToShow_entityScroll = invSetToShow_entityId;
	}

	public int getInvDisplay(int index) {
		return invDisplay[index];
	}

	public void setInvDisplay(int invDisplayNo, int index) {
		this.invDisplay[index] = invDisplayNo;
	}
}
