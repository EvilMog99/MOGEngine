package toolbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectByteConverter 
{
	private ByteArrayOutputStream byteOutputStream;
	private ByteArrayInputStream byteInputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	public ObjectByteConverter() 
	{
		super();
		// TODO Auto-generated constructor stub
		
		byteOutputStream = new ByteArrayOutputStream();
		byteInputStream = new ByteArrayInputStream(new byte[0]);
	}
	
	public byte[] objectToByteArray(Object obj)
	{
		byte[] ret = new byte[0];
		try {
			objectOutputStream = new ObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
			ret = byteOutputStream.toByteArray();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			byteOutputStream.close();
		}
		catch (IOException e)
		{
			e.getStackTrace();
		}
		
		return ret;
	}
	
	public Object byteArrayToObject(byte[] byteArr)
	{
		Object ret = null;
		try {
			objectInputStream = new ObjectInputStream(byteInputStream);
			ret = objectInputStream.readObject();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			byteInputStream.close();
		}
		catch (IOException e)
		{
			e.getStackTrace();
		}
		
		return ret;
	}
}
