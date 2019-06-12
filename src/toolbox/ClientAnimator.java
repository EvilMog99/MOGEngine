package toolbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class ClientAnimator implements ActionListener 
{
	private Timer animationRunner;
	private boolean cycleRunning = false;
	private float animationSpeed = 0;
	private float updateFrames = 0f;
	
	public ClientAnimator(int animSpeed) 
	{
		super();
		setUpAnimator(animSpeed);
	}
	
	private void setUpAnimator(int animSpeed)
	{
		animationSpeed = animSpeed / 10;
		animationRunner = new Timer(animSpeed / 10, this);
	}
	
	public void startAnimator()
	{
		animationRunner.start();
	}
	public void stopAnimator()
	{
		animationRunner.stop();
		System.out.println("stop animator");
	}

	//handler for any event which occurs
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();//get the object which created the event
		
		if (source == animationRunner && !cycleRunning)//run the game here
		{
			cycleRunning = true;
			
			updateFrames += animationSpeed;
			
			cycleRunning = false;
		}
	}
	
	//all getters and setters
	public float getUpdateFrames() {
		return updateFrames;
	}

	public void setUpdateFrames(float updateFrames) {
		this.updateFrames = updateFrames;
	}

}
