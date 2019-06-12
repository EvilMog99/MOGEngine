package gameMenus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import entities.Camera;
import entities.Entity;
import gameRunner.AssetLoader;
import gameRunner.ClientManager;
import gameRunner.GameData;
import gameRunner.KeyboardListener;
import gameRunner.MouseListener;
import guiOperations.GuiEvents;
import guiOperations.GuiPanel;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.displayManager;
import serverFiles.Client;
import serverFiles.ConnectionManager;
import serverFiles.DataPacket;
import serverFiles.GameBackend;
import shaders.StaticShader;
import toolbox.JFrameIO;
import toolbox.ScalingTools;
import toolbox.Time;
import toolbox.UI;

public class MainMenu 
{
	static final float GRID_OFFSET_X = -15f;
	static final float GRID_OFFSET_Y = -32f - 36f;
	
	static final float TILE_SCALE = 1f;
	static final float TILE_WIDTH = TILE_SCALE * 0.5f;
	static final float TILE_HEIGHT = TILE_SCALE * 0.5f;
	static final float TILE_DISTANCE = -3f;
	
	static final float GM_TILE_WIDTH = 134f;
	static final float GM_TILE_HEIGHT = 100f;
	static final float GM_TILE_WIDTH_HALF = 82f;
	static final float GM_TILE_HEIGHT_HALF = 50f;
	
	static int prepEntityNo = 4000;
	static Entity[] allPreparedEntities;
	
	
	static float basicTile_z = -5;
	
	static Texture[][][][] allBlockImgs;
	
	static int[][][] titleScreenBlocks;
	
