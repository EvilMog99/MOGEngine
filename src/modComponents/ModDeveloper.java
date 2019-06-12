package modComponents;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import entities.Camera;
import entities.Entity;
import gameRunner.AssetLoader;
import gameRunner.GameData;
import gameRunner.KeyboardListener;
import gameRunner.MouseListener;
import gameRunner.MusicHandler;
import guiOperations.GuiAnimation;
import guiOperations.GuiEvents;
import guiOperations.GuiObject;
import guiOperations.GuiPanel;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.displayManager;
import serverFiles.Client;
import shaders.StaticShader;
import toolbox.Animator;
import toolbox.BasicFunctions;
import toolbox.GameGuiTextures;
import toolbox.JFrameIO;
import toolbox.Saving;
import toolbox.Time;
import toolbox.UI;

public class ModDeveloper {
	//display settings
	static final float GRID_OFFSET_X = -15f;
	static final float GRID_OFFSET_Y = -32f - 36f;
	
	static final float TILE_SCALE = 1f;
	static final float TILE_WIDTH = TILE_SCALE * 0.5f;
	static final float TILE_HEIGHT = TILE_SCALE * 0.5f;
	static final float TILE_DISTANCE = -3f;
	
	static final float GM_TILE_WIDTH = 134f;
	static final float GM_TILE_HEIGHT = 134f;
	static final float GM_TILE_WIDTH_HALF = 82f;
	static final float GM_TILE_HEIGHT_HALF = 82f;
	
	static final float scrCentreX = 750f;
	static final float scrCentreY = 500f;

	static int serverPort = 8567;

	static int currentWorld = 0;
	static Texture[][][][] allBlockImgs;
	
	static Texture allCreatureSprites[][][][];
	
	static Texture[] allHealthBarImgs;
	
	static int prepEntityNo = 4000;
	//static Entity[] allPreparedEntities;
	static Entity blankEntity;
	
	static int playAnimationIndex;
	
	static float basicTile_z = -5;
	
	static boolean canViewMod = false;
	static boolean entityButtonsAreVisible = true;
	static Mod modBeingEdited;
	static ModData modData_fileInfo;
	
	
	static GuiPanel modObjectBuildPanel_structureComponents;
	static GuiPanel modObjectBuildPanel_entityAnimations;
	static GuiPanel modObjectBuildPanel_buildUI;
	static GuiPanel modObjectBuildPanel_tagLists;
	static GuiPanel modObjectBuildPanel_code;
	static GuiPanel scrollNavigationPanel;
	
	//Mover panel
	static GuiPanel frameMoverPanel = new GuiPanel();
	
	//static GuiTexture testgui = new GuiTexture(-1, -1, -1, 0, new Vector2f(0, 0), new Vector2f(0, 0));
	
	//gui command IDs
	private static int lastCommand = 0;
	
	private static final int GUICLICK_FILE = 	-101;
	private static final int GUICLICK_EDIT = 	-102;
	private static final int GUICLICK_CREATE = 	-103;
	
	private static final int GUICLICK_FILE_OpenMod = 	-201;
	private static final int GUICLICK_FILE_NewMod = 	-202;
	private static final int GUICLICK_FILE_SaveMod = 	-203;
	private static final int GUICLICK_FILE_SaveAsMod = 	-204;
	
	//no edit button yet -300
	private static final int GUICLICK_EDIT_Entity = 			-301;
	
	private static final int GUICLICK_EDIT_Entity_ScrollUp = 	-351;
	private static final int GUICLICK_EDIT_Entity_ScrollDown = 	-352;
	private static final int GUICLICK_EDIT_Entity_Select = 		-353;
	
	private static final int GUICLICK_CREATE_Creature = 		-401;
	private static final int GUICLICK_CREATE_Block = 			-402;
	private static final int GUICLICK_CREATE_Structure = 		-403;
	private static final int GUICLICK_CREATE_Dimension = 		-404;
	
	private static final int GUICLICK_CREATE_Block_BlankBlock =	-421;
	private static final int GUICLICK_CREATE_Block_Material = 	-422;
//	private static final int GUICLICK_CREATE_Block_Bush = 		-423;
//	private static final int GUICLICK_CREATE_Block_Ore = 		-424;
//	private static final int GUICLICK_CREATE_Block_Decore = 	-425;
//	private static final int GUICLICK_CREATE_Block_Machine = 	-426;
//	private static final int GUICLICK_CREATE_Block_Pipe = 		-427;
	
	
	private static final int GUICLICK_CREATE_Structure_BlankStructure =	-451;
	private static final int GUICLICK_CREATE_Structure_Statue = 		-452;
	
	private static final int GUICLICK_CREATE_Dimension_EmptySpace = 	-481;
	
	private static final int GUICLICK_CREATE_Block_NewTexture1 = 		-501;
	private static final int GUICLICK_CREATE_Block_NewTexture2 = 		-502;
	private static final int GUICLICK_CREATE_Block_NewTexture3 = 		-503;
	private static final int GUICLICK_CREATE_Block_NewTexture4 = 		-504;
	private static final int GUICLICK_CREATE_Block_NewTexture5 = 		-505;
	private static final int GUICLICK_CREATE_Block_NewTexture6 = 		-506;
	private static final int GUICLICK_CREATE_Block_NewTexture7 = 		-507;
	private static final int GUICLICK_CREATE_Block_NewTexture8 = 		-508;
	private static final int GUICLICK_CREATE_Block_NewTexture9 = 		-509;
	private static final int GUICLICK_CREATE_Block_NewTexture10 =	 	-510;
	
	private static final int GUICLICK_TEXTURE_ADD_Frame =			 	-601;
	private static final int GUICLICK_TEXTURE_COPY_Frame =			 	-602;
	private static final int GUICLICK_TEXTURE_REMOVE_Frame =		 	-603;
	private static final int GUICLICK_TEXTURE_ADD_Animation =		 	-604;
	private static final int GUICLICK_TEXTURE_REMOVE_Animation =	 	-605;
	private static final int GUICLICK_TEXTURE_ADD_Variety =			 	-606;
	private static final int GUICLICK_TEXTURE_REMOVE_Variety =		 	-607;
	private static final int GUICLICK_TEXTURE_ADD_Texture =			 	-608;
	private static final int GUICLICK_TEXTURE_REMOVE_Texture =		 	-609;
	
	private static final int GUICLICK_ButtonSymbol_Down =				-620;
	private static final int GUICLICK_ButtonSymbol_Up =	 				-621;
	private static final int GUICLICK_ButtonSymbol_Left =	 			-622;
	private static final int GUICLICK_ButtonSymbol_Right =	 			-623;
	private static final int GUICLICK_ButtonSymbol_DownLeft =			-624;
	private static final int GUICLICK_ButtonSymbol_UpLeft =	 			-625;
	private static final int GUICLICK_ButtonSymbol_DownRight =			-626;
	private static final int GUICLICK_ButtonSymbol_UpRight =	 		-627;
	
	private static final int GUICLICK_ANIMATION_Play =		 			-651;
	private static final int GUICLICK_ANIMATION_Stop =		 			-652;
	
	private static final int GUICLICK_EDITORNAVIGATION_EditAnimations =	-701;
	private static final int GUICLICK_EDITORNAVIGATION_EditUI =			-702;
	private static final int GUICLICK_EDITORNAVIGATION_EditTagLists =	-703;
	private static final int GUICLICK_EDITORNAVIGATION_EditCode =		-704;
	private static final int GUICLICK_EDITORNAVIGATION_SeeStructure =	-705;
	
	private static final int GUICLICK_EDITGUI_EnableInventory =			-721;
	private static final int GUICLICK_EDITGUI_DisableInventory=			-722;
	private static final int GUICLICK_EDITGUI_AddInvBasicSlot = 		-723;
	private static final int GUICLICK_EDITGUI_AddInvInput =				-724;
	private static final int GUICLICK_EDITGUI_AddInvOutput =			-725;
	private static final int GUICLICK_EDITGUI_AddProgressArrowRight =	-726;
	private static final int GUICLICK_EDITGUI_AddEnergyBar =			-727;
	private static final int GUICLICK_EDITGUI_AddDangerBar =			-728;
	private static final int GUICLICK_EDITGUI_AddLabel =				-729;
	private static final int GUICLICK_EDITGUI_RemoveElement =			-730;
	
	
	private static final int GUICLICK_EDITGUI_AddElementToPanel =		-751;
	private static final int GUICLICK_EDITGUI_EditElementInPanel =		-752;
	
	
	
	private static final int GUICLICK_EDITCODE_ShowAllSpawnTypes=		-901;
	private static final int GUICLICK_EDITCODE_SelectSpawnType=			-902;
	private static final int GUICLICK_EDITCODE_SpawnType_ScrollUp=		-903;
	private static final int GUICLICK_EDITCODE_SpawnType_ScrollDown=	-904;
	
	
	private static final int SELECTIONID_Blank = 					-1;
	private static final int SELECTIONID_NewGuiElement = 			0;
	
