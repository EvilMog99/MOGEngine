package serverFiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import org.lwjgl.Sys;

import gameRunner.Block;
import gameRunner.Creature;
import gameRunner.Dimension;
import gameRunner.GameData;
import gameRunner.LevelGenerator;
import modComponents.GameEntity;
import modComponents.GameEntityData;
import modComponents.IntegratedModData;
import modComponents.Mod;
import modComponents.SpawnType;
import toolbox.GameGuiCommands;
import toolbox.Saving;
import toolbox.Time;
/*By Christopher Deane*/
public class GameBackend implements ActionListener
{

	private Timer worldRunner;
	private static int WorldSpeed = 0;
	private static int WorldProcessSpeed = 1;//for processing entities
	private boolean cycleRunning = false;
	private boolean endGame = false;
	
	private int UpdateRows = 10;
	
	private static final int maxClients = 20;
	
	private static final float movementTestIncrements = 0.05f;
	
	private ConnectionManager connectionManager;
	private Time timer;
	
	private ServerGameData serverGameData;
	private Saving saver;
	private String saveFileName;
	
	private Random rnd;
	
	//private List<Mod> allMods;
	
	private List<ModStorage> allReceivedMods;
	private List<IntegratedModData> allIntegratedMods;
	
	private List<ServerDataOnIntegratedMods> allModsBeingIntegratedInGame;
	
	private static final int BlocksInViewX = 23;
	private static final int BlocksInViewY = 15;
	private static final int CreaturePlayersInViewX = 12;
	private static final int CreaturePlayersInViewY = 8;
	
	private static float currentTime;
	private static float previousTime;
	private static float delta;
	
	//temporarily used variables
	float tempMaxIndex;
	GameEntity tempLoopEntity;
	
	//int cycleNo = 0;
	
	public GameBackend ()
	{
	}
	
	public GameBackend (ConnectionManager cm, Saving saver, String saveFileName)
	{
		this.rnd = new Random();
		
		this.saver = saver;
		this.saveFileName = saveFileName;
		
		//prepare server
		serverGameData = new ServerGameData(0);		
		
		allReceivedMods = new ArrayList<ModStorage>();
		allIntegratedMods = new ArrayList<IntegratedModData>();
		
		//get server file info
		serverGameData.serverFileInfo = saver.open_serverFileInfo(saveFileName);
		
		if (!saver.isFileSuccess())
		{
			System.out.println("Couldn't find Server File Info");
			
			serverGameData.serverFileInfo = new ServerFileInfo();
			
			System.out.println("Created Server File Info");
			
			saver.save_serverFileInfo(saveFileName, serverGameData.serverFileInfo);

			System.out.println("Saved Server File Info");
		}
		else
		{
			System.out.println("Loaded Server File Info");
		}
		
		
		//find all mods that are in the game
		if (serverGameData.serverFileInfo.getServerDataOnIntegratedModsLength() > 0)
		{
			IntegratedModData tempIMD = new IntegratedModData();
			int totalMods = serverGameData.serverFileInfo.getServerDataOnIntegratedModsLength();
			IntegratedModData intgMod;
			System.out.println("Discovered " + totalMods);
			boolean failure = false;
			for (int i = 0; i < serverGameData.serverFileInfo.getServerDataOnIntegratedModsLength(); i++)
			{
				allIntegratedMods.add(tempIMD);
				System.out.println("Loading Mod " + (i+1) + "/" + totalMods + ": " + serverGameData.serverFileInfo.getServerDataOnIntegratedMod(i).getIntegratedModName());
				
				intgMod = new IntegratedModData(serverGameData.serverFileInfo.getServerDataOnIntegratedMod(i).getIntegratedModName(), saver);
				if (intgMod.isIntergratedModInServer())
				{
					allIntegratedMods.set(allIntegratedMods.size() - 1, intgMod);
					System.out.println("Successfully integrated: " + intgMod.getModRefName() + " to server");
					
					System.out.println("Number of entities: " + intgMod.getMod().getAllEntityData().length);
				}
				else
				{
					System.out.println("Failed to integrate: Mod No" + i + " to server");
					failure = true;
				}
			}
			
			if (!failure)
			{
				System.out.println("All mods where loaded and implemented successfully");
			}
		}
		
		//set list of all mods still being integrated into the game
		allModsBeingIntegratedInGame = new ArrayList<ServerDataOnIntegratedMods>();
		if (serverGameData.serverFileInfo.getServerDataOnIntegratedModsLength() > 0)
		{
			for (int i = 0; i < serverGameData.serverFileInfo.getServerDataOnIntegratedModsLength(); i++)
			{
				if (serverGameData.serverFileInfo.getServerDataOnIntegratedMod(i).isBeingIntegrated())
				{
					allModsBeingIntegratedInGame.add(serverGameData.serverFileInfo.getServerDataOnIntegratedMod(i));
				}
			}
		}
		
		//get player account data
		serverGameData.playerAccountsFile = saver.open_playerAccountsFile(saveFileName);
		
		if (!saver.isFileSuccess())
		{
			System.out.println("Couldn't find Server Accounts File");
			
			serverGameData.playerAccountsFile = new PlayerAccountsFile();
			
			System.out.println("Created Server Accounts File");
			
			saver.save_playerAccountsFile(saveFileName, serverGameData.playerAccountsFile);

			System.out.println("Saved Server Accounts File");
		}
		else
		{
			System.out.println("Loaded Server Accounts File");
		}
		
		//load world here
		for (int i = 0; i < serverGameData.universe.length; i++)
		{
			serverGameData.universe[i] = saver.open_serverDimensionFile(saveFileName, i);
			
			if (!saver.isFileSuccess())
			{
				System.out.println("Couldn't find Server Dimension " + i + " File");
				
				serverGameData.universe[i] = new Dimension(300, 100, i);
				
				System.out.println("Created Server Dimension " + i + " File");
				
				saver.save_serverDimensionFile(saveFileName, serverGameData.universe[i], i);
			}
			else
			{
				System.out.println("Loaded Server Dimension " + i + " File");
			}
		
		}
		
		timer = new Time();
		previousTime = timer.getTime();

		connectionManager = cm;
		
		setUpTimer();
		
	}
	
	private void setUpTimer()
	{
		worldRunner = new Timer(WorldSpeed, this);
	}
	
	
	
	public void startGame()
	{
		worldRunner.start();
	}
	public void stopGame()
	{
		worldRunner.stop();
	}
	
	
	//handler for any event which occurs
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();//get the object which created the event
		
