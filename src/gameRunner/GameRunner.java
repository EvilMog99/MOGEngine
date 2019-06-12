package gameRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import entities.Camera;
import entities.Entity;
import entities.PlayerFile;
import guiOperations.GuiEvents;
import guiOperations.GuiObject;
import guiOperations.GuiPanel;
import guis.GuiRenderer;
import guis.GuiTexture;
import modComponents.ClientIntegratedMod;
import modComponents.GameEntityData;
import modComponents.IntegratedModData;
import modComponents.Mod;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.displayManager;
import serverFiles.AdminServerCommandsFc;
import serverFiles.Client;
import serverFiles.ConnectionManager;
import serverFiles.DataPacket;
import serverFiles.GameBackend;
import serverFiles.MessageToClientFs;
import serverFiles.MessageToServerFc;
import serverFiles.ModStorage;
import serverFiles.SendObject;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Animator;
import toolbox.BasicFunctions;
import toolbox.ClientAnimator;
import toolbox.GameGuiCommands;
import toolbox.GameGuiTextures;
import toolbox.Saving;
import toolbox.UI;

/*By Christopher Deane*/
public class GameRunner 
{
	//display settings
	static int screenWidth = 1;
	static int screenHeight = 1;
	static final float GRID_OFFSET_X = -15f;//-25f;
	static final float GRID_OFFSET_Y = -32f - 36f;//-49;
	
	static final float TILE_SCALE = 0.5f;
	static final float TILE_WIDTH = TILE_SCALE * 0.5f;
	static final float TILE_HEIGHT = TILE_SCALE * 0.5f;
	static final float TILE_DISTANCE = -3f;
	
	static final float GM_ENTITY_SPACING = 0.15f;
	static final float GM_ENTITY_SCALE = 1.5f;
	static final float GM_TILE_WIDTH = 0.05f * GM_ENTITY_SCALE;//0.075f
	static final float GM_TILE_HEIGHT = 0.05f * GM_ENTITY_SCALE;
	static final float GM_TILE_WIDTH_HALF = GM_TILE_WIDTH / 2f;
	static final float GM_TILE_HEIGHT_HALF = GM_TILE_HEIGHT / 2f;
	
	static final float scrCentreX = 750f;
	static final float scrCentreY = 500f;
	
	private static final int BlocksInViewX = 23;
	private static final int BlocksInViewY = 15;
	
	private static final int BlocksInViewX_startVal = -BlocksInViewX / 2;
	private static final int BlocksInViewY_startVal = -BlocksInViewY / 2;
	private static final int BlocksInViewX_endVal = (BlocksInViewX / 2) + 1;
	private static final int BlocksInViewY_endVal = (BlocksInViewY / 2) + 1;

	static int animationGridStartingX = 0;
	static int animationGridStartingY = 0;
	
	static int serverPort = 8567;

	static int currentWorld = 0;
	//static Texture[][][][] allBlockImgs;
	
	//static Texture allCreatureSprites[][][][];
	static int allPlayerSprites[][][][];
	
	static int[] allHealthBarImgs;
	
	static int prepEntityNo = 2000;
	static List<GuiTexture> allPreparedGameTextures;
	static Entity blankEntity;
	
	static float basicTile_z = -5;
	
	static int basicBlankTexture;
	static int basicTransparentTexture;
	
	static boolean playerIsWalking = false;
	static int playersAnimationNo = 0;
	static int playersFrameNo = 0;
	static int playersFrameDivisionNo = 5;
	
	static int heldItem_modIndex = -1;
	static int heldItem_entityIndex = -1;
	
	private static Clip current_music;
	