	private static final String[] uiElementDefaultNames = new String[] {
			"", "", "", 
			"InvSlot", "InvInput", "InvOutput",
			"ProgressArrow", "EnergyBar", "DangerBar",
			"Label", "shouldnt_be_shown", 
	};
	
	
	public static int startModDevelopment(Saving saver)
	{
		int retNextScene = 0;
		int previousGuiCommand;
		
		canViewMod = false;

		playAnimationIndex = 0;
		
		//String modFileName = "";
		
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
		
		GameGuiTextures.loadGameGuiTextures(loader);
		
		//variables
		int editScrollTopIndex = 0;
		int spawnTypeScrollTopIndex = 0;
		
		//All Entities in development
		GameEntityData tempGed;
		GameEntityData emptyGed = new GameEntityData("Null Block", -1, new int[] {0, 0, 0}, new UploadedTexture(loader.getNullImage_data(), loader.getNullImage_id())
				, false, new EntityGUIObject[0], 0, 1, 0, 0, 0, 0);
		GameEntityData[] tempArray = new GameEntityData[10];
		for (int i = 0; i < tempArray.length; i++)
		{
			tempArray[i] = null;
		}
		
		GameEntityData[] allGameEntitiesBeingDeveloped = tempArray;
		
		
		//setup list of GUI elements
		GuiPanel toolBar = new GuiPanel(); toolBar.visible = true;
		GuiPanel file_dropDown = new GuiPanel(); file_dropDown.visible = false;
		GuiPanel edit_dropDown = new GuiPanel(); edit_dropDown.visible = false;
		GuiPanel edit_dropDown_selectEntity = new GuiPanel(); edit_dropDown_selectEntity.visible = false;
		GuiPanel edit_dropDown_selectionEntities = new GuiPanel(); edit_dropDown_selectionEntities.visible = false;
		GuiPanel create_dropDown = new GuiPanel(); create_dropDown.visible = false;
		GuiPanel create_dropDown_creature = new GuiPanel(); create_dropDown_creature.visible = false;
		GuiPanel create_dropDown_block = new GuiPanel(); create_dropDown_block.visible = false;
		GuiPanel create_dropDown_structure = new GuiPanel(); create_dropDown_structure.visible = false;
		GuiPanel create_dropDown_dimension = new GuiPanel(); create_dropDown_dimension.visible = false;
		GuiPanel code_dropDown_selectSpawnTypes = new GuiPanel(); code_dropDown_selectSpawnTypes.visible = false;
		
		GuiPanel modObjectBuildPanel;
		
		
		GuiPanel modObjectBuildPanel_blankBlock = new GuiPanel(); modObjectBuildPanel_blankBlock.visible = false;
		
		modObjectBuildPanel_structureComponents = new GuiPanel(); modObjectBuildPanel_structureComponents.visible = false;
		modObjectBuildPanel_entityAnimations = new GuiPanel(); modObjectBuildPanel_entityAnimations.visible = false;
		modObjectBuildPanel_buildUI = new GuiPanel(); modObjectBuildPanel_buildUI.visible = false;
		modObjectBuildPanel_tagLists = new GuiPanel(); modObjectBuildPanel_tagLists.visible = false;
		modObjectBuildPanel_code = new GuiPanel(); modObjectBuildPanel_code.visible = false;
//		modObjectBuildPanel = new GuiPanel(); modObjectBuildPanel.visible = false;
//		modObjectBuildPanel_blankBlock.panels.add(modObjectBuildPanel);
		
		GuiPanel modObjectBuildPanel_buildUI_invPanel = new GuiPanel(); modObjectBuildPanel_buildUI_invPanel.visible = true;
		GuiPanel modObjectBuildPanel_buildUI_invElementNames = new GuiPanel(); modObjectBuildPanel_buildUI_invElementNames.visible = true;
		
	
		
		//GuiPanel  = new GuiPanel();
		
		GuiPanel frameNavigationPanel = new GuiPanel(); frameNavigationPanel.visible = true;
		scrollNavigationPanel = new GuiPanel(); scrollNavigationPanel.visible = true;
		GuiPanel frameNavigationPanel_navButtons = new GuiPanel(); frameNavigationPanel_navButtons.visible = true;
		
//		GuiTexture followMouseTexture = new GuiTexture(-1, -1, -1, allGuiButtonTextures[0][0], new Vector2f(-0f, 0f), new Vector2f(0.1f, 0.1f)); followMouseTexture.setVisible(true);
//		GuiPanel priorityGraphics = new GuiPanel(); priorityGraphics.visible = false;
//		priorityGraphics.guis.add(followMouseTexture);
		
		
		GuiPanel[][] allGuiPanels = new GuiPanel[][] { 
/*layer  0*/	{ modObjectBuildPanel_entityAnimations, modObjectBuildPanel_buildUI, 
				modObjectBuildPanel_tagLists, modObjectBuildPanel_code }, //layer 0
/*layer  1*/	{ frameNavigationPanel }, //layer 1
/*layer  2*/	{  }, //layer 2
/*layer  3*/	{  }, //layer 3
/*layer  4*/	{  }, //layer 4
/*layer  5*/	{  }, //layer 5
/*layer  6*/	{  }, //layer 6
/*layer  7*/	{  }, //layer 7
/*layer  8*/	{  }, //layer 8
/*layer  9*/	{  }, //layer 9
/*layer 10*/	{ toolBar, 
				file_dropDown, 
				edit_dropDown, edit_dropDown_selectEntity, edit_dropDown_selectionEntities, 
				create_dropDown, create_dropDown_block, create_dropDown_structure, create_dropDown_creature, create_dropDown_dimension }, 
				
		};
		
		GuiPanel[] allGuiPanels_inToolBar = new GuiPanel[] { 
				file_dropDown, 
				edit_dropDown, edit_dropDown_selectEntity, edit_dropDown_selectionEntities, 
				create_dropDown, create_dropDown_block, create_dropDown_structure, create_dropDown_creature, create_dropDown_dimension
		};
		
		GuiPanel[] allModObjectBuildPanels = new GuiPanel[] {
				modObjectBuildPanel_entityAnimations
		};
		
		List<GuiTexture> uiEditorPanelButtons = new ArrayList<GuiTexture>();
		
		GuiTexture enableEntityGuiPanel, disableEntityGuiPanel;
		
		GuiTexture[] makeVisibleWhenModLoads = new GuiTexture[2];
		List<GuiTexture> makeVisibleWhenEntityLoads = new ArrayList<GuiTexture>();
		
		GuiTexture gui;
		GuiPanel pnl;

		//toolBar
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(0f, 0.95f), 2f, 0.05f);
		toolBar.guis.add(gui);
		gui = new GuiTexture(GUICLICK_FILE, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.95f, 0.95f), 0.07f, 0.05f
				, new GuiObject(null, "File", 24, Color.black, 0));
		toolBar.guis.add(gui);
		gui = new GuiTexture(GUICLICK_EDIT, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.85f, 0.95f), 0.07f, 0.05f
				, new GuiObject(null, "Edit", 24, Color.black, 0));
		makeVisibleWhenModLoads[0] = gui;
		toolBar.guis.add(gui);
		gui = new GuiTexture(GUICLICK_CREATE, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.73f, 0.95f), 0.1f, 0.05f
				, new GuiObject(null, "Create", 24, Color.black, 0));
		makeVisibleWhenModLoads[1] = gui;
		toolBar.guis.add(gui);

		//file_dropDown
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.865f, 0.6f), 0.2f, 0.3f);
		file_dropDown.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_FILE_NewMod, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.865f, 0.845f), 0.19f, 0.045f
				, new GuiObject(null, "New Mod", 24, Color.black, 0));
		file_dropDown.guis.add(gui);
		gui = new GuiTexture(GUICLICK_FILE_OpenMod, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.865f, 0.745f), 0.19f, 0.045f
				, new GuiObject(null, "Open Mod", 24, Color.black, 0));
		file_dropDown.guis.add(gui);
		gui = new GuiTexture(GUICLICK_FILE_SaveMod, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.865f, 0.645f), 0.19f, 0.045f
				, new GuiObject(null, "Save", 24, Color.black, 0));
		file_dropDown.guis.add(gui);
		
//		//GUICLICK_FILE_SaveAsMod
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.865f, 0.545f), 0.19f, 0.045f
//				, new GuiObject(null, "Save As", 24, Color.darkGray, 0));
//		file_dropDown.guis.add(gui);
		
		//edit_dropDown
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.765f, 0.6f), 0.2f, 0.3f);
		edit_dropDown.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDIT_Entity, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.765f, 0.845f), 0.19f, 0.045f
				, new GuiObject(null, "Edit Entity", 24, Color.black, 0));
		edit_dropDown.guis.add(gui);

		
		//edit_dropDown_selectEntity
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.500f, 0.0f), 0.2f, 0.91f);
		edit_dropDown_selectEntity.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDIT_Entity_ScrollUp, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.500f, 0.845f), 0.19f, 0.045f
				, new GuiObject(null, "Scroll Up", 24, Color.black, 0));
		edit_dropDown_selectEntity.guis.add(gui);
		gui = new GuiTexture(GUICLICK_EDIT_Entity_ScrollDown, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.500f, -0.855f), 0.19f, 0.045f
				, new GuiObject(null, "Scroll Down", 24, Color.black, 0));
		edit_dropDown_selectEntity.guis.add(gui);

		edit_dropDown_selectionEntities = new GuiPanel();
		edit_dropDown_selectEntity.panels.add(edit_dropDown_selectionEntities);
		
		
		
		

		//create_dropDown
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.63f, 0.6f), 0.25f, 0.3f);
		create_dropDown.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_CREATE_Creature, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.63f, 0.845f), 0.24f, 0.045f
				, new GuiObject(null, "Create Creature", 24, Color.black, 0));
		create_dropDown.guis.add(gui);
		gui = new GuiTexture(GUICLICK_CREATE_Block, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.63f, 0.745f), 0.24f, 0.045f
				, new GuiObject(null, "Create Block", 24, Color.black, 0));
		create_dropDown.guis.add(gui);
		gui = new GuiTexture(GUICLICK_CREATE_Structure, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.63f, 0.645f), 0.24f, 0.045f
				, new GuiObject(null, "Create Structure", 24, Color.black, 0));
		create_dropDown.guis.add(gui);
		gui = new GuiTexture(GUICLICK_CREATE_Dimension, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.63f, 0.545f), 0.24f, 0.045f
				, new GuiObject(null, "Create Dimension", 24, Color.black, 0));
		create_dropDown.guis.add(gui);
		
		
		
		//create_dropDown - Create Block
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.33f, 0.5f), 0.2f, 0.3f);
		create_dropDown_block.guis.add(gui);
		gui = new GuiTexture(GUICLICK_CREATE_Block_BlankBlock, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.33f, 0.745f), 0.19f, 0.045f
				, new GuiObject(null, "Blank Block", 24, Color.black, 0));
		create_dropDown_block.guis.add(gui);
		//GUICLICK_CREATE_Block_Material
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.33f, 0.645f), 0.19f, 0.045f
//				, new GuiObject(null, "Material", 24, Color.darkGray, 0));
//		create_dropDown_block.guis.add(gui);
		
		//create_dropDown - Create Structure
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.33f, 0.4f), 0.2f, 0.3f);
		create_dropDown_structure.guis.add(gui);
		
		//GUICLICK_CREATE_Structure_BlankStructure
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.33f, 0.645f), 0.19f, 0.045f
//				, new GuiObject(null, "Blank Structure", 24, Color.darkGray, 0));
//		create_dropDown_structure.guis.add(gui);
		//GUICLICK_CREATE_Structure_Statue
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.33f, 0.545f), 0.19f, 0.045f
//				, new GuiObject(null, "Statue", 24, Color.darkGray, 0));
//		create_dropDown_structure.guis.add(gui);
		
		
		//create_dropDown - Create Dimension
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0], new Vector2f(-0.33f, 0.3f), 0.2f, 0.3f);
		create_dropDown_dimension.guis.add(gui);
		//GUICLICK_CREATE_Dimension_EmptySpace
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.33f, 0.545f), 0.19f, 0.045f
//				, new GuiObject(null, "Empty Space", 24, Color.darkGray, 0));
//		create_dropDown_dimension.guis.add(gui);
		
		//Energy type list
		
		
		
		//Material Tag
		

		//Structures entities
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGreyX2], new Vector2f(0.0f, 0.0f), 1.5f, 2.0f);
		modObjectBuildPanel_structureComponents.guis.add(gui);
		
		
		//Mod Object Panel - Entity Animations
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][1], new Vector2f(0.0f, 0.0f), 1.5f, 2.0f);
		modObjectBuildPanel_entityAnimations.guis.add(gui);
		
		
		//Mod Object Panel - Create UI
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGreyX2], new Vector2f(0.0f, 0.0f), 1.5f, 2.0f);
		modObjectBuildPanel_buildUI.guis.add(gui);
		
