package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.displayManager;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop 
{
	/*public static void main(String[] args)
	{
		displayManager.createDisplay();
		
		//setup objects here
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		
		//must draw vertices counter clockwise
		
		int[] indices = {
				
				0,1,3,	
				3,1,2,	
				
				/*0, 1, 3, 			//Top Left Triangle
				3, 1, 2				//Bottom Right Triangle*/
	/*	};
		
		float[] vertices = {
				
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				
				/*-0.5f, 0.5f, 0,  	//V0
				-0.5f, -0.5f, 0,	//V1
				0.5f, -0.5f, 0,		//V2
				0.5f, 0.5f, 0		//V3*/
	/*	};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			

				
				/*0, 0,				//V0
				0, 1, 				//V1
				1, 1, 				//V2
				1, 0				//V3*/
	/*	};
		
		//prepare assets
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("basic texture"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		Camera mainCamera = new Camera();
		
		
		while(!Display.isCloseRequested())
		{
			//entity.increasePosition(0f, 0, -0.005f);
			//entity.increaseRotation(1f, 1, 0);
			
			mainCamera.move();
			
			renderer.prepare();
			//game logic
			
			shader.start();
			shader.loadViewMatrix(mainCamera);
			renderer.render(entity, shader);
			shader.stop();
			
			
			
			displayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		displayManager.closeDisplay();
		
	}*/
	
	
	
}
