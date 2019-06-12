package guis;

import org.lwjgl.util.vector.Vector2f;

import guiOperations.GuiObject;
import toolbox.ScalingTools;

public class GuiTexture 
{
	private int texture;

	private Vector2f position;
	private Vector2f combinedPosition = new Vector2f(0, 0);
	private Vector2f scale;
	
	private boolean visible = true;

	private int leftClickCommand;
	private int rightClickCommand;
	private int mouseOverCommand;
	
	private int[] additionalNumbers = new int[0];
	private int[] selectionNumbers = new int[0];
	
	private GuiObject guiObject;
	private boolean guiObjectSet = false;

	public GuiTexture(int leftCommand, int rightCommand, int mouseOverComand, int texture, Vector2f position, Vector2f scale) 
	{
		super();
		this.leftClickCommand = leftCommand;
		this.rightClickCommand = rightCommand;
		this.mouseOverCommand = mouseOverComand;
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	//scaled automatically
	public GuiTexture(int leftCommand, int rightCommand, int mouseOverComand, int texture, Vector2f position, float width, float height) 
	{
		super();
		this.leftClickCommand = leftCommand;
		this.rightClickCommand = rightCommand;
		this.mouseOverCommand = mouseOverComand;
		this.texture = texture;
		this.position = position;
		this.scale = ScalingTools.scale2D_object(width, height);
	}
	
	public GuiTexture(int leftCommand, int rightCommand, int mouseOverComand, int texture, Vector2f position, Vector2f scale, GuiObject guiObject) 
	{
		super();
		this.leftClickCommand = leftCommand;
		this.rightClickCommand = rightCommand;
		this.mouseOverCommand = mouseOverComand;
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		
		this.guiObjectSet = true;
		this.guiObject = guiObject;
	}
	
	//scaled automatically
	public GuiTexture(int leftCommand, int rightCommand, int mouseOverComand, int texture, Vector2f position, float width, float height, GuiObject guiObject) 
	{
		super();
		this.leftClickCommand = leftCommand;
		this.rightClickCommand = rightCommand;
		this.mouseOverCommand = mouseOverComand;
		this.texture = texture;
		this.position = position;
		this.scale = ScalingTools.scale2D_object(width, height);
		
		this.guiObjectSet = true;
		this.guiObject = guiObject;
		guiObject.setGuiTexture(this);
	}
	
	
	//all getter and seters
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int[] getAdditionalNumbers() {
		return additionalNumbers;
	}

	public void setAdditionalNumbers(int[] additionalNumbers) {
		this.additionalNumbers = additionalNumbers;
	}
	
	public int getAdditionalNumber(int index) {
		return additionalNumbers[index];
	}

	public void setAdditionalNumber(int additionalNumber, int index) {
		this.additionalNumbers[index] = additionalNumber;
	}
	
	public void setSelectionNumbers(int[] selectionNumbers) 
	{
		this.selectionNumbers = selectionNumbers;
	}
	public int[] getSelectionNumbers() 
	{
		return selectionNumbers;
	}
	
	public GuiObject getGuiObject() {
		return guiObject;
	}

	public boolean isGuiObjectSet() {
		return guiObjectSet;
	}

	public int getLeftClickCommand() {
		return leftClickCommand;
	}


	public int getRightClickCommand() {
		return rightClickCommand;
	}
	
	public void setLeftClickCommand(int leftClickCommand) {
		this.leftClickCommand = leftClickCommand;
	}

	public void setRightClickCommand(int rightClickCommand) {
		this.rightClickCommand = rightClickCommand;
	}

	public void setMouseOverCommand(int mouseOverCommand) {
		this.mouseOverCommand = mouseOverCommand;
	}
	
	public int getMouseOverCommand() {
		return mouseOverCommand;
	}
	
	public void setTexture(int texture) {
		this.texture = texture;
	}

	public int getTexture() {
		return texture;
	}
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}
	public Vector2f getPosition() {
		return position;
	}
	public void setScaledPosition(float x, float y)
	{
		this.position = ScalingTools.scale2D_object(x, y);
	}
	public void setPositionOnParent(float parentPositionX, float parentPositionY) {
		combinedPosition.x = position.x + parentPositionX;
		combinedPosition.y = position.y + parentPositionY;
	}

	public Vector2f getPositionOnParent() {
		return combinedPosition;
	}
	public Vector2f getScale() {
		return scale;
	}
	
	
}