//		gui = new GuiTexture(GUICLICK_EDITGUI_AddInvInput, -1, -1, allGuiButtonTextures[GUITEXTURE_Background][GUITEXTURE_Background_DarkGrey]
//				, new Vector2f(0.15f, 0.87f), new Vector2f(0.85f, 0.06f)
//				, new GuiObject(null, "", Color.black, 0));
//		modObjectBuildPanel_buildUI.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddInvBasicSlot, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.55f, 0.87f), new Vector2f(0.145f, 0.05f)
				, new GuiObject(null, "Inventory Slot", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Inv, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Inv, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddInvInput, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.30f, 0.87f), new Vector2f(0.095f, 0.05f)
				, new GuiObject(null, "Input Slot", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Input, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Input, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddInvOutput, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(-0.10f, 0.87f), new Vector2f(0.095f, 0.05f)
				, new GuiObject(null, "Output Slot", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Output, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Output, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddProgressArrowRight, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.15f, 0.87f), new Vector2f(0.145f, 0.05f)
				, new GuiObject(null, "Process Arrow", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_ProgressRight, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_ProgressRight, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddEnergyBar, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.40f, 0.87f), new Vector2f(0.095f, 0.05f)
				, new GuiObject(null, "Energy Bar", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Energy, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Energy, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddDangerBar, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.60f, 0.87f), new Vector2f(0.095f, 0.05f)
				, new GuiObject(null, "Danger Bar", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Danger, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Danger, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_AddLabel, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.76f, 0.87f), new Vector2f(0.055f, 0.05f)
				, new GuiObject(null, "Label", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Label, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Label, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITGUI_RemoveElement, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]
				, new Vector2f(0.89f, 0.87f), new Vector2f(0.065f, 0.05f)
				, new GuiObject(null, "Delete", 24, Color.black, 0));
		gui.setAdditionalNumbers(new int[] { GameGuiTextures.GUITEXTURE_EntityInvGUI_Blank, 0, -1, -1 });
		gui.setSelectionNumbers(new int[] { SELECTIONID_NewGuiElement, GameGuiTextures.GUITEXTURE_EntityInvGUI_Blank, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(gui);
		uiEditorPanelButtons.add(gui);
		
		enableEntityGuiPanel = new GuiTexture(GUICLICK_EDITGUI_EnableInventory, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Green]
				, new Vector2f(-0.10f, 0.75f), new Vector2f(0.155f, 0.05f)
				, new GuiObject(null, "Enable Inventory", 24, Color.black, 0));
		enableEntityGuiPanel.setAdditionalNumbers(new int[] { -1, -1, -1, -1 });
		modObjectBuildPanel_buildUI.guis.add(enableEntityGuiPanel);
		
		disableEntityGuiPanel = new GuiTexture(GUICLICK_EDITGUI_DisableInventory, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Red]
				, new Vector2f(0.30f, 0.75f), new Vector2f(0.155f, 0.05f)
				, new GuiObject(null, "Disable Inventory", 24, Color.black, 0));
		disableEntityGuiPanel.setAdditionalNumbers(new int[] { -1, -1, -1, -1 });
		disableEntityGuiPanel.setVisible(false);
		modObjectBuildPanel_buildUI.guis.add(disableEntityGuiPanel);
		
		
		pnl = new GuiPanel(); pnl.visible = true;
		pnl.position = new Vector2f(0.15f, -0.15f);
		
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(0f, 0f), 1.2f, 0.8f);
		pnl.guis.add(gui);

		pnl.panels.add(modObjectBuildPanel_buildUI_invPanel);
		pnl.panels.add(modObjectBuildPanel_buildUI_invElementNames);
		
		modObjectBuildPanel_buildUI.panels.add(pnl);
		
		
		//Mod Object Panel - Tag Lists
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][1], new Vector2f(0.0f, 0.0f), 1.5f, 2.0f);
		modObjectBuildPanel_tagLists.guis.add(gui);
		
		//Mod Object Panel - Code
		pnl = new GuiPanel(); pnl.visible = true;
		pnl.position = new Vector2f(0.0f, 0.0f);
		
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_Grey]
				, new Vector2f(-0.50f, 0.80f), new Vector2f(0.155f, 0.05f)
				, new GuiObject(null, "Selected Spawn Type: ", 16, Color.black, 0));
		pnl.guis.add(gui);
		
		GuiTexture code_showAllSpawnTypes = new GuiTexture(GUICLICK_EDITCODE_ShowAllSpawnTypes, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGreyX2]
				, new Vector2f(-0.00f, 0.80f), new Vector2f(0.350f, 0.05f)
				, new GuiObject(null, "Spawn Type", 16, Color.black, 0));
		//code_showAllSpawnTypes.setAdditionalNumbers(new int[] { -1, -1, -1, -1 });
		pnl.guis.add(code_showAllSpawnTypes);
		
		//code_dropDown_selectSpawnTypes
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][0]
				, new Vector2f(-0.000f, 0.05f), new Vector2f(0.350f, 0.70f));
		code_dropDown_selectSpawnTypes.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITCODE_SpawnType_ScrollUp, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0]
				, new Vector2f(-0.000f, 0.690f), new Vector2f(0.340f, 0.05f)
				, new GuiObject(null, "Scroll Up", 20, Color.black, 0));
		code_dropDown_selectSpawnTypes.guis.add(gui);
		gui = new GuiTexture(GUICLICK_EDITCODE_SpawnType_ScrollDown, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0]
				, new Vector2f(-0.000f, -0.590f), new Vector2f(0.340f, 0.05f)
				, new GuiObject(null, "Scroll Down", 20, Color.black, 0));
		code_dropDown_selectSpawnTypes.guis.add(gui);
		
		code_dropDown_selectSpawnTypes.panels.add(new GuiPanel());//give it a GuiPanel to use for options
		pnl.panels.add(code_dropDown_selectSpawnTypes);
		//array of all spawn types
		
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[0][1], new Vector2f(0.0f, 0.0f), 1.5f, 2.0f);
		modObjectBuildPanel_code.guis.add(gui);
		
		modObjectBuildPanel_code.panels.add(pnl);
		
		//\\\\\\\
		
		//
		frameNavigationPanel.panels.add(scrollNavigationPanel);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_Left, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Left], new Vector2f(-0.65f, -0.05f), new Vector2f(0.05f, 0.86f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_Right, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Right], new Vector2f(0.95f, -0.05f), new Vector2f(0.05f, 0.86f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_Up, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Up], new Vector2f(0.15f, 0.86f), new Vector2f(0.75f, 0.05f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_Down, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Down], new Vector2f(0.15f, -0.96f), new Vector2f(0.75f, 0.05f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_UpLeft, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_UpLeft], new Vector2f(-0.65f, 0.86f), new Vector2f(0.05f, 0.05f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_DownLeft, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_DownLeft], new Vector2f(-0.65f, -0.96f), new Vector2f(0.05f, 0.05f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_UpRight, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_UpRight], new Vector2f(0.95f, 0.86f), new Vector2f(0.05f, 0.05f));
		scrollNavigationPanel.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_ButtonSymbol_DownRight, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_DownRight], new Vector2f(0.95f, -0.96f), new Vector2f(0.05f, 0.05f));
		scrollNavigationPanel.guis.add(gui);

		
		frameNavigationPanel.panels.add(frameNavigationPanel_navButtons);
		
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGreyX2], new Vector2f(-0.85f, -0.05f), new Vector2f(0.15f, 0.95f));
		frameNavigationPanel.guis.add(gui);
		
		//frameNavigationPanel.guis.add(followMouseTexture);
		
		//GUICLICK_EDITORNAVIGATION_SeeStructure
//		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(-0.85f, 0.84f), new Vector2f(0.14f, 0.045f)
//				, new GuiObject(null, "See Structure", 24, Color.darkGray, 0));
//		makeVisibleWhenEntityLoads.add(gui);
//		frameNavigationPanel_navButtons.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITORNAVIGATION_EditAnimations, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(-0.85f, 0.69f), new Vector2f(0.14f, 0.045f)
				, new GuiObject(null, "Edit Animations", 24, Color.black, 0));
		makeVisibleWhenEntityLoads.add(gui);
		frameNavigationPanel_navButtons.guis.add(gui);
		
		//GUICLICK_EDITORNAVIGATION_EditUI
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(-0.85f, 0.54f), new Vector2f(0.14f, 0.045f)
				, new GuiObject(null, "Edit User Interface", 24, Color.darkGray, 0));
		makeVisibleWhenEntityLoads.add(gui);
		frameNavigationPanel_navButtons.guis.add(gui);
		
		//GUICLICK_EDITORNAVIGATION_EditTagLists
		gui = new GuiTexture(-1, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(-0.85f, 0.39f), new Vector2f(0.14f, 0.045f)
				, new GuiObject(null, "Edit Tags", 24, Color.darkGray, 0));
		makeVisibleWhenEntityLoads.add(gui);
		frameNavigationPanel_navButtons.guis.add(gui);
		
		gui = new GuiTexture(GUICLICK_EDITORNAVIGATION_EditCode, -1, -1, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey], new Vector2f(-0.85f, 0.24f), new Vector2f(0.14f, 0.045f)
				, new GuiObject(null, "Edit Code", 24, Color.black, 0));
		makeVisibleWhenEntityLoads.add(gui);
		frameNavigationPanel_navButtons.guis.add(gui);
		
		//createGuiPanel(gui, inventory, loader, new int[] 	{ 0, 1, 4, 5,-1, 6 });	inventory.visible = true;	

		
		//gui = new GuiTexture(1, -1, loader.loadTextureID("blocks/Blk"+i+"Tx0An0F0"), new Vector2f(0.5f, -0.5f), new Vector2f(0.15f, 0.25f));
		//guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		GuiEvents guiEvents = new GuiEvents(displayManager.getDisplayWidth(), displayManager.getDisplayHeight());

		
		
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
		
		//allCreatureSprites = assets.loadAllPlayers("player skins", GameData.allPlayerSpriteData);
		
		allHealthBarImgs = assets.loadAllFrames_SpecifiedLength_Texture("InGameImgs", "healthBar", 17);
		
		TexturedModel basicTexturedModel = assets.loadImageByID("blocks", "basic texture");
		/*Entity basicEntity = new Entity(texturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);*/
		
