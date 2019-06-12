package modComponents;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

public class EntityGUIObject implements Serializable
{
	private int elementID;
	private Vector2f elementPosition;
	private String elementName;
	private String elementText;
	
	//add stored item here
	int heldItem_modID = -1;
	int heldItem_entityID = -1;
	float heldItem_quantity = -1;
	
	public EntityGUIObject(int elementID, Vector2f elementPosition, String elementName, String elementText) {
		super();
		this.elementID = elementID;
		this.elementPosition = elementPosition;
		this.elementName = elementName;
		this.elementText = elementText;
	}
	//all getters and setters
	
	public int getElementID() {
		return elementID;
	}
	public void setElementID(int elementID) {
		this.elementID = elementID;
	}
	public Vector2f getElementPosition() {
		return elementPosition;
	}
	public void setElementPosition(Vector2f elementPosition) {
		this.elementPosition = elementPosition;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getElementText() {
		return elementText;
	}
	public void setElementText(String elementText) {
		this.elementText = elementText;
	}
	
	
}
