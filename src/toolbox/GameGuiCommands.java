package toolbox;

public class GameGuiCommands 
{
	//for server application
	//public static final int GUI_SERVER_UPLOADMOD_StartSending = 	101;
	public static final int GUI_CLIENT_ADMIN_AddSelectedMod = 		102;
	public static final int GUI_CLIENT_CLICKEDBLOCK = 				103;
	public static final int Game_CLIENT_INV_NormalMode = 			203;
	public static final int Game_CLIENT_INV_CreateMode = 			204;
	
	//for client application
	public static final int GUI_CLIENT_UPLOADMOD_Select = 			-101; 
	public static final int GUI_CLIENT_OpenInv = 					-102; 
	public static final int GUI_CLIENT_ADMIN_SeeUploadedMods = 		-103;
	public static final int GUI_CLIENT_ADMIN_SeeUploadedMods_Up = 	-104;
	public static final int GUI_CLIENT_ADMIN_SeeUploadedMods_Down = -105;
	public static final int GUI_CLIENT_ADMIN_SelectUploadMod = 		-106;
	public static final int GUI_CLIENT_ADMIN_ViewAdminOptions = 	-107;
	public static final int GUI_CLIENT_ADMIN_ViewNonAdminOptions = 	-108;
	public static final int GUI_CLIENT_ADMIN_CloseModViewerPanel = 	-109;
	public static final int GUI_CLIENT_ADMIN_AddModToGame = 		-110;
	public static final int GUI_CLIENT_ADMIN_DeleteMod = 			-111;
	public static final int GUI_CLIENT_ADMIN_SaveWorld = 			-112;
	
	public static final int Game_CLIENT_CLICKBLOCK_Left = 			-113;
	public static final int Game_CLIENT_CLICKBLOCK_Right = 			-114;
	
	public static final int Game_CLIENT_CLICK_Notification_OK = 	-115;
	
	public static final int Game_CLIENT_INV_Refresh = 				-201;
	public static final int Game_CLIENT_INV_Close = 				-202;
	public static final int Game_CLIENT_INV_ScrollModsLeft = 		-203;
	public static final int Game_CLIENT_INV_ScrollModsRight = 		-204;
	public static final int Game_CLIENT_INV_ScrollEntitiesUp = 		-205;
	public static final int Game_CLIENT_INV_ScrollEntitiesDown = 	-206;
	public static final int Game_CLIENT_INV_ClickMod = 				-207;
	public static final int Game_CLIENT_INV_ClickEntity = 			-208;
}