//		int entityIndex = 0;
//		allPreparedEntities = new Entity[prepEntityNo];
//		for (int i = 0; i < allPreparedEntities.length; i++)
//		{
//			allPreparedEntities[i] = new Entity(basicTexturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);
//		}	
		blankEntity = new Entity(basicTexturedModel, new Vector3f(0, 0, basicTile_z), 0, 0, 0, 1f);

		
		Camera mainCamera = new Camera();
		
		Animator animator = new Animator();
		
		animator.startAnimator();
		
		
		int readPacketID;//which packet should be read
		
		int runCount = 0;
		
		try {
			while(!Display.isCloseRequested())
			{
				Time.calculateDeltaTime();
				
				//entity.increasePosition(0f, 0, -0.005f);
				//entity.increaseRotation(1f, 1, 0);
				
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
				
				if (mouse.isGuiClicked() && mouse.getClickProcessed() == 0)
				{
					previousGuiCommand = mouse.getGuiCommand();
					
					if (previousGuiCommand != -1)
					{
						for (GuiPanel guiPanel : allGuiPanels_inToolBar)
						{
							guiPanel.visible = false;
						}
						frameNavigationPanel_navButtons.visible = true;
						resetUiEditorButtons(uiEditorPanelButtons);
					}
					
//					if (followMouseTexture.isVisible())
//					{
// //						followMouseTexture.setPosition(
// //								roundedGuiElementPosition((mouse.getMouseX() - displayManager.getDisplayWidth() / 2f) - 1), 
// //								roundedGuiElementPosition((mouse.getMouseY() - displayManager.getDisplayHeight() / 2f) - 1));
//						
//						followMouseTexture = new GuiTexture(GUICLICK_EDITGUI_AddElementToPanel, -1, -1
//								, allGuiButtonTextures[GUITEXTURE_EntityInvGUI][GUITEXTURE_EntityInvGUI_Input]
//								, new Vector2f(roundedGuiElementPosition(mouse.getMouseX()), roundedGuiElementPosition(mouse.getMouseY()))
//								, 0.5f, 0.5f
//								, new GuiObject(null, "Input Slot", Color.black, 0));
//						//followMouseTexture.setPosition(0 + mouse.getMouseX() / 1000f, 0);
//						//followMouseTexture.setPosition(followMouseTexture.getPosition().x + 0.0001f, 0);
//						//followMouseTexture.setPosition(new Vector2f(followMouseTexture.getPosition().x + 1f, 0f));
//						System.out.println("gui pos x: " + followMouseTexture.getPosition().x + " y: " + followMouseTexture.getPosition().y);
//					}
					
					if (lastCommand != previousGuiCommand)
					{
						System.out.println("clicked button: " + lastCommand + " / " + GUICLICK_CREATE_Block_NewTexture1);
						switch (previousGuiCommand)
						{
							case GUICLICK_FILE://file_dropDown
								mouse.setClickProcessed(0);
	
								file_dropDown.visible = true;
								frameNavigationPanel_navButtons.visible = false;
								break;
								
							case GUICLICK_EDIT://edit_dropDown
								mouse.setClickProcessed(0);
	
								edit_dropDown.visible = true;
								frameNavigationPanel_navButtons.visible = false;
								break;
								
							case GUICLICK_CREATE://create_dropDown
								mouse.setClickProcessed(0);
	
								create_dropDown.visible = true;
								frameNavigationPanel_navButtons.visible = false;
								break;
								
							case GUICLICK_FILE_OpenMod://file_dropDown - Open Mod
								mouse.setClickProcessed(0);
	
								//open mod here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								Mod tempMod = saver.open_modFile_manual(loader);
								
								if (tempMod != null)
								{
									modBeingEdited = tempMod;
									canViewMod = true;
									reset_modObjectBuildPanels();
									allGameEntitiesBeingDeveloped[0] = null;
									
								}
								break;
								
							case GUICLICK_FILE_NewMod://file_dropDown - New Mod
								mouse.setClickProcessed(0);
								
								//new mod here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

								modBeingEdited = new Mod(JFrameIO.getTextInput("Please enter a name for this Mod")
										, saver.getPlayerFile().getUsername(), new String[] { "" }, new String [0]);
								
								System.out.println("Saving mod");
								modData_fileInfo = new ModData(modBeingEdited);
								
								saver.save_mod(modData_fileInfo, modBeingEdited);
								System.out.println("Saved mod success was: " + saver.isFileSuccess());
								
								canViewMod = true;
								break;
								
							case GUICLICK_FILE_SaveMod://file_dropDown - Save Mod
								mouse.setClickProcessed(0);
	
								//save mod here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								if (canViewMod)
								{
									System.out.println("Saving mod");
									modData_fileInfo = new ModData(modBeingEdited);
									
									saver.save_mod(modData_fileInfo, modBeingEdited);
									System.out.println("Saved mod success was: " + saver.isFileSuccess());
									
									//saver.deletePreviousModFile();
								}
								break;
								
							case GUICLICK_FILE_SaveAsMod://file_dropDown - Save As Mod
								mouse.setClickProcessed(0);
	
								//save as mod here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								if (canViewMod)
								{
//									System.out.println("Saving mod");
//									modData_fileInfo = new ModData(modBeingEdited);
//									
//									saver.save_mod(modData_fileInfo, modBeingEdited);
//									System.out.println("Saved mod");
								}
								break;
								
							case GUICLICK_EDIT_Entity:
								mouse.setClickProcessed(0);
								
								edit_dropDown.visible = true;
								edit_dropDown_selectEntity.visible = true;
								edit_dropDown_selectionEntities.visible = true;
								
								setupEntityPanel(edit_dropDown_selectionEntities, modBeingEdited.getAllEntityData(), editScrollTopIndex);
								break;
								
							case GUICLICK_EDIT_Entity_ScrollUp:
								mouse.setClickProcessed(0.01f);
								
								edit_dropDown.visible = true;
								edit_dropDown_selectEntity.visible = true;
								edit_dropDown_selectionEntities.visible = true;
								
								if (editScrollTopIndex > 0) 
								{ 
									editScrollTopIndex--; 
									setupEntityPanel(edit_dropDown_selectionEntities, modBeingEdited.getAllEntityData(), editScrollTopIndex);
								}
								break;
								
							case GUICLICK_EDIT_Entity_ScrollDown:
								mouse.setClickProcessed(0.01f);
								
								edit_dropDown.visible = true;
								edit_dropDown_selectEntity.visible = true;
								edit_dropDown_selectionEntities.visible = true;
								
								if (editScrollTopIndex < modBeingEdited.getAllEntityData().length - 1) 
								{ 
									editScrollTopIndex++; 
									setupEntityPanel(edit_dropDown_selectionEntities, modBeingEdited.getAllEntityData(), editScrollTopIndex);
								}
								break;
								
							case GUICLICK_EDIT_Entity_Select:
								mouse.setClickProcessed(0f);
								
								allGameEntitiesBeingDeveloped = new GameEntityData[1];
								modObjectBuildPanel_entityAnimations.visible = true;

								allGameEntitiesBeingDeveloped[0] = modBeingEdited.getEntityData(mouse.getGuiExtraNumber(0));
								
								//update everything for this entity
								createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
										, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								frameMoverPanel.position = new Vector2f(0f, 0f);
								
								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								
								updateCodePanel(code_showAllSpawnTypes, allGameEntitiesBeingDeveloped[0]);
								
								if (modObjectBuildPanel_entityAnimations.visible
										&& !(modObjectBuildPanel_buildUI.visible || modObjectBuildPanel_structureComponents.visible
												|| modObjectBuildPanel_code.visible || modObjectBuildPanel_tagLists.visible))//if the animation panel is visible?
								{
									scrollNavigationPanel.visible = true;
								}
								break;
								
							case GUICLICK_CREATE_Creature://create_dropDown - Creature
								mouse.setClickProcessed(0);
	
								//create creature here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								break;
								
							case GUICLICK_CREATE_Block://create_dropDown - Block
								mouse.setClickProcessed(0);
	
								create_dropDown.visible = true;
								create_dropDown_block.visible = true;
								break;
								
							case GUICLICK_CREATE_Structure://create_dropDown - Structure
								mouse.setClickProcessed(0);
	
								create_dropDown.visible = true;
								create_dropDown_structure.visible = true;
								break;
								
							case GUICLICK_CREATE_Dimension://create_dropDown - Dimension
								mouse.setClickProcessed(0);
	
								create_dropDown.visible = true;
								create_dropDown_dimension.visible = true;
								break;
								
							//Blocks
							case GUICLICK_CREATE_Block_BlankBlock://create_dropDown - Block - BlankBlock
								mouse.setClickProcessed(0);
								
								allGameEntitiesBeingDeveloped = new GameEntityData[1];
								modObjectBuildPanel_entityAnimations.visible = true;
								tempGed = new GameEntityData(
										JFrameIO.getTextInput("Please enter a name for this Block")
										, modBeingEdited.getAllEntityData().length
										, new int[] { 0, 0, 0 }
										, new UploadedTexture(loader.getNullImage_data(), loader.getNullImage_id())
										, false, new EntityGUIObject[0]
										, 0, 200, 0, 0, 0, 0);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!this line contains values for changing in the editor
								
								allGameEntitiesBeingDeveloped[0] = tempGed;
								
								saver.save_mod_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getStartingImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0])
										, saver.getNullBufferedImage());
								
								BasicFunctions.addNewGedToMod(modBeingEdited, tempGed
										);
								
								createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
										, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								frameMoverPanel.position = new Vector2f(0f, 0f);
								
								createNewEntityGUI(allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_CREATE_Block_Material://create_dropDown - Block - Material
								mouse.setClickProcessed(0);
								
								//create block - material here!!!!!!!!!!!!!!!!!!!!!!!!
								break;
								
							//Structures
							case GUICLICK_CREATE_Structure_BlankStructure://create_dropDown - Structure - BlankStructure
								mouse.setClickProcessed(0);
								
								//create structure - blank structure here!!!!!!!!!!!!!!!!!!!!!!!!
								break;
								
							case GUICLICK_CREATE_Structure_Statue://create_dropDown - Structure - Statue
								mouse.setClickProcessed(0);
								
								//create structure - statue here!!!!!!!!!!!!!!!!!!!!!!!!
								break;
								
								
							//Dimension
							case GUICLICK_CREATE_Dimension_EmptySpace://create_dropDown - Dimension - EmptySpace
								mouse.setClickProcessed(0);
								
								//create dimension - empty space here!!!!!!!!!!!!!!!!!!!!!!!!
								break;
								
								
							case GUICLICK_CREATE_Block_NewTexture1://Texture for Block 0
								System.out.println("upload new image n0: " 
										+ mouse.getGuiExtraNumbers()[0]
										+ "n1: " + mouse.getGuiExtraNumbers()[1]
										+ "n2: " + mouse.getGuiExtraNumbers()[2]
										+ "n3: " + mouse.getGuiExtraNumbers()[3]
												);
								mouse.setClickProcessed(0);
								allGameEntitiesBeingDeveloped[0].setUploadedTexture( 
										JFrameIO.setExistingTextureWithSelectedTexture("Select a texture for this Block", ""
												, saver, loader
												, allGameEntitiesBeingDeveloped[0].getUploadedTexture(
														mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], 
														mouse.getGuiExtraNumbers()[2], mouse.getGuiExtraNumbers()[3])
												, modBeingEdited
												, JFrameIO.getImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0]
														, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]
																, mouse.getGuiExtraNumbers()[2], mouse.getGuiExtraNumbers()[3])) 
										, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]
										, mouse.getGuiExtraNumbers()[2], mouse.getGuiExtraNumbers()[3]);
								
								//update UI
								mouse.getGuiTextureClicked().setTexture(allGameEntitiesBeingDeveloped[0].getUploadedTexture(
										mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], 
										mouse.getGuiExtraNumbers()[2], mouse.getGuiExtraNumbers()[3]).getTextureID());
								break;
								
							case GUICLICK_TEXTURE_ADD_Frame://Adding a Frame
								mouse.setClickProcessed(0f);
								BasicFunctions.addToFrameArray(new UploadedTexture(loader.getNullImage_data(), loader.getNullImage_id())
										, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
											, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]);
								
								saver.save_mod_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getImageName(modBeingEdited
										, allGameEntitiesBeingDeveloped[0], mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]
										, allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumber(0)][mouse.getGuiExtraNumber(1)][mouse.getGuiExtraNumber(2)].length - 1)
										, saver.getNullBufferedImage());
								
								createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
										, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								
								break;
								
							case GUICLICK_TEXTURE_COPY_Frame://Copy and add the previous a Frame
								mouse.setClickProcessed(0.2f);
								
								int currentAnimationLength = allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumbers()[0]]
										[mouse.getGuiExtraNumbers()[1]][mouse.getGuiExtraNumbers()[2]].length;
								
								if (currentAnimationLength > 0)
								{
									String previousImg_Dir = saver.getCurrentLoadedModFoldDirectory();
									String previousImg_File = JFrameIO.getImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0]
											, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2], currentAnimationLength - 1);
									System.out.println("img dir: " + previousImg_Dir);
											
