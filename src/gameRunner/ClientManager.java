package gameRunner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Timer;

import serverFiles.Client;
import toolbox.GameGuiCommands;

public class ClientManager implements ActionListener
{
	
	private Client client;
	
	private boolean sendMessage = true;//if a message is being sent? (if not its an objects' turn)
	private int messageType = 0;//what type of message is being sent

	// 0 = up, 1 = down, 2 = left, 3 = right, 4 = left click, 5 = right click, 6 = X coordinate, 7 = Y coordinate, 
	// 8 = guiClicked, 9 = guiCommand, 10 = heldItem_modIndex, 11 = heldItem_entityIndex, 12 = invView_modId, 13 = invView_entityScroll
	public int[] inputCommands = { 0, 0, 0, 0
			, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0 };
	
	public boolean[] inputCommandsReset = { true, true, true, true
			, true, true, false, false, true, true, true, true, false, false };
	
	public int[] inputCommandsResetNumber = { 0, 0, 0, 0
			, 0, 0, 1, 1, 0, -1, -2, -1, 0, 0 };
	
	public List<File> modData_forServer;//stores mod data to be sent one file at a time to the server
	public List<String> modFileNames_forServer;//stores all the file names for the server to save the files as
	
	private Timer clientListener;
	private static int ListenerSpeed = 10;
	private boolean cycleRunning = false;
	
	
	private float temp_screenX, temp_screenY;
	
	
	public ClientManager(Client client, List<String> allRequiredModNames, List<Integer> allRequiredModFileLengths) 
	{
		// TODO Auto-generated constructor stub
		
		this.client = client;
		
		for (int i = 0; i < client.dataPacket.length; i++)
		{
			client.dataPacket[i].setAllRequiredModNames(allRequiredModNames);
			client.dataPacket[i].setAllRequiredModFileLengths(allRequiredModFileLengths);
		}
		
		setUpTimer();
		startListening();
	}
	
	
	private void setUpTimer()
	{
		clientListener = new Timer(ListenerSpeed, this);
	}
	
	
	
	public void startListening()
	{
		clientListener.start();
	}
	public void stopListening()
	{
		clientListener.stop();
	}
	
	
	//handler for any event which occurs
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();//get the object which created the event
		
		if (source == clientListener && !cycleRunning)//run the game here
		{
			cycleRunning = true;
			
			if (client.inUse)
			{
				client.send(client.prepareClientMessage(inputCommands));
			}
			
			for (int i = 0; i < inputCommands.length; i++)
			{
				if (inputCommandsReset[i])
				{
					inputCommands[i] = inputCommandsResetNumber[i];
				}
			}
			
			cycleRunning = false;
		}
	}
	
	public void updateKeyboard(KeyboardListener keyboard)
	{
		if (keyboard.isMoveUp() && inputCommands[0] == 0)//up
		{
			inputCommands[0] = 1;
		}
		if (keyboard.isMoveDown() && inputCommands[1] == 0)//down
		{
			inputCommands[1] = 1;
		}
		
		if (keyboard.isMoveRight() && inputCommands[3] == 0)//right
		{
			inputCommands[3] = 1;
		}		
		if (keyboard.isMoveLeft() && inputCommands[2] == 0)//left
		{
			inputCommands[2] = 1;
		}
	}
	
	public void updateMouse(MouseListener mouse, boolean clickedGameBlock, boolean clickedGameBlockOnLeft, int mouseWorldClickX, int mouseWorldClickY)
	{		
		inputCommands[6] = mouseWorldClickX;
		inputCommands[7] = mouseWorldClickY;
		
		inputCommands[8] = 1;
		
		if (clickedGameBlock)//if block was clicked?
		{
			if (clickedGameBlockOnLeft)
			{
				inputCommands[4] = 1;
				inputCommands[8] = 0;
			}
			
			if (!clickedGameBlockOnLeft)
			{
				inputCommands[5] = 1;
				inputCommands[8] = 0;
			}
		}
		else if (mouse.isGuiClicked() && mouse.getGuiCommand() > -2)
		{
			if (mouse.isLeftDown())
			{
				inputCommands[4] = 1;
				
				inputCommands[9] = mouse.getGuiCommand();
			}
			
			if (mouse.isRightDown())
			{
				inputCommands[5] = 1;
				
				inputCommands[9] = mouse.getGuiCommand();
			}
			
			mouse.resetGuiEvent();
		}
	}
	
	public void updateItemSelection(int modIndex, int entityIndex)
	{		
		inputCommands[10] = modIndex;
		inputCommands[11] = entityIndex;
	}
	
	public void updateInvView(int modIndex, int entityScroll)
	{		
		inputCommands[12] = modIndex;
		inputCommands[13] = entityScroll;
	}
	
}
