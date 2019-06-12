package gameRunner;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;



public class AssetLoader 
{
	private boolean foundFile;
	
	private RawModel rawModel;
	private Loader loader;
	
	public AssetLoader(RawModel rawModel, Loader loader) {
		super();
		this.rawModel = rawModel;
		this.loader = loader;
	}

	
	//to load all player images
	public int[][][][] loadAllPlayers(String subFolder, int[][] frameData)
	{
		int[][][][] ret = new int[frameData.length][][][];
		
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = loadAllVarieties(subFolder, "Pl" + i, frameData[i][0], frameData[i][1]);
		}
		
		return ret;
	}
	
	
	//to load all entity texture packs
	public int[][][][][] loadAllEntities(String subFolder, int[][] blockData)
	{
		int[][][][][] ret = new int[blockData.length][][][][];
		
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = loadAllTextures(subFolder, "Blk" + i, blockData[i][0], blockData[i][1], blockData[i][2]);
		}
		
		return ret;
	}
	
	//to load all entity varieties
	public int[][][][] loadAllTextures(String subFolder, String fileName, int noTextures, int noVarieties, int noAnimations)
	{
		int[][][][] ret = new int[noTextures][][][];
		
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = loadAllVarieties(subFolder, "Tx" + i, noVarieties, noAnimations);
		}
		
		return ret;
	}
	
	
	
	private int[][][] loadAllVarieties(String subFolder, String fileName, int noVarieties, int noAnimations)
	{
		int[][][] ret = new int[noVarieties][][];
		
		for (int i = 0; i < noVarieties; i++)
		{
			ret[i] = loadAllAnimations(subFolder, fileName + "Vr" + i, noAnimations);
		}
		
		return ret;
	}
	
	private int[][] loadAllAnimations(String subFolder, String fileName, int noAnimations)
	{
		int[][] ret = new int[noAnimations][];
		
		for (int i = 0; i < noAnimations; i++)
		{
			ret[i] = loadAllFrames_NoLength(subFolder, fileName + "An" + i);
		}
		
		return ret;
	}
	
	public int[] loadAllFrames_NoLength(String subFolder, String fileName)
	{
		List<Integer> ret = new ArrayList<Integer>();
		
		for (int i = 0; i == i; i++)
		{
			int txtr = loadTextureByID(subFolder, fileName + "F" + i);

			if (txtr != -1)
			{
				ret.add(txtr);
			}
			else
			{
				break;
			}
		}
		
		int[] arr = new int[ret.size()];
		
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = ret.get(i);
		}
		
		return arr;
	}
	
	
	public int[] loadAllFrames_SpecifiedLength_ID(String subFolder, String fileName, int animationLength)
	{
		int[] ret = new int[animationLength];
		
		for (int i = 0; i < animationLength; i++)
		{
			ret[i] = loadTextureByID(subFolder, fileName + "F" + i);
		}
		
		return ret;
	}
	
	public Texture[] loadAllFrames_SpecifiedLength_Texture(String subFolder, String fileName, int animationLength)
	{
		Texture[] ret = new Texture[animationLength];
		
		for (int i = 0; i < animationLength; i++)
		{
			ret[i] = loadImageAsTexture(subFolder, fileName + "F" + i);
		}
		
		return ret;
	}
	
	
	/*public boolean loadImageTest(String subFolder, String img_name, List<TexturedModel> animation)
	{	
		boolean success = false;
		
		//TextureLoader.getTexture("PNG", new FileInputStream("assets/" + subFolder + "/" + img_name + ".png"));

		
		
		ModelTexture texture = new ModelTexture(loader.loadTexture(subFolder + "/" + img_name));
		
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);
		
		animation.add(texturedModel);
		
		success = true;


		return success;
	}*/
	
	
	public Texture loadImageAsTexture(String subFolder, String img_name)
	{

		Texture texture = loader.loadTexture(subFolder + "/" + img_name, true);
		//TexturedModel texturedModel = new TexturedModel(rawModel, texture);

		if (texture == null)
		{
			foundFile = false;
			System.out.println("Failed to find file: " + img_name);
		}
		else
		{
			foundFile = true;
			System.out.println("Loaded: assets/" + subFolder + "/" + img_name + ".png");
		}
		
		return texture;
	}
	
	
	public TexturedModel loadImageByID(String subFolder, String img_name)
	{

		ModelTexture texture = new ModelTexture(loader.loadTextureID(subFolder + "/" + img_name, true));
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);

		System.out.println("Loaded: assets/" + subFolder + "/" + img_name + ".png");
		
		return texturedModel;
	}
	
	public int loadTextureByID(String subFolder, String img_name)
	{
		return loader.loadTextureID(subFolder + "/" + img_name, true);
	}
}
