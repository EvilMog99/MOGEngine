package guiOperations;

import guis.GuiTexture;
import modComponents.UploadedTexture;

public class GuiAnimation 
{
	boolean play = false;
	GuiTexture guiTexture;
	UploadedTexture[][][][] animation;
	int textureIndex;
	int versionIndex;
	int animationIndex;
	int currentFrameIndex;
	
	
	public GuiAnimation(GuiTexture guiTexture, UploadedTexture[][][][] animation) {
		super();
		this.guiTexture = guiTexture;
		this.animation = animation;
		
		textureIndex = 0;
		versionIndex = 0;
		animationIndex = 0;
		currentFrameIndex = 0;
	}
	public GuiAnimation(GuiTexture guiTexture, UploadedTexture[][][][] animation, int textureIndex
			, int versionIndex, int animationIndex, int currentFrameIndex) {
		super();
		this.guiTexture = guiTexture;
		this.animation = animation;
		
		this.textureIndex = textureIndex;
		this.versionIndex = versionIndex;
		this.animationIndex = animationIndex;
		this.currentFrameIndex = currentFrameIndex;
	}
	
	public void incrementFrame()
	{
		if (currentFrameIndex >= animation[textureIndex][versionIndex][animationIndex].length - 1)
		{
			currentFrameIndex = 0;
		}
		else
		{
			currentFrameIndex++;
		}
		guiTexture.setTexture(animation[textureIndex][versionIndex][animationIndex][currentFrameIndex].getTextureID());
		System.out.println("Texture: " + guiTexture.getTexture());
	}
	
	public void resetAnimation()
	{
		currentFrameIndex = 0;
		guiTexture.setTexture(animation[textureIndex][versionIndex][animationIndex][currentFrameIndex].getTextureID());
		
		//test indexes are inbounds first
		if (textureIndex >= animation.length)
		{
			textureIndex = 0;
		}
		if (versionIndex >= animation[textureIndex].length)
		{
			versionIndex = 0;
		}
		if (animationIndex >= animation[textureIndex][versionIndex].length)
		{
			animationIndex = 0;
		}
	}
	
	public void resetIndexes()
	{
		textureIndex = 0;
		versionIndex = 0;
		animationIndex = 0;
		currentFrameIndex = 0;
		guiTexture.setTexture(animation[textureIndex][versionIndex][animationIndex][currentFrameIndex].getTextureID());
	}
	
	//all getters and setters 
	
	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}
	
	public GuiTexture getGuiTexture() {
		return guiTexture;
	}
	public void setGuiTexture(GuiTexture guiTexture) {
		this.guiTexture = guiTexture;
	}
	public UploadedTexture[][][][] getAnimation() {
		return animation;
	}
	public void setAnimation(UploadedTexture[][][][] animation) {
		this.animation = animation;
	}
	
	public int getTextureIndex() {
		return textureIndex;
	}


	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}


	public int getVersionIndex() {
		return versionIndex;
	}


	public void setVersionIndex(int versionIndex) {
		this.versionIndex = versionIndex;
	}


	public int getAnimationIndex() {
		return animationIndex;
	}


	public void setAnimationIndex(int animationIndex) {
		this.animationIndex = animationIndex;
	}


	public int getCurrentFrameIndex() {
		return currentFrameIndex;
	}


	public void setCurrentFrameIndex(int currentFrameIndex) {
		this.currentFrameIndex = currentFrameIndex;
	}
	
}