		if (source == worldRunner && !cycleRunning)//run the game here
		{
			//cycleRunning = true;
			worldRunner.stop();
			//currentTime = timer.getTime();
			delta = 1f;//(currentTime - previousTime);
			//Time.calculateDeltaTime();
			//System.out.println("Started: " + cycleNo);
			
			//process received messages
			ServerListener sl;
			for (int i = 0; i < connectionManager.allConnections.size(); i++ )
			{
				sl = connectionManager.allConnections.get(i);
				
				if (!sl.dataPacket.isExceptionThrown())
				{
					if (!sl.setPlayerData && sl.dataPacket.getPlayerRndIDs()[0] != -1)//if this client is not connected to player data
					{
						int tempIndex = findPlayerIndex(sl.dataPacket.getPlayerRndIDs(), sl.dataPacket.getPlayerUsername());
						
						if (tempIndex != -1)//if the player exists
						{
							//connect this connection to their player data
							sl.playerData = serverGameData.playerAccountsFile.allAccounts.get(tempIndex);
							sl.setPlayerData = true;
							
							System.out.println("Found existing Player in game");
						}
						else//create a new account
						{
							PlayerData temp = new PlayerData(sl.dataPacket.getPlayerRndIDs(), sl.dataPacket.getPlayerUsername()
									, GameData.getCharacterSpawnX(serverGameData.universe[0])
									, GameData.getCharacterSpawnY(serverGameData.universe[0])
									, serverGameData.playerAccountsFile.allAccounts.size());
							sl.playerData = temp;
							
							if (serverGameData.playerAccountsFile.allAccounts.size() == 0)//if this is the admin
							{
								temp.setAdmin(true);
								System.out.println("Added new Admin Player Data to game");
							}
							else
							{
								System.out.println("Added new Player Data to game");
							}
							
							List<Integer> tempList;
							for (int j = 0; j < allIntegratedMods.size(); j++)
							{
								//setup this new player with a new inv list for all mods' entities
								tempList = new ArrayList<Integer>();
								for (int k = 0; k < allIntegratedMods.get(j).getMod().getAllEntityData().length; k++)
								{
									tempList.add(GameData.startingInvStack);
								}
								sl.playerData.addNewInventoryList(tempList);
							}
							
							serverGameData.playerAccountsFile.allAccounts.add(temp);
							
							sl.setPlayerData = true;
						}
						
					}
					else if (sl.dataPacket.getPlayerRndIDs()[0] != -1)//else if they are connected to player data 
					{
						//run client
						
						//keyboard inputs
						if (sl.dataPacket.inputCommands[0] != 0)//up key
						{
							sl.dataPacket.inputCommands[0] = 0;
							if (sl.playerData.isOnGround())
							{
								sl.playerData.updateVelocityY(GameData.playerJumpSpeed_verticle);
								sl.playerData.setOnGround(false);
							}
						}
						
						if (sl.dataPacket.inputCommands[1] != 0)//down key
						{
							sl.dataPacket.inputCommands[1] = 0;
							sl.playerData.updateVelocityY(GameData.playerAddFallSpeed_horizontal);
						}
						
						sl.playerData.updateVelocityY(GameData.playerAddFallSpeed_horizontal);//increase falling speed
						sl.playerData.velocityY = moveToTileVerticle(delta, sl.playerData.positionX, sl.playerData.positionY, sl.playerData.velocityY, serverGameData.universe[sl.playerData.currentDimensionID].allBlocks, (sl.playerData.velocityY > 0f)? true : false);
						sl.playerData.positionY += sl.playerData.velocityY;// * Time.getDeltaTime()); * Time.getDeltaTime()	
						if (sl.playerData.positionY < 9)
						{
							sl.playerData.positionY = 9;
						}
						else if (sl.playerData.positionY 
								> serverGameData.universe[sl.playerData.currentDimensionID].getWorldHeight() - 9)
						{
							sl.playerData.positionY = serverGameData.universe[sl.playerData.currentDimensionID].getWorldHeight() - 9;
						}
						
						//System.out.println("prev Y: " + sl.playerData.previousFinalVelocityY + " vel Y: " + sl.playerData.velocityY);
						if (sl.playerData.previousFinalVelocityY == sl.playerData.velocityY)//if the player has stopped moving? (used to test for 0)
						{
							sl.playerData.setOnGround(true);
						}
						else
						{
							sl.playerData.setOnGround(false);
						}
						sl.playerData.previousFinalVelocityY = sl.playerData.velocityY;//update what this player's final cycle was 
						
						if (sl.dataPacket.inputCommands[2] != 0)//left key
						{
							sl.dataPacket.inputCommands[2] = 0;
							sl.playerData.updateVelocityX(-GameData.playerAddWalkSpeed_verticle);
						}
						
						if (sl.dataPacket.inputCommands[3] != 0)//right key
						{
							sl.dataPacket.inputCommands[3] = 0;
							sl.playerData.updateVelocityX(GameData.playerAddWalkSpeed_verticle);
						}
						
						sl.playerData.velocityX -= sl.playerData.velocityX * GameData.playerAddFriction_verticle;//apply friction
						sl.playerData.velocityX = moveToTileHorizontal(delta, sl.playerData.positionX, sl.playerData.positionY, sl.playerData.velocityX, serverGameData.universe[sl.playerData.currentDimensionID].allBlocks, (sl.playerData.velocityX > 0f)? true : false);
						
						//check world bounds
						sl.playerData.positionX += sl.playerData.velocityX;
						if (sl.playerData.positionX < 14)
						{
							sl.playerData.positionX = 14;
						}
						else if (sl.playerData.positionX 
								> serverGameData.universe[sl.playerData.currentDimensionID].getWorldWidth() - 23)
						{
							sl.playerData.positionX = serverGameData.universe[sl.playerData.currentDimensionID].getWorldWidth() - 23;
						}
						
						//mouse inputs
						sl.playerData.mouseX = (int)sl.playerData.positionX + sl.dataPacket.inputCommands[6];
						sl.playerData.mouseY = (int)sl.playerData.positionY + sl.dataPacket.inputCommands[7];
						
						//try to set a new held item
						if (sl.dataPacket.inputCommands[10] > -2)
						{
							sl.playerData.setHeldItem_modIndex(sl.dataPacket.inputCommands[10]);
							sl.playerData.setHeldItem_entityIndex(sl.dataPacket.inputCommands[11]);
						}
						
						//set which inv entities are in view
						sl.playerData.setInvView_modId(sl.dataPacket.inputCommands[12]);
						sl.playerData.setInvView_entityScroll(sl.dataPacket.inputCommands[13]);
						
						if (sl.dataPacket.inputCommands[8] == 0
								&& (sl.playerData.mouseX > -1f) && (sl.playerData.mouseX + 1f < serverGameData.universe[sl.playerData.currentDimensionID]
										.allBlocks.length) && (sl.playerData.mouseY > -1f) 
										&& (sl.playerData.mouseY + 1f < serverGameData.universe[sl.playerData.currentDimensionID]
										.allBlocks
												[ 0 ].length))
						{
							if (sl.dataPacket.inputCommands[4] != 0)//left mouse button
							{
								sl.dataPacket.inputCommands[4] = 0;
								System.out.println("try to place block");
								//System.out.println("click X: " + sl.dataPacket.inputCommands[6]);
								//System.out.println("click Y: " + sl.dataPacket.inputCommands[7]);
								
								if (serverGameData.universe[sl.playerData.currentDimensionID]
										.allBlocks
												[ sl.playerData.mouseX ]
														[ sl.playerData.mouseY ].getEntityID_inMod() == -1 )
								{
									//test if a mod block has been selected
									if (sl.playerData.getHeldItem_modIndex() > -1 && sl.playerData.getHeldItem_entityIndex() > -1)
									{
										if (sl.playerData.getHeldItem_modIndex() < allIntegratedMods.size())
										{
											if (sl.playerData.getHeldItem_entityIndex() < allIntegratedMods.get(sl.playerData.getHeldItem_modIndex()).getMod().getAllEntityData().length)
											{
												//test if game mode is creative
												if (sl.playerData.getGameMode() == GameData.GameMode_Create)
												{
													serverGameData.universe[sl.playerData.currentDimensionID]
															.allBlocks
																	[ sl.playerData.mouseX ]
																			[ sl.playerData.mouseY ].setEntityID(sl.playerData.getHeldItem_modIndex()
																					, allIntegratedMods.get(sl.playerData.getHeldItem_modIndex()).getMod()
																						, serverGameData.universe[sl.playerData.currentDimensionID]
																								, allIntegratedMods.get(sl.playerData.getHeldItem_modIndex()).getMod().getEntityData(sl.playerData.getHeldItem_entityIndex())
																									, SpawnType.PlacedByPlayer);
												}
												else if (sl.playerData.getGameMode() == GameData.GameMode_Normal)//only place if there is space
												{
													if (sl.playerData.getInventoryItem(sl.playerData.getHeldItem_modIndex(), sl.playerData.getHeldItem_entityIndex()) > 0)
													{
														sl.playerData.decrementInventory(1, sl.playerData.getHeldItem_modIndex(), sl.playerData.getHeldItem_entityIndex());
														serverGameData.universe[sl.playerData.currentDimensionID]
																.allBlocks
																		[ sl.playerData.mouseX ]
																				[ sl.playerData.mouseY ].setEntityID(sl.playerData.getHeldItem_modIndex()
																						, allIntegratedMods.get(sl.playerData.getHeldItem_modIndex()).getMod()
																							, serverGameData.universe[sl.playerData.currentDimensionID]
																									, allIntegratedMods.get(sl.playerData.getHeldItem_modIndex()).getMod().getEntityData(sl.playerData.getHeldItem_entityIndex())
																										, SpawnType.PlacedByPlayer);
													}
												}
											}
										}
									}
									else //if not place null block
									{
										//basic block adding
										serverGameData.universe[sl.playerData.currentDimensionID]
												.allBlocks
														[ sl.playerData.mouseX ]
																[ sl.playerData.mouseY ].setEntityID(-1, 0, null, serverGameData.universe[sl.playerData.currentDimensionID]
																		, 0, 100, 1, 0, SpawnType.PlacedByPlayer);
									}
									
									//System.out.println("after click X: " + sl.playerData.mouseX);
									//System.out.println("after click Y: " + sl.playerData.mouseY);
								}
							}
							if (sl.dataPacket.inputCommands[5] != 0)//right mouse button
							{
								sl.dataPacket.inputCommands[5] = 0;
								System.out.println("try to break block");
								tryToBreakBlock(serverGameData.universe[sl.playerData.currentDimensionID]
										.allBlocks
												[ sl.playerData.mouseX ]
														[ sl.playerData.mouseY ], 
														
										serverGameData.universe[sl.playerData.currentDimensionID], 
														
										sl.playerData);
								
							}
						}
						else if (sl.dataPacket.inputCommands[8] == 1)//gui commands 
						{
							if (sl.dataPacket.inputCommands[9] != -1)
							{
								//System.out.println("gui: " + sl.dataPacket.inputCommands[9]);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								
								
								//run gui commands here
								switch (sl.dataPacket.inputCommands[9])
								{
								case 0://GameGuiCommands.
									
									break;
									
//								case GameGuiCommands.://for admin commands
//									if (sl.dataPacket.isExistingAdminServerCommandsFc())
//									{
//										
//									}
//									break;
									
								case GameGuiCommands.Game_CLIENT_INV_NormalMode:
									sl.playerData.setGameMode(GameData.GameMode_Normal);
									break;
									
								case GameGuiCommands.Game_CLIENT_INV_CreateMode:
									sl.playerData.setGameMode(GameData.GameMode_Create);
									break;
									
								default:
									break;
								}
							}
							
							//if this player is an admin
							if (sl.playerData.isAdmin())
							{
								//general commands
								if (sl.dataPacket.getAdminServerCommandsFc().isGetModData())
								{
									//send data about specific mod back to user
									if (!sl.dataPacket.getAdminServerCommandsFc().modRefName_change.equals(""))
									{
										sl.dataPacket.getAdminClientData().selectedModData[0] = "This Mod has been removed";
										for (int j = 0; j < allReceivedMods.size(); j++)
										{
											if (allReceivedMods.get(j).getModRefName().equals(sl.dataPacket.getAdminServerCommandsFc().modRefName_change))
											{
												//data for selected mod
												sl.adminClientData.selectedModData[0] = allReceivedMods.get(j).getModRefName();
												sl.adminClientData.selectedModData[1] = allReceivedMods.get(j).getMaxNumberOfFiles() + "";
												break;
											}
										}
									}
									else
									{
										sl.dataPacket.getAdminClientData().selectedModData[0] = "No mod here";
										sl.adminClientData.selectedModData[1] = "0";
									}
								}
								if (sl.dataPacket.getAdminServerCommandsFc().modsInViewIndex0 > -1)
								{
									int writeIndex = 0;
									for (int j = sl.dataPacket.getAdminServerCommandsFc().modsInViewIndex0; 
											writeIndex < sl.adminClientData.viewingModsBasicData.length; j++)
									{
										if (j < allReceivedMods.size() && allReceivedMods.get(j).isFinishedLoadingMod())
										{
											sl.adminClientData.viewingModsBasicData[writeIndex] = allReceivedMods.get(j).getModRefName();
										}
										else
										{//no mod here
											sl.adminClientData.viewingModsBasicData[writeIndex] = "";
										}
										writeIndex++;
									}
								}
								
								//specific commands
								//System.out.println("received command: " + sl.dataPacket.getAdminServerCommandsFc().getCommandID());
								switch (sl.dataPacket.getAdminServerCommandsFc().getCommandID())
								{
									
								case AdminServerCommandsFc.MOD_Add:
									if (!sl.dataPacket.getAdminServerCommandsFc().modRefName_change.equals(""))
									{
										for (int j = 0; j < allReceivedMods.size(); j++)
										{
											if (allReceivedMods.get(j).getModRefName().equals(sl.dataPacket.getAdminServerCommandsFc().modRefName_change))
											{
												if (!allReceivedMods.get(j).isBeingAddedToGame())
												{
													allReceivedMods.get(j).setBeingAddedToGame(true);
													System.out.println("Try to integrate mod");
													IntegratedModData intgMod = new IntegratedModData(sl.dataPacket.getAdminServerCommandsFc().modRefName_change
															, saver, allReceivedMods.get(j).getAllModFiles_clientUpload(), serverGameData.serverFileInfo
															, allModsBeingIntegratedInGame
															, serverGameData.universe[0].lastProcessed_X, serverGameData.universe[0].lastProcessed_Y);
													if (intgMod.isIntergratedModInServer())
													{
														allIntegratedMods.add(intgMod);
														allReceivedMods.remove(j);
														
														List<Integer> temp;
														//update all players with a new inv list for this mod's entities
														for (ServerListener connection : connectionManager.allConnections)
														{
															temp = new ArrayList<Integer>();
															for (int k = 0; k < intgMod.getMod().getAllEntityData().length; k++)
															{
																temp.add(GameData.startingInvStack);
															}
															connection.playerData.addNewInventoryList(temp);
														}
														break;
													}
													else
													{
														allReceivedMods.get(j).setBeingAddedToGame(false);
													}
												}
												else
												{
													System.out.println("This mod is currently being added");
													break;
												}
											}
										}
									}
									break;
									
								case AdminServerCommandsFc.MOD_Delete:
									if (!sl.dataPacket.getAdminServerCommandsFc().modRefName_change.equals(""))
									{
										System.out.println("Try to delete buffered mod: " + sl.dataPacket.getAdminServerCommandsFc().modRefName_change);
										for (int j = 0; j < allReceivedMods.size(); j++)
										{
											if (allReceivedMods.get(j).getModRefName().equals(sl.dataPacket.getAdminServerCommandsFc().modRefName_change))
											{
												System.out.println("Delete buffered mod: " + allReceivedMods.get(j).getModRefName());
												allReceivedMods.get(j).clearFiles();
												allReceivedMods.remove(j);
											}
										}
									}
									break;
									
								case AdminServerCommandsFc.SaveWorld:
									saveGame();
									break;
									
									default:
								}
							}
							else//no admin commads 
							{
								
							}
							
							sl.setConfirmCommand(sl.dataPacket.getAdminServerCommandsFc().getCommandID());
							sl.dataPacket.getAdminServerCommandsFc().resetCommandIdInServer();
						}
						else
						{
						}
						
						sl.dataPacket.inputCommands[4] = 0;
						sl.dataPacket.inputCommands[5] = 0;
						sl.dataPacket.inputCommands[8] = 0;
						
						//retrieve uploaded mod file
						boolean placedFile;
						while (sl.bufferedRecievedObjects.size() > 0)
						{
							placedFile = false;
							for (int k = 0; k < allReceivedMods.size() && !placedFile; k++)
							{
								if (allReceivedMods.get(k).getModRefName().equals(sl.bufferedRecievedObjects.get(0).modFileName_ref))
								{
									allReceivedMods.get(k).addFile_clientUpload(sl.bufferedRecievedObjects.get(0));
									sl.bufferedRecievedObjects.remove(0);
									placedFile = true;
								}
							}
							
							if (!placedFile)
							{
								allReceivedMods.add(new ModStorage(sl.bufferedRecievedObjects.get(0).modFileName_ref
										, sl.bufferedRecievedObjects.get(0).maxNumberOfFiles, sl.bufferedRecievedObjects.get(0)));
								System.out.println("Started Loading mod: " + sl.bufferedRecievedObjects.get(0).modFileName_ref);
								sl.bufferedRecievedObjects.remove(0);
								placedFile = true;
							}
						}
					}
				}
				
				//process this player
//				sl.playerData.updateVelocityY(0.1f);
//				sl.playerData.positionY += sl.playerData.getVelocityY();
//				
//				if (canMoveToTileVerticle(sl.playerData.positionX, sl.playerData.positionY + (GameData.playerWalkSpeed_verticle), 1, serverGameData.universe[sl.playerData.currentDimensionID].allBlocks, false))
//				{
//					sl.dataPacket.inputCommands[0] = 0;
//					sl.playerData.positionY += (GameData.playerWalkSpeed_verticle);// * Time.getDeltaTime()); * Time.getDeltaTime()
//				}
				
			}
			
			//process mods
			
//			for (int i = 0; i < allIntegratedMods.size(); i++)
//			{
//				//if ()
//			}
			

			//run each world
			
			for (int dimNo = 0; dimNo < serverGameData.universe.length; dimNo++)
			{
				//process fast blocks
				tempMaxIndex = serverGameData.universe[dimNo].allFastActiveBlocks.size() / (100f / WorldProcessSpeed);
				for (int i = 0; i < tempMaxIndex; i++)
				{
					if (tempMaxIndex < 1f)
					{
						serverGameData.serverFileInfo.lastFastBlockProcessed_incVal += tempMaxIndex;
						
						if (serverGameData.serverFileInfo.lastFastBlockProcessed_incVal >= 1f)
						{
							serverGameData.serverFileInfo.lastFastBlockProcessed++;
							serverGameData.serverFileInfo.lastFastBlockProcessed_incVal -= 1f;
							serverGameData.serverFileInfo.lastFastBlockProcessed_newVal = true;
						}
						else
						{
							serverGameData.serverFileInfo.lastFastBlockProcessed_newVal = false;
						}
					}
					else//normal increment
					{
						serverGameData.serverFileInfo.lastFastBlockProcessed++;
						serverGameData.serverFileInfo.lastFastBlockProcessed_newVal = true;
					}
					
					if (serverGameData.serverFileInfo.lastFastBlockProcessed_newVal)
					{
						if (serverGameData.serverFileInfo.lastFastBlockProcessed >= serverGameData.universe[dimNo].allFastActiveBlocks.size())
						{
							serverGameData.serverFileInfo.lastFastBlockProcessed = 0;
						}
						
						if (serverGameData.universe[dimNo]
								.allFastActiveBlocks.get(serverGameData.serverFileInfo.lastFastBlockProcessed).getEntitysModID() < 0
								||
								serverGameData.universe[dimNo]
								.allFastActiveBlocks.get(serverGameData.serverFileInfo.lastFastBlockProcessed).getEntityID_inMod() < 0
								||
								allIntegratedMods.get(serverGameData.universe[dimNo]
										.allFastActiveBlocks.get(serverGameData.serverFileInfo.lastFastBlockProcessed).getEntitysModID()).getMod()
									.getAllEntityData()[serverGameData.universe[dimNo]
											.allFastActiveBlocks.get(serverGameData.serverFileInfo.lastFastBlockProcessed).getEntityID_inMod()].getEntityPriority() != 2
								)
						{
							serverGameData.universe[dimNo].allFastActiveBlocks.remove(serverGameData.serverFileInfo.lastFastBlockProcessed);
						}
						
						//process block
						//System.out.println("Processing Fast " + serverGameData.serverFileInfo.lastFastBlockProcessed);
						tryUpdateAnimation(serverGameData.universe[dimNo].allFastActiveBlocks.get(serverGameData.serverFileInfo.lastFastBlockProcessed));
					}
				}
				
				//process medium blocks
				tempMaxIndex = serverGameData.universe[dimNo].allMediumActiveBlocks.size() / (500f / WorldProcessSpeed);
				for (int i = 0; i < tempMaxIndex; i++)
				{
					if (tempMaxIndex < 1f)
					{
						serverGameData.serverFileInfo.lastMediumBlockProcessed_incVal += tempMaxIndex;
						
						if (serverGameData.serverFileInfo.lastMediumBlockProcessed_incVal >= 1f)
						{
							serverGameData.serverFileInfo.lastMediumBlockProcessed++;
							serverGameData.serverFileInfo.lastMediumBlockProcessed_incVal -= 1f;
							serverGameData.serverFileInfo.lastMediumBlockProcessed_newVal = true;
						}
						else
						{
							serverGameData.serverFileInfo.lastMediumBlockProcessed_newVal = false;
						}
					}
					else//normal increment
					{
						serverGameData.serverFileInfo.lastMediumBlockProcessed++;
						serverGameData.serverFileInfo.lastMediumBlockProcessed_newVal = true;
					}

					if (serverGameData.serverFileInfo.lastMediumBlockProcessed_newVal)
					{
						if (serverGameData.serverFileInfo.lastMediumBlockProcessed >= serverGameData.universe[dimNo].allMediumActiveBlocks.size())
						{
							serverGameData.serverFileInfo.lastMediumBlockProcessed = 0;
						}
						
						if (serverGameData.universe[dimNo]
								.allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed).getEntitysModID() < 0
								||
								serverGameData.universe[dimNo]
								.allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed).getEntityID_inMod() < 0
								||
								allIntegratedMods.get(serverGameData.universe[dimNo]
										.allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed).getEntitysModID()).getMod()
									.getAllEntityData()[serverGameData.universe[dimNo]
											.allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed).getEntityID_inMod()].getEntityPriority() != 2
								)
						{
							serverGameData.universe[dimNo].allMediumActiveBlocks.remove(serverGameData.serverFileInfo.lastMediumBlockProcessed);
						}
						
