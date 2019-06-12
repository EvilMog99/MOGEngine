package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram
{
	//load GLSL - OpenGL Shading Language
	private static final String VERTEX_FILE = "/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	
	private int locationLightPosition;
	private int locationLightColour;
	
	private int locationShineDamper;
	private int locationReflectivity;
	
	
	public TerrainShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);//to construct ShaderProgram with
	}

	public TerrainShader(String vertexFile, String fragmentFile) 
	{
		super(vertexFile, fragmentFile);//to construct ShaderProgram with
	}
	
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	protected void bindAttributes()
	{
		//connect variables from GLSL script to the Java scripts
		super.bindAttribute(0, "positions");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	protected void getAllUniformLocations()
	{
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		
		locationLightPosition = super.getUniformLocation("lightPosition");
		locationLightColour = super.getUniformLocation("lightColour");
		
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadLight(Light light)
	{
		super.loadVector(locationLightPosition, light.getPosition());
		super.loadVector(locationLightColour, light.getColour());
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
