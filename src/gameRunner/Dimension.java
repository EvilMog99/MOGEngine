package gameRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import modComponents.GameEntity;

public class Dimension implements Serializable
{
	private int worldWidth, worldHeight;
	private int wldID;

	public List<GameEntity> allCreatures;
	
	public GameEntity[][] allBlocks;
	
	public List<GameEntity> allFastActiveBlocks;
	public List<GameEntity> allMediumActiveBlocks;
	public List<GameEntity> allSlowActiveBlocks;
	
	public int lastProcessed_X = 0;
	public int lastProcessed_Y = 0;
	
	
	//constructor
	public Dimension(int width, int height, int ID)
	{
		worldWidth = width;
		worldHeight = height;
		
		allBlocks = new GameEntity[worldWidth][worldHeight];
		
		allCreatures = new ArrayList<GameEntity>();
		
		allFastActiveBlocks = new ArrayList<GameEntity>();
		allMediumActiveBlocks = new ArrayList<GameEntity>();
		allSlowActiveBlocks = new ArrayList<GameEntity>();
		
		LevelGenerator.generateLevel(this, new int[0][0]);//generate this level
		
		
	}
	
	
	
	//all getters and setters

	public int getWorldWidth() {
		return worldWidth;
	}


	public int getWorldHeight() {
		return worldHeight;
	}


	public int getWldID() {
		return wldID;
	}
	
}