//											allGameEntitiesBeingDeveloped[0].getUploadedTexture(
//											mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], 
//											mouse.getGuiExtraNumbers()[2], 
//											allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumbers()[0]]
//													[mouse.getGuiExtraNumbers()[1]][mouse.getGuiExtraNumbers()[2]].length - 1);
									
									BasicFunctions.addToFrameArray(new UploadedTexture(loader.loadTexture(previousImg_Dir + previousImg_File, false), loader.loadTextureID(previousImg_Dir + previousImg_File, false))
											, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
												, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]);
									
									saver.save_mod_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getImageName(modBeingEdited
											, allGameEntitiesBeingDeveloped[0], mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]
											, allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumber(0)][mouse.getGuiExtraNumber(1)][mouse.getGuiExtraNumber(2)].length - 1)
											, saver.open_GedImagePngFile(previousImg_Dir, previousImg_File));
									
									frameMoverPanel.position.x -= 0.3333f;
									
									createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
											, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								}
								break;
								
							case GUICLICK_TEXTURE_REMOVE_Frame://Removing a Frame
								mouse.setClickProcessed(0f);
								
								if (allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumbers()[0]]
										[mouse.getGuiExtraNumbers()[1]][mouse.getGuiExtraNumbers()[2]].length > 1)
								{
									saver.delete_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0]
											, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]
													, allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumbers()[0]]
															[mouse.getGuiExtraNumbers()[1]][mouse.getGuiExtraNumbers()[2]].length - 1));
									if (saver.isFileSuccess() || true)
									{
										BasicFunctions.removeFrameFromArray(
												allGameEntitiesBeingDeveloped[0].getUploadedTextures()
													, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1], mouse.getGuiExtraNumbers()[2]);
									}
									createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
											, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								}
								break;
								
							case GUICLICK_TEXTURE_ADD_Animation://Adding an Animation
								mouse.setClickProcessed(0f);
								
								UploadedTexture tempUT = new UploadedTexture(loader.getNullImage_data(), loader.getNullImage_id());
								BasicFunctions.addToAnimationArray(new UploadedTexture[] { tempUT }
										, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
											, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]);
								
								saver.save_mod_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0]
										, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]
										, allGameEntitiesBeingDeveloped[0].getUploadedTextures()[mouse.getGuiExtraNumber(0)][mouse.getGuiExtraNumber(1)].length - 1
										, 0)
										, saver.getNullBufferedImage());
								
								createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
										, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								
								break;
								
							case GUICLICK_TEXTURE_REMOVE_Animation://Removing an Animation
								mouse.setClickProcessed(0f);
								
//								saver.save_mod_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getStartingImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0])
//										, saver.getNullBufferedImage());
								UploadedTexture[][][][] tempUTArray = allGameEntitiesBeingDeveloped[0].getUploadedTextures();
								if (tempUTArray[mouse.getGuiExtraNumbers()[0]]
										[mouse.getGuiExtraNumbers()[1]].length > 1)
								{
									for (int i = 0; i < tempUTArray[mouse.getGuiExtraNumbers()[0]]
											[mouse.getGuiExtraNumbers()[1]][mouse.getGuiExtraNumbers()[2]].length; i++)
									{
										saver.delete_GedImagePngFile(modBeingEdited.getModName(), JFrameIO.getImageName(modBeingEdited, allGameEntitiesBeingDeveloped[0]
												, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]
														, mouse.getGuiExtraNumbers()[2], i));
										
										if (!saver.isFileSuccess()) { break; }
									}
									
									if (saver.isFileSuccess() || true)
									{
										BasicFunctions.removeAnimationFromArray(
												allGameEntitiesBeingDeveloped[0].getUploadedTextures()
													, mouse.getGuiExtraNumbers()[0], mouse.getGuiExtraNumbers()[1]);
									}
									
									createBlockImageDisplayPanel(allGameEntitiesBeingDeveloped, 0, allGameEntitiesBeingDeveloped[0].getUploadedTextures()
											, animator, gui, modObjectBuildPanel_entityAnimations, 0, 0);
								}
								break;
								
							case GUICLICK_TEXTURE_ADD_Variety://Adding a Variety
								mouse.setClickProcessed(0.05f);
								
								
								break;
								
							case GUICLICK_TEXTURE_ADD_Texture://Adding a Texture
								mouse.setClickProcessed(0.05f);
								
								
								break;
								
							case GUICLICK_ButtonSymbol_Left:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x += 0.1f;
								break;
							case GUICLICK_ButtonSymbol_Right:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x -= 0.1f;
								break;
							case GUICLICK_ButtonSymbol_Up:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.y -= 0.1f;
								break;
							case GUICLICK_ButtonSymbol_Down:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.y += 0.1f;
								break;
							case GUICLICK_ButtonSymbol_UpLeft:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x += 0.1f;
								frameMoverPanel.position.y -= 0.1f;
								break;
							case GUICLICK_ButtonSymbol_DownLeft:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x += 0.1f;
								frameMoverPanel.position.y += 0.1f;
								break;
							case GUICLICK_ButtonSymbol_UpRight:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x -= 0.1f;
								frameMoverPanel.position.y -= 0.1f;
								break;
							case GUICLICK_ButtonSymbol_DownRight:
								mouse.setClickProcessed(0);
								frameMoverPanel.position.x -= 0.1f;
								frameMoverPanel.position.y += 0.1f;
								break;
								
							case GUICLICK_ANIMATION_Play:
								mouse.setClickProcessed(0f);
								
								playAnimationIndex = mouse.getGuiExtraNumber(0);
								animator.getGuiAnimation(playAnimationIndex).setPlay(true);
								break;
							
							case GUICLICK_ANIMATION_Stop:
								mouse.setClickProcessed(0f);

								playAnimationIndex = mouse.getGuiExtraNumber(0);
								animator.getGuiAnimation(playAnimationIndex).setPlay(false);
								break;
								
							case GUICLICK_EDITORNAVIGATION_SeeStructure:
								mouse.setClickProcessed(0f);
								
								reset_modObjectBuildPanels();
								modObjectBuildPanel_structureComponents.visible = true;
								break;
								
							case GUICLICK_EDITORNAVIGATION_EditAnimations:
								mouse.setClickProcessed(0f);
								
								reset_modObjectBuildPanels();
								modObjectBuildPanel_entityAnimations.visible = true;
								scrollNavigationPanel.visible = true;
								break;
								
							case GUICLICK_EDITORNAVIGATION_EditUI:
								mouse.setClickProcessed(0f);
								
								reset_modObjectBuildPanels();
								modObjectBuildPanel_buildUI.visible = true;
								
								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_EDITORNAVIGATION_EditTagLists:
								mouse.setClickProcessed(0f);
								
								reset_modObjectBuildPanels();
								modObjectBuildPanel_tagLists.visible = true;
								break;
								
							case GUICLICK_EDITORNAVIGATION_EditCode:
								mouse.setClickProcessed(0f);
								
								reset_modObjectBuildPanels();
								modObjectBuildPanel_code.visible = true;
								
								updateCodePanel(code_showAllSpawnTypes, allGameEntitiesBeingDeveloped[0]);
								setupSpawnTypePanel(code_dropDown_selectSpawnTypes, spawnTypeScrollTopIndex);
								break;
								
							case GUICLICK_EDITGUI_EnableInventory:
								mouse.setClickProcessed(0f);
								allGameEntitiesBeingDeveloped[0].setEntityHasGUI(true);
								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_EDITGUI_DisableInventory:
								mouse.setClickProcessed(0f);
								allGameEntitiesBeingDeveloped[0].setEntityHasGUI(false);
								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_EDITGUI_AddInvBasicSlot:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddInvInput:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
