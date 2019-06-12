package toolbox;

import org.lwjgl.Sys;

public class Time 
{
	private static long previousTime = 0;
	private static long currentTime = 0;
	private static float deltaTime = 0;
	public static float getDeltaTime() { return deltaTime; }
	
	public Time()
	{
		
	}
	
	public static void calculateDeltaTime()//derived from: http://wiki.lwjgl.org/wiki/LWJGL_Basics_4_(Timing).html
	{
		currentTime = (Sys.getTime() * 1000 / Sys.getTimerResolution());
		deltaTime = (currentTime - previousTime) * 0.001f;
		previousTime = currentTime;
	}
	
	public long getTime()
	{
		return (Sys.getTime() * 1000 / Sys.getTimerResolution());
	}
}
