package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import gameRunner.KeyboardListener;

public class Camera 
{
	public Vector3f position = new Vector3f(0, 0, 0);
	
	
	//rotation
	/*private float pitch;//high/low
	private float yaw;//left/rigth
	private float roll;//how much tilted (180 = upside down)*/
	
	public Camera() {}
	
	
	public void move(KeyboardListener keyboard)
	{
		if (keyboard.isMoveUp())//up
		{
			position.y += 0.02f;
		}
		if (keyboard.isMoveDown())//down
		{
			position.y -= 0.02f;
		}
		
		if (keyboard.isMoveRight())//right
		{
			position.x += 0.02f;
		}		
		if (keyboard.isMoveLeft())//left
		{
			position.x -= 0.02f;
		}
	}

	
	// all getters
	
	public Vector3f getPosition() {
		return position;
	}


	public void setPosition(Vector3f position) {
		this.position = position;
	}
	


	/*public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}*/
	
}