//								System.out.println("clicked element: " + mouse.getGuiTextureClicked().getGuiObject().getText());
								
//								followMouseTexture = new GuiTexture(GUICLICK_EDITGUI_AddElementToPanel, -1, -1
//										, allGuiButtonTextures[GUITEXTURE_EntityInvGUI][GUITEXTURE_EntityInvGUI_Input]
//										, new Vector2f(roundedGuiElementPosition(mouse.getMouseX()), roundedGuiElementPosition(mouse.getMouseY()))
//										, 0.5f, 0.5f
//										, new GuiObject(null, "Input Slot", Color.black, 0));
								break;
								
							case GUICLICK_EDITGUI_AddInvOutput:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddProgressArrowRight:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddEnergyBar:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddDangerBar:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddLabel:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_RemoveElement:
								mouse.setClickProcessed(0f);
								mouse.getGuiTextureClicked().setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_DarkGrey]);
								break;
								
							case GUICLICK_EDITGUI_AddElementToPanel:
								mouse.setClickProcessed(0f);
								if (mouse.getSelectionNumbers(0) == SELECTIONID_NewGuiElement)
								{
									allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
											.setElementID(mouse.getSelectionNumbers(1));
								}
								
								if (allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
										.getElementID() == GameGuiTextures.GUITEXTURE_EntityInvGUI_Blank)
								{
									EntityGUIObject ego = allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)];
									
									ego.setElementID(-1);
									ego.setElementName("");
									ego.setElementText("");
								}
								else if (allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
										.getElementID() == GameGuiTextures.GUITEXTURE_EntityInvGUI_Label)
								{
									allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
											.setElementText(JFrameIO.getSetTextInput("Enter this element's text", ""));
								}
								else
								{
									//get newName with suggestion
									String newName = JFrameIO.getSetTextInput("Enter this element's name"
											, JFrameIO.findUniqueNameInGuiTextures(uiElementDefaultNames[mouse.getSelectionNumbers(1)]
													, allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()));
									
									//set newName with any edits needed
									allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
											.setElementName(JFrameIO.findUniqueNameInGuiTextures(newName
													, allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()));
									
									allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
											.setElementText(allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)].getElementName());
								}
								
								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								break;
								
								
							case GUICLICK_EDITGUI_EditElementInPanel:
								mouse.setClickProcessed(0f);

								if (allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
										.getElementID() == GameGuiTextures.GUITEXTURE_EntityInvGUI_Label)
								{
									allGameEntitiesBeingDeveloped[0].getEntityGUIObjects()[mouse.getGuiExtraNumber(0)]
											.setElementText(JFrameIO.getSetTextInput("Enter this element's text", ""));
								}

								updateEntityGUIPanel(modObjectBuildPanel_buildUI_invPanel
										, modObjectBuildPanel_buildUI_invElementNames
										, enableEntityGuiPanel, disableEntityGuiPanel, allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_EDITCODE_ShowAllSpawnTypes:
								mouse.setClickProcessed(0.05f);
								
								code_dropDown_selectSpawnTypes.visible = !code_dropDown_selectSpawnTypes.visible;
								break;
								
							case GUICLICK_EDITCODE_SelectSpawnType:
								mouse.setClickProcessed(0f);
								//set this entity to have this button's spawn type
								code_dropDown_selectSpawnTypes.visible = false;
								allGameEntitiesBeingDeveloped[0].setSpawnTypeID(mouse.getGuiExtraNumber(0));
								allGameEntitiesBeingDeveloped[0].setEntityPriority(SpawnType.getAllSpawnPriorities(mouse.getGuiExtraNumber(0)));
								updateCodePanel(code_showAllSpawnTypes, allGameEntitiesBeingDeveloped[0]);
								break;
								
							case GUICLICK_EDITCODE_SpawnType_ScrollUp:
								mouse.setClickProcessed(0f);
								
								if (spawnTypeScrollTopIndex > 0)
								{
									spawnTypeScrollTopIndex--;
									
									setupSpawnTypePanel(code_dropDown_selectSpawnTypes, spawnTypeScrollTopIndex);
								}
								break;
								
							case GUICLICK_EDITCODE_SpawnType_ScrollDown:
								mouse.setClickProcessed(0f);
								
								if (spawnTypeScrollTopIndex < SpawnType.AllSpawnTypes.length - 1)
								{
									spawnTypeScrollTopIndex++;
									
									setupSpawnTypePanel(code_dropDown_selectSpawnTypes, spawnTypeScrollTopIndex);
								}
								break;
								
							default:
								break;
						}
						
						lastCommand = previousGuiCommand;
					}
					
					if (previousGuiCommand > -1)
					{
						retNextScene = previousGuiCommand;
					}

				}
				
				if (canViewMod && !makeVisibleWhenModLoads[0].isVisible())//if a mod can be viewed but isn't visible?
				{
					for (GuiTexture gT : makeVisibleWhenModLoads)
					{
						gT.setVisible(true);
					}
				}	
				else if (!canViewMod && makeVisibleWhenModLoads[0].isVisible())//if a mod can't be viewed but is visible?
				{
					for (GuiTexture gT : makeVisibleWhenModLoads)
					{
						gT.setVisible(false);
					}
				}
				
				if (!entityButtonsAreVisible && allGameEntitiesBeingDeveloped[0] != null)//if a mod entity can be viewed but isn't visible?
				{
					entityButtonsAreVisible = true;
					for (GuiTexture gT : makeVisibleWhenEntityLoads)
					{
						gT.setVisible(true);
					}
				}
				else if (entityButtonsAreVisible && allGameEntitiesBeingDeveloped[0] == null)//if a mod entity can't be viewed but is visible?
				{
					entityButtonsAreVisible = false;
					for (GuiTexture gT : makeVisibleWhenEntityLoads)
					{
						gT.setVisible(false);
					}
				}

				
//				musicHandler.runMusic();//run music

				renderer.prepare();
				//game logic
				
				
				shader.start();
				shader.loadViewMatrix(mainCamera);
			
				
				//draw all blocks in view
				//entityIndex = 0;


									
