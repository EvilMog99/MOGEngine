package gameRunner;

import java.awt.List;
import java.util.Random;

import modComponents.GameEntity;
import modComponents.GameEntityData;
import modComponents.IntegratedModData;
import modComponents.Mod;
import modComponents.SpawnType;

public class LevelGenerator 
{
	private static final int plantAbundancy = 60;
	private static final int waterAbundancy = 50;
	private static final int dirtyWaterAbundancy = 200;
	
	private static Random rnd;
	
	//blocks for each area
//	private static int[] earthSea = { 2 };
//	private static int[] earthIsland = { 3, 3, 3, 3, 3, 5 };
//	private static int[] earthInland = { 0, 1 };
//	private static int[] earthOres = { 6, 7, 8, 8 };
	
//	private static int countSurroundingBlocks(int blkSearchID, Dimension wld, int lvl, int x, int y)
//	{
//		int numberSurroundedBy = 0;
//		
//		if ( (x - 1 > -1) && (wld.allBlocks[x - 1][y].getEntityID_inMod() == blkSearchID) )
//		{
//			numberSurroundedBy++;
//		}
//		if ( (x + 1 < wld.allBlocks[lvl].length) && (wld.allBlocks[x + 1][y].getBlkID() == blkSearchID) )
//		{
//			numberSurroundedBy++;
//		}
//		if ( (y - 1 > -1) && (wld.allBlocks[x][y - 1].getBlkID() == blkSearchID) )	
//		{
//			numberSurroundedBy++;
//		}
//		if ( (y + 1 < wld.allBlocks[x].length) && (wld.allBlocks[x][y + 1].getBlkID() == blkSearchID) )
//		{
//			numberSurroundedBy++;
//		}
//		
//		return numberSurroundedBy;
//	}
	
