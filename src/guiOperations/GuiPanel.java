package guiOperations;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiTexture;

public class GuiPanel 
{
	public Vector2f position = new Vector2f(0f, 0f);
	public List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public List<GuiPanel> panels = new ArrayList<GuiPanel>();
	public boolean visible = false;
}
