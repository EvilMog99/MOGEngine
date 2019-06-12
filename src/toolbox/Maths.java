package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths 
{
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x,  scale.y,  1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz
			, float scale )
	{
		Matrix4f retMatrix = new Matrix4f();
		retMatrix.setIdentity();
		Matrix4f.translate(translation, retMatrix, retMatrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), retMatrix, retMatrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), retMatrix, retMatrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), retMatrix, retMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), retMatrix, retMatrix);
		return retMatrix;
	}
	
	
	public static Matrix4f createViewMatrix(Camera camera)
	{
		Matrix4f retViewMatrix = new Matrix4f();
		retViewMatrix.setIdentity();

		/*Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), retViewMatrix, retViewMatrix);//rotate x
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), retViewMatrix, retViewMatrix);//rotate y
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), retViewMatrix, retViewMatrix);//rotate z*/
		
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, retViewMatrix, retViewMatrix);
		
		return retViewMatrix;
	}
}
