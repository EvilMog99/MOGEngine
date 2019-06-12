package toolbox;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import guis.GuiTexture;
import modComponents.EntityGUIObject;
import modComponents.GameEntityData;
import modComponents.Mod;
import modComponents.ModData;
import modComponents.UploadedTexture;
import renderEngine.Loader;

public class JFrameIO extends JFrame
{
	public static String getTextInput(String message)
	{
		return JOptionPane.showInputDialog(message);
	}
	
	public static String getTextInput(String message, String presetText)
	{
		return JOptionPane.showInputDialog(message, presetText);
	}
	
	public static String getSetTextInput(String message, String presetText)
	{
		String str;
		do {
			str = JOptionPane.showInputDialog(message, presetText);
		} while (!testVariableName(str));
		return str;
	}
	
	public static boolean testVariableName(String str)
	{
		if (str == "" || str == null 
				 || str.charAt(0) == '0' || str.charAt(0) == '1' || str.charAt(0) == '2' 
				 || str.charAt(0) == '3' || str.charAt(0) == '4' || str.charAt(0) == '5'
				 || str.charAt(0) == '6' || str.charAt(0) == '7' || str.charAt(0) == '8'
				 || str.charAt(0) == '9' || str.charAt(0) == ' ')
		{
			return false;
		}
		else 
		{
			for (int i = 0; i < str.length(); i++)
			{
				if (str.charAt(i) == ' ' || str.charAt(i) == '\''
						|| str.charAt(i) == '\"' || str.charAt(i) == '?'
						|| str.charAt(i) == '!' || str.charAt(i) == '>'
						|| str.charAt(i) == '<' || str.charAt(i) == '|')//add more
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static String findUniqueNameInGuiTextures(String startingStr, EntityGUIObject[] allGTs)
	{
		String[] allNames = new String[allGTs.length];
		
		for (int i = 0; i < allNames.length; i++)
		{
			allNames[i] = allGTs[i].getElementName();
		}
		
		return findUniqueName(startingStr, allNames);
	}
	
	public static String findUniqueName(String startingStr, String[] allNames)
	{
		String testString = startingStr;//set to ideal name
		boolean notUnique = false;
		
		//test without number on the end
		for (int i = 0; i < allNames.length; i++)
		{
			if (testString.equals(allNames[i]))
			{
				notUnique = true;
			}
		}

		//test with number on the end
		int addOnNumber = 0;
		while(notUnique && addOnNumber <= 999999999)
		{
			addOnNumber++;
			testString = startingStr + addOnNumber;
			
			notUnique = false;
			for (int i = 0; i < allNames.length; i++)
			{
				if (testString.equals(allNames[i]))
				{
					notUnique = true;
				}
			}
		}
		
		return testString;
	}
	
	public static File getSelectedFile(String message, String startingDir)
	{
		FileDialog fileDialog = new FileDialog((Frame)null, message);
		if (!startingDir.equals(null) || !startingDir.equals(""))
		{
			fileDialog.setDirectory(startingDir);
		}
		fileDialog.setMode(FileDialog.LOAD);
		File file;
		String modFolderDir;
		String modFileName;
		do {
		fileDialog.setVisible(true);
		modFolderDir = fileDialog.getDirectory();
		modFileName = fileDialog.getFile();
		file = new File(modFolderDir + modFileName);
		} while (modFileName.equals(null) || file == null);
		fileDialog.dispose();
		
		return file;
	}
	
	public static String getSelectedDirectory(String message, String startingDir)
	{
		FileDialog fileDialog = new FileDialog((Frame)null, message);
		if (!startingDir.equals(null) || !startingDir.equals(""))
		{
			fileDialog.setDirectory(startingDir);
		}
		fileDialog.setMode(FileDialog.LOAD);
		String modFolderDir;
		String modFileName;
		do {
		fileDialog.setVisible(true);
		modFolderDir = fileDialog.getDirectory();
		modFileName = fileDialog.getFile();
		} while (modFileName.equals(null));
		fileDialog.dispose();
		
		return modFolderDir + modFileName;
	}
	
	public static UploadedTexture getSelectedTexture(String message, String startingDir
			, Saving saver, Loader loader, Mod mod, String nameToResave)
	{
		UploadedTexture uploadedTexture = new UploadedTexture();
		
		return setExistingTextureWithSelectedTexture(message, startingDir, saver, loader, uploadedTexture, mod, nameToResave);
	}
	
	public static UploadedTexture setExistingTextureWithSelectedTexture(String message, String startingDir
			, Saving saver, Loader loader, UploadedTexture uploadedTexture, Mod mod, String nameToResave)
	{
		FileDialog fileDialog = new FileDialog((Frame)null, message);
		if (!startingDir.equals(null) || !startingDir.equals(""))
		{
			fileDialog.setDirectory(startingDir);
		}
		fileDialog.setMode(FileDialog.LOAD);
		String modFolderDir;
		String modFileName;

		fileDialog.setVisible(true);
		modFolderDir = fileDialog.getDirectory();
		modFileName = fileDialog.getFile();
		fileDialog.dispose();
		
		if (modFileName != null && modFolderDir != null)
		{
			uploadedTexture.setTextureData(loader.loadTexture_completeDir(modFolderDir + modFileName));
			uploadedTexture.setTextureID(loader.loadTextureID_completeDir(modFolderDir + modFileName));
	
			if (modFileName.equals(null) || uploadedTexture.getTextureData() == null || uploadedTexture.getTextureID() == -1)
			{
				System.out.println("Failed to load image. Loading nullImage");
				uploadedTexture.setTextureData(loader.getNullImage_data());
				uploadedTexture.setTextureID(loader.getNullImage_id());
			}
			else
			{
				saver.save_mod_GedImagePngFile(mod.getModName(), nameToResave
						, saver.open_GedImagePngFile(modFolderDir, modFileName));
			}
		}
		
		return uploadedTexture;
	}
	
	static public String getStartingImageName(Mod mod, GameEntityData ged)
	{
		return mod.getModName() + "_" + ged.getEntityName() 
		+ "_Blk" + ged.getEntityIndex() 
		+ "Tx" + 0
		+ "Vr" + 0
		+ "An" + 0
		+ "Frm" + 0;
	}
	
	static public String getImageName(Mod mod, GameEntityData ged, int tx, int vr, int an, int frm)
	{
		return mod.getModName() + "_" + ged.getEntityName() 
		+ "_Blk" + ged.getEntityIndex() 
		+ "Tx" + tx
		+ "Vr" + vr
		+ "An" + an
		+ "Frm" + frm;
	}
}