	public static int startMainGame(Saving saver, String worldFileName, boolean hostServer, String ipAddress)
	{
		int retNextScene = 0;
		
		int lookingModIndex = 0;
		int modScrollIndex = 0;
		int entityScrollIndex = 0;
		
		
		PlayerFile playerFile = saver.getPlayerFile();
		
		displayManager.createDisplay();
		
		//setup objects here
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		renderer.setup();
		
		KeyboardListener keyboard = new KeyboardListener();
		MouseListener mouse = new MouseListener();
		
		UI ui = new UI();
		
		GameGuiTextures.loadGameGuiTextures(loader);
		
		//MusicHandler musicHandler = new MusicHandler();
		
		//setup list of GUI elements
//		GuiPanel invSelector = new GuiPanel();
//		GuiPanel inventory = new GuiPanel();
		GuiPanel gameBackground = new GuiPanel(); gameBackground.visible = true;
		GuiPanel gameTexturesOutput = new GuiPanel(); gameTexturesOutput.visible = true;
		GuiPanel clientItemListPanel = new GuiPanel(); clientItemListPanel.visible = false;
		GuiPanel clientOptionsPanel = new GuiPanel(); clientOptionsPanel.visible = true;
		GuiPanel modUploadPanel = new GuiPanel(); modUploadPanel.visible = true;
		GuiPanel adminPanel = new GuiPanel(); adminPanel.visible = false;
		GuiPanel viewUploadedModListPanel = new GuiPanel(); viewUploadedModListPanel.visible = false;
		
		GuiPanel uploadedModOverviewPanel = new GuiPanel(); uploadedModOverviewPanel.visible = false;
		
		GuiPanel notificationPanel = new GuiPanel(); notificationPanel.visible = true;
		
		GuiPanel[][] allGuiPanels = new GuiPanel[][] { 
/*layer  0*/	{ gameBackground }, //layer 0
/*layer  1*/	{ gameTexturesOutput }, //layer 1
/*layer  2*/	{  }, //layer 2
/*layer  3*/	{  }, //layer 3
/*layer  4*/	{  }, //layer 4
/*layer  5*/	{  }, //layer 5
/*layer  6*/	{ clientItemListPanel }, //layer 6
/*layer  7*/	{ clientOptionsPanel }, //layer 7
/*layer  8*/	{ viewUploadedModListPanel }, //layer 8
/*layer  9*/	{ uploadedModOverviewPanel }, //layer 9
/*layer 10*/	{ notificationPanel }, 
				
		};
		
		List<GuiTexture> uiEditorPanelButtons = new ArrayList<GuiTexture>();
		GuiPanel[] allGuiPanels_inToolBar = new GuiPanel[] { 
				viewUploadedModListPanel
		};
		GuiTexture[] allBufferedModListButtons = new GuiTexture[8];
		
		GuiPanel pnl;
		GuiTexture gui;
		
		
		//create gui
		
		//game background
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_InGame]
				, new Vector2f(0.35f, -0.25f), 2.2f, 1.5f);
		gameBackground.guis.add(gui);
		
		//notificationPanel
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Grey]
				, new Vector2f(0.00f, -0.00f), 0.5f, 0.25f
				, new GuiObject(null, "Remember to take regular breaks", 16, Color.black, 0));
		notificationPanel.guis.add(gui);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_CLICK_Notification_OK, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.00f, -0.20f), 0.10f, 0.03f
				, new GuiObject(null, "OK", 16, Color.black, 0));
		notificationPanel.guis.add(gui);
		
		//clientOptionsPanel
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]
				, new Vector2f(0f, 0.94f), 1.5f, 0.07f);
		clientOptionsPanel.guis.add(gui);
		
		clientOptionsPanel.panels.add(modUploadPanel);
		clientOptionsPanel.panels.add(adminPanel);
		
		//clientItemListPanel
		pnl = new GuiPanel(); 	pnl.visible = true;
		clientItemListPanel.panels.add(pnl);
		
		pnl = new GuiPanel(); 	pnl.visible = true;
		for (int i = 0; i < 40; i++)
		{
			gui = new GuiTexture(-1, -1, -1
					, basicTransparentTexture
					, new Vector2f(0.45f + ((i % 4) * 0.15f), 0.34f - ((int)(i / 4) * 0.15f)), 0.0f, 0.0f
					, new GuiObject(null, "", 16, Color.black, 4));
			pnl.guis.add(gui);
		}
		clientItemListPanel.panels.add(pnl);
		
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Grey]
				, new Vector2f(0.69f, -0.065f), 0.5f, 0.94f);
		clientItemListPanel.guis.add(gui);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_Refresh, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.50f, 0.83f), 0.10f, 0.03f
				, new GuiObject(null, "Refresh", 16, Color.black, 0));
		clientItemListPanel.guis.add(gui);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ClickEntity, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.80f, 0.83f), 0.11f, 0.03f
				, new GuiObject(null, "Null Block", 16, Color.black, 0));
		gui.setAdditionalNumbers(new int[] {-1, -1});
		clientItemListPanel.guis.add(gui);
		
		GuiTexture gt_button_inv_normalMode = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_NormalMode, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.50f, 0.75f), 0.15f, 0.03f
				, new GuiObject(null, "Normal Mode", 16, Color.black, 0));
		clientItemListPanel.guis.add(gt_button_inv_normalMode);
		
		GuiTexture gt_button_inv_createMode = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_CreateMode, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.80f, 0.75f), 0.15f, 0.03f
				, new GuiObject(null, "Create Mode", 16, Color.black, 0));
		clientItemListPanel.guis.add(gt_button_inv_createMode);
		
			//mod scroll
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ScrollModsLeft, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Left]
				, new Vector2f(0.41f, 0.60f), 0.05f, 0.1f);
		clientItemListPanel.guis.add(gui);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ScrollModsRight, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Right]
				, new Vector2f(0.95f, 0.60f), 0.05f, 0.1f);
		clientItemListPanel.guis.add(gui);
			
			//entity scroll
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ScrollEntitiesUp, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Up]
				, new Vector2f(0.70f, 0.45f), 0.5f, 0.02f);
		clientItemListPanel.guis.add(gui);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ScrollEntitiesDown, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Down]
				, new Vector2f(0.70f, -0.95f), 0.5f, 0.02f);
		clientItemListPanel.guis.add(gui);
		
		
		
		//mod upload panel
		GuiTexture gt_button_viewAdminOptions = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_ViewAdminOptions, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.80f, 0.96f), 0.20f, 0.02f
				, new GuiObject(null, "View Admin Options", 16, Color.black, 0));
		gt_button_viewAdminOptions.setVisible(false);
		modUploadPanel.guis.add(gt_button_viewAdminOptions);
		uiEditorPanelButtons.add(gt_button_viewAdminOptions);
		
		GuiTexture gt_button_uploadMod = new GuiTexture(GameGuiCommands.GUI_CLIENT_UPLOADMOD_Select, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.94f), 0.18f, 0.05f
				, new GuiObject(null, "Upload Mod", 24, Color.black, 0));
		modUploadPanel.guis.add(gt_button_uploadMod);
		uiEditorPanelButtons.add(gt_button_uploadMod);
		
		GuiTexture gt_button_openInventory = new GuiTexture(GameGuiCommands.GUI_CLIENT_OpenInv, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.50f, 0.94f), 0.18f, 0.05f
				, new GuiObject(null, "Open Inventory", 24, Color.black, 0));
		modUploadPanel.guis.add(gt_button_openInventory);
		uiEditorPanelButtons.add(gt_button_openInventory);
		
		gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_Close, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.75f, 0.94f), 0.18f, 0.05f
				, new GuiObject(null, "Close", 24, Color.black, 0));
		modUploadPanel.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		//admin panel
		gui = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_ViewNonAdminOptions, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.80f, 0.92f), 0.20f, 0.02f
				, new GuiObject(null, "View Normal Options", 16, Color.black, 0));
		adminPanel.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		GuiTexture gt_button_seeUploadMods = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.94f), 0.18f, 0.05f
				, new GuiObject(null, "Buffered Mods", 24, Color.black, 0));
		adminPanel.guis.add(gt_button_seeUploadMods);
		uiEditorPanelButtons.add(gt_button_seeUploadMods);
		
		GuiTexture gt_button_saveWorld = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SaveWorld, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.25f, 0.94f), 0.18f, 0.05f
				, new GuiObject(null, "Save Game", 24, Color.black, 0));
		adminPanel.guis.add(gt_button_saveWorld);
		uiEditorPanelButtons.add(gt_button_saveWorld);
		
		//uploaded mod list
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGreyX2]
				, new Vector2f(-0.50f, 0.355f), 0.4f, 0.52f);
		viewUploadedModListPanel.guis.add(gui);
			//buttons
		gui = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods_Up, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Up]
				, new Vector2f(-0.50f, 0.81f), 0.38f, 0.04f);
		viewUploadedModListPanel.guis.add(gui);
		
		GuiTexture bufferedModsList_index0 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.71f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 0", 20, Color.black, 0));
		bufferedModsList_index0.setAdditionalNumbers(new int[] { 0, -1, -1, -1 });
		allBufferedModListButtons[0] = bufferedModsList_index0;
		viewUploadedModListPanel.guis.add(bufferedModsList_index0);
		uiEditorPanelButtons.add(bufferedModsList_index0);
		
		GuiTexture bufferedModsList_index1 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.61f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 1", 20, Color.black, 0));
		bufferedModsList_index1.setAdditionalNumbers(new int[] { 1, -1, -1, -1 });
		allBufferedModListButtons[1] = bufferedModsList_index1;
		viewUploadedModListPanel.guis.add(bufferedModsList_index1);
		uiEditorPanelButtons.add(bufferedModsList_index1);
		
		GuiTexture bufferedModsList_index2 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.51f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 2", 20, Color.black, 0));
		bufferedModsList_index2.setAdditionalNumbers(new int[] { 2, -1, -1, -1 });
		allBufferedModListButtons[2] = bufferedModsList_index2;
		viewUploadedModListPanel.guis.add(bufferedModsList_index2);
		uiEditorPanelButtons.add(bufferedModsList_index2);
		
		GuiTexture bufferedModsList_index3 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.41f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 3", 20, Color.black, 0));
		bufferedModsList_index3.setAdditionalNumbers(new int[] { 3, -1, -1, -1 });
		allBufferedModListButtons[3] = bufferedModsList_index3;
		viewUploadedModListPanel.guis.add(bufferedModsList_index3);
		uiEditorPanelButtons.add(bufferedModsList_index3);
		
		GuiTexture bufferedModsList_index4 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.31f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 4", 20, Color.black, 0));
		bufferedModsList_index4.setAdditionalNumbers(new int[] { 4, -1, -1, -1 });
		allBufferedModListButtons[4] = bufferedModsList_index4;
		viewUploadedModListPanel.guis.add(bufferedModsList_index4);
		uiEditorPanelButtons.add(bufferedModsList_index4);
		
		GuiTexture bufferedModsList_index5 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.21f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 5", 20, Color.black, 0));
		bufferedModsList_index5.setAdditionalNumbers(new int[] { 5, -1, -1, -1 });
		allBufferedModListButtons[5] = bufferedModsList_index5;
		viewUploadedModListPanel.guis.add(bufferedModsList_index5);
		uiEditorPanelButtons.add(bufferedModsList_index5);
		
		GuiTexture bufferedModsList_index6 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.11f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 6", 20, Color.black, 0));
		bufferedModsList_index6.setAdditionalNumbers(new int[] { 6, -1, -1, -1 });
		allBufferedModListButtons[6] = bufferedModsList_index6;
		viewUploadedModListPanel.guis.add(bufferedModsList_index6);
		uiEditorPanelButtons.add(bufferedModsList_index6);
		
		GuiTexture bufferedModsList_index7 = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.50f, 0.01f), 0.38f, 0.04f
				, new GuiObject(null, "Buffered Mod 7", 20, Color.black, 0));
		bufferedModsList_index7.setAdditionalNumbers(new int[] { 7, -1, -1, -1 });
		allBufferedModListButtons[7] = bufferedModsList_index7;
		viewUploadedModListPanel.guis.add(bufferedModsList_index7);
		uiEditorPanelButtons.add(bufferedModsList_index7);
		
		gui = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods_Down, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Down]
				, new Vector2f(-0.50f, -0.09f), 0.38f, 0.04f);
		viewUploadedModListPanel.guis.add(gui);
		
		
		
		//selected uploaded mod data
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]
				, new Vector2f(0.00f, 0.00f), 1.5f, 1.0f);
		uploadedModOverviewPanel.guis.add(gui);
		
		GuiTexture gt_label_seeUploadedMods_modName = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.00f, 0.95f), 0f, 0f
				, new GuiObject(null, "Mod Name:", 36, Color.black, 0));
		uploadedModOverviewPanel.guis.add(gt_label_seeUploadedMods_modName);
		
		GuiTexture gt_label_seeUploadedMods_modNumberOfFiles = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.00f, 0.80f), 0f, 0f
				, new GuiObject(null, "File Count:", 36, Color.black, 0));
		uploadedModOverviewPanel.guis.add(gt_label_seeUploadedMods_modNumberOfFiles);
		
		
		//edit buttons
		GuiTexture gt_button_seeUploadedMods_closePanel = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_CloseModViewerPanel, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.80f, -0.80f), 0.2f, 0.04f
				, new GuiObject(null, "Close Window", 16, Color.black, 0));
		uploadedModOverviewPanel.guis.add(gt_button_seeUploadedMods_closePanel);
		uiEditorPanelButtons.add(gt_button_seeUploadedMods_closePanel);
		
		GuiTexture gt_button_seeUploadedMods_addMod = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_AddModToGame, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.70f, 0.60f), 0.3f, 0.04f
				, new GuiObject(null, "Add Mod To Game", 24, Color.black, 0));
		uploadedModOverviewPanel.guis.add(gt_button_seeUploadedMods_addMod);
		uiEditorPanelButtons.add(gt_button_seeUploadedMods_addMod);
		
		GuiTexture gt_button_seeUploadedMods_deleteMod = new GuiTexture(GameGuiCommands.GUI_CLIENT_ADMIN_DeleteMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.70f, 0.40f), 0.3f, 0.04f
				, new GuiObject(null, "Delete Mod", 24, Color.black, 0));
		uploadedModOverviewPanel.guis.add(gt_button_seeUploadedMods_deleteMod);
		uiEditorPanelButtons.add(gt_button_seeUploadedMods_deleteMod);
		
		
