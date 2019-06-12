package gameRunner;

import org.lwjgl.Sys;

import modComponents.GameEntity;
import modComponents.SpawnType;

public class GameData 
{
	//entity values
	public static final float playerJumpSpeed_verticle = 0.1f * 2.5f;
	public static final float playerMaxFallSpeed_horizontal = -0.30f;
	public static final float playerAddFallSpeed_horizontal = -0.001f * 4;
	public static final float playerMaxWalkSpeed_verticle = 0.30f;
	public static final float playerAddWalkSpeed_verticle = 0.1f  * 1.5f;
	public static final float playerAddFriction_verticle = 0.5f;

	
	public static final int EntitiesDisplayedInInv = 40;

	public static final int GameMode_Normal = 0;
	public static final int GameMode_Create = 1;
	
	public static final int startingInvStack = 20;
	
//	
//	public static int getRandomNumber(int min, int max)
//	{
//		return min + (int)(Math.floor(Math.random() * (max - min) ));//max Num never reached
//	}
//	
	public static String removeEscapeCharacters(String orig)
	{
		char[] charArray = orig.toCharArray();
		String ret = "";
		for (int i = 0; i < charArray.length; i++)
		{
			if (charArray[i] == '\\')
			{
				ret += '/';
			}
			else
			{
				ret += charArray[i];
			}
		}
		

		return ret;
	}
//	
//	
//	public static int getBlockBreakTime(int index)
//	{
//		if (index > -1)
//		{
//			return allBlockData[index][3];
//		}
//		else
//		{
//			return 0;
//		}
//	}
//	
//	public static int[][] allBlockData = 
//		{
//				/*
//				 * 0 = number of textures, 
//				 * 1 = number of varieties,
//				 * 2 = number of animations, 
//				 * 3 = if its constantly processed 1=1frame,2=10frames,3=100frames, 
//				 * 4 = time to break it
//				 */
//				{ 2, 1, 1, 0,  1 },	//ID: 0 - Dirt
//				{ 5, 1, 1, 0,  3 },	//ID: 1 - Stone
//				{ 1, 1, 1, 3,  1 },	//ID: 2 - Water
//				{ 1, 1, 1, 0,  1 },	//ID: 3 - Sand
//				{ 4, 1, 1, 0,  2 },	//ID: 4 - Silt
//				{ 1, 1, 1, 0,  2 },	//ID: 5 - Clay
//				{ 3, 1, 1, 0,  5 },	//ID: 6 - Coal Ore
//				{ 3, 1, 1, 0,  5 },	//ID: 7 - Oil Field
//				{ 3, 1, 1, 0,  5 },	//ID: 8 - Iron Ore
//		};
//	
//	public static int[][] allUnbreakableBlocks = 
//		{
//				
//		};
	
	public static int[][] allPlayerSpriteData = 
		{
				/*number of textures, number of animations*/
				{ 1, 2 },	//ID: 0 - pl 1
		};
	
//	
//	public static String[] allCreatureNames =
//		{
//				"Cre 0", //ID: 0 - 
//		};
//	
//	
//	public static int[][] world_backgroundMusic = 
//		{
//				{ 1 }, 	//Earth
//				{ 0 }, 	//Mars
//		};

	public static void setEntityCollisions(GameEntity ge, int naturalSpawnType)
	{
		//test if it exists
		if (ge.getEntityID_inMod() > -1)
		{
			//if not solid
			if (naturalSpawnType == SpawnType.Block_SingleBlockPlant || naturalSpawnType == SpawnType.Block_Tree_SaplingBlock
					 || naturalSpawnType == SpawnType.Block_Furniture || naturalSpawnType == SpawnType.Block_Food)
			{
				ge.setHasCollisions(false);
				ge.setLiquid(false);
			}
			//if liquid
			else if (naturalSpawnType == SpawnType.Block_Sea || naturalSpawnType == SpawnType.Block_Sludge
					|| naturalSpawnType == SpawnType.Block_DeepUndergroundPool || naturalSpawnType == SpawnType.Block_HighUndergroundPool)
			{
				ge.setHasCollisions(false);
				ge.setLiquid(true);
			}
			else //is solid
			{
				ge.setHasCollisions(true);
				ge.setLiquid(false);
			}
		}
		else//entity is air
		{
			ge.setHasCollisions(false);
			ge.setLiquid(false);
		}
	}
	
	public static float calculateFillValue(int naturalSpawnType)
	{
		if (naturalSpawnType == SpawnType.Block_Sea || naturalSpawnType == SpawnType.Block_Sludge
				 || naturalSpawnType == SpawnType.Block_DeepUndergroundPool || naturalSpawnType == SpawnType.Block_HighUndergroundPool)	
		{
			return 100f;
		}
		return 0f;
	}
	
	public static int getCharacterSpawnX(Dimension wld)
	{
		return wld.getWorldWidth() / 2;
	}
	public static int getCharacterSpawnY(Dimension wld)
	{
		return wld.getWorldHeight() - (wld.getWorldHeight() / 4) + 10;
	}
	
	
}
