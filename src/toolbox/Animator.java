package toolbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import guiOperations.GuiAnimation;

public class Animator implements ActionListener
{
	private Timer animationRunner;
	public static final int AnimationSpeed = 40;
	private boolean cycleRunning = false;
	
	private GuiAnimation[] allGuiAnimations = new GuiAnimation[0];
	

	public Animator() 
	{
		super();
		setUpAnimator();
	}
	
	private void setUpAnimator()
	{
		animationRunner = new Timer(AnimationSpeed, this);
	}
	
	public void setGuiAnimations(GuiAnimation[] guiAnimation)
	{
		this.allGuiAnimations = guiAnimation;
	}
	
	public void startAnimator()
	{
		if (allGuiAnimations != null && allGuiAnimations.length > 0)
		{
			animationRunner.start();
		}
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
			
			if (allGuiAnimations == null)
			{
				stopAnimator();
			}
			else
			{
				for (GuiAnimation gA : allGuiAnimations)
				{
					if (gA.isPlay())
					{
						gA.incrementFrame();
					}
					else
					{
						gA.resetAnimation();
					}
				}
			}
			
			
			cycleRunning = false;
		}
	}
	
	public GuiAnimation[] getAllGuiAnimations() {
		return allGuiAnimations;
	}

	public void setAllGuiAnimations(GuiAnimation[] allGuiAnimations) {
		this.allGuiAnimations = allGuiAnimations;
	}
	
	public GuiAnimation getGuiAnimation(int index) {
		return allGuiAnimations[index];
	}

	public void setGuiAnimations(GuiAnimation guiAnimation, int index) {
		this.allGuiAnimations[index] = guiAnimation;
	}
}