	static int[][][] makeScreenBlocks()
	{
		int[][][] retBlocks = new int[15][15][4];
		
		Random rnd = new Random();
		
		for (int lvl = 0; lvl < 2; lvl++)
		{
			for (int x = 0; x < 15; x++)
			{
				for (int y = 0; y < 15; y++)
				{
					retBlocks[x][y][0] = 0;//rnd.nextInt(allBlockImgs.length);
					retBlocks[x][y][1] = 0;
					retBlocks[x][y][2] = 0;
					retBlocks[x][y][3] = 0;
				}
			}
		}
		
		return retBlocks;
	}
	
	
	public static int runDisplay()
	{
		int retNextScene = -1;//-1 = no scene
		int previousGuiCommand;
		
		displayManager.createDisplay();
		
		//setup objects here
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		renderer.setup();
		
		KeyboardListener keyboard = new KeyboardListener();
		MouseListener mouse = new MouseListener();
		
		//setup list of GUI elements
		GuiPanel panel1_optionSelector = new GuiPanel(); 			panel1_optionSelector.visible = true;
		GuiPanel panel2_serverOpterationSelector = new GuiPanel(); 	panel2_serverOpterationSelector.visible = false; 
		
		GuiPanel[] allGuiPanels = new GuiPanel[] { 
				panel1_optionSelector,
				panel2_serverOpterationSelector
		};
		
		
		GuiTexture gui;
		gui = new GuiTexture(-4, -4, -1, loader.loadTextureID("guis/startButton", true), new Vector2f(0.0f, 0.6f), 0.2f, 0.1f);
		panel1_optionSelector.guis.add(gui);
		
//		gui = new GuiTexture(2, 2, -1, loader.loadTextureID("guis/joinButton", true), new Vector2f(0.5f, 0.6f), 0.2f, 0.1f);
//		panel1_optionSelector.guis.add(gui);
		
		gui = new GuiTexture(4, 4, -1, loader.loadTextureID("guis/makeModButton", true), new Vector2f(0.0f, 0.0f), 0.2f, 0.1f);
		panel1_optionSelector.guis.add(gui);
		
		gui = new GuiTexture(-2, -2, -1, loader.loadTextureID("guis/exitButton", true), new Vector2f(0f, -0.6f), 0.2f, 0.1f);
		panel1_optionSelector.guis.add(gui);
		
		
		gui = new GuiTexture(1, 1, -1, loader.loadTextureID("guis/newFileButton", true), new Vector2f(-0.5f, 0.5f), 0.2f, 0.1f);
		panel2_serverOpterationSelector.guis.add(gui);
		
		gui = new GuiTexture(3, 3, -1, loader.loadTextureID("guis/openFileButton", true), new Vector2f(0.5f, 0.5f), 0.2f, 0.1f);
		panel2_serverOpterationSelector.guis.add(gui);
		
		gui = new GuiTexture(-3, -3, -1, loader.loadTextureID("guis/backButton", true), new Vector2f(0f, -0.5f), 0.2f, 0.1f);
		panel2_serverOpterationSelector.guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		GuiEvents guiEvents = new GuiEvents(displayManager.getDisplayWidth(), displayManager.getDisplayHeight());
		
		
		UI ui = new UI();
		
		ConnectionManager connentionManager;
		GameBackend game;
		Client client;
		ClientManager clientManager;

		
		
		//must draw vertices counter clockwise
		
		int[] indices = {
				0,1,3,	//Top Left Triangle
				3,1,2,	//Bottom Right Triangle
		};
		
		float[] vertices = {
				-TILE_WIDTH,TILE_HEIGHT,TILE_DISTANCE,	//V1
				-TILE_WIDTH,-TILE_HEIGHT,TILE_DISTANCE,	//V2
				TILE_WIDTH,-TILE_HEIGHT,TILE_DISTANCE,	//V3
				TILE_WIDTH,TILE_HEIGHT,TILE_DISTANCE	//V4
		};
		
		float[] textureCoords = {
				0,0,	//V1
				0,1,	//V2
				1,1,	//V3
				1,0,	//V4	
		};
		
		
		//prepare assets
		RawModel rawModel = loader.loadToVAO(vertices, textureCoords, indices);
		
		AssetLoader assets = new AssetLoader(rawModel, loader);
		//allBlockImgs = assets.loadAllBlocks("blocks", GameData.allBlockData);
		titleScreenBlocks = makeScreenBlocks();
		
		
		TexturedModel basicTexturedModel = assets.loadImageByID("blocks", "basic texture");
		/*Entity basicEntity = new Entity(texturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);*/
		
		int entityIndex = 0;
		allPreparedEntities = new Entity[prepEntityNo];
		for (int i = 0; i < allPreparedEntities.length; i++)
		{
			allPreparedEntities[i] = new Entity(basicTexturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);
		}	

		
		Camera mainCamera = new Camera();
		

		int runCount = 0;
		while(!Display.isCloseRequested() && retNextScene == -1)
		{
			//entity.increasePosition(0f, 0, -0.005f);
			//entity.increaseRotation(1f, 1, 0);
			
			Time.calculateDeltaTime();
			
			keyboard.testKeyboard();
			mouse.testMouse();

			for (int i = 0; i < allGuiPanels.length; i++)
			{
				if (allGuiPanels[i].visible)
				{
					guiEvents.testClick(allGuiPanels[i].guis, mouse, 0f, 0f);
				}
			}

			//System.out.println("cp: " + mouse.getClickProcessed());
			if (mouse.isGuiClicked() && mouse.getClickProcessed() == 0)
			{
				previousGuiCommand = mouse.getGuiCommand();
				System.out.println("mouse code: " + previousGuiCommand);
				switch (previousGuiCommand)
				{
					case -3:
						mouse.setClickProcessed(0.5f);
						allGuiPanels[0].visible = true;
						allGuiPanels[1].visible = false;
						previousGuiCommand = -1;
						break;
						
					case -4:
						mouse.setClickProcessed(0.5f);
						allGuiPanels[1].visible = true;
						allGuiPanels[0].visible = false;
						previousGuiCommand = -1;
						break;
					
					default:
						break;
				}
				
				if (previousGuiCommand > -3)
				{
					retNextScene = previousGuiCommand;
				}
			}
			
			
			renderer.prepare();
			//game logic
			
			
			shader.start();
			shader.loadViewMatrix(mainCamera);
			
			entityIndex = 0;
			
			for (int x = 0; x < 15; x++)
			{
				for (int y = 0; y < 15; y++)
				{
					if (titleScreenBlocks[x][y][0] != -1)
					{
//							allPreparedEntities[entityIndex].setTexture(
//									allBlockImgs[ titleScreenBlocks[lvl][x][y][0] ]
//									[ titleScreenBlocks[lvl][x][y][1] ]
//											[ titleScreenBlocks[lvl][x][y][2] ]
//													[ titleScreenBlocks[lvl][x][y][3] ]);
						
						
						//allPreparedEntities[entityIndex].setTexture();
						
						allPreparedEntities[entityIndex].position.x = x - 7;
						allPreparedEntities[entityIndex].position.y = y;//((y - 7) * 0.75f) + (lvl / 4f);
						allPreparedEntities[entityIndex].position.z = basicTile_z + (0 / 1000f);
						
						//renderer.render(allPreparedEntities[entityIndex], shader);
						
						entityIndex++;
					}
				}
			}

							
			shader.stop();
			
			for (int i = 0; i < allGuiPanels.length; i++)
			{
				if (allGuiPanels[i].visible)
				{
					guiRenderer.render(allGuiPanels[i].guis, 0f, 0f);
				}
			}

			//render GUI to screen

			//ui.drawString_toPixel(100, 50, "Main Menu", Color.black);
			
			displayManager.updateDisplay();
		}
		
		
		shader.cleanUp();
		loader.cleanUp();
		displayManager.closeDisplay();
		
		
		return retNextScene;
	}
}
