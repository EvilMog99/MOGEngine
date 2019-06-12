package serverFiles;

import java.io.Serializable;
import java.util.List;

public class SendObject implements Serializable
{
	public byte[] modFile;
	//public String fileNameDir;
	public String fileName;
	public String modFileName_ref;
	public int maxNumberOfFiles;
	public Class<?> objType;
	private boolean saveOnArrival = false;

	public <T> SendObject(byte[] modFile, Class <?> T, String fileName, String modFileName_ref) {
		super();
		objType = T;
		this.modFile = modFile;
		this.fileName = fileName;
		this.modFileName_ref = modFileName_ref;
		maxNumberOfFiles = 0;
	}
	
	public <T> SendObject(SendObject source) {
		super();
		this.modFile = new byte[source.modFile.length];
		for (int i = 0; i < source.modFile.length; i++)
		{
			this.modFile[i] = source.modFile[i];
		}
		
		this.fileName = source.fileName;
		this.modFileName_ref = source.modFileName_ref;
		this.maxNumberOfFiles = source.maxNumberOfFiles;
		this.objType = source.objType;
		this.saveOnArrival = source.saveOnArrival;
	}
	
	public boolean isSaveOnArrival() {
		return saveOnArrival;
	}
	public void setSaveOnArrival(boolean saveOnArrival) {
		this.saveOnArrival = saveOnArrival;
	}
}
