package guiOperations;

import org.newdawn.slick.Color;

import guis.GuiTexture;

public class GuiObject 
{
	private GuiTexture guiTexture;
	private String text;
	private int textSize;
	private Color textColor;
	private int textPositionType;//0 = centred, 1 = hang left

	public GuiObject(GuiTexture guiTexture, String text, int textSize, Color textColor, int textPositionType)
	{
		this.text = text;
		this.guiTexture = guiTexture;
		this.textSize = textSize;
		this.textColor = textColor;
		this.textPositionType = textPositionType;
	}
	
	//all getters and setters
	public GuiTexture getGuiTexture() {
		return guiTexture;
	}

	public void setGuiTexture(GuiTexture guiTexture) {
		this.guiTexture = guiTexture;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	public int getTextPositionType() {
		return textPositionType;
	}

	public void setTextPositionType(int textPositionType) {
		this.textPositionType = textPositionType;
	}
	
	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
}
