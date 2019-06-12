package renderEngine;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;
import toolbox.ScalingTools;

public class Loader 
{
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> allTextureIDs = new ArrayList<Integer>();
	
	private Texture nullImage_data;
	private int nullImage_id;
	
	public Loader() 
	{
		super();
		String dir = "BasicImages\\" + "nullImage";
		nullImage_data = this.loadTexture(dir, true);
		nullImage_id = this.loadTextureID(dir, true);
		System.out.println("loaded nullImage");
	}

	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		
		storeDataInAttributeList(0, 3, positions);//store vertex coordinate (in 3D)
		storeDataInAttributeList(1, 2, textureCoords);//store texture coordinates (in 2D)
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions)
	{
		int vaoID = createVAO();
		
		storeDataInAttributeList(0, 2, positions);//store vertex coordinate (in 3D)
		//storeDataInAttributeList(1, 2, textureCoords);//store texture coordinates (in 2D)
		unbindVAO();
		
		return new RawModel(vaoID, positions.length/2);
	}
	
	public int loadTextureID(String fileName, boolean usingAssetsDir) 
	{
		Texture txtr = null;
		int textureID = -1;
		
		try {
			txtr = TextureLoader.getTexture("PNG", new FileInputStream(((usingAssetsDir)?"assets/":"") + fileName + ".png"));
			
			if (txtr == null)
			{
				System.out.println("Texture: " + fileName + " couldn't be loaded");
			}
			else
			{
				textureID = txtr.getTextureID();
				allTextureIDs.add(textureID);
			}
			
//			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D
//					, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D
//					, GL14.GL_TEXTURE_LOD_BIAS, -20f);
			/*GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D
					, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D
					, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);*///0 for blur, -0.4f for sharpness
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Failed dir: " + ((usingAssetsDir)?"assets/":"") + fileName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Failed dir: " + ((usingAssetsDir)?"assets/":"") + fileName);
		}
		
		return textureID;
	}
	
	public Texture loadTexture(String fileName, boolean usingAssetsDir) 
	{
		Texture txtr = null;
		
		try {
			txtr = TextureLoader.getTexture("PNG", new FileInputStream(((usingAssetsDir)?"assets/":"") + fileName + ".png"));
			
			/*GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D
					, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D
					, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);*///0 for blur, -0.4f for sharpness
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Failed dir: " + ((usingAssetsDir)?"assets/":"") + fileName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Failed dir: " + ((usingAssetsDir)?"assets/":"") + fileName);
		}
		return txtr;
	}
	

	public int loadTextureID_completeDir(String dir) 
	{
		Texture txtr = null;
		
		try {
			txtr = TextureLoader.getTexture("PNG", new FileInputStream(dir));
			
			if (txtr == null)
			{
				System.out.println("Texture: " + dir + " couldn't be loaded");
			}
			/*GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D
					, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D
					, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);*///0 for blur, -0.4f for sharpness
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Failed complete dir: " + dir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Failed complete dir: " + dir);
		}
		
		int textureID = txtr.getTextureID();
		allTextureIDs.add(textureID);
		System.out.println("added texture id: " + textureID);
		return textureID;
	}
	
	public Texture loadTexture_completeDir(String dir) 
	{
		Texture txtr = null;
		
		try {
			txtr = TextureLoader.getTexture("PNG", new FileInputStream(dir));
			
			/*GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D
					, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D
					, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);*///0 for blur, -0.4f for sharpness
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Failed complete dir: " + dir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Failed complete dir: " + dir);
		}
		
		//int textureID = txtr.getTextureID();
		//allTextureIDs.add(textureID);
		return txtr;
	}
	
	public void cleanUp()
	{
		for (int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		for (int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		
		for (int t:allTextureIDs)
		{
			GL11.glDeleteTextures(t);
		}
			
			
	}
	
	private int createVAO() 
	{
		int vaoID = GL30.glGenVertexArrays();
		
		vaos.add(vaoID);
		
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,  buffer,  GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	//all getters and setters
	
	public Texture getNullImage_data() {
		return nullImage_data;
	}

	public int getNullImage_id() {
		return nullImage_id;
	}
}
