package toolbox;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.displayManager;

public class ScalingTools 
{
	public static float img_width = 0;
	public static float img_height = 0;
	
//	public static Vector2f scale2D_object(float size)
//	{
//		float x = 0;
//		
//		x = img_width * ((displayManager.getDisplayHeight() * 1f) / displayManager.getDisplayWidth());
//		
//		System.out.println("di: " + ((displayManager.getDisplayHeight() * 1f) / displayManager.getDisplayWidth()));
//		System.out.println("wid: " + x);
//		
//		return new Vector2f(x * size, img_height * size);
//	}
	
	public static Vector2f scale2D_object(float width, float height)
	{
		float x = 0;
		float y = 0;
		
		x = (1000f / displayManager.getDisplayWidth()) * width;
		y = (1000f / displayManager.getDisplayHeight()) * height;
		
		return new Vector2f(x, y);
	}
	
//	public static Vector2f pixelPosition2D_object(float width, float height)
//	{
//		float x = 0;
//		float y = 0;
//		
//		x = (1000f / displayManager.getDisplayWidth()) * width;
//		y = (1000f / displayManager.getDisplayHeight()) * height;
//		
//		return new Vector2f(x, y);
//	}
	
	
}