	public static void generateLevel(Dimension wld, int[][] itemsInWld)
	{
		switch (wld.getWldID())
		{
			case 0://earth
				//createEarth(wld);
				createBasicWorld(wld);
				break;
			
			default:
				createBasicWorld(wld);
				break;
		}
		
	}
	
	
	private static void createBasicWorld(Dimension wld)
	{
		rnd = new Random();
		
		//make top level blocks second
		for (int x = 0; x < wld.getWorldWidth(); x++)
		{
			for (int y = 0; y < wld.getWorldHeight(); y++)
			{
				wld.allBlocks[x][y] = new GameEntity(-1, -1, 0, 0, 0, 0, "", x, y, 0, 0, 0, rnd, false);
				
				if (y > wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2))
				{
					if (y > wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 0.5f))
					{
						wld.allBlocks[x][y].setEntityID(-1, -1, null, wld, 0, 0, 0, 0, SpawnType.NoSpawn);//no block
					}
					else if (rnd.nextInt(5) > 1 && testThereIsBlockBelow_any(wld, x, y))
					{
						wld.allBlocks[x][y].setEntityID(-1, 0, null, wld, 0, 0, 1, 0, SpawnType.NoSpawn);//null block
					}
					else
					{
						wld.allBlocks[x][y].setEntityID(-1, -1, null, wld, 0, 0, 0, 0, SpawnType.NoSpawn);//no block
					}
					
				}
				else
				{
					wld.allBlocks[x][y].setEntityID(-1, 0, null, wld, 0, 0, 1, 0, SpawnType.NoSpawn);//null block
				}
			}
		}
		
		//second spawn wave
		//backwards
		for (int x = wld.getWorldWidth() - 1; x > -1; x--)
		{
			//for (int y = (int)(wld.getWorldHeight() - (wld.getWorldHeight() / 6f)); y < wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2); y++)
			for (int y = 0; y < wld.getWorldHeight(); y++)
			{
				if (y > wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2))
				{
					if (rnd.nextInt(5) > 1 && 0 < testSurroundingBlocks_sideToSide(-1, 0, wld, x, y))
					{
						wld.allBlocks[x][y].setEntityID(-1, 0, null, wld, 0, 0, 1, 0, SpawnType.NoSpawn);//null block
					}
				}
			}
		}
		//forwards
		for (int x = 0; x < wld.getWorldWidth(); x++)
		{
			for (int y = 0; y < wld.getWorldHeight(); y++)
			{
				if (y > wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2))
				{
					if (rnd.nextInt(5) > 1 && 0 < testSurroundingBlocks_sideToSide(-1, 0, wld, x, y))
					{
						wld.allBlocks[x][y].setEntityID(-1, 0, null, wld, 0, 0, 1, 0, SpawnType.NoSpawn);//null block
					}
				}
			}
		}
		
		
		//spawn creatures
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Steve", GameData.getCharacterSpawnX(wld) + 20, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Alex", GameData.getCharacterSpawnX(wld) + 80, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Jeff", GameData.getCharacterSpawnX(wld) + 120, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Daisy", GameData.getCharacterSpawnX(wld) - 50, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Mark", GameData.getCharacterSpawnX(wld) - 100, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
		wld.allCreatures.add(new GameEntity(-1, 0, 0, 0, 0, 0, "Herobrine", GameData.getCharacterSpawnX(wld) - 150, GameData.getCharacterSpawnY(wld), 15, 20, 0, rnd, false));
	}
	
	
	public static boolean spawnEntityBasedOnSpawnType(Dimension wld, int wld_x, int wld_y
			, int integratedModIndex, Mod integratedMod, GameEntityData ged, Random rnd)
	{
//		System.out.print("Try new block Mod: " + ged.getEntityIndex()
//		+ " ModID: " + integratedModIndex 
//		+ " Entity: " + ged.getEntityName()
//		+ " at X: " + wld_x 
//		+ " Y: " + wld_y
//		+ " Cur Stype: " + wld.allBlocks[wld_x][wld_y].getEntitySpawnType()
//		+ " New Stype: " + ged.getSpawnTypeID());
		
		//test current block
		if (replacementTable(wld.allBlocks[wld_x][wld_y].getEntitySpawnType(), ged.getSpawnTypeID(), wld.allBlocks[wld_x][wld_y], rnd, wld_x, wld_y, wld))
		{	
			//System.out.print(" - Set: True");
			wld.allBlocks[wld_x][wld_y].setEntityID(integratedModIndex, integratedMod, wld, ged, ged.getSpawnTypeID());
			
			spawnMass(wld, wld_x, wld_y, integratedModIndex, ged, rnd);
			return true;
		}
		//System.out.println("");
		return false;
	}
	
	private static void spawnMass(Dimension wld, int wld_x, int wld_y
			, int integratedModIndex, GameEntityData ged, Random rnd)
	{
		switch (ged.getSpawnTypeID())
		{
		case SpawnType.PlacedByPlayer: 				//Placed by player
			break;
		
		case SpawnType.NoSpawn:						//NoSpawn,
			break; 							
			
		case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
			break;
			
		case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
			break;				
			
		case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
			break;			
			
		case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
			break;				
			
		case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
			break;			
			
		case SpawnType.Block_RockVein:				//Block_RockVein,
			break;						
			
		case SpawnType.Block_OreVein:				//Block_OreVein,
			break;					
			
		case SpawnType.Block_Sea:					//Block_Sea,
			break;							
			
		case SpawnType.Block_Sludge:					//Block_Sludge,
			break;						
			
		case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
			for (int i = -10; i < 10; i++)
			{
				int rndDepth = rnd.nextInt(5);
				for (int j = 0; j < 50 + rndDepth; j++)
				{
					if (wld_x + i > -1 && wld_x + i < wld.allBlocks.length
							&& wld_y + j > -1 && wld_y + j < wld.allBlocks[0].length)
					{
						wld.allBlocks[wld_x][wld_y].setEntityID(integratedModIndex, null, wld, ged, ged.getSpawnTypeID());
					}
					else
					{
						break;
					}
				}
			}
			break;
			
		case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
			break; 
			
		case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
			break;			
			
		case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
			break;	
			
		case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
			break;
			
		case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
			break;		
			
		case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
			break;				
		}
	}
	
	private static boolean replacementTable(int currentSpawnType, int newSpawnType, GameEntity ge, Random rnd, int wld_x, int wld_y, Dimension wld)
	{
		//test how likely/if the new entity can replace the old one
		if (wld.allBlocks[wld_x][wld_y].getEntityID_inMod() < 0 && wld.allBlocks[wld_x][wld_y].getEntitysModID() < 0)
		{
			return false;
		}
		else if (currentSpawnType == SpawnType.NoSpawn && !(
				newSpawnType == SpawnType.NoSpawn || newSpawnType == SpawnType.Block_RockVein 
				|| newSpawnType == SpawnType.Block_OreVein || newSpawnType == SpawnType.Block_Sea
						|| newSpawnType == SpawnType.Block_Sludge || newSpawnType == SpawnType.Block_HighUndergroundPool
				|| newSpawnType == SpawnType.Block_DeepUndergroundPool || newSpawnType == SpawnType.Block_MultiBlockBush
				|| newSpawnType == SpawnType.Block_SingleBlockBush || newSpawnType == SpawnType.Block_SingleBlockPlant
				|| newSpawnType == SpawnType.Block_Tree_SaplingBlock || newSpawnType == SpawnType.Block_Tree_SpawnBlock))
		{
			return true;
		}
		
		int a, b, c;
		
		switch (newSpawnType)
		{
		case SpawnType.PlacedByPlayer: 				//Placed by player
			return false;
		
		case SpawnType.NoSpawn:						//NoSpawn,
			return false; 							
			
		case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
			
			//if block is on this level?
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2)) 
			&& wld_y <= (wld.getWorldHeight() - (wld.getWorldHeight() / 6f)) )
			{
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					return true; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
//					if ( (rnd.nextInt(5) > 1 && testThereIsBlockBelow_equal(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) 
//							)//|| (rnd.nextInt(6) < (1 + testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y))) ) 
//					{
//						return true;
//					}
					return false;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					return true;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return true; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return true; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return true; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return true; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return true; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return true; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return true;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
				
			}
			return false; 					
			
		case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
			//if block is on this level?
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 3)) 
			&& wld_y <= (wld.getWorldHeight() - (wld.getWorldHeight() / 6f) * 2) )
			{
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					return true; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return true;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					if ( (rnd.nextInt(20) == 1) 
							|| (rnd.nextInt(6) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
					{
						return true;
					}
					return false;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return true; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return true; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return true; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
				
			}
			return false; 					
			
		case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
			//if block is on this level?
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 4)) 
			&& wld_y <= (wld.getWorldHeight() - (wld.getWorldHeight() / 6f) * 3) )
			{
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					return true; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return true;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					return true;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					if ( (rnd.nextInt(20) == 1) 
							|| (rnd.nextInt(6) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
					{
						return true;
					}
					return false; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return true; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return true; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
				
			}
			return false; 				
			
		case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
			//if block is on this level?
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 5)) 
			&& wld_y <= (wld.getWorldHeight() - (wld.getWorldHeight() / 6f) * 4) )
			{
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					return true; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return true;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					return true;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return true; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					if ( (rnd.nextInt(20) == 1) 
							|| (rnd.nextInt(6) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
					{
						return true;
					}
					return false; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return true; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return true; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return true; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
				
			}
			return false; 					
			
		case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
			//if block is on this level?
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 6)) 
			&& wld_y <= (wld.getWorldHeight() - (wld.getWorldHeight() / 6f) * 5) )
			{
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					return true; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return true;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					return true;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return true; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return true; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					if ( (rnd.nextInt(20) == 1) 
							|| (rnd.nextInt(10) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
					{
						return true;
					}
					return false; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false;
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return true; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return true; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
				
			}
			return false; 				
			
		case SpawnType.Block_RockVein:				//Block_RockVein,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				return true; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ( (rnd.nextInt(80) == 1) 
						)//|| (rnd.nextInt(28) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				if ( (rnd.nextInt(70) == 1) 
						)//|| (rnd.nextInt(24) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				if ( (rnd.nextInt(60) == 1) 
						)//|| (rnd.nextInt(16) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				if ( (rnd.nextInt(50) == 1) 
						)//|| (rnd.nextInt(12) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				if ( (rnd.nextInt(80) == 1) 
						)//|| (rnd.nextInt(8) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				if ( (rnd.nextInt(100) == 1) 
						)//|| (rnd.nextInt(24) < testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y)) ) 
				{
					return true;
				}
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false; 						
			
		case SpawnType.Block_OreVein:				//Block_OreVein,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				if ( (rnd.nextInt(180) == 1) 
						)//|| (a > 0 && rnd.nextInt(10) < a) ) 
				{
					return true;
				}
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				if ( (rnd.nextInt(140) == 1) 
						)//|| (a > 0 && rnd.nextInt(8) < a) ) 
				{
					return true;
				}
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				if ( (rnd.nextInt(100) == 1) 
						)//|| (a > 0 && rnd.nextInt(6) < a) ) 
				{
					return true;
				}
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				if ( (rnd.nextInt(60) == 1) 
						)//|| (a > 0 && rnd.nextInt(6) < a) ) 
				{
					return true;
				}
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				if ( (rnd.nextInt(80) == 1) 
						)//|| (a > 0 && rnd.nextInt(12) < a) ) 
				{
					return true;
				}
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false; 						
			
		case SpawnType.Block_Sea:					//Block_Sea,
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 2f)) 
			&& wld_y <= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 1f)) )
			{
				System.out.println("try to spawn water0 cur spawn: " + currentSpawnType);
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					if ((rnd.nextInt(waterAbundancy) == 1))
					//|| (a > 0 && rnd.nextInt(4) != 1))
					//&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
					{
						return true;
					}
					return false; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					System.out.println("try to add sea block");
					if ((rnd.nextInt(waterAbundancy) == 1))
					{
						System.out.println("add sea block");
						return true;
					}
					return false;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
//					if ((rnd.nextInt(waterAbundancy) == 1)
//					//|| (a > 0 && rnd.nextInt(4) != 1))
//					&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
//					{
//						return true;
//					}
					return false;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return false; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return false; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return false; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
			}
			return false; 							
			
		case SpawnType.Block_Sludge:					//Block_Sludge,
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 3f)) 
			&& wld_y <= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 0f)) )
			{
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					if ((rnd.nextInt(dirtyWaterAbundancy * 10) == 1))
					{
						return true;
					}
					return false; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					if ((rnd.nextInt(dirtyWaterAbundancy * 10) == 1))
					{
						return true;
					}
					return false;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					if ((rnd.nextInt(dirtyWaterAbundancy * 10) == 1))
					{
						return true;
					}
					return false;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					if ((rnd.nextInt(dirtyWaterAbundancy * 10) == 1))
					{
						return true;
					}
					return false; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return false; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return false; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
			}
			return false; 						
			
		case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 3f)) 
			&& wld_y <= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 0f)) )
			{
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return false;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					return false; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					return false; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
			}
			return false; 		
			
		case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
			if ( wld_y >= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 6f)) 
			&& wld_y <= (wld.getWorldHeight() - ((wld.getWorldHeight() / 6f) * 3f)) )
			{
				a = testSurroundingBlocks_forOne(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y);
				switch (currentSpawnType)
				{
				case SpawnType.PlacedByPlayer: 				//Placed by player
					return false;
				
				case SpawnType.NoSpawn:						//NoSpawn,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false; 							
					
				case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
					return false;
					
				case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
					return false;					
					
				case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
					return false; 				
					
				case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false; 					
					
				case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
					if ((rnd.nextInt(dirtyWaterAbundancy) == 1))
					{
						return true;
					}
					return false; 				
					
				case SpawnType.Block_RockVein:				//Block_RockVein,
					return false; 						
					
				case SpawnType.Block_OreVein:				//Block_OreVein,
					return false; 						
					
				case SpawnType.Block_Sea:					//Block_Sea,
					return false; 							
					
				case SpawnType.Block_Sludge:					//Block_Sludge,
					return false; 						
					
				case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
					return false; 		
					
				case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
					return false;		 
					
				case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
					return false; 			
					
				case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
					return false; 			
					
				case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
					return false;			
					
				case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
					return false;			
					
				case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
					return false; 						
				}
			}
			return false;		
			
			
			
		case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2)
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false; 			
			
		case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false; 			
			
		case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false;			
			
		case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false;			
			
		case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
			switch (currentSpawnType)
			{
			case SpawnType.PlacedByPlayer: 				//Placed by player
				return false;
			
			case SpawnType.NoSpawn:						//NoSpawn,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2
						&& testThereIsBlockBelow_any(wld, wld_x, wld_y))
				{
					return true;
				}
				return false; 							
				
			case SpawnType.Block_Lv1_TopSoil:			//Block_Lv1_TopSoil,
				if ((rnd.nextInt(plantAbundancy) == 1) 
						&& !testThereIsBlockAbove(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) 
						&& testSurroundingBlocks_sideToSide(ge.getEntitysModID(), ge.getEntityID_inMod(), wld, wld_x, wld_y) < 2)
				{
					return true;
				}
				return false;
				
			case SpawnType.Block_Lv2_LowerGround:		//Block_Lv2_LowerGround,
				return false;					
				
			case SpawnType.Block_Lv3_MainRockBed:		//Block_Lv3_MainRockBed,
				return false; 				
				
			case SpawnType.Block_Lv4_HardRock:			//Block_Lv4_HardRock,
				return false; 					
				
			case SpawnType.Block_Lv5_HardestRock:		//Block_Lv5_HardestRock,
				return false; 				
				
			case SpawnType.Block_RockVein:				//Block_RockVein,
				return false; 						
				
			case SpawnType.Block_OreVein:				//Block_OreVein,
				return false; 						
				
			case SpawnType.Block_Sea:					//Block_Sea,
				return false; 							
				
			case SpawnType.Block_Sludge:					//Block_Sludge,
				return false; 						
				
			case SpawnType.Block_HighUndergroundPool:	//Block_HighUndergroundPool,
				return false; 		
				
			case SpawnType.Block_DeepUndergroundPool:	//Block_DeepUndergroundPool,
				return false;		 
				
			case SpawnType.Block_SingleBlockBush:		//Block_SingleBlockBush,
				return false; 			
				
			case SpawnType.Block_MultiBlockBush:		//Block_MultiBlockBush,
				return false; 			
				
			case SpawnType.Block_Tree_SpawnBlock:		//Block_Tree_SpawnBlock,
				return false;			
				
			case SpawnType.Block_Tree_SaplingBlock:		//Block_Tree_SaplingBlock,
				return false;			
				
			case SpawnType.Block_SingleBlockPlant:		//Block_Plant,
				return false; 						
			}
			return false; 						
			
			default:
				return false;
		}

	}
	
	public static int testSurroundingBlocks_forOne(int integratedModId, int entityDataId, Dimension wld, int wld_x, int wld_y)
	{
		int numberFound = 0;
		
		if (testBlockIsInBounds(wld, wld_x + 1, wld_y))
		{
			if (wld.allBlocks[wld_x + 1][wld_y].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x + 1][wld_y].getEntityID_inMod() == entityDataId)
			{
//				System.out.println("x+ en mod id: " + wld.allBlocks[wld_x + 1][wld_y].getEntitysModID());
//				System.out.println("x+ int mod id: " + integratedModId);
//				System.out.println("x+ en id in mod: " + wld.allBlocks[wld_x + 1][wld_y].getEntityID_inMod());
//				System.out.println("x+ en data id: " + entityDataId);
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x - 1, wld_y))
		{
			if (wld.allBlocks[wld_x - 1][wld_y].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x - 1][wld_y].getEntityID_inMod() == entityDataId)
			{
//				System.out.println("x- en mod id: " + wld.allBlocks[wld_x - 1][wld_y].getEntitysModID());
//				System.out.println("x- int mod id: " + integratedModId);
//				System.out.println("x- en id in mod: " + wld.allBlocks[wld_x - 1][wld_y].getEntityID_inMod());
//				System.out.println("x- en data id: " + entityDataId);
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x, wld_y + 1))
		{
			if (wld.allBlocks[wld_x][wld_y + 1].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x][wld_y + 1].getEntityID_inMod() == entityDataId)
			{
//				System.out.println("y+ en mod id: " + wld.allBlocks[wld_x][wld_y + 1].getEntitysModID());
//				System.out.println("y+ int mod id: " + integratedModId);
//				System.out.println("y+ en id in mod: " + wld.allBlocks[wld_x][wld_y + 1].getEntityID_inMod());
//				System.out.println("y+ en data id: " + entityDataId);
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x, wld_y - 1))
		{
			if (wld.allBlocks[wld_x][wld_y - 1].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x][wld_y - 1].getEntityID_inMod() == entityDataId)
			{
//				System.out.println("y- en mod id: " + wld.allBlocks[wld_x][wld_y - 1].getEntitysModID());
//				System.out.println("y- int mod id: " + integratedModId);
//				System.out.println("y- en id in mod: " + wld.allBlocks[wld_x][wld_y - 1].getEntityID_inMod());
//				System.out.println("y- en data id: " + entityDataId);
				numberFound++;
			}
		}
		
		return numberFound;
	}
	
	public static int testSurroundingBlocks_forAny(int integratedModId, int entityDataId, Dimension wld, int wld_x, int wld_y)
	{
		int numberFound = 0;
		
		if (testBlockIsInBounds(wld, wld_x + 1, wld_y))
		{
			if (wld.allBlocks[wld_x + 1][wld_y].getEntityID_inMod() > -1)
			{
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x - 1, wld_y))
		{
			if (wld.allBlocks[wld_x - 1][wld_y].getEntityID_inMod() > -1)
			{
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x, wld_y + 1))
		{
			if (wld.allBlocks[wld_x][wld_y + 1].getEntityID_inMod() > -1)
			{
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x, wld_y - 1))
		{
			if (wld.allBlocks[wld_x][wld_y - 1].getEntityID_inMod() > -1)
			{
				numberFound++;
			}
		}
		
		return numberFound;
	}
	
	public static boolean testThereIsBlockBelow_equal(int integratedModId, int entityDataId, Dimension wld, int wld_x, int wld_y)
	{	
		if (testBlockIsInBounds(wld, wld_x, wld_y - 1))
		{
			if (wld.allBlocks[wld_x][wld_y - 1].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x][wld_y - 1].getEntityID_inMod() == entityDataId)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean testThereIsBlockBelow_any(Dimension wld, int wld_x, int wld_y)
	{	
		if (testBlockIsInBounds(wld, wld_x, wld_y - 1))
		{
			if (wld.allBlocks[wld_x][wld_y - 1].getEntityID_inMod() > -1)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean testThereIsBlockAbove(int integratedModId, int entityDataId, Dimension wld, int wld_x, int wld_y)
	{	
		if (testBlockIsInBounds(wld, wld_x, wld_y + 1))
		{
			if (wld.allBlocks[wld_x][wld_y + 1].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x][wld_y + 1].getEntityID_inMod() == entityDataId)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static int testSurroundingBlocks_sideToSide(int integratedModId, int entityDataId, Dimension wld, int wld_x, int wld_y)
	{
		int numberFound = 0;
		
		if (testBlockIsInBounds(wld, wld_x + 1, wld_y))
		{
			if (wld.allBlocks[wld_x + 1][wld_y].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x + 1][wld_y].getEntityID_inMod() == entityDataId)
			{
				numberFound++;
			}
		}
		if (testBlockIsInBounds(wld, wld_x - 1, wld_y))
		{
			if (wld.allBlocks[wld_x - 1][wld_y].getEntitysModID() == integratedModId 
					&& wld.allBlocks[wld_x - 1][wld_y].getEntityID_inMod() == entityDataId)
			{
				numberFound++;
			}
		}
		
		return numberFound;
	}
	
	public static boolean testBlockIsInBounds(Dimension wld, int test_x, int test_y)
	{
		if (test_x < wld.getWorldWidth() && test_x > 0
				&& test_y < wld.getWorldHeight() && test_y > 0)
		{
			return true;
		}
		return false;
	}
	
	private static void createEarth(Dimension wld)
	{
		rnd = new Random();
		
//		//make top level blocks second
//		for (int x = 0; x < wld.getWorldWidth(); x++)
//		{
//			for (int y = 0; y < wld.getWorldHeight(); y++)
//			{
//				wld.allBlocks[x][y] = new Block(rnd);
//				wld.allBlocks[x][y].setBlkID( -1, wld );
//			}
//		}
//		
//		//make sea floor first
//		for (int x = 0; x < wld.getWorldWidth(); x++)
//		{
//			for (int y = 250; y < wld.getWorldHeight(); y++)
//			{
//				wld.allBlocks[x][y] = new Block(rnd);
//				
//				wld.allBlocks[x][y].setBlkID( earthSea[rnd.nextInt(earthSea.length)] , wld );
//			}
//		}
//		
//		
//		//make islands floor
//		for (int i = 0; i < 20; i++)
//		{
//			for (int x = 0; x < wld.getWorldWidth(); x++)
//			{
//				for (int y = 0; y < wld.getWorldHeight(); y++)
//				{
//					if ((rnd.nextInt(1000) == 1 && i < 2) || (countSurroundingBlocks(earthIsland[0], wld, 0, x, y) > rnd.nextInt(6)) )
//					{
//						wld.allBlocks[x][y].setBlkID( earthIsland[rnd.nextInt(earthIsland.length)] , wld );
//					}
//				}
//			}
//		}
//		
//		//make inland
//		int  temp1, temp2, temp3, temp4, tempID, tempOreID;
//		for (int i = 0; i < 7; i++)
//		{
//			tempID = rnd.nextInt(earthInland.length);
//			tempOreID = rnd.nextInt(earthOres.length);
//			
//			for (int x = 0; x < wld.getWorldWidth(); x++)
//			{
//				for (int y = 0; y < wld.getWorldHeight(); y++)
//				{
//					temp1 = countSurroundingBlocks(earthIsland[0], wld, 0, x, y);
//					temp2 = countSurroundingBlocks(earthInland[0], wld, 0, x, y);
//					temp3 = countSurroundingBlocks(earthInland[1], wld, 0, x, y);
//					temp4 = countSurroundingBlocks(earthSea[0], wld, 0, x, y);
//					
//					if ((temp1 == 4) && (rnd.nextInt(6) < 3) )
//					{
//						//try and build one type of land 
//						tempID = (rnd.nextInt(15) == 1)? rnd.nextInt(earthInland.length) : tempID;
//						wld.allBlocks[x][y].setBlkID( earthInland[tempID] , wld );
//					}
//					else if (temp3 * 2 > rnd.nextInt(8))
//					{
//						tempID = 1;
//						wld.allBlocks[x][y].setBlkID( earthInland[tempID] , wld );
//					}
//					else if (temp2 * 2 > rnd.nextInt(7) && temp4 == 0)
//					{
//						tempID = 0;
//						wld.allBlocks[x][y].setBlkID( earthInland[tempID] , wld );
//					}
//					
//					//if this is stone?
//					if (wld.allBlocks[x][y].getBlkID() == 1)
//					{
//						boolean addedOre = false;
//						
//						for (int j = 0; j < earthOres.length; j++)
//						{
//							if (rnd.nextInt(5) == 1 && countSurroundingBlocks(earthOres[j], wld, 0, x, y) > 0)
//							{
//								wld.allBlocks[x][y].setBlkID( earthOres[j] , wld );
//								if (rnd.nextInt(5) != 1)
//								{
//									wld.allBlocks[x][y].setBlkID( 1 , wld );
//								}
//								addedOre = true;
//								break;
//							}
//						}
//						
//						if(!addedOre && rnd.nextInt(20) == 1)
//						{
//							tempOreID = (rnd.nextInt(10) == 1)? rnd.nextInt(earthOres.length) : tempOreID;
//							wld.allBlocks[x][y].setBlkID( earthOres[tempOreID] , wld );
//							wld.allBlocks[x][y].setBlkID( 1 , wld );
//						}
//						else if (rnd.nextInt(10) == 1)
//						{
//							wld.allBlocks[x][y].setBlkID( 1 , wld );
//						}
//					}
//				}
//			}
//		}
//		
//		
//		/*//make top level blocks second
//		for (int x = 0; x < wld.getWorldWidth(); x++)
//		{
//			for (int y = 0; y < wld.getWorldHeight(); y++)
//			{
//				wld.allBlocks[1][x][y] = new Block(rnd);
//				wld.allBlocks[1][x][y].setBlkID( -1, wld );
//			}
//		}*/
//		
//		
//		
//		//spawn creatures
//		wld.allCreatures.add(new Creature(250, 250, 0, 0, 0, 0, 0, 20, 20));
//		wld.allCreatures.add(new Creature(255, 251, 0, 0, 0, 0, 0, 15, 20));
//		wld.allCreatures.add(new Creature(252, 256, 0, 0, 0, 0, 0, 20, 20));
//		wld.allCreatures.add(new Creature(246, 249, 0, 0, 0, 0, 0, 20, 30));
//		wld.allCreatures.add(new Creature(260, 260, 0, 0, 0, 0, 0, 50, 50));
	}
	
	
	
	
	
	private int[] getAbundancy(int[][] inWld)
	{
		//setup how abundant each ID in the world is
		int num = 0;
		for (int i = 0; i < inWld.length; i++)
		{
			num += inWld[i][1];
		}
		
		int[] abundancyIDs = new int[num];
		
		num = 0;
		for (int i = 0; i < inWld.length; i++)
		{
			for (int j = 0; j < inWld[i].length; j++)
			{
				abundancyIDs[num] = inWld[i][0];
			}
		}
		
		return abundancyIDs;
	}
	
}