//		gui = new GuiTexture(-2, -1, -1, loader.loadTextureID("guis/Inv Earth Button"), new Vector2f(-0.5f, 0.8f), new Vector2f(0.10f, 0.15f));
//		invSelector.guis.add(gui);
		
//		invSelector.visible = true;
		

//		createGuiPanel(gui, inventory, loader, new int[] 	{ 0, 1, 4, 5,-1, 6 });	inventory.visible = true;	

		
		//gui = new GuiTexture(1, -1, loader.loadTextureID("blocks/Blk"+i+"Tx0An0F0"), new Vector2f(0.5f, -0.5f), new Vector2f(0.15f, 0.25f));
		//guis.add(gui);
		
		screenWidth = displayManager.getDisplayWidth();
		screenHeight = displayManager.getDisplayHeight();
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		GuiEvents guiEvents = new GuiEvents(screenWidth, screenHeight);
		
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
		
		allPlayerSprites = assets.loadAllPlayers("player skins", GameData.allPlayerSpriteData);
		
		allHealthBarImgs = assets.loadAllFrames_SpecifiedLength_ID("InGameImgs", "healthBar", 17);
		
		TexturedModel basicTexturedModel = assets.loadImageByID("BasicImages", "nullImage");
		basicBlankTexture = assets.loadTextureByID("BasicImages", "nullImage");//basicTexturedModel.getTexture().getID();//assets.loadImageAsTexture("BasicImages", "nullImage");
		basicTransparentTexture = assets.loadTextureByID("BasicImages", "transImage");
		/*Entity basicEntity = new Entity(texturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);*/
		
		int entityIndex = 0;
		allPreparedGameTextures = new ArrayList<GuiTexture>();
		for (int i = 0; i < prepEntityNo; i++)
		{
			allPreparedGameTextures.add(new GuiTexture(GameGuiCommands.Game_CLIENT_CLICKBLOCK_Left, GameGuiCommands.Game_CLIENT_CLICKBLOCK_Right
					, -1, basicBlankTexture, new Vector2f(0.5f, 0.5f), GM_TILE_WIDTH, GM_TILE_HEIGHT
					, new GuiObject(null, "", 16, Color.black, 2)));
			allPreparedGameTextures.get(i).setVisible(false);
			allPreparedGameTextures.get(i).setAdditionalNumbers(new int[2]);
		}
//		allPreparedGameTextures.add(new GuiTexture(GameGuiCommands.Game_CLIENT_CLICKBLOCK_Left, GameGuiCommands.Game_CLIENT_CLICKBLOCK_Right
//				, -1, basicBlankTexture, new Vector2f(0.5f, 0.5f), 0.05f, 0.05f));
		//setup game output
		gameTexturesOutput.guis = allPreparedGameTextures;
		boolean clickedGameBlock = false;
		boolean clickedGameBlockOnLeft = false;

		blankEntity = new Entity(basicTexturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);
		
		Camera mainCamera = new Camera();
		Vector2f blockOffset = new Vector2f(0f, 0f);
		
		//start server here!
		
		if (hostServer)
		{
			connentionManager = new ConnectionManager(serverPort);
			game = new GameBackend(connentionManager, saver, worldFileName);
			game.startGame();
		}
		else
		{
			connentionManager = new ConnectionManager();
			game = new GameBackend();//for a null server
		}
		
		int svrDataIndex = 0;//continuously existing tile reading index
		
		//setup client here
		//List<SendObject> bufferedRecievedModFiles = new ArrayList<SendObject>();
		client = new Client(ipAddress, serverPort, new DataPacket(), new DataPacket());
		
		