						//process block
						//System.out.println("Processing Medium " + serverGameData.serverFileInfo.lastMediumBlockProcessed);
						tryUpdateAnimation(serverGameData.universe[dimNo].allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed));
						
						//last to process incase blocks are deleted
						processBlockFluids(
								serverGameData.universe[dimNo].allMediumActiveBlocks.get(serverGameData.serverFileInfo.lastMediumBlockProcessed)
								, serverGameData.universe[dimNo], allIntegratedMods);
					}
				}
				
				//process slow blocks
				tempMaxIndex = serverGameData.universe[dimNo].allSlowActiveBlocks.size() / (10000f / WorldProcessSpeed);
				for (int i = 0; i < tempMaxIndex; i++)//100000f
				{
					if (tempMaxIndex < 1f)
					{
						serverGameData.serverFileInfo.lastSlowBlockProcessed_incVal += tempMaxIndex;
						
						if (serverGameData.serverFileInfo.lastSlowBlockProcessed_incVal >= 1f)
						{
							serverGameData.serverFileInfo.lastSlowBlockProcessed++;
							serverGameData.serverFileInfo.lastSlowBlockProcessed_incVal -= 1f;
							serverGameData.serverFileInfo.lastSlowBlockProcessed_newVal = true;
						}
						else
						{
							serverGameData.serverFileInfo.lastSlowBlockProcessed_newVal = false;
						}
					}
					else//normal increment
					{
						serverGameData.serverFileInfo.lastSlowBlockProcessed++;
						serverGameData.serverFileInfo.lastSlowBlockProcessed_newVal = true;
					}
					
					if (serverGameData.serverFileInfo.lastSlowBlockProcessed_newVal)
					{
						if (serverGameData.serverFileInfo.lastSlowBlockProcessed >= serverGameData.universe[dimNo].allSlowActiveBlocks.size())
						{
							serverGameData.serverFileInfo.lastSlowBlockProcessed = 0;
						}
						
	//					if (serverGameData.universe[dimNo]
	//							.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getBlkID() < 0 
	//					|| GameData.allBlockData[serverGameData.universe[dimNo]
	//							.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getBlkID()][2] != 1)
						if (serverGameData.universe[dimNo]
								.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getEntitysModID() < 0
								||
								serverGameData.universe[dimNo]
								.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getEntityID_inMod() < 0
								||
								allIntegratedMods.get(serverGameData.universe[dimNo]
										.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getEntitysModID()).getMod()
									.getAllEntityData()[serverGameData.universe[dimNo]
											.allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed).getEntityID_inMod()].getEntityPriority() != 1
								)
						{
							serverGameData.universe[dimNo].allSlowActiveBlocks.remove(serverGameData.serverFileInfo.lastSlowBlockProcessed);
						}
						
						//process block
						tryUpdateAnimation(serverGameData.universe[dimNo].allSlowActiveBlocks.get(serverGameData.serverFileInfo.lastSlowBlockProcessed));
					}
				}
				
				//run creatures in world
				for (int i = 0; i < serverGameData.universe[dimNo].allCreatures.size(); i++)
				{
					tempLoopEntity = serverGameData.universe[dimNo].allCreatures.get(i);
					
					//tempLoopEntity
					tempLoopEntity.updateVelocityY(GameData.playerAddFallSpeed_horizontal);//increase falling speed
					tempLoopEntity.setVelocityY(moveToTileVerticle(delta, tempLoopEntity.getPositionX(), tempLoopEntity.getPositionY(), tempLoopEntity.getVelocityY(), serverGameData.universe[tempLoopEntity.getCurrentDimensionID()].allBlocks, (tempLoopEntity.getVelocityY() > 0f)? true : false));
					tempLoopEntity.setPositionY(tempLoopEntity.getPositionY() + tempLoopEntity.getVelocityY());// * Time.getDeltaTime()); * Time.getDeltaTime()	
				}
				
				
				//recharge one block in every dimension
				int bl = 0;
				while (bl++ < UpdateRows)
				{					
					//process normal stuff
					if (serverGameData.universe[dimNo].lastProcessed_X < serverGameData.universe[dimNo].allBlocks.length - 1)
					{
						//System.out.println("Recharge X: " + serverGameData.universe[dimNo].lastProcessed_X + " Y: " + serverGameData.universe[dimNo].lastProcessed_Y);
						
						//fix health
						serverGameData.universe[dimNo].allBlocks
								[serverGameData.universe[dimNo].lastProcessed_X]
										[serverGameData.universe[dimNo].lastProcessed_Y].addToEntityCurrentHealth(0.5f);
						
						//try to change animation
						GameEntity tempGe = serverGameData.universe[dimNo].allBlocks
								[serverGameData.universe[dimNo].lastProcessed_X]
										[serverGameData.universe[dimNo].lastProcessed_Y];
						
						//change animation
						tryUpdateAnimation(tempGe);
						
						serverGameData.universe[dimNo].lastProcessed_X++;
					}
					else
					{
						serverGameData.universe[dimNo].lastProcessed_X = 0;
						serverGameData.universe[dimNo].lastProcessed_Y++;
						
						if (serverGameData.universe[dimNo].lastProcessed_Y >= serverGameData.universe[dimNo].allBlocks[0].length)
						{
							serverGameData.universe[dimNo].lastProcessed_Y = 0;
						}

					}
					
					//process all mods being integrated in this world
					if (allModsBeingIntegratedInGame.size() > 0)
					{
						//loop through each mod
						for (int i = 0; i < allModsBeingIntegratedInGame.size(); i++)
						{
							//test all entities in mod and try to integrate them here
							int tempIndex = allModsBeingIntegratedInGame.get(i).getIntegratedModIndex();

							//loop through and find the best entity to add
							for (int eI = 0; eI < allIntegratedMods.get(tempIndex).getMod().getAllEntityData().length; eI++)
							{
								boolean didSpawn = LevelGenerator.spawnEntityBasedOnSpawnType(serverGameData.universe[dimNo]
										, serverGameData.universe[dimNo].lastProcessed_X, serverGameData.universe[dimNo].lastProcessed_Y
										, tempIndex, allIntegratedMods.get(tempIndex).getMod(), allIntegratedMods.get(tempIndex).getMod().getEntityData(eI), rnd);
								
								if (didSpawn)
								{
									//set random animation
									GameEntity tempGe = serverGameData.universe[dimNo].allBlocks
											[serverGameData.universe[dimNo].lastProcessed_X]
													[serverGameData.universe[dimNo].lastProcessed_Y];
									//change to random animation
									if (tempGe.getEntitysModID() > -1 && tempGe.getEntityID_inMod() > -1)
									{
										tempGe.setEntity_animationNo(rnd.nextInt(allIntegratedMods.get(tempGe.getEntitysModID()).getMod()
												.getAllEntityData()[tempGe.getEntityID_inMod()].getUploadedTextures()
												[tempGe.getEntity_textureNo()][tempGe.getEntity_variationNo()].length));
									}
								}
							}
						}
					}
					
					//set new X and Y value for current processed value
					if (dimNo == 0)
					{
						for (int i = 0; i < allModsBeingIntegratedInGame.size(); i++)
						{
							allModsBeingIntegratedInGame.get(i).setCurrentWldX(serverGameData.universe[dimNo].lastProcessed_X);
							allModsBeingIntegratedInGame.get(i).setCurrentWldY(serverGameData.universe[dimNo].lastProcessed_Y);
							
							if (allModsBeingIntegratedInGame.get(i).getCurrentWldX() == allModsBeingIntegratedInGame.get(i).getStartingWldX()
									&& allModsBeingIntegratedInGame.get(i).getCurrentWldY() == allModsBeingIntegratedInGame.get(i).getStartingWldY())
							{
								//this mod has now been integrated
								allModsBeingIntegratedInGame.get(i).setBeingIntegrated(false);
								System.out.println("Finished Intgrating " + allModsBeingIntegratedInGame.get(i).getIntegratedModName() + " in game");
								allModsBeingIntegratedInGame.remove(i);
							}
						}
					}
				}
			}
			

			
			//process messages to send
			String clientUpdateMessage;
			//System.out.println("Connections: " + connectionManager.allConnections.size());
			
			for (int i = 0; i < connectionManager.allConnections.size(); i++ )
			{
				if (connectionManager.allConnections.get(i).inUse && connectionManager.allConnections.get(i).setPlayerData)
				{					
					clientUpdateMessage = "";
					
					clientUpdateMessage += playerData(connectionManager.allConnections.get(i).playerData, serverGameData.serverFileInfo.serverState);
					
					if (!connectionManager.allConnections.get(i).playerData.isSendWorldData())
					{
						connectionManager.allConnections.get(i).playerData.setSendWorldData(false);
						clientUpdateMessage += worldData(serverGameData.universe[connectionManager.allConnections.get(i).playerData.currentDimensionID]);
					}
					
					clientUpdateMessage += findFieldOfView(connectionManager.allConnections.get(i).playerData.positionX, connectionManager.allConnections.get(i).playerData.positionY
							, serverGameData.universe[connectionManager.allConnections.get(i).playerData.currentDimensionID]);
					
					clientUpdateMessage += findPlayersInView(i, connectionManager.allConnections.get(i).playerData.positionX, connectionManager.allConnections.get(i).playerData.positionY, connectionManager.allConnections.get(i).playerData.currentDimensionID
							, connectionManager);
					
					clientUpdateMessage += findCreaturesInView(connectionManager.allConnections.get(i).playerData.positionX, connectionManager.allConnections.get(i).playerData.positionY, connectionManager.allConnections.get(i).playerData.currentDimensionID
							, serverGameData.universe[connectionManager.allConnections.get(i).playerData.currentDimensionID].allCreatures, allIntegratedMods);
					
					clientUpdateMessage += allRequiredModNames(connectionManager.allConnections.get(i), allIntegratedMods);
					
					//if this client needs another mod sending to it but is not ready yet
					if (connectionManager.allConnections.get(i).isRequestedMod() && !connectionManager.allConnections.get(i).isSendObjectReady())
					{
						//try to connect mod data to serverlistener
						for (int j = 0; j < allIntegratedMods.size(); j++)
						{
							if (allIntegratedMods.get(j).getModRefName().equals(connectionManager.allConnections.get(i).getRequestedModName()))
							{
								//set serverlistener with raw files to send to client
								connectionManager.allConnections.get(i).setBufferedSendObjects(allIntegratedMods.get(j).getRawFilesForClients());
								break;
							}
						}
					}
					
					connectionManager.allConnections.get(i).send(clientUpdateMessage, connectionManager.allConnections.get(i).playerData.isAdmin()
							, (connectionManager.allConnections.get(i).playerData.isAdmin())? connectionManager.allConnections.get(i).adminClientData : null);
				}
			}
			
			//System.out.println("Ended: " + cycleNo);
			//cycleNo++;
			//cycleRunning = false;
			previousTime = currentTime;
			
			if (!endGame)
			{
				worldRunner.start();
			}
		}
		
	}
	
	private boolean tryToBreakBlock(GameEntity block, Dimension dimension, PlayerData player)
	{
		if (block.getEntityCurrentHealth() <= 0f)
		{
			//player.addInventoryItem(block.getBlkID(), 1);
			//add to players inventory
			if (block.getEntitysModID() > -1 && block.getEntityID_inMod() > -1)
			{
				player.incrementInventory(1, block.getEntitysModID(), block.getEntityID_inMod());
			}
			
			block.setEntityID(-1, -1, null, dimension, 0, 0, 0, 0, SpawnType.PlacedByPlayer);
			return true;
		}
		else
		{
			//block.removeFromEntityCurrentHealth(10f * Time.getDeltaTime());
			block.removeFromEntityCurrentHealth(10000f * Time.getDeltaTime());
			return false;
		}
	}
	
	private void processBlockFluids(GameEntity source, Dimension dimension, List<IntegratedModData> intgMods)
	{
		//System.out.println("processing: " + source.getEntityID_inMod());
		//int sourceST = getGameEntitysSpawnType(source, intgMods);
		if (source.getPositionY() > 0f)//if has block below it? 
		{
			//runSubstanceShare(source, dimension.allBlocks[(int)source.getPositionX()][(int)source.getPositionY() - 1], intgMods, dimension);
			runSubstanceMove(source, dimension.allBlocks[(int)source.getPositionX()][(int)source.getPositionY() - 1], intgMods, dimension);
		}
//		if (source.getPositionY() + 1f < dimension.allBlocks[0].length)//if has block above?
//		{
//			runSubstanceShare(source, dimension.allBlocks[(int)source.getPositionX()][(int)source.getPositionY() + 1], intgMods, dimension);
//		}
		if (source.getPositionX() > 0f)//if has block to its left? 
		{
			runSubstanceShare(source, dimension.allBlocks[(int)source.getPositionX() - 1][(int)source.getPositionY()], intgMods, dimension);
		}
		if (source.getPositionX() + 1f < dimension.getWorldWidth())//if has block below?
		{
			runSubstanceShare(source, dimension.allBlocks[(int)source.getPositionX() + 1][(int)source.getPositionY()], intgMods, dimension);
		}
	}
	
	private int getGameEntitysSpawnType(GameEntity ge, List<IntegratedModData> intgMods)
	{
		if (ge.getEntitysModID() > -1 && ge.getEntityID_inMod() > -1)
		{
			intgMods.get(ge.getEntitysModID()).getMod().getAllEntityData()[ge.getEntityID_inMod()].getSpawnTypeID();
		}
		return SpawnType.NoSpawn;
	}
	
	private void runSubstanceMove(GameEntity source, GameEntity neighbour, List<IntegratedModData> intgMods, Dimension dimension)//used to total how many blocks the source has to share its values with
	{
		//if they have enough to share?
		if (source.getFillValue() + neighbour.getFillValue() <= 100f)
		{
			//if they can share?
//			if (source.getEntitysModID() == neighbour.getEntitysModID() 
//					&& source.getEntityID_inMod() == neighbour.getEntityID_inMod())
			if (source.isLiquid() && neighbour.isLiquid())
			{
				neighbour.setFillValue(source.getFillValue() + neighbour.getFillValue());
				source.setFillValue(0f);
				source.setEntityID(-1, -1, null, dimension, 0, 0, 0, 0, 0);
			}
			//else if the neighbour can become like the source?
			else if (neighbour.getEntitysModID() == -1 
					&& neighbour.getEntityID_inMod() == -1)
			{
				neighbour.setEntityID(source.getEntitysModID()
						, intgMods.get(source.getEntitysModID()).getMod(), dimension
							, intgMods.get(source.getEntitysModID()).getMod().getAllEntityData()[source.getEntityID_inMod()]
								, source.getEntitySpawnType());
				neighbour.setFillValue(source.getFillValue());
				
				source.setEntityID(-1, -1, null, dimension, 0, 0, 0, 0, 0);
				source.setFillValue(0);
			}
		}
		else if (source.getFillValue() + neighbour.getFillValue() > 100f)
		{
			//if they can share?
//			if (source.getEntitysModID() == neighbour.getEntitysModID() 
//					&& source.getEntityID_inMod() == neighbour.getEntityID_inMod())
			if (source.isLiquid() && neighbour.isLiquid())
			{
				source.setFillValue(source.getFillValue() + neighbour.getFillValue() - 100f);
				neighbour.setFillValue(100f);
			}
			//else if the neighbour can become like the source?
			else if (neighbour.getEntitysModID() == -1 
					&& neighbour.getEntityID_inMod() == -1)
			{
				neighbour.setEntityID(source.getEntitysModID()
						, intgMods.get(source.getEntitysModID()).getMod(), dimension
							, intgMods.get(source.getEntitysModID()).getMod().getAllEntityData()[source.getEntityID_inMod()]
								, source.getEntitySpawnType());
				source.setFillValue(source.getFillValue() + neighbour.getFillValue() - 100f);
				neighbour.setFillValue(100f);
			}
		}
	}
	
	private void runSubstanceShare(GameEntity source, GameEntity neighbour, List<IntegratedModData> intgMods, Dimension dimension)//used to total how many blocks the source has to share its values with
	{
		//if they have enough to share?
		if (source.getFillValue() + neighbour.getFillValue() >= 0.5f)
		{
			//if they can share?
//			if (source.getEntitysModID() == neighbour.getEntitysModID() 
//					&& source.getEntityID_inMod() == neighbour.getEntityID_inMod())
			if (source.isLiquid() && neighbour.isLiquid())
			{
				source.setFillValue((source.getFillValue() + neighbour.getFillValue()) / 2f);
				neighbour.setFillValue(source.getFillValue());
			}
			//else if the neighbour can become like the source?
			else if (neighbour.getEntitysModID() == -1 
					&& neighbour.getEntityID_inMod() == -1)
			{
				neighbour.setEntityID(source.getEntitysModID()
						, intgMods.get(source.getEntitysModID()).getMod(), dimension
							, intgMods.get(source.getEntitysModID()).getMod().getAllEntityData()[source.getEntityID_inMod()]
								, source.getEntitySpawnType());
				source.setFillValue(source.getFillValue() / 2f);
				neighbour.setFillValue(source.getFillValue());
			}
		}
		else if (source.getFillValue() < 0.5f)
		{
			source.setEntityID(-1, -1, null, dimension, 0, 0, 0, 0, SpawnType.NoSpawn);
			source.setFillValue(0);
		}
	}
	
	private int findPlayerIndex(int[] playerRndIDs, String username)
	{
		System.out.println("Number of stored accounts: " + serverGameData.playerAccountsFile.allAccounts.size());
		
		int ret = -1;
		
		for (int i = 0; i < serverGameData.playerAccountsFile.allAccounts.size(); i++ )
		{
			if (serverGameData.playerAccountsFile.allAccounts.get(i).getUsername().equals(username)
					&& serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[0] == playerRndIDs[0]
					&& serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[1] == playerRndIDs[1]
					&& serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[2] == playerRndIDs[2]
					&& serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[3] == playerRndIDs[3])
			{
//				System.out.println("name: " + serverGameData.playerAccountsFile.allAccounts.get(i).getUsername() + " = " + username);
//				System.out.println("rnd 1: " + serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[0] + " = " + playerRndIDs[0]);
//				System.out.println("rnd 2: " + serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[1] + " = " + playerRndIDs[1]);
//				System.out.println("rnd 3: " + serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[2] + " = " + playerRndIDs[2]);
//				System.out.println("rnd 4: " + serverGameData.playerAccountsFile.allAccounts.get(i).getPlayerRndIDs()[3] + " = " + playerRndIDs[3]);
//				
				ret = i;
				System.out.println("existing");
				break;
			}
		}
		
		return ret;
	}
	
	private static String allRequiredModNames(ServerListener sl, List<IntegratedModData> allIntegratedModData)
	{
		String retData = "";
		
		//for every mod on the server that the client hasn't confirmed yet
		for (int i = sl.getNextRequiredModIndex(); i < allIntegratedModData.size(); i++)
		{
			retData += "M";//start block data
			
			retData += "n";//mod index
			retData += "" + i;
			
			retData += "n";//mod name
			retData += allIntegratedModData.get(i).getModRefName() + '\\';
			
			retData += "n";//number of files in mod
			retData += "" + allIntegratedModData.get(i).getMaxNumberOfFiles();
			retData += "e";//
			
			System.out.println(sl.playerData.getUsername() + " requires: " + allIntegratedModData.get(i).getModRefName()
					+ ", length: " + allIntegratedModData.get(i).getMaxNumberOfFiles());
			
			sl.setNextRequiredModIndex(i + 1);
		}


		retData += "e";//

				
		return retData;
	}
	
	private static String playerData(PlayerData pl, int serverState)
	{
		String retData = "";
		
		retData += "D";//start block data
		
		//current server state
		retData += "n";
		retData += serverState;
		
		//current game mode
		retData += "n";//start number
		retData += pl.getGameMode();
		
		//current world ID
		retData += "n";//start number
		retData += pl.currentDimensionID;
		
		//grid position
		retData += "n";//start number
		retData += (int)((pl.positionX % 1) * 100);
		retData += "n";//start number
		retData += (int)((pl.positionY % 1) * 100);
		
		//world position
		retData += "n";//start number
		retData += (int)(pl.positionX);
		retData += "n";//start number
		retData += (int)(pl.positionY);
		
		retData += "n";//Img ID
		retData += pl.creatureID;
		retData += "n";//texture
		retData += pl.TextureNo;
		retData += "n";//animation
		retData += pl.AnimationNo;
		retData += "n";//frame
		retData += pl.FrameNo;
		
		retData += "n";//HP
		retData += "" + sendHealth(pl.getHealth(), pl.getHealth_Max());//!!!

		for (int i = 0; i < GameData.EntitiesDisplayedInInv; i++)
		{
			retData += "n";//number of held entities
			retData += pl.getInventoryItem(pl.getInvView_modId(), pl.getInvView_entityScroll() + i);
		}
		retData += "n";//modId
		retData += pl.getInvView_modId();
		retData += "n";//entityScroll
		retData += pl.getInvView_entityScroll();
		
		retData += "e";//

				
		return retData;
	}
	
	private static String worldData(Dimension wld)
	{
		String retData = "";
		
		retData += "W";//start world data
		
		retData += "n";//wld length x
		retData += wld.getWorldWidth();
		retData += "n";//wld length y
		retData += wld.getWorldHeight();

		retData += "e";//

				
		return retData;
	}
	
	private static String findFieldOfView(float plX, float plY, Dimension wld)
	{
		String retData = "";
		int startX = (int)plX - (BlocksInViewX / 2);
		int startY = (int)plY - (BlocksInViewY / 2);
		
		for (int x = 0; x < BlocksInViewX && (x + startX) > 0 && (x + startX) < wld.allBlocks.length; x++)
		{
			for (int y = 0; y < BlocksInViewY && (y + startY) > 0 && (y + startY) < wld.allBlocks[0].length; y++)
			{
				retData += "B";//start block data
				retData += "n";//
				retData += "" + wld.allBlocks[x + startX][y + startY].getEntitysModID();
				retData += "n";//
				retData += "" + wld.allBlocks[x + startX][y + startY].getEntityID_inMod();
				
				retData += "n";//
				retData += "" + wld.allBlocks[x + startX][y + startY].getEntity_textureNo();
				
				retData += "n";//
				retData += "" + wld.allBlocks[x + startX][y + startY].getEntity_variationNo();
				
				retData += "n";//
				retData += "" + wld.allBlocks[x + startX][y + startY].getEntity_animationNo();
				
				retData += "n";//HP
				retData += "" + sendHealth(wld.allBlocks[x + startX][y + startY].getEntityCurrentHealth(), wld.allBlocks[x + startX][y + startY].getEntityMaxHealth());
				
				retData += "n";//Fill Value
				retData += "" + (int)wld.allBlocks[x + startX][y + startY].getFillValue();
				
				retData += "e";//end block data
			}
		}
		
		return retData;
	}
	
																																																																																																	/*By Christopher Deane*/
	private static String findPlayersInView(int plIndex, float plX, float plY, int plDimension, ConnectionManager connectionManager)
	{
		String retData = "";
		float tempX = 0;
		float tempY = 0;
		
		for (int i = 0; i < connectionManager.allConnections.size(); i++)
		{
			if (connectionManager.allConnections.get(i).playerData != null 
					&& connectionManager.allConnections.get(i).inUse
						&& i != plIndex)
			{
				tempX = connectionManager.allConnections.get(i).playerData.positionX - (int)plX;
				tempY = connectionManager.allConnections.get(i).playerData.positionY - (int)plY;
				
				if (plDimension == connectionManager.allConnections.get(i).playerData.currentDimensionID
						&& Math.abs(tempX) < CreaturePlayersInViewX && Math.abs(tempY) < CreaturePlayersInViewY)//more work!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				{
					retData += "O";//start player data
					
					//grid position
					retData += "n";//start number
					retData += (int)((tempX) * 100);
					retData += "n";//start number
					retData += (int)((tempY) * 100);
					
					retData += "n";//Mod ID - for player texture
					retData += -1;
					retData += "n";//Img ID
					retData += connectionManager.allConnections.get(i).playerData.creatureID;
					retData += "n";//texture
					retData += connectionManager.allConnections.get(i).playerData.TextureNo;
					retData += "n";//animation
					retData += connectionManager.allConnections.get(i).playerData.AnimationNo;
					retData += "n";//frame
					retData += connectionManager.allConnections.get(i).playerData.FrameNo;
	
					retData += "n";//HP
					retData += "" + sendHealth(connectionManager.allConnections.get(i).playerData.getHealth(), connectionManager.allConnections.get(i).playerData.getHealth_Max());
					
					retData += "n";//username
					retData += connectionManager.allConnections.get(i).playerData.getUsername() + '\\';
					
					retData += "e";//
				}
			}
		}
		
		return retData;
	}
	
	
	private static String findCreaturesInView(float plX, float plY, int plDimension, List<GameEntity> allCreatures, List<IntegratedModData> allMods)
	{
		String retData = "";
		float tempX = 0;
		float tempY = 0;
		
		for (int i = 0; i < allCreatures.size(); i++)
		{
			tempX = allCreatures.get(i).getPositionX() - (int)plX;
			tempY = allCreatures.get(i).getPositionY() - (int)plY;
			
			//System.out.println("cr off i: " + i + " x: " + tempX + " y: " + tempY);
			
			if (plDimension == allCreatures.get(i).getCurrentDimensionID()
					&& Math.abs(tempX) < CreaturePlayersInViewX && Math.abs(tempY) < CreaturePlayersInViewY)
			{
				retData += "o";//start creature data
				
				//grid position
				retData += "n";//start number
				retData += (int)((tempX) * 100);
				retData += "n";//start number
				retData += (int)((tempY) * 100);
				
				retData += "n";//Mod ID
				retData += allCreatures.get(i).getEntitysModID();
				retData += "n";//Img ID
				retData += allCreatures.get(i).getEntityID_inMod();
				retData += "n";//texture
				retData += allCreatures.get(i).getEntity_textureNo();
				retData += "n";//variety
				retData += allCreatures.get(i).getEntity_variationNo();
				retData += "n";//animation
				retData += allCreatures.get(i).getEntity_animationNo();

				retData += "n";//HP
				retData += "" + sendHealth(allCreatures.get(i).getEntityCurrentHealth(), allCreatures.get(i).getEntityMaxHealth());
				
				retData += "n";//username
				if (allCreatures.get(i).getEntitysModID() != -1)
				{
					retData += allMods.get(allCreatures.get(i).getEntitysModID()).getMod().getEntityData(allCreatures.get(i).getEntityID_inMod()).getEntityName() + '\\';//GameData.allCreatureNames[allCreatures.get(i).creatureID] + '\\';
				}
				else 
				{
					retData += allCreatures.get(i).getEntityNickName() + '\\';//"Creature" + '\\';
				}
				retData += "e";//
			}
		}
		
		return retData;
	}
	
	private static int sendHealth(float current, float max)
	{
		return (int)((1000 / max) * current);
	}
	
