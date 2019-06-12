package gameMenus;

import javax.swing.JOptionPane;

import gameRunner.GameRunner;
import modComponents.ModDeveloper;
import toolbox.JFrameIO;
import toolbox.Saving;

public class StartProgram 
{
	static int loadScene = 0;
	static String serverWorldFile = "World";
	
	public static void startProgram(String[] args)
	{
		boolean hostingServer = true;
		Saving saver = new Saving();

		do
		{
		
			switch(loadScene)
			{
			//-2 = quit
			
			case -1://no scene
				loadScene = MainMenu.runDisplay();
				break;
				
			case 0://main menu scene
				loadScene = MainMenu.runDisplay();
				break;
				
			case 1://game scene (with server) - create new server
				serverWorldFile = JFrameIO.getTextInput("Set world name", "New World Name") + "-";//!!!!!!!!
				saver.updateWorldName(serverWorldFile);
				loadScene = GameRunner.startMainGame(saver, serverWorldFile, hostingServer, "127.0.0.1");
				break;
				
			case 2://game scene (without server)
				String ipAddress = JOptionPane.showInputDialog("Please enter the server's IP Address");
				
				loadScene = GameRunner.startMainGame(saver, serverWorldFile, false, ipAddress);
				break;
				
			case 3://game scene (with server) - open existing server
				serverWorldFile = JFrameIO.getTextInput("Enter the name of your world", "World Name") + "-";//JFrameIO.getSelectedFile("Open server file", "");//!!!!!!!!!!
				saver.updateWorldName(serverWorldFile);
				loadScene = GameRunner.startMainGame(saver, serverWorldFile, hostingServer, "127.0.0.1");
				break;
				
			case 4://mod development scene
				loadScene = ModDeveloper.startModDevelopment(saver);
				break;
			}
		} while(loadScene > -1);
		
		
	}
}