//		System.out.println("Sent: " + "pn" + playerFile.getRnd1()
//				+ "n" + playerFile.getRnd2()
//				+ "n" + playerFile.getRnd3()
//				+ "n" + playerFile.getRnd4()
//				+ "n" + playerFile.getUsername() + "e");//start connection with connection ID);
		
		client.send(new MessageToServerFc("pn" + playerFile.getRnd1()
				+ "n" + playerFile.getRnd2()
				+ "n" + playerFile.getRnd3()
				+ "n" + playerFile.getRnd4()
				+ "n" + playerFile.getUsername() + "e", false, null));//start connection with connection ID
		
		//client side mod stuff
		int loadingModIndex = -1;
		List<ClientIntegratedMod> allClientMods = new ArrayList<ClientIntegratedMod>();
		List<String> allRequiredModNames = new ArrayList<String>();
		List<Integer> allRequiredModFileLengths = new ArrayList<Integer>();
		
		clientManager = new ClientManager(client, allRequiredModNames, allRequiredModFileLengths);
		
		float receivedCliX, receivedCliY;
		
		
		int readPacketID;//which packet should be read
		
		int runCount = 0;
		
		Music m = new Music();
		current_music = m.getMusic();
		
		try {
			while(!Display.isCloseRequested())
			{
				//entity.increasePosition(0f, 0, -0.005f);
				//entity.increaseRotation(1f, 1, 0);
				
//				for (int i = 0; i < allGuiPanels.length; i++)
//				{
//					if (allGuiPanels[i].visible)
//					{
//						guiEvents.testClick(allGuiPanels[i].guis, mouse, 0f, 0f);
//					}
//				}
				
				//process raw mod files from server
//				if (bufferedRecievedModFiles.size() > 0)
//				{
//					for (int i = 0; i < bufferedRecievedModFiles.size(); i++)
//					{
//						if ()
//					}
//				}
				
				//process required mods
				if (loadingModIndex == -1 && allClientMods.size() < allRequiredModNames.size())//if there is another mod to load
				{
					System.out.println("Add recieved mod");
					loadingModIndex = allClientMods.size();
					allClientMods.add(new ClientIntegratedMod(allRequiredModNames.get(loadingModIndex), saver, loader));

					if (allClientMods.get(loadingModIndex).isLoadedModFromFile())//if the mod has been successfully loaded?
					{
						//mod has been successfully loaded from file
						System.out.println("! Successfully loaded REQUIRED mod from server: " + allClientMods.get(loadingModIndex).getModRefName());
						loadingModIndex = -1;//allow another mod to be loaded
					}
					System.out.println("loadModIndex: " + loadingModIndex);
				}
				else if (loadingModIndex != -1)//else if a mod is being downloaded from client?
				{
					if (allClientMods.get(loadingModIndex).isAskServerForMod())// && not asking for mod
					{
						allClientMods.get(loadingModIndex).setAskServerForMod(false);//server will be asked for mod
						System.out.println("Requesting: " + allRequiredModNames.get(loadingModIndex));
						System.out.println("Number of expected files: " + allRequiredModFileLengths.get(loadingModIndex));
						client.receivedMod = new ModStorage(allRequiredModNames.get(loadingModIndex), allRequiredModFileLengths.get(loadingModIndex));
						client.receivedModIsReady = true;
					}
					else if (!allClientMods.get(loadingModIndex).isAskServerForMod() 
							&& client.receivedMod.isFinishedLoadingMod())//&&not asking for mod //if a new mod has just been saved to client try to load files again
					{
						if (client.receivedModIsReady)
						{
							client.receivedModIsReady = false;
							System.out.println("mod fully recieved");
							
							boolean successfullySavedAllFiles = true;
							for (int i = 0; i < client.receivedMod.getAllModFiles_clientDownload().size(); i++)
							{
								System.out.println("File name " + client.receivedMod.getAllModFiles_clientDownload().get(i).fileName
										+ " length: " + client.receivedMod.getAllModFiles_clientDownload().get(i).modFile.length + " Bytes");
								if (!saver.save_mod_rawFileOnClient(client.receivedMod.getAllModFiles_clientDownload().get(i)))
								{
									System.out.println("Failed to save file: " + client.receivedMod.getAllModFiles_clientDownload().get(i).modFileName_ref);
									successfullySavedAllFiles = false;
									break;
								}
							}
							
							System.out.println("mod fully saved");
							
							allClientMods.get(loadingModIndex).retryModFiles(saver, loader);
							
							if (allClientMods.get(loadingModIndex).isLoadedModFromFile())//if the mod has been successfully loaded?
							{
								System.out.println("! Successfully loaded NEW mod from server: " + allClientMods.get(loadingModIndex).getModRefName());
								loadingModIndex = -1;//allow another mod to be loaded
							}
							else
							{
								System.out.println("mod failed to load");
							}
						}
					}
				}
				
				//input commands
				keyboard.testKeyboard();
				mouse.testMouse();
				guiEvents.resetGuiClicked();
				for (int i = 0; i < allGuiPanels.length; i++)
				{
					for (int j = 0; j < allGuiPanels[i].length; j++)
					{
						if (allGuiPanels[i][j].visible)
						{
							guiEvents.testClick(allGuiPanels[i][j].guis, mouse, allGuiPanels[i][j].position.x, allGuiPanels[i][j].position.y);
							
							BasicFunctions.testGuiLayers_click(allGuiPanels[i][j], guiEvents, mouse, allGuiPanels[i][j].position.x, allGuiPanels[i][j].position.y);
						}
					}
				}
				
				readPacketID = client.getUpdatedPacket();
				client.dataPacket[readPacketID].beingRead = true;

				
				//handle GUI elements

					//process received admin data
				if (client.dataPacket[readPacketID].isExistingAdminClientData())
				{
					gt_button_viewAdminOptions.setVisible(true);

					//commands for server data
					if (uploadedModOverviewPanel.visible)
					{
						//show selected mod's data
						client.adminServerCommandsFc.setGetModData(true);
						gt_label_seeUploadedMods_modName.getGuiObject().setText("Mod Name: " + client.dataPacket[readPacketID].getAdminClientData().selectedModData[0]);
						gt_label_seeUploadedMods_modNumberOfFiles.getGuiObject().setText("Number of Files: " + client.dataPacket[readPacketID].getAdminClientData().selectedModData[1]);
//						if (client.adminServerCommandsFc.getCommandID() == AdminServerCommandsFc.NoCommand)
//						{
//							client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.ViewModInDetail);
//							System.out.println("details are visible");
//						}
					}
					else
					{
						client.adminServerCommandsFc.setGetModData(false);
					}
					
					if (viewUploadedModListPanel.visible)
					{
						for (int i = 0; i < client.dataPacket[readPacketID].getAdminClientData().viewingModsBasicData.length; i++)
						{
							allBufferedModListButtons[i].getGuiObject().setText(client.dataPacket[readPacketID].getAdminClientData().viewingModsBasicData[i]);
						}
						
						
//						if (client.adminServerCommandsFc.getCommandID() == AdminServerCommandsFc.NoCommand)
//						{
//							client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.ViewModList);
//							System.out.println("list is visible");
//						}
					}
					else
					{
						client.adminServerCommandsFc.modsInViewIndex0 = -1;
					}
				}
				else
				{
					gt_button_viewAdminOptions.setVisible(false);
				}
				
				if (client.allModFiles_clientUpload.size() == 0)
				{
					gt_button_uploadMod.setVisible(true);
				}
				else
				{
					gt_button_uploadMod.setVisible(false);
				}

				//process confirm command
				client.adminServerCommandsFc.tryToResetCommandID(client.dataPacket[readPacketID].getServerConfirmCommand());
				//read events
				// negative commands are for the client, positive are for the server
				if (guiEvents.isGuiClicked() && mouse.getGuiCommand() < 0)
				{
					resetUiTempPanels(allGuiPanels_inToolBar);
					
					if (mouse.getGuiCommand() != -1)
					{
						resetUiEditorButtons(uiEditorPanelButtons);
					}

					switch (mouse.getGuiCommand())
					{
//					case -2://Inventory
//						//changeInventory(0, allInventoryGuiPanels);
//						break;
						
					case GameGuiCommands.GUI_CLIENT_UPLOADMOD_Select:
						System.out.println("select mod");
						
						//Get mod files
						//saver.
						client.allModFiles_clientUpload.clear();
						
						if (prepareModUpload(saver, loader, client.allModFiles_clientUpload))
						{
							gt_button_uploadMod.setVisible(false);
							
							client.setUploadClientMod(true); 
						}
						
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods:
						client.adminServerCommandsFc.modsInViewIndex0 = 0;
						viewUploadedModListPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods_Up:
						if (client.adminServerCommandsFc.modsInViewIndex0 > 0)
						{
							client.adminServerCommandsFc.modsInViewIndex0--;
						}
						viewUploadedModListPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_SeeUploadedMods_Down:
						client.adminServerCommandsFc.modsInViewIndex0++;
						viewUploadedModListPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_SelectUploadMod:
						client.adminServerCommandsFc.modRefName_change = client.dataPacket[readPacketID].getAdminClientData().viewingModsBasicData[mouse.getGuiExtraNumber(0)];
						uploadedModOverviewPanel.visible = true;
						clientOptionsPanel.visible = false;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_ViewAdminOptions:
						adminPanel.visible = true;
						modUploadPanel.visible = false;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_ViewNonAdminOptions:
						adminPanel.visible = false;
						modUploadPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_CloseModViewerPanel:
						client.adminServerCommandsFc.modRefName_change = "";
						//client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.NoCommand);
						uploadedModOverviewPanel.visible = false;
						clientOptionsPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_AddModToGame:
						client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.MOD_Add);
						System.out.println("Click add mod");
						uploadedModOverviewPanel.visible = false;
						clientOptionsPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_DeleteMod:
						client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.MOD_Delete);
						uploadedModOverviewPanel.visible = false;
						clientOptionsPanel.visible = true;
						break;
						
					case GameGuiCommands.GUI_CLIENT_ADMIN_SaveWorld:
						client.adminServerCommandsFc.setCommandID(AdminServerCommandsFc.SaveWorld);
						break;
						
					case GameGuiCommands.Game_CLIENT_CLICKBLOCK_Left:
						clickedGameBlock = true;
						clickedGameBlockOnLeft = true;
						//System.out.println("left click block x: " + mouse.getGuiExtraNumber(0) + " y: " + mouse.getGuiExtraNumber(1));
						break;
						
					case GameGuiCommands.Game_CLIENT_CLICKBLOCK_Right:
						clickedGameBlock = true;
						clickedGameBlockOnLeft = false;
						//System.out.println("right click block x: " + mouse.getGuiExtraNumber(0) + " y: " + mouse.getGuiExtraNumber(1));
						break;
						
					case GameGuiCommands.GUI_CLIENT_OpenInv:
						clientItemListPanel.visible = true;
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_Refresh:
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_Close:
						clientItemListPanel.visible = false;
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ScrollModsLeft:
						mouse.setClickProcessed(0.4f);
						if (modScrollIndex > 3)
						{
							modScrollIndex -= 4;
						}
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ScrollModsRight:
						mouse.setClickProcessed(0.4f);
						if (modScrollIndex < allClientMods.size() - 4)
						{
							modScrollIndex += 4;
						}
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ScrollEntitiesUp:
						mouse.setClickProcessed(0.4f);
						if (entityScrollIndex > 39)
						{
							entityScrollIndex -= 40;
						}
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ScrollEntitiesDown:
						mouse.setClickProcessed(0.4f);
						if (entityScrollIndex < allClientMods.get(modScrollIndex).getMod().getAllEntityData().length - 1)
						{
							entityScrollIndex += 40;
						}
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ClickMod:
						lookingModIndex = mouse.getGuiExtraNumber(0);
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_INV_ClickEntity:
						heldItem_modIndex = mouse.getGuiExtraNumber(0);
						heldItem_entityIndex = mouse.getGuiExtraNumber(1);
						refreshInvPanel(clientItemListPanel.panels.get(0), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
						clientManager.updateItemSelection(heldItem_modIndex, heldItem_entityIndex);
						break;
						
					case GameGuiCommands.Game_CLIENT_CLICK_Notification_OK:
						notificationPanel.visible = false;
						break;
						
						default:
							break;
					}
					
					mouse.resetGuiEvent();
				}
				
				refreshInvPanelNumbers(clientItemListPanel.panels.get(1), client.dataPacket[client.getUpdatedPacket()], allClientMods, modScrollIndex, lookingModIndex, entityScrollIndex);
				clientManager.updateInvView(lookingModIndex, entityScrollIndex);
				
				//update Inv Gui
				if (clientItemListPanel.visible)
				{
					switch (client.dataPacket[client.getUpdatedPacket()].getPlayerGameMode())
					{
						case 0://normal
							gt_button_inv_normalMode.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Green]);
							gt_button_inv_createMode.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]);
							break;
							
						case 1://create
							gt_button_inv_normalMode.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]);
							gt_button_inv_createMode.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Green]);
							break;
					}
				}
				
				//musicHandler.runMusic(client.dataPacket[readPacketID]);//run music
				
				//setup correct coordinates to draw blocks
				receivedCliX = client.dataPacket[readPacketID].getCliPositionX() / 200f;
				receivedCliY = client.dataPacket[readPacketID].getCliPositionY() / 200f;