//	private boolean canMoveToTileVerticle(float x, float y, int lvl, GameEntity[][] wld, boolean up)
//	{
//		//System.out.println("move x: " + x + " y: " + y);
//		
//		if ( (up)? (y > -1f) : (y + 1f < wld[0].length) )
//		{
//			if ( (up)? (wld[(int)(x + 0.1f)][(int)(y)].getEntityID_inMod() == -1) && (wld[(int)(x + 0.9f)][(int)(y)].getEntityID_inMod() == -1) :
//					 (wld[(int)(x + 0.1f)][(int)(y + 1f)].getEntityID_inMod() == -1) && (wld[(int)(x + 0.9f)][(int)(y + 1f)].getEntityID_inMod() == -1) )
//			{
//				return true;
//			}
//		}
//		return false;
//	}
	
	private boolean isInWater(float x, float y, GameEntity[][] wld)
	{
		//System.out.println("move x: " + x + " y: " + y);
		if ( (wld[(int)(x + 0.3f)][(int)(y + 0.7f)].isLiquid()) || (wld[(int)(x + 0.7f)][(int)(y + 0.7f)].isLiquid()) ||
				 (wld[(int)(x + 0.3f)][(int)(y)].isLiquid()) || (wld[(int)(x + 0.7f)][(int)(y)].isLiquid()) )
		{
			return true;
		}
		return false;
	}
	
	private float moveToTileVerticle(float delta, float x, float y, float velocityY, GameEntity[][] wld, boolean up)
	{
		velocityY *= delta;
		
		if (isInWater(x, y, wld))
		{
			velocityY *= 0.6f;
		}
		
		float minus = movementTestIncrements;
		while(minus < 1f)
		{
			if (canMoveToTileVerticle(x, y + (velocityY * minus), wld, up))
			{
				minus += movementTestIncrements;//increment value
			}
			else//can't move here
			{
				break;
			}
		}
		return velocityY * (minus - movementTestIncrements);//move to last good position (or by 0 if there were none)
	}
	
	private boolean canMoveToTileVerticle(float x, float y, GameEntity[][] wld, boolean up)
	{
		//System.out.println("move x: " + x + " y: " + y);
		
		if ( (up)? (y > -1f) : (y + 1f < wld[0].length) )
		{
			if ( (up)? (!wld[(int)(x + 0.3f)][(int)(y + 0.7f)].isHasCollisions()) && (!wld[(int)(x + 0.7f)][(int)(y + 0.7f)].isHasCollisions()) :
					 (!wld[(int)(x + 0.3f)][(int)(y)].isHasCollisions()) && (!wld[(int)(x + 0.7f)][(int)(y)].isHasCollisions()) )
			{
				return true;
			}
		}
		return false;
	}
	
	private float moveToTileHorizontal(float delta, float x, float y, float velocityX, GameEntity[][] wld, boolean left)
	{
		velocityX *= delta;
		
		if (isInWater(x, y, wld))
		{
			velocityX *= 0.6f;
		}
		
		float minus = movementTestIncrements;
		while(minus < 1f)
		{
			if (canMoveToTileHorizontal(x + (velocityX * minus), y, wld, left))
			{
				minus += movementTestIncrements;//increment value
			}
			else//can't move here
			{
				break;
			}
		}
		return velocityX * (minus - movementTestIncrements);//move to last good position (or by 0 if there were none)
	}
	
	private boolean canMoveToTileHorizontal(float x, float y, GameEntity[][] wld, boolean left)
	{
		//System.out.println("move x: " + x + " y: " + y);
		
		if ((left)? (x > -1f) : (x + 1f < wld.length) )
		{
			if ( (left)? (!wld[(int)(x + 0.7f)][(int)(y + 0.1f)].isHasCollisions()) && (!wld[(int)(x + 0.7f)][(int)(y + 0.7f)].isHasCollisions()) :
					 (!wld[(int)(x + 0.3f)][(int)(y + 0.1f)].isHasCollisions()) && (!wld[(int)(x + 0.3f)][(int)(y + 0.7f)].isHasCollisions()) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void tryUpdateAnimation(GameEntity tempGe)
	{
		//change animation
		if (tempGe.getEntitysModID() > -1 && tempGe.getEntityID_inMod() > -1)
		{
			//System.out.println("Length: " + allIntegratedMods.size() + " mod id: " + tempGe.getEntitysModID());
			GameEntityData refGed = allIntegratedMods.get(tempGe.getEntitysModID()).getMod().getAllEntityData()[tempGe.getEntityID_inMod()];
			
			if ((refGed.getSpawnTypeID() == SpawnType.Block_SingleBlockBush || refGed.getSpawnTypeID() == SpawnType.Block_SingleBlockPlant
					|| refGed.getSpawnTypeID() == SpawnType.Block_Tree_SaplingBlock || refGed.getSpawnTypeID() == SpawnType.Block_Tree_SpawnBlock) 
					&& (tempGe.getEntity_animationNo() + 1
					<
					refGed.getUploadedTextures()[tempGe.getEntity_textureNo()][tempGe.getEntity_variationNo()].length))
			{
				tempGe.setEntity_animationNo(tempGe.getEntity_animationNo() + 1);
			}
		}
	}
	
	
//	private boolean canMoveToTile(int x, int y, int lvl, Block[][] wld)
//	{
//		if (wld[x][y].getBlkID() == -1)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
	
	
	public void saveGame()
	{
		stopGame();
		serverGameData.serverFileInfo.numberOfUniverses = serverGameData.universe.length;
		saver.save_serverFileInfo(saveFileName, serverGameData.serverFileInfo);
		
		saver.save_playerAccountsFile(saveFileName, serverGameData.playerAccountsFile);

		for (int i = 0; i < serverGameData.universe.length; i++)
		{
			saver.save_serverDimensionFile(saveFileName, serverGameData.universe[i], i);
		}
		startGame();
	}
	
	
	public void endGame()
	{
		endGame = true;
		stopGame();

		serverGameData.serverFileInfo.numberOfUniverses = serverGameData.universe.length;
		saver.save_serverFileInfo(saveFileName, serverGameData.serverFileInfo);
		
		saver.save_playerAccountsFile(saveFileName, serverGameData.playerAccountsFile);

		for (int i = 0; i < serverGameData.universe.length; i++)
		{
			saver.save_serverDimensionFile(saveFileName, serverGameData.universe[i], i);
		}
		//server.setEndServer(true);
	}
	
																																																																																		/*By Christopher Deane*/
	
}