//							entityIndex = drawTileToScreen(entityIndex, x, y, 0
//									, client, readPacketID, svrDataIndex, renderer, shader);
				
				
//				//draw other players
//				for (int i = 0; i < client.dataPacket[readPacketID].otherPlayers_Int.length; i++)
//				{
//					if (client.dataPacket[readPacketID].otherPlayers_Int[i][0] != -1)
//					{
//						entityIndex = drawCreatureToScreen(entityIndex
//								, client.dataPacket[readPacketID].otherPlayers_Int[i][0] / 100f
//										, client.dataPacket[readPacketID].otherPlayers_Int[i][1] / 100f
//												, 1
//										
//								, client.dataPacket[readPacketID].otherPlayers_Int[i][2]
//										, client.dataPacket[readPacketID].otherPlayers_Int[i][3]
//												, client.dataPacket[readPacketID].otherPlayers_Int[i][4]
//														, client.dataPacket[readPacketID].otherPlayers_Int[i][5]
//																, client.dataPacket[readPacketID].otherPlayers_Int[i][6]
//								, readPacketID, renderer, shader);
//						
//					}
//					else
//					{
//						//System.out.println("no");
//						break;
//					}	
//				}
				
				
//				//draw other creatures
//				for (int i = 0; i < client.dataPacket[readPacketID].otherNPCs_Int.length; i++)
//				{
//					if (client.dataPacket[readPacketID].otherNPCs_Int[i][0] != -1)
//					{
//						entityIndex = drawCreatureToScreen(entityIndex
//								, (client.dataPacket[readPacketID].otherNPCs_Int[i][0] / 100f)
//										, (client.dataPacket[readPacketID].otherNPCs_Int[i][1] / 100f)
//												, 1
//										
//								, client.dataPacket[readPacketID].otherNPCs_Int[i][2]
//										, client.dataPacket[readPacketID].otherNPCs_Int[i][3]
//												, client.dataPacket[readPacketID].otherNPCs_Int[i][4]
//														, client.dataPacket[readPacketID].otherNPCs_Int[i][5]
//																, client.dataPacket[readPacketID].otherNPCs_Int[i][6]
//								, readPacketID, renderer, shader);
//					}
//					else
//					{
//						//System.out.println("no");
//						break;
//					}	
//				}
				
	
				shader.stop();
				
				
				
				//render GUI to screen
				for (int i = 0; i < allGuiPanels.length; i++)
				{
					for (int j = 0; j < allGuiPanels[i].length; j++)
					{
						BasicFunctions.drawGuiLayers(allGuiPanels[i][j], guiRenderer, shader, ui, allGuiPanels[i][j].position.x, allGuiPanels[i][j].position.y, blankEntity, loader.getNullImage_data());
					}
				}
				
				//drawHealthBarToScreen(0, 0, 0, 0, 0, renderer, shader);
				
				ui.prepareDrawing(blankEntity, shader, allHealthBarImgs[0]);
				
				if (allGameEntitiesBeingDeveloped[0] != null)
				{
					ui.drawCentredString_toPixel(displayManager.getDisplayWidth() / 2f, 0f, "Mod Developer - Entity: " + allGameEntitiesBeingDeveloped[0].getEntityName(), Color.blue);
				}
				else
				{
					ui.drawCentredString_toPixel(displayManager.getDisplayWidth() / 2f, 0f, "Mod Developer", Color.yellow);
				}

				displayManager.updateDisplay();
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		
		//musicHandler.stop();
		animator.stopAnimator();
		
		guiRenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		displayManager.closeDisplay();
		
		return retNextScene;
	}
	
	private static void resetUiEditorButtons(List<GuiTexture> uiEditorPanelButtons)
	{
		for (GuiTexture gt : uiEditorPanelButtons)
		{
			gt.setTexture(GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_Background][GameGuiTextures.GUITEXTURE_Background_LightGrey]);
		}
	}
	
	private static void createNewEntityGUI(GameEntityData ged)
	{
		EntityGUIObject ego;
		for (float x = 0; x < 12; x++)
		{
			for (float y = 0; y < 8; y++)
			{
				ego = new EntityGUIObject(-1, new Vector2f((x / 8f) - 0.68f, (y / 5.5f) - 0.65f)
						, "Named Slot " + x + "," + y, "");
				BasicFunctions.addToEntityGUIObjectArray(ego, ged);
			}
		}
	}
	
	private static void updateEntityGUIPanel(GuiPanel modObjectBuildPanel_buildUI_invElementNames, GuiPanel modObjectBuildPanel_buildUI_invPanel
			, GuiTexture enableEntityGuiPanel, GuiTexture disableEntityGuiPanel
			, GameEntityData ged)
	{
		modObjectBuildPanel_buildUI_invPanel.guis.clear();
		modObjectBuildPanel_buildUI_invElementNames.guis.clear();
		modObjectBuildPanel_buildUI_invPanel.visible = true;
		modObjectBuildPanel_buildUI_invElementNames.visible = true;	
		
		if (ged.isEntityHasGUI())
		{	
			enableEntityGuiPanel.setVisible(false);
			disableEntityGuiPanel.setVisible(true);
			
			System.out.println("egos length: " + ged.getEntityGUIObjects().length);
			GuiTexture guiT;
			EntityGUIObject eGO;
			for (int i = 0; i < ged.getEntityGUIObjects().length; i++)
			{
				eGO = ged.getEntityGUIObjects()[i];
				guiT = new GuiTexture(GUICLICK_EDITGUI_AddElementToPanel, GUICLICK_EDITGUI_EditElementInPanel, -1
								, GameGuiTextures.allGuiButtonTextures[(eGO.getElementID() == -1)? GameGuiTextures.GUITEXTURE_EntityInvGUI_Blank : eGO.getElementID()][0]
								, eGO.getElementPosition()
								, 0.09f, 0.09f
								, new GuiObject(null, eGO.getElementText() + "", 24, Color.black, 1));
				guiT.setAdditionalNumbers(new int[] { i, -1, -1, -1 });
				guiT.setVisible(true);
				
				modObjectBuildPanel_buildUI_invPanel.guis.add(guiT);
				
				guiT = new GuiTexture(-1, -1, -1
						, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_EntityInvGUI_Blank][0]
						, new Vector2f(eGO.getElementPosition().x - 0.005f, eGO.getElementPosition().y - 0.005f)
						, 0f, 0f
						, new GuiObject(null, eGO.getElementText() + "", 16, Color.blue, 1));
				guiT.setAdditionalNumbers(new int[] { i, -1, -1, -1 });
				guiT.setVisible(true);
				
				modObjectBuildPanel_buildUI_invElementNames.guis.add(guiT);
				
	//			System.out.println("ego id: " + eGO.getElementID()
	//								+ " x: " + eGO.getElementPosition().x
	//								+ " y: " + eGO.getElementPosition().y);
			}
		}
		else
		{
			enableEntityGuiPanel.setVisible(true);
			disableEntityGuiPanel.setVisible(false);
		}
	}
	
	private static void reset_modObjectBuildPanels()
	{
		modObjectBuildPanel_structureComponents.visible = false;
		modObjectBuildPanel_entityAnimations.visible = false;
		modObjectBuildPanel_buildUI.visible = false;
		modObjectBuildPanel_tagLists.visible = false;
		modObjectBuildPanel_code.visible = false;
		scrollNavigationPanel.visible = false;
	}
	
	private static float roundedGuiElementPosition(float val)
	{
		return ((int)(val / 10f)) * 10;
	}
	
	private static void setupEntityPanel(GuiPanel edit_dropDown_selectionEntities, GameEntityData[] geds, int topIndex)
	{
		edit_dropDown_selectionEntities.guis.clear();
		
		GuiTexture gui;
		int j = 0;
		for (int i = topIndex; i < geds.length && j < 16; i++)
		{
			gui = new GuiTexture(GUICLICK_EDIT_Entity_Select, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0], new Vector2f(-0.500f, 0.745f - (0.1f * j)), 0.19f, 0.045f
					, new GuiObject(null, i + "." + geds[i].getEntityName(), 24, Color.black, 0));
			gui.setAdditionalNumbers(new int[] { i, -1, -1, -1 });
			edit_dropDown_selectionEntities.guis.add(gui);
			
			j++;
		}

	}
	
	private static void setupSpawnTypePanel(GuiPanel code_dropDown_selectionSpawnTypes, int topIndex)
	{
		code_dropDown_selectionSpawnTypes.panels.get(0).guis.clear();
		code_dropDown_selectionSpawnTypes.panels.get(0).visible = true;
		
		GuiTexture gui;
		int j = 0;
		for (int i = topIndex; i < SpawnType.AllSpawnTypes.length && j < 12; i++)
		{
			gui = new GuiTexture(GUICLICK_EDITCODE_SelectSpawnType, -1, -1, GameGuiTextures.allGuiButtonTextures[1][0]
					, new Vector2f(-0.000f, 0.585f - (0.0975f * j)), new Vector2f(0.340f, 0.04f) //0.69f, 0.045f
					, new GuiObject(null, SpawnType.getAllSpawnNames(SpawnType.AllSpawnTypes[i]), 24, Color.black, 0));
			gui.setAdditionalNumbers(new int[] { SpawnType.AllSpawnTypes[i], -1, -1, -1 });
			code_dropDown_selectionSpawnTypes.panels.get(0).guis.add(gui);
			
			j++;
		}

	}
	
	private static void updateCodePanel(GuiTexture code_showAllSpawnTypes, GameEntityData ged)
	{
		updateSpawnType(code_showAllSpawnTypes, ged);
	}
	
	private static void updateSpawnType(GuiTexture code_showAllSpawnTypes, GameEntityData ged)
	{
		code_showAllSpawnTypes.getGuiObject().setText(SpawnType.getAllSpawnNames(ged.getSpawnTypeID()));
	}
	
	private static GuiPanel getNewMoverPanel(GuiPanel guiPanel)
	{
		Vector2f position = new Vector2f(2f, 0f);
		if (guiPanel.panels.size() > 0)
		{
			position = guiPanel.panels.get(0).position;
			guiPanel.panels.remove(0);
		}
		
		GuiPanel moverPanel = new GuiPanel(); moverPanel.visible = true;
		moverPanel.position = position;
		frameMoverPanel = moverPanel;
		return moverPanel;
	}
	
	private static void createBlockImageDisplayPanel(GameEntityData[] allGameEntitiesBeingDeveloped, int entityBeingDeveloped
			, UploadedTexture[][][][] uploadTextures, Animator animator
			, GuiTexture gui, GuiPanel guiPanel, int textureNo, int versionNo)
	{
		animator.stopAnimator();
		
		GuiAnimation guiAnimation;
		GuiAnimation[] allGuiAnimations = new GuiAnimation[uploadTextures[textureNo][versionNo].length];
		
		GuiPanel moverPanel = getNewMoverPanel(guiPanel);
		
		float x = -0.5f;
		float y = 0.5f;
		GuiTexture guiTexture;
		
		int an;
		for (an = 0; an < uploadTextures[textureNo][versionNo].length; an++)//Animations
		{
			//System.out.println("number of frames: " + uploadTextures[textureNo][VersionNo][an].length);
			guiTexture = new GuiTexture(-1, -1, -1
					, uploadTextures[textureNo][versionNo][an][0].getTextureID()
							, new Vector2f(x + (-1.8f / 3f) - 0.05f, y + (-an / 2f)), 0.2f, 0.2f);
			moverPanel.guis.add(guiTexture);
			allGuiAnimations[an] = new GuiAnimation(guiTexture, uploadTextures, textureNo, versionNo, an, 0);
			
			guiTexture = new GuiTexture(GUICLICK_ANIMATION_Play, -1, -1
					, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Play]
							, new Vector2f(x + (-1f / 3f) - 0.05f, y + (-an / 2f) + 0.1f), 0.08f, 0.08f);
			guiTexture.setAdditionalNumbers(new int[] { an, -1, -1, -1 });
			moverPanel.guis.add(guiTexture);
			
			guiTexture = new GuiTexture(GUICLICK_ANIMATION_Stop, -1, -1
					, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Stop]
							, new Vector2f(x + (-1f / 3f) - 0.05f, y + (-an / 2f) - 0.1f), 0.08f, 0.08f);
			guiTexture.setAdditionalNumbers(new int[] { an, -1, -1, -1 });
			moverPanel.guis.add(guiTexture);
			
			
			int fr;
			
			for (fr = 0; fr < uploadTextures[textureNo][versionNo][an].length; fr++)//Frames
			{
				addFrameUpload(gui, allGameEntitiesBeingDeveloped, entityBeingDeveloped, moverPanel
						, textureNo, versionNo, an, fr
						, new int[] { textureNo, versionNo, an, fr }
						, x + (fr / 3f), y + (-an / 2f));
			}
			
			System.out.println("frame plus, an: " + an);
			//put frame + - buttons here
			guiTexture = new GuiTexture(GUICLICK_TEXTURE_ADD_Frame, -1, -1
					, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Plus]
							, new Vector2f(x + (fr / 3f) - 0.05f, y + (-an / 2f) + 0.1f), 0.05f, 0.05f);
			guiTexture.setAdditionalNumbers(new int[] { textureNo, versionNo, an, -1 });
			moverPanel.guis.add(guiTexture);
			
			guiTexture = new GuiTexture(GUICLICK_TEXTURE_COPY_Frame, -1, -1
					, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Copy]
							, new Vector2f(x + (fr / 3f) - 0.05f, y + (-an / 2f) + 0.0f), 0.05f, 0.05f);
			guiTexture.setAdditionalNumbers(new int[] { textureNo, versionNo, an, -1 });
			moverPanel.guis.add(guiTexture);
			
			guiTexture = new GuiTexture(GUICLICK_TEXTURE_REMOVE_Frame, -1, -1
					, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Minus]
							, new Vector2f(x + (fr / 3f) - 0.05f, y + (-an / 2f) - 0.1f), 0.05f, 0.05f);
			guiTexture.setAdditionalNumbers(new int[] { textureNo, versionNo, an, -1 });
			moverPanel.guis.add(guiTexture);
		}
		
		//put animation + - buttons here
		guiTexture = new GuiTexture(GUICLICK_TEXTURE_ADD_Animation, -1, -1
				, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Plus]
						, new Vector2f(x - 0.1f, y + (-an / 2f) + 0.05f), 0.05f, 0.05f);
		guiTexture.setAdditionalNumbers(new int[] { textureNo, versionNo, an - 1, -1 });
		moverPanel.guis.add(guiTexture);
		
		guiTexture = new GuiTexture(GUICLICK_TEXTURE_REMOVE_Animation, -1, -1
				, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Minus]
						, new Vector2f(x + 0.1f, y + (-an / 2f) + 0.05f), 0.05f, 0.05f);
		guiTexture.setAdditionalNumbers(new int[] { textureNo, versionNo, an - 1, -1 });
		moverPanel.guis.add(guiTexture);
		
		guiPanel.panels.add(moverPanel);
		
		animator.setGuiAnimations(allGuiAnimations);
		
		animator.startAnimator();
	}
	
	private static void addFrameUpload(GuiTexture gui, GameEntityData[] allGameEntitiesBeingDeveloped, int entityBeingDeveloped
			, GuiPanel guiPanel, int TextureNo, int VarietyNo, int AnimationNo, int FrameNo, int[] additionalNumbers
			, float x, float y)
	{
		gui = new GuiTexture(GUICLICK_CREATE_Block_NewTexture1, -1, -1
				, allGameEntitiesBeingDeveloped[entityBeingDeveloped]
						.getUploadedTexture_id(TextureNo, VarietyNo, AnimationNo, FrameNo), new Vector2f(x, y), 0.2f, 0.2f);
		gui.setAdditionalNumbers(additionalNumbers);
		
		guiPanel.guis.add(gui);
		
		gui = new GuiTexture(-1, -1, -1
				, GameGuiTextures.allGuiButtonTextures[GameGuiTextures.GUITEXTURE_ButtonSymbol][GameGuiTextures.GUITEXTURE_ButtonSymbol_Plus]
						, new Vector2f(x, y + 0.23f), 0.00f, 0.00f
						, new GuiObject(null, "Anim: " + AnimationNo + " Frm: " + FrameNo, 16, Color.black, 0));
		guiPanel.guis.add(gui);
		
	}
	
	
	private static void instantiateAllGameEntities(Saving saver, GameEntityData[] openGeds, String entityName, String[] subNames)
	{
		
		for (int i = 0; i < openGeds.length; i++)
		{
			openGeds[i] = new GameEntityData();
			BasicFunctions.addToGedArray(openGeds[i], modBeingEdited.getAllEntityData());
			//modBeingEdited.getModName()
			
			
		}
	}

	private static void changeInventory(int invID, GuiPanel[] allInventoryGuiPanels)
	{
		for (int i = 0; i < allInventoryGuiPanels.length; i++)
		{
			if (i == invID)
			{
				allInventoryGuiPanels[i].visible = true;
			}
			else
			{
				allInventoryGuiPanels[i].visible = false;
			}
		}
	}
	