//				mainCamera.position.x = receivedCliX;
//				mainCamera.position.y = receivedCliY;
				blockOffset.x = -receivedCliX * 2f;
				blockOffset.y = -receivedCliY * 2f;
				
				animationGridStartingX = client.dataPacket[readPacketID].getCliWorldPositionX() - BlocksInViewX_startVal;
				animationGridStartingY = client.dataPacket[readPacketID].getCliWorldPositionY() - BlocksInViewY_startVal;
				
				//mainCamera.position.x *= 1f;
				//mainCamera.position.y *= 0.75f;
				
				//work out players animation
				if (keyboard.isMoveLeft())
				{
					playerIsWalking = true;
					playersAnimationNo = 0;
				}
				else if (keyboard.isMoveRight())
				{
					playerIsWalking = true;
					playersAnimationNo = 1;
				}
				else
				{
					playerIsWalking = false;
				}
				
				clientManager.updateKeyboard(keyboard);
				if (clickedGameBlock)
				{
					clientManager.updateMouse(mouse, clickedGameBlock, clickedGameBlockOnLeft, mouse.getGuiExtraNumber(0), mouse.getGuiExtraNumber(1));
				}
				else
				{
					clientManager.updateMouse(mouse, clickedGameBlock, clickedGameBlockOnLeft, 0, 0);
				}
				clickedGameBlock = false;//reset value

				//reset game guis
				for (GuiTexture gt : allPreparedGameTextures)
				{
					gt.setVisible(false);
					gt.getGuiObject().setText("");
				}
				
				renderer.prepare();
				//game logic
				
				
				shader.start();
				shader.loadViewMatrix(mainCamera);
		
				//draw all blocks in view
				entityIndex = 0;
				svrDataIndex = 0;
				//client.dataPacket[readPacketID].viewedTiles[svrDataIndex][0] != -2
				
				
				for (int x = BlocksInViewX_startVal; x < BlocksInViewX_endVal; x++)
				{
					for (int y = BlocksInViewY_startVal; y < BlocksInViewY_endVal; y++)
					{
//						if (client.dataPacket[readPacketID].viewedTiles[svrDataIndex][1] != -1)
//						{	
							//allPreparedEntities[entityIndex].setModel(basicTexturedModel);
									
						entityIndex = drawTileToScreen(entityIndex, x, y, blockOffset
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][0]
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][1]
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][2]
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][3]
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][4]
								, getRunEntityFrameNo(x, y, allClientMods, client
										, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][0]
										, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][1]
										, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][2]
										, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][3]
										, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][4])
								, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][5]
								, client, readPacketID, svrDataIndex, allClientMods);
							
