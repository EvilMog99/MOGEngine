package gameRunner;

import org.lwjgl.input.Mouse;

import guis.GuiTexture;
import toolbox.Time;

public class MouseListener 
{
	private boolean LeftDown = false;
	private boolean RightDown = false;
	private int mouseX;
	private int mouseY;
	
	private float clickProcessed = 0;

	private boolean guiClicked = false;
	private int guiCommand;
	private int[] guiExtraNumbers;
	private GuiTexture guiTextureClicked = null;
	private int[] selectionNumbers;//previously set numbers that will be used in the next click

	public void testMouse()
	{
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		
		if (Mouse.isButtonDown(0) && !LeftDown)//if left button is down
		{
			LeftDown = true;
		}
		else { LeftDown = false; }
		
		if (Mouse.isButtonDown(1) && !RightDown)//if right button is down
		{
			RightDown = true;
		}
		else { RightDown = false; }
		
		if (!LeftDown && !RightDown)
		{
			if (clickProcessed > 0)
			{
				clickProcessed -= Time.getDeltaTime();
				LeftDown = false;
				RightDown = false;
				guiClicked = false;
			}
			else
			{
				clickProcessed = 0;
			}
		}
		else if (clickProcessed > 0)
		{
			//clickProcessed = 2;
			clickProcessed -= Time.getDeltaTime();
			LeftDown = false;
			RightDown = false;
			guiClicked = false;
		}
	}
	
	
	//gui code
	public void setGuiEvent(int guiCommand, int[] extraNumbers, int[] selectionNumbers, GuiTexture guiTexture)
	{
		guiClicked = true;

		this.guiCommand = guiCommand;
		this.guiExtraNumbers = extraNumbers;
		this.guiTextureClicked = guiTexture;
		if (selectionNumbers != null && selectionNumbers.length > 0)
		{
			this.selectionNumbers = selectionNumbers;
		}
	}
	
	public void resetGuiEvent()
	{
		guiClicked = false;
		//clickProcessed = 0;
		LeftDown = false;
		RightDown = false;
	}
	
	
	//all getters
	
	public float getClickProcessed() {
		return clickProcessed;
	}


	public void setClickProcessed(float clickProcessed) {
		if (this.clickProcessed <= 0)
		{
			this.clickProcessed = clickProcessed;
		}
	}
	
	public void heardSetClickProcessed(float clickProcessed) {
		this.clickProcessed = clickProcessed;
	}
	
	public boolean isGuiClicked() {
		return guiClicked;
	}


	public int getGuiCommand() {
		return guiCommand;
	}

	public int getMouseX() {
		return mouseX;
	}


	public int getMouseY() {
		return mouseY;
	}

	public boolean isLeftDown() {
		return LeftDown;
	}

	public boolean isRightDown() {
		return RightDown;
	}

	public int[] getGuiExtraNumbers() {
		return guiExtraNumbers;
	}
	
	public int getGuiExtraNumber(int index) {
		if (index > -1 && index < guiExtraNumbers.length)
		{
			return guiExtraNumbers[index];
		}
		else
		{
			return -1;
		}
	}

	public int[] getSelectionNumbers() 
	{
		return selectionNumbers;
	}
	public int getSelectionNumbers(int index) {
		if (index > -1 && index < guiExtraNumbers.length)
		{
			return selectionNumbers[index];
		}
		else
		{
			return -1;
		}
	}

	public GuiTexture getGuiTextureClicked() {
		return guiTextureClicked;
	}


	public void setGuiTextureClicked(GuiTexture guiTextureClicked) {
		this.guiTextureClicked = guiTextureClicked;
	}
}
