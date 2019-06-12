package toolbox;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.Font;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import entities.Entity;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import renderEngine.displayManager;
import shaders.StaticShader;


public class UI 
{
	private TrueTypeFont font;
	private Font awtFont;
	
	private TrueTypeFont[] allFonts;
	private boolean fontsSet = false;
	private TrueTypeFont fontInUse;
	
	public UI()
	{
		awtFont = new Font("Arial", Font.BOLD, 40);
		font = new TrueTypeFont(awtFont, false);
		
		allFonts = new TrueTypeFont[40];
		for (int i = 0; i < allFonts.length; i++)
		{
			awtFont = new Font("Arial", Font.PLAIN, (i + 1) * 2);
			allFonts[i] = new TrueTypeFont(awtFont, false);
		}
	}
	
	public void changeFont(String name, int type, int size)
	{
		awtFont = new Font(name, type, size);
		font = new TrueTypeFont(awtFont, false);
	}
	
	public void prepareDrawing(Entity entity, StaticShader shader, Texture texture)
	{
		entity.setTexture(texture);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		TexturedModel texturedModel = entity.getModel();
		RawModel model = texturedModel.getRawModel();
		
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		

		
		//bind texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());//!!!!!!!!!!!!!!!!!!!!!!!!!!
		entity.getTexture().bind();
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public void drawString_toPixel(float x, float y, String str, Color color)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		font.drawString(x, y, str, color);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawCentredString_toPixel(float x, float y, String str, Color color)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		font.drawString(x - (font.getWidth(str)/2), y, str, color);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawCentredString_toGui(GuiTexture guiTexture, String str, Color color, int size, float addX, float addY)
	{
		if ((size / 2 < allFonts.length) && (size / 2 > 0))
		{
			fontInUse = allFonts[size / 2];
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		float x = ((guiTexture.getPositionOnParent().x + 1f) / 2f) * displayManager.getDisplayWidth();
		float y = (((guiTexture.getPositionOnParent().y * -1) + 1f) / 2f) * displayManager.getDisplayHeight();
		
		x += addX;
		y += addY;
		
		fontInUse.drawString(x - (fontInUse.getWidth(str)/2), y - (fontInUse.getHeight(str)/2), str, color);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawLeftString_toGui(GuiTexture guiTexture, String str, Color color, int size, float parentPositionX, float parentPositionY)
	{
		fontInUse = allFonts[size / 2];
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayManager.getDisplayWidth(), displayManager.getDisplayHeight(), 0, 1, -1);
		
		float x = (((guiTexture.getPosition().x - (guiTexture.getScale().x / 2f)) + 1f) / 2f) * displayManager.getDisplayWidth();
		float y = (((guiTexture.getPosition().y * -1) + 1f) / 2f) * displayManager.getDisplayHeight();
		
		x += parentPositionX;
		y += parentPositionY;
		
		fontInUse.drawString(x, y, str, color);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	
	
	

}
