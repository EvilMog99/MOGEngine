package toolbox;

import renderEngine.Loader;

public class GameGuiTextures 
{
	public static int[][] allGuiButtonTextures;
	
	public static void loadGameGuiTextures(Loader loader)
	{
		//get gui textures
		allGuiButtonTextures = new int[][] {
			{ loader.loadTextureID("guis/guiBackground0", true), loader.loadTextureID("guis/guiBackground1", true),
				loader.loadTextureID("guis/guiBackground2", true), loader.loadTextureID("guis/guiBackground3", true),
				loader.loadTextureID("guis/guiBackground4", true), loader.loadTextureID("guis/guiBackground5", true), 
				loader.loadTextureID("guis/guiBackground6", true), loader.loadTextureID("guis/guiBackground7", true)}, //Background textures
			{ loader.loadTextureID("guis/buttonBackground0", true), loader.loadTextureID("guis/buttonBackground1", true) }, //Button textures
			{ loader.loadTextureID("guis/buttonIcon_down", true), loader.loadTextureID("guis/buttonIcon_up", true),
				loader.loadTextureID("guis/buttonIcon_left", true), loader.loadTextureID("guis/buttonIcon_right", true),
				loader.loadTextureID("guis/buttonIcon_downleft", true), loader.loadTextureID("guis/buttonIcon_upleft", true),
				loader.loadTextureID("guis/buttonIcon_downright", true), loader.loadTextureID("guis/buttonIcon_upright", true),
				loader.loadTextureID("guis/buttonIcon_plus", true), loader.loadTextureID("guis/buttonIcon_minus", true),
				loader.loadTextureID("guis/buttonIcon_play", true), loader.loadTextureID("guis/buttonIcon_stop", true),
				loader.loadTextureID("guis/buttonIcon_copy", true) }, //Button icons
			
			{ loader.loadTextureID("guis/entityGuiItem0", true)									}, //entity gui icons - grey
			{ loader.loadTextureID("guis/entityGuiItem1", true)									}, //entity gui icons - slight blue
			{ loader.loadTextureID("guis/entityGuiItem2", true)									}, //entity gui icons - slight green
			
			{ loader.loadTextureID("guis/entityGuiProcessArrowRight0", true), 
																											}, //entity gui icons - right arrow
			{ loader.loadTextureID("guis/entityGuiProcessEnergy0", true), loader.loadTextureID("guis/entityGuiProcessEnergy1", true),
				loader.loadTextureID("guis/entityGuiProcessEnergy2", true), loader.loadTextureID("guis/entityGuiProcessEnergy3", true),
				loader.loadTextureID("guis/entityGuiProcessEnergy4", true), loader.loadTextureID("guis/entityGuiProcessEnergy5", true),
				loader.loadTextureID("guis/entityGuiProcessEnergy6", true), loader.loadTextureID("guis/entityGuiProcessEnergy7", true),
				loader.loadTextureID("guis/entityGuiProcessEnergy8", true), loader.loadTextureID("guis/entityGuiProcessEnergy9", true),
				loader.loadTextureID("guis/entityGuiProcessEnergy10", true)
																											}, //entity gui icon - process bar
			{ loader.loadTextureID("guis/entityGuiProcessDanger0", true), loader.loadTextureID("guis/entityGuiProcessDanger1", true),
				loader.loadTextureID("guis/entityGuiProcessDanger2", true), loader.loadTextureID("guis/entityGuiProcessDanger3", true),
				loader.loadTextureID("guis/entityGuiProcessDanger4", true), loader.loadTextureID("guis/entityGuiProcessDanger5", true),
				loader.loadTextureID("guis/entityGuiProcessDanger6", true), loader.loadTextureID("guis/entityGuiProcessDanger7", true),
				loader.loadTextureID("guis/entityGuiProcessDanger8", true), loader.loadTextureID("guis/entityGuiProcessDanger9", true),
				loader.loadTextureID("guis/entityGuiProcessDanger10", true)
																										}, //entity gui icon - danger bar
			{ loader.loadTextureID("guis/guiBlank", true)									}, //entity gui icons - label
			{ loader.loadTextureID("guis/guiBlank", true)									}, //entity gui icons - null
		};
	}
	
	public static final int GUITEXTURE_Background =	 					0;
	public static final int GUITEXTURE_Background_Grey =	 			0;
	public static final int GUITEXTURE_Background_LightGrey =			1;
	public static final int GUITEXTURE_Background_LightGreyX2 =			2;
	public static final int GUITEXTURE_Background_DarkGrey =			3;
	public static final int GUITEXTURE_Background_DarkGreyX2 =	 		4;
	public static final int GUITEXTURE_Background_Red =	 				5;
	public static final int GUITEXTURE_Background_Green =			 	6;
	public static final int GUITEXTURE_Background_InGame =			 	7;
	
	public static final int GUITEXTURE_Button =	 						1;
	public static final int GUITEXTURE_Button_LightGrey =	 			0;
	public static final int GUITEXTURE_Button_LightBlue =	 			1;
	
	public static final int GUITEXTURE_ButtonSymbol =	 				2;
	public static final int GUITEXTURE_ButtonSymbol_Down =				0;
	public static final int GUITEXTURE_ButtonSymbol_Up =	 			1;
	public static final int GUITEXTURE_ButtonSymbol_Left =	 			2;
	public static final int GUITEXTURE_ButtonSymbol_Right =	 			3;
	public static final int GUITEXTURE_ButtonSymbol_DownLeft =			4;
	public static final int GUITEXTURE_ButtonSymbol_UpLeft =	 		5;
	public static final int GUITEXTURE_ButtonSymbol_DownRight =			6;
	public static final int GUITEXTURE_ButtonSymbol_UpRight =		 	7;
	public static final int GUITEXTURE_ButtonSymbol_Plus =	 			8;
	public static final int GUITEXTURE_ButtonSymbol_Minus =				9;
	public static final int GUITEXTURE_ButtonSymbol_Play =	 			10;
	public static final int GUITEXTURE_ButtonSymbol_Stop =				11;
	public static final int GUITEXTURE_ButtonSymbol_Copy =	 			12;
	
	public static final int GUITEXTURE_EntityInvGUI_Inv =				3;
	public static final int GUITEXTURE_EntityInvGUI_Input =				4;
	public static final int GUITEXTURE_EntityInvGUI_Output =			5;
	
	public static final int GUITEXTURE_EntityInvGUI_ProgressRight =		6;
	public static final int GUITEXTURE_EntityInvGUI_Energy =			7;
	public static final int GUITEXTURE_EntityInvGUI_Danger =		 	8;
	public static final int GUITEXTURE_EntityInvGUI_Label =	 			9;
	public static final int GUITEXTURE_EntityInvGUI_Blank =	 			10;
	
	
}
