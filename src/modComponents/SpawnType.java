package modComponents;

public class SpawnType 
{
	public static final int PlacedByPlayer				= -111;
	public static final int NoSpawn 					= 0;
	
	//levels
	public static final int Block_Lv1_TopSoil 			= 101;
	public static final int Block_Lv2_LowerGround 		= 102;
	public static final int Block_Lv3_MainRockBed 		= 103;
	public static final int Block_Lv4_HardRock	 		= 104;
	public static final int Block_Lv5_HardestRock 		= 105;
	
	//veins
	public static final int Block_RockVein	 			= 201;
	public static final int Block_OreVein 				= 202;
	
	//liquids
	public static final int Block_Sea			 		= 301;
	public static final int Block_Sludge		 		= 302;
	public static final int Block_HighUndergroundPool 	= 303;
	public static final int Block_DeepUndergroundPool 	= 304;
	
	//Greenery
	public static final int Block_SingleBlockBush 		= 401;
	public static final int Block_MultiBlockBush 		= 402;
	public static final int Block_Tree_SpawnBlock 		= 403;
	public static final int Block_Tree_SaplingBlock 	= 404;
	public static final int Block_SingleBlockPlant 		= 405;
	
	//Building materials
	public static final int Block_SmoothStone	 		= 501;
	public static final int Block_CrackedStone	 		= 502;
	public static final int Block_Bricks		 		= 503;
	public static final int Block_Metal			 		= 504;
	public static final int Block_Wood			 		= 505;
	public static final int Block_Furniture		 		= 506;
	public static final int Block_AgedMaterial	 		= 507;
	public static final int Block_Food			 		= 508;
	
	public static final int[] AllSpawnTypes = 
		{
				NoSpawn,
				Block_Lv1_TopSoil,
				Block_Lv2_LowerGround,
				Block_Lv3_MainRockBed,
				Block_Lv4_HardRock,
				Block_Lv5_HardestRock,
				
				Block_RockVein,
				Block_OreVein,
				
				Block_Sea,
				Block_Sludge,
				Block_HighUndergroundPool,
				Block_DeepUndergroundPool,
				
				Block_SingleBlockBush,
				Block_MultiBlockBush,
				Block_Tree_SpawnBlock,
				Block_Tree_SaplingBlock,
				Block_SingleBlockPlant,
				
				Block_SmoothStone,
				Block_CrackedStone,
				Block_Bricks,
				Block_Metal,
				Block_Wood,
				Block_Furniture,
				Block_AgedMaterial,
				Block_Food,
		};
	
	public static int getAllSpawnPriorities(int spawnType) 
	{
		switch (spawnType)
		{
		case NoSpawn:
			return 0; 										//NoSpawn,
			
		case Block_Lv1_TopSoil:
			return 0; 										//Block_Lv1_TopSoil,
			
		case Block_Lv2_LowerGround:
			return 0;										//Block_Lv2_LowerGround,
			
		case Block_Lv3_MainRockBed:
			return 0; 										//Block_Lv3_MainRockBed,
			
		case Block_Lv4_HardRock:
			return 0; 										//Block_Lv4_HardRock,
			
		case Block_Lv5_HardestRock:
			return 0; 										//Block_Lv5_HardestRock,
			
		case Block_RockVein:
			return 0; 										//Block_RockVein,
			
		case Block_OreVein:
			return 0; 										//Block_OreVein,
			
		case Block_Sea:
			return 2; 										//Block_Sea,
			
		case Block_Sludge:
			return 2; 										//Block_Lake,
			
		case Block_HighUndergroundPool:
			return 2; 										//Block_HighUndergroundPool,
			
		case Block_DeepUndergroundPool:
			return 2;										 //Block_DeepUndergroundPool,
			
		case Block_SingleBlockBush:
			return 1; 										//Block_SingleBlockBush,
			
		case Block_MultiBlockBush:
			return 0; 										//Block_MultiBlockBush,
			
		case Block_Tree_SpawnBlock:
			return 0;										//Block_Tree_SpawnBlock,
			
		case Block_Tree_SaplingBlock:
			return 0;										//Block_Tree_SaplingBlock,
			
		case Block_SingleBlockPlant:
			return 1;										//Block_Plant,
			
		case Block_SmoothStone:
			return 0;
		case Block_CrackedStone:
			return 0;
		case Block_Bricks:
			return 0;
		case Block_Metal:
			return 0;
		case Block_Wood:
			return 0;
		case Block_Furniture:
			return 0;
		case Block_AgedMaterial:
			return 0;
		case Block_Food:
			return 0;
			
			default:
				return 0;
		}
	}
	
	public static String getAllSpawnNames(int spawnType) 
	{
		switch (spawnType)
		{
		case NoSpawn:
			return "No Spawning"; 							//NoSpawn,
			
		case Block_Lv1_TopSoil:
			return "Level 1: Top Soil"; 					//Block_Lv1_TopSoil,
			
		case Block_Lv2_LowerGround:
			return "Level 2: Lower Ground";					//Block_Lv2_LowerGround,
			
		case Block_Lv3_MainRockBed:
			return "Level 3: Main Rock Bed"; 				//Block_Lv3_MainRockBed,
			
		case Block_Lv4_HardRock:
			return "Level 4: Hard Rock"; 					//Block_Lv4_HardRock,
			
		case Block_Lv5_HardestRock:
			return "Level 5: Hardest Rock"; 				//Block_Lv5_HardestRock,
			
		case Block_RockVein:
			return "Vein: Rock Vein"; 						//Block_RockVein,
			
		case Block_OreVein:
			return "Vein: Ore Vein"; 						//Block_OreVein,
			
		case Block_Sea:
			return "Liquids: Sea"; 							//Block_Sea,
			
		case Block_Sludge:
			return "Liquids: Sludge"; 						//Block_Sludge,
			
		case Block_HighUndergroundPool:
			return "Liquids: High Underground Pool"; 		//Block_HighUndergroundPool,
			
		case Block_DeepUndergroundPool:
			return "Liquids: Deep Underground Pool";		 //Block_DeepUndergroundPool,
			
		case Block_SingleBlockBush:
			return "Greenery: Single Block Bush"; 			//Block_SingleBlockBush,
			
		case Block_MultiBlockBush:
			return "Greenery: Multi Block Bush"; 			//Block_MultiBlockBush,
			
		case Block_Tree_SpawnBlock:
			return "Greenery: Tree Spawn Block";			//Block_Tree_SpawnBlock,
			
		case Block_Tree_SaplingBlock:
			return "Greenery: Tree Sapling Block";			//Block_Tree_SaplingBlock,
			
		case Block_SingleBlockPlant:
			return "Greenery: Plant"; 						//Block_Plant,
			
		case Block_SmoothStone:
			return "Smooth Stone";
			
		case Block_CrackedStone:
			return "Cracked Stone";
			
		case Block_Bricks:
			return "Bricks";
			
		case Block_Metal:
			return "Metal";
			
		case Block_Wood:
			return "Wood";
			
		case Block_Furniture:
			return "Furniture";
			
		case Block_AgedMaterial:
			return "Aged Material";
			
		case Block_Food:
			return "Food";
			
			default:
				return "";
		}
	}
}
