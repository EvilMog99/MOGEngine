package guiOperations;

import java.util.List;

import gameRunner.MouseListener;
import guis.GuiTexture;

public class GuiEvents 
{
	private float screenWidth;
	private float screenHeight;
	
	private boolean guiWasClicked = false;
	private boolean guiClicked = false;
	public boolean isGuiClicked() { return guiClicked; }
	public void resetGuiClicked() { guiWasClicked = false; }
	
	public GuiEvents(int screenWidth, int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void testClick(List<GuiTexture> allGuis, MouseListener mouse, float parentPositionX, float parentPositionY)
	{
		float glMouseX = (mouse.getMouseX() / screenWidth * 2) - 1;
		float glMouseY = (mouse.getMouseY() / screenHeight * 2) - 1;
		
		//guiWasClicked = false;
		
		if (mouse.isLeftDown())
		{
			for (GuiTexture gui : allGuis)
			{
				//System.out.println("left command: " + gui.getLeftClickCommand()); 
				
				gui.setPositionOnParent(parentPositionX, parentPositionY);
				if ( gui.isVisible() && 
						(glMouseX >= gui.getPositionOnParent().x - gui.getScale().x) && (glMouseY >= gui.getPositionOnParent().y - gui.getScale().y)
						&& (glMouseX <= gui.getPositionOnParent().x + gui.getScale().x) && (glMouseY <= gui.getPositionOnParent().y + gui.getScale().y))
				{
					System.out.println("left click: " + gui.getLeftClickCommand()); 
					mouse.setGuiEvent(gui.getLeftClickCommand(), gui.getAdditionalNumbers(), gui.getSelectionNumbers(), gui);
					guiWasClicked = true;
				}
			}
		}
		else if (mouse.isRightDown())
		{
			for (GuiTexture gui : allGuis)
			{
				gui.setPositionOnParent(parentPositionX, parentPositionY);
				if ( gui.isVisible() && 
						(glMouseX >= gui.getPositionOnParent().x - gui.getScale().x) && (glMouseY >= gui.getPositionOnParent().y - gui.getScale().y)
						&& (glMouseX <= gui.getPositionOnParent().x + gui.getScale().x) && (glMouseY <= gui.getPositionOnParent().y + gui.getScale().y))
				{
					mouse.setGuiEvent(gui.getRightClickCommand(), gui.getAdditionalNumbers(), gui.getSelectionNumbers(), gui);
					guiWasClicked = true;
				}
			}
		}
		else
		{
			//test for mouse over
			for (GuiTexture gui : allGuis)
			{
				if ( (glMouseX >= gui.getPosition().x - gui.getScale().x) && (glMouseY >= gui.getPosition().y - gui.getScale().y)
						&& (glMouseX <= gui.getPosition().x + gui.getScale().x) && (glMouseY <= gui.getPosition().y + gui.getScale().y))
				{
					mouse.setGuiEvent(gui.getMouseOverCommand(), gui.getAdditionalNumbers(), gui.getSelectionNumbers(), gui);
					
				}
			}
		}
		
		if (guiWasClicked)
		{
			guiClicked = true;
		}
		else
		{
			guiClicked = false;
		}	
		
	}
}