//							allPreparedEntities[entityIndex].setTexture(allBlockImgs[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][0] ]
//									[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][1] ]
//											[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][2] ]
//													[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][3] ]);
//							
//							allPreparedEntities[entityIndex].position.x = x;
//							allPreparedEntities[entityIndex].position.y = (y * 0.75f) + (lvl / 4f);
//							allPreparedEntities[entityIndex].position.z = basicTile_z + (lvl / 1000f);// + (0.00001f * y);
//							
//							renderer.render(allPreparedEntities[entityIndex], shader);
//							
//							entityIndex++;
							
							//System.out.println("x: " + allPreparedEntities[entityIndex].position.x 
								//	+ " y: " + allPreparedEntities[entityIndex].position.y);
							
							
//						}
//						else
//						{
//							
//						}
						svrDataIndex++;
					}
				}
				
				//draw this player 
				if (playerIsWalking)
				{
					if (((playersFrameNo + 1) / playersFrameDivisionNo) < allPlayerSprites
							[client.dataPacket[readPacketID].playersTextureNo][client.dataPacket[readPacketID].playersVariationNo]
									[playersAnimationNo].length)
					{
						playersFrameNo += 1;
					}
					else
					{
						playersFrameNo = 0;
					}
				}
				//client.dataPacket[readPacketID].playersAnimationNo
				//receivedCliX * 2f, receivedCliY * 2f
				entityIndex = drawPlayerToScreen(entityIndex, 0, 0, blockOffset, playerFile.getUsername(), Color.gray
						, -1
						, client.dataPacket[readPacketID].playersCreatureID, client.dataPacket[readPacketID].playersTextureNo
						, client.dataPacket[readPacketID].playersVariationNo, playersAnimationNo
						, (playersFrameNo / playersFrameDivisionNo)
						, client.dataPacket[readPacketID].playersHP_Percent
						, readPacketID, true);
				
				
				//draw other players
				for (int i = 0; i < client.dataPacket[readPacketID].otherPlayers_Int.length; i++)
				{
					if (client.dataPacket[readPacketID].otherPlayers_Int[i][0] != -1)
					{
						entityIndex = drawPlayerToScreen(entityIndex
								, client.dataPacket[readPacketID].otherPlayers_Int[i][0] / 100f
										, client.dataPacket[readPacketID].otherPlayers_Int[i][1] / 100f
												, blockOffset
												
								, client.dataPacket[readPacketID].otherPlayers_Str[i], Color.cyan
										
								, client.dataPacket[readPacketID].otherPlayers_Int[i][2]
										, client.dataPacket[readPacketID].otherPlayers_Int[i][3]
												, client.dataPacket[readPacketID].otherPlayers_Int[i][4]
														, client.dataPacket[readPacketID].otherPlayers_Int[i][5]
																, client.dataPacket[readPacketID].otherPlayers_Int[i][6]
																		, 0
																			, client.dataPacket[readPacketID].otherNPCs_Int[i][7]
								, readPacketID, false);
						
					}
					else
					{
						//System.out.println("no");
						break;
					}	
				}
				
				
				//draw other creatures
				for (int i = 0; i < client.dataPacket[readPacketID].otherNPCs_Int.length; i++)
				{
					if (client.dataPacket[readPacketID].otherNPCs_Int[i][0] != -1)
					{
						entityIndex = drawCreatureToScreen(entityIndex
								, (client.dataPacket[readPacketID].otherNPCs_Int[i][0] / 100f)
										, (client.dataPacket[readPacketID].otherNPCs_Int[i][1] / 100f)
												, blockOffset
										
								, client.dataPacket[readPacketID].otherNPCs_Str[i], Color.blue
												
								, client.dataPacket[readPacketID].otherNPCs_Int[i][2]
										, client.dataPacket[readPacketID].otherNPCs_Int[i][3]
												, client.dataPacket[readPacketID].otherNPCs_Int[i][4]
														, client.dataPacket[readPacketID].otherNPCs_Int[i][5]
																, client.dataPacket[readPacketID].otherNPCs_Int[i][6]
																		, 0
																			, client.dataPacket[readPacketID].otherNPCs_Int[i][7]
								, readPacketID);
					}
					else
					{
						//System.out.println("no");
						break;
					}	
				}
			

				shader.stop();
				
				//ui.drawString(100, 100, "Hello World! blah blah blah", Color.red);
				
				//render GUI to screen
				for (int i = 0; i < allGuiPanels.length; i++)
				{
					for (int j = 0; j < allGuiPanels[i].length; j++)
					{
						BasicFunctions.drawGuiLayers(allGuiPanels[i][j], guiRenderer, shader, ui, allGuiPanels[i][j].position.x, allGuiPanels[i][j].position.y, blankEntity, loader.getNullImage_data());
					}
				}
				
				
				client.dataPacket[readPacketID].beingRead = false;
	

				displayManager.updateDisplay();
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		
		if (hostServer)
		{
			connentionManager.close();
			game.endGame();
		}
		
		if (current_music != null)
		{
			current_music.stop();
			current_music.close();
		}
		
		//musicHandler.stop();
		
		clientManager.stopListening();
		client.close();
		
		guiRenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		displayManager.closeDisplay();
		
		return retNextScene;
	}
	
	private static int getRunEntityFrameNo(int x_offset, int y_offset, List<ClientIntegratedMod> allClientMods, Client client
			, int modID, int entityID, int textureNo, int variationNo, int animationNo)
	{
		if (modID > -1 && entityID > -1 && modID < allClientMods.size())
		{
			tempGED = allClientMods.get(modID).getMod().getAllEntityData();
			
			//System.out.println("mod: " + modID + " ent len: " + tempGED.length + " index: " + entityID);
			
			if (client.getAllEntityAnimations(animationGridStartingX + x_offset, animationGridStartingY + y_offset) + 1 
					< tempGED[entityID].getUploadedTextures()[textureNo][variationNo][animationNo].length)
			{
				client.setAllEntityAnimations(client.getAllEntityAnimations(animationGridStartingX + x_offset, animationGridStartingY + y_offset) + 1
						, animationGridStartingX + x_offset, animationGridStartingY + y_offset);
			}
			else
			{
				client.setAllEntityAnimations(0
						, animationGridStartingX + x_offset, animationGridStartingY + y_offset);
			}
			return client.getAllEntityAnimations(animationGridStartingX + x_offset, animationGridStartingY + y_offset);
		}
		
		return 0;
	}
	
	private static void refreshInvPanelNumbers(GuiPanel panel, DataPacket packet, List<ClientIntegratedMod> allClientMods, int modScrollIndex, int modViewIndex, int entityScrollIndex)
	{
		if (modScrollIndex > -1 && modScrollIndex < allClientMods.size())
		{
			if (entityScrollIndex > -1)
			{
				for (int i = 0; i < 40; i++)
				{
					if (entityScrollIndex + i < allClientMods.get(modViewIndex).getMod().getAllEntityData().length)
					{
						if (modViewIndex == packet.getInvSetToShow_modId() && entityScrollIndex == packet.getInvSetToShow_entityId())
						{
							panel.guis.get(i).getGuiObject().setText("" + packet.getInvDisplay(i));
						}
						else
						{
							panel.guis.get(i).getGuiObject().setText("");
						}
					}
					else
					{
						panel.guis.get(i).getGuiObject().setText("");
					}
				}
			}
		}
		
	}
	
	private static void refreshInvPanel(GuiPanel panel, DataPacket packet, List<ClientIntegratedMod> allClientMods, int modScrollIndex, int modViewIndex, int entityScrollIndex)
	{
		panel.guis.clear();
		
		if (modScrollIndex > -1 && modScrollIndex < allClientMods.size())
		{
			
			GuiTexture gui;
			for (int i = 0; i < 4; i++)
			{
				if (modScrollIndex + i < allClientMods.size())
				{
					gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ClickMod, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
							, new Vector2f(0.57f + ((i % 2f) * 0.22f), 0.65f - ((int)(i / 2) * 0.1f)), 0.15f, 0.04f
							, new GuiObject(null, allClientMods.get(modScrollIndex + i).getMod().getModName(), 20, Color.black, 0));
					gui.setAdditionalNumbers(new int[] { modScrollIndex + i });
					panel.guis.add(gui);
				}
				else
				{
					break;
				}
			}
			
			if (entityScrollIndex > -1)
			{
				for (int i = 0; i < 40; i++)
				{
					if (entityScrollIndex + i < allClientMods.get(modViewIndex).getMod().getAllEntityData().length)
					{
						if (modViewIndex == heldItem_modIndex && entityScrollIndex + i == heldItem_entityIndex)
						{
							gui = new GuiTexture(-1, -1, -1
									, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Green]
									, new Vector2f(0.45f + ((i % 4) * 0.15f), 0.34f - ((int)(i / 4) * 0.15f)), 0.06f, 0.06f);
							panel.guis.add(gui);
						}
						
						gui = new GuiTexture(GameGuiCommands.Game_CLIENT_INV_ClickEntity, -1, -1
								, allClientMods.get(modViewIndex).getMod().getAllEntityData()[entityScrollIndex + i].getUploadedTextures()[0][0][allClientMods.get(modViewIndex).getMod().getAllEntityData()[entityScrollIndex + i].getUploadedTextures()[0][0].length - 1][0].getTextureID()
								, new Vector2f(0.45f + ((i % 4) * 0.15f), 0.34f - ((int)(i / 4) * 0.15f)), 0.05f, 0.05f
								, new GuiObject(null, allClientMods.get(modViewIndex).getMod().getAllEntityData()[entityScrollIndex + i].getEntityName(), 16, Color.black, 3));
						gui.setAdditionalNumbers(new int[] { modViewIndex, entityScrollIndex + i });
						panel.guis.add(gui);
					}
					else
					{
						break;
					}
				}
			}
		}
		
	}
	
	private static boolean prepareModUpload(Saving saver, Loader loader
			, List<SendObject> allModFiles)
	{
		if (saver.open_modFiles_forUpload(loader, allModFiles))
		{
			return true;
		}
		
		return false;
	}

	
	private static GameEntityData[] tempGED;
	private static int tempTexture;
	private static int drawTileToScreen(int entityIndex, float x, float y, Vector2f blockOffset
			, int modID, int entityID, int textureNo, int variationNo, int animationNo, int frameNo, int health
			, Client client, int readPacketID, int svrDataIndex, List<ClientIntegratedMod> allClientMods)
	{
		if (entityID == -1)//if there is no block
		{
			allPreparedGameTextures.get(entityIndex).setTexture(basicTransparentTexture);//look at removing this texture being drawn!!
		}
		else if (modID == -1)//if there is no mod
		{
			//allPreparedEntities[entityIndex].setTexture(basicBlankTexture);
			allPreparedGameTextures.get(entityIndex).setTexture(basicBlankTexture);
		}
		else
		{
			//get image from mod
			if (modID < allClientMods.size())
			{
				tempGED = allClientMods.get(modID).getMod().getAllEntityData();
				if (entityID < tempGED.length)
				{
					tempTexture = tempGED[entityID].getUploadedTextures()[textureNo][variationNo][animationNo][frameNo].getTextureID();
					if (tempTexture != -1)
					{
						allPreparedGameTextures.get(entityIndex).setTexture(tempTexture);
					}
				}
			}
		}
		//allPreparedGameTextures.get(entityIndex).setPosition(x * GM_ENTITY_SCALE, y * GM_ENTITY_SCALE);
		allPreparedGameTextures.get(entityIndex).setAdditionalNumber((int)x, 0);
		allPreparedGameTextures.get(entityIndex).setAdditionalNumber((int)y, 1);
		allPreparedGameTextures.get(entityIndex).setScaledPosition((x + blockOffset.x) * GM_ENTITY_SPACING, (y + blockOffset.y) * GM_ENTITY_SPACING);
		allPreparedGameTextures.get(entityIndex).setVisible(true);
		
//		allPreparedGameTextures[entityIndex].position.x = x * GM_ENTITY_SCALE;
//		allPreparedGameTextures[entityIndex].position.y = y * GM_ENTITY_SCALE;//(y * 0.75f) + (lvl / 4f);
//		allPreparedGameTextures[entityIndex].position.z = basicTile_z + (lvl / 1000f) - (0.0001f * y);
		//renderer.render(allPreparedGameTextures[entityIndex], shader);
		entityIndex++;
		
		if (client.dataPacket[readPacketID].viewedTiles[svrDataIndex][4] < 999)
		{
			entityIndex = drawHealthBarToScreen(entityIndex, x, y, blockOffset, health);
		}
		return entityIndex;
	}
	
	
	private static int drawCreatureToScreen(int entityIndex, float x, float y, Vector2f blockOffset, String name, Color col
			, int modID, int creatureID, int textureNo, int variationNo, int animationNo, int frameNo, int HP_Percent, int readPacketID)
	{
		//System.out.println("creature x: " + x + " y: " + y);
		
		if (modID == -1)//if there is no mod
		{
			//!!!!!!!!
			allPreparedGameTextures.get(entityIndex).setTexture(allPlayerSprites[ textureNo ]
					[ variationNo ]
							[ animationNo ]
									[ frameNo ]);
		}
		else
		{
			//get image from mod
			//...
		}
		
		allPreparedGameTextures.get(entityIndex).setScaledPosition((x + blockOffset.x) * GM_ENTITY_SPACING, (y + blockOffset.y) * GM_ENTITY_SPACING);
		allPreparedGameTextures.get(entityIndex).setVisible(true);
		allPreparedGameTextures.get(entityIndex).getGuiObject().setText(name);
		allPreparedGameTextures.get(entityIndex).getGuiObject().setTextColor(col);
//		allPreparedEntities[entityIndex].position.x = x * GM_ENTITY_SCALE;
//		allPreparedEntities[entityIndex].position.y = y * GM_ENTITY_SCALE;
//		allPreparedEntities[entityIndex].position.z = basicTile_z + (lvl / 1000f) - (0.0001f * y);
		
		//renderer.render(allPreparedGameTextures[entityIndex], shader);
		
		entityIndex++;
		
		if (HP_Percent < 999)
		{
			entityIndex = drawHealthBarToScreen(entityIndex, x, y, blockOffset, HP_Percent);
		}
		
		return entityIndex;
	}
	
	private static int drawPlayerToScreen(int entityIndex, float x, float y, Vector2f blockOffset, String name, Color col
			, int modID, int creatureID, int textureNo, int variationNo, int animationNo, int frameNo, int HP_Percent, int readPacketID, boolean isAtCentre)
	{
		//System.out.println("creature x: " + x + " y: " + y);
		
		if (modID == -1)//if there is no mod
		{
			allPreparedGameTextures.get(entityIndex).setTexture(allPlayerSprites[ textureNo ]
					[ variationNo ]
							[ animationNo ]
									[ frameNo ]);
		}
		else
		{
			//get image from mod
			//...
		}
		
		allPreparedGameTextures.get(entityIndex).setVisible(true);
		if (isAtCentre)
		{
			allPreparedGameTextures.get(entityIndex).setScaledPosition(0f, 0f);
		}
		else
		{
			allPreparedGameTextures.get(entityIndex).setScaledPosition((x + blockOffset.x) * GM_ENTITY_SPACING, (y + blockOffset.y) * GM_ENTITY_SPACING);
		}
		allPreparedGameTextures.get(entityIndex).getGuiObject().setText(name);
		allPreparedGameTextures.get(entityIndex).getGuiObject().setTextColor(col);
//		allPreparedEntities[entityIndex].position.x = x * GM_ENTITY_SCALE;
//		allPreparedEntities[entityIndex].position.y = y * GM_ENTITY_SCALE;
//		allPreparedEntities[entityIndex].position.z = basicTile_z + (lvl / 1000f) - (0.0001f * y);
		
		//renderer.render(allPreparedGameTextures[entityIndex], shader);
		
		entityIndex++;
		
		if (HP_Percent < 999)
		{
			entityIndex = drawHealthBarToScreen(entityIndex, x, y, blockOffset, HP_Percent);
		}
		
		return entityIndex;
	}
	
	
	private static int drawHealthBarToScreen(int entityIndex, float x, float y, Vector2f blockOffset
			, float HP_Percent)
	{
		//System.out.println("creature x: " + x + " y: " + y);
		
		allPreparedGameTextures.get(entityIndex).setTexture(allHealthBarImgs[ (int)(((1000f - HP_Percent) / 1000f) * (allHealthBarImgs.length - 1)) ]);
		
		allPreparedGameTextures.get(entityIndex).setPosition((x + blockOffset.x) * GM_ENTITY_SPACING, (y + blockOffset.y) * GM_ENTITY_SPACING);
//		allPreparedEntities[entityIndex].position.x = x * GM_ENTITY_SCALE;
//		allPreparedEntities[entityIndex].position.y = y * GM_ENTITY_SCALE;//(y * 0.75f) + (lvl / 4f);
//		allPreparedEntities[entityIndex].position.z = basicTile_z + ((lvl + 1f) / 1000f) - (0.0001f * y);
		
		//renderer.render(allPreparedEntities[entityIndex], shader);
		
		entityIndex++;
		
		return entityIndex;
	}
	
	
	