//	private static void createGuiPanel(GuiTexture gui, GuiPanel guiPanel, Loader loader, int[] invIDs)
//	{
//		for (int i = 0; i < invIDs.length; i++)
//		{		
//			if (invIDs[i] > -1)
//			{
//				gui = new GuiTexture(invIDs[i], -1, -1, loader.loadTextureID("blocks/Blk"+invIDs[i]+"Tx0An0F0", true), new Vector2f(-0.5f + (i / 10f), 0.5f), new Vector2f(0.05f, 0.10f));
//				guiPanel.guis.add(gui);
//			}
//		}
//	}

//		private static int drawTileToScreen(int entityIndex, float x, float y, float lvl
//				, Client client, int readPacketID, int svrDataIndex, Renderer renderer, StaticShader shader)
//		{
//	//		allPreparedEntities[entityIndex].setTexture(allBlockImgs[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][0] ]
//	//				[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][1] ]
//	//						[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][2] ]
//	//								[ client.dataPacket[readPacketID].viewedTiles[svrDataIndex][3] ]);
//			
//			allPreparedEntities[entityIndex].setTexture(allCreatureSprites[0][0][0][0]);
//			
//			allPreparedEntities[entityIndex].position.x = x;
//			allPreparedEntities[entityIndex].position.y = y;//(y * 0.75f) + (lvl / 4f);
//			allPreparedEntities[entityIndex].position.z = basicTile_z + (lvl / 1000f) - (0.0001f * y);
//			
//			renderer.render(allPreparedEntities[entityIndex], shader);
//			
//			entityIndex++;
//			
//			if (client.dataPacket[readPacketID].viewedTiles[svrDataIndex][4] < 999)
//			{
//				entityIndex = drawHealthBarToScreen(entityIndex, x, y, lvl, client.dataPacket[readPacketID].viewedTiles[svrDataIndex][4], renderer, shader);
//			}
//			
//			return entityIndex;
//		}
//		
//		
//		private static int drawCreatureToScreen(int entityIndex, float x, float y, float lvl
//				, int creatureID, int textureNo, int animationNo, int frameNo, int HP_Percent, int readPacketID, Renderer renderer, StaticShader shader)
//		{
//			//System.out.println("creature x: " + x + " y: " + y);
//			
//			allPreparedEntities[entityIndex].setTexture(allCreatureSprites[ creatureID ]
//					[ textureNo ]
//							[ animationNo ]
//									[ frameNo ]);
//			
//			allPreparedEntities[entityIndex].position.x = x;
//			allPreparedEntities[entityIndex].position.y = y;
//			allPreparedEntities[entityIndex].position.z = basicTile_z + (lvl / 1000f) - (0.0001f * y);
//			
//			renderer.render(allPreparedEntities[entityIndex], shader);
//			
//			entityIndex++;
//			
//			if (HP_Percent < 999)
//			{
//				entityIndex = drawHealthBarToScreen(entityIndex, x, y, lvl, HP_Percent, renderer, shader);
//			}
//			
//			return entityIndex;
//		}
//		
//		
//		private static int drawHealthBarToScreen(int entityIndex, float x, float y, float lvl
//				, float HP_Percent, Renderer renderer, StaticShader shader)
//		{
//			//System.out.println("creature x: " + x + " y: " + y);
//			
//			allPreparedEntities[entityIndex].setTexture(allHealthBarImgs[ (int)(((1000f - HP_Percent) / 1000f) * (allHealthBarImgs.length - 1)) ]);
//			
//			allPreparedEntities[entityIndex].position.x = x;
//			allPreparedEntities[entityIndex].position.y = y;//(y * 0.75f) + (lvl / 4f);
//			allPreparedEntities[entityIndex].position.z = basicTile_z + ((lvl + 1f) / 1000f) - (0.0001f * y);
//			
//			renderer.render(allPreparedEntities[entityIndex], shader);
//			
//			entityIndex++;
//			
//			return entityIndex;
//		}
	
	
	
	private static float getWldX_FromScreenX(float x_loc, float Xoffset)
	{
		float temp_screenX = x_loc - scrCentreX - GRID_OFFSET_X - GM_TILE_WIDTH_HALF + GM_TILE_WIDTH + (Xoffset * GM_TILE_WIDTH);
		
		temp_screenX = ( (temp_screenX) / GM_TILE_WIDTH) - ((temp_screenX < 0)? 1 : 0 );
		System.out.println("new x: " + temp_screenX);
		return temp_screenX;
	}
	private static float getWldY_FromScreenY(float y_loc, float Yoffset)
	{
		float temp_screenY = y_loc - scrCentreY - GRID_OFFSET_Y + (Yoffset * GM_TILE_HEIGHT);
		
		temp_screenY = ( (temp_screenY) / GM_TILE_HEIGHT) - ((temp_screenY < 0)? 1 : 0 );
		System.out.println("new x: " + temp_screenY);
		return temp_screenY;
	}
	
	
	private static float getScreenX_FromWldX(float wldX, float Xoffset)
	{
		
		float temp_screenX = wldX - (Xoffset * 100f);//( (wldX + ((wldX < 0)? 1 : 0 )) );
		temp_screenX = (temp_screenX * 0.01f) * GM_TILE_WIDTH;
		//System.out.println("new bfr x: " + temp_screenX);
		temp_screenX = temp_screenX + scrCentreX + GRID_OFFSET_X + GM_TILE_WIDTH_HALF - GM_TILE_WIDTH ;//- (Xoffset );//* GM_TILE_WIDTH
		
		//System.out.println("new x: " + temp_screenX);
		return temp_screenX;
	}
	private static float getScreenY_FromWldY(float wldY, float Yoffset)
	{//= (y * 0.75f) + (lvl / 4f)
		
		float temp_screenY = -wldY + (Yoffset * 100f);//(wldY * 0.75f);// - (1 / 4f);
		temp_screenY = (temp_screenY * 0.01f) * GM_TILE_HEIGHT;
		
		//System.out.println("new y offset: " + Yoffset);
		temp_screenY = temp_screenY + scrCentreY + GRID_OFFSET_Y + GM_TILE_HEIGHT_HALF - GM_TILE_HEIGHT;
		//System.out.println("new y: " + temp_screenY);
		return temp_screenY;
	}
	
}


