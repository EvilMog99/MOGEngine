package modComponents;

import java.io.Serializable;

import org.newdawn.slick.opengl.Texture;

public class UploadedTexture implements Serializable
{
	private Texture textureData;
	private int textureID;
	private String fileName;

	public UploadedTexture() {
	}
	
	public UploadedTexture(Texture textureData, int textureID) {
		super();
		this.textureData = textureData;
		this.textureID = textureID;
	}
	
	
	public Texture getTextureData() {
		return textureData;
	}
	public void setTextureData(Texture textureData) {
		this.textureData = textureData;
	}
	public int getTextureID() {
		return textureID;
	}
	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
