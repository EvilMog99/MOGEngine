package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

public class StaticShader extends ShaderProgram
{
	//load GLSL - OpenGL Shading Language
	private static final String VERTEX_FILE = "/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/fragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	
	
	public StaticShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);//to construct ShaderProgram with
	}

	public StaticShader(String vertexFile, String fragmentFile) 
	{
		super(vertexFile, fragmentFile);//to construct ShaderProgram with
	}
	
	protected void bindAttributes()
	{
		//connect variables from GLSL script to the Java scripts
		super.bindAttribute(0, "positions");
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void getAllUniformLocations()
	{
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
}
