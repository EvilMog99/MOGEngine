package gameRunner;

import org.lwjgl.input.Keyboard;

public class KeyboardListener 
{
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	
	
	
	public void testKeyboard()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))//up
		{
			moveUp = true;
		} else { moveUp = false; }
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S))//down
		{
			moveDown = true;
		} else { moveDown = false; }
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))//right
		{
			moveRight = true;
		} else { moveRight = false; }
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A))//left
		{
			moveLeft = true;
		} else { moveLeft = false; }
		
	}


	//all getters and setters

	public boolean isMoveUp() {
		return moveUp;
	}



	public boolean isMoveDown() {
		return moveDown;
	}



	public boolean isMoveLeft() {
		return moveLeft;
	}



	public boolean isMoveRight() {
		return moveRight;
	}
}
