package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

public class GuiRenderer 
{
	private final RawModel quadModel;
	
	private GuiShader shader;
	
	public GuiRenderer (Loader loader)
	{
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		
		quadModel = loader.loadToVAO(positions);
		shader = new GuiShader();
	}
	
	public void render(List<GuiTexture> guis, float parentPositionX, float parentPositionY)
	{
		shader.start();
		
		GL30.glBindVertexArray(quadModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glEnable(GL11.GL_NEAREST);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		//GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for (GuiTexture gui : guis)
		{
			if (gui.isVisible())
			{	
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
				
				//GL11.glEnable(GL11.GL_TEXTURE_2D);
				//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				//GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				
				gui.setPositionOnParent(parentPositionX, parentPositionY);
//				if (gui.getLeftClickCommand() == -751) 
//					{ System.out.println("real x: " + gui.getPositionOnParent().x + " y: " + gui.getPositionOnParent().y); }
				Matrix4f matrix = Maths.createTransformationMatrix(gui.getPositionOnParent(), gui.getScale());
				shader.loadTransformation(matrix);
				
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quadModel.getVertexCount());
			}
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