//	private static float getWldX_FromScreenX(float x_loc, float Xoffset)
//	{
//		float temp_screenX = x_loc - scrCentreX - GRID_OFFSET_X - GM_TILE_WIDTH_HALF + GM_TILE_WIDTH + (Xoffset * GM_TILE_WIDTH);
//		
//		temp_screenX = ( (temp_screenX) / GM_TILE_WIDTH) - ((temp_screenX < 0)? 1 : 0 );
//		System.out.println("new x: " + temp_screenX);
//		return temp_screenX;
//	}
//	private static float getWldY_FromScreenY(float y_loc, float Yoffset)
//	{
//		float temp_screenY = y_loc - scrCentreY - GRID_OFFSET_Y + (Yoffset * GM_TILE_HEIGHT);
//		
//		temp_screenY = ( (temp_screenY) / GM_TILE_HEIGHT) - ((temp_screenY < 0)? 1 : 0 );
//		System.out.println("new x: " + temp_screenY);
//		return temp_screenY;
//	}
//	
//	
//	private static float getScreenX_FromWldX(float wldX, float Xoffset)
//	{
////		float temp_screenX = wldX - scrCentreX - GRID_OFFSET_X - GM_TILE_WIDTH_HALF + GM_TILE_WIDTH + (Xoffset * GM_TILE_WIDTH);
////		temp_screenX = ( (temp_screenX) / GM_TILE_WIDTH) - ((temp_screenX < 0)? 1 : 0 );
//		
//		float temp_screenX = wldX - (Xoffset * 100f);//( (wldX + ((wldX < 0)? 1 : 0 )) );
//		temp_screenX = (temp_screenX * 0.01f) * GM_TILE_WIDTH;
//		//System.out.println("new bfr x: " + temp_screenX);
//		temp_screenX = temp_screenX + scrCentreX + GRID_OFFSET_X + GM_TILE_WIDTH_HALF - GM_TILE_WIDTH ;//- (Xoffset );//* GM_TILE_WIDTH
//		
//		//System.out.println("new x: " + temp_screenX);
//		return temp_screenX;
//	}
//	private static float getScreenY_FromWldY(float wldY, float Yoffset)
//	{//= (y * 0.75f) + (lvl / 4f)
////		float temp_screenY = wldY - scrCentreY - GRID_OFFSET_Y + (Yoffset * GM_TILE_HEIGHT);
////		temp_screenY = ( (temp_screenY) / GM_TILE_HEIGHT) - ((temp_screenY < 0)? 1 : 0 );
//		
//		float temp_screenY = -wldY + (Yoffset * 100f);//(wldY * 0.75f);// - (1 / 4f);
//		temp_screenY = (temp_screenY * 0.01f) * GM_TILE_HEIGHT;
//		
//		//System.out.println("new y offset: " + Yoffset);
//		temp_screenY = temp_screenY + scrCentreY + GRID_OFFSET_Y + GM_TILE_HEIGHT_HALF - GM_TILE_HEIGHT;
//		//System.out.println("new y: " + temp_screenY);
//		return temp_screenY;
//	}
	
	private static void resetUiEditorButtons(List<GuiTexture> uiEditorPanelButtons)
	{
		for (GuiTexture gt : uiEditorPanelButtons)
		{
			gt.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]);
		}
	}
																																																																									/*By Christopher Deane*/
	private static void resetUiTempPanels(GuiPanel[] guiPanels)
	{
		for (GuiPanel guiPanel : guiPanels)
		{
			guiPanel.visible = false;
		}
	}
	
}

