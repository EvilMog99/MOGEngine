package toolbox;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.omg.CORBA.portable.InputStream;

import entities.PlayerFile;
import gameRunner.Dimension;
import gameRunner.GameData;
import modComponents.ClientIntegratedMod;
import modComponents.GameEntityData;
import modComponents.GameEntityDataFile;
import modComponents.GameEntityFile;
import modComponents.IntegratedModData;
import modComponents.Mod;
import modComponents.ModData;
import renderEngine.Loader;
import serverFiles.PlayerAccountsFile;
import serverFiles.SendObject;
import serverFiles.ServerFileInfo;
import serverFiles.ServerGameData;

public class Saving 
{
	private String dirName = "MOGEngine SaveData";
	
	private String playerFolderName = "//" + "PlayerData";
	private String serverFolderName = "//" + "ServerData";
	private String usersModsFolderName = "//" + "MyMods";
	private String modEntitiesFolderName = "//" + "Entities";
	private String serversModsFolderName = "//" + "ServerMods";
	
	private String clientFolderName = "//" + "ClientData";
	private String clientModsFolderName = "//" + "ClientMods";
	
	private String currentLoadedModFoldDirectory = "";

	private String playerFileName = "PlayerFile";
	private String serverFileName = "ServerFile";
	
	private final String Extension_MODDATA = ".MODDATA";
	private final String Extension_ENTITY = ".ENTITY";
	
	private boolean fileSuccess = false;
	
	
	private File saving_dir;
	private String filePath;
	
	
	private PlayerFile playerFile;
	
	private ObjectByteConverter objectByteConverter;
	
	private BufferedImage nullBufferedImage;
	
	private String pathOfLastModDataFileLoaded = "";
	private String pathOfLastModDataFileSaved = "";
	
	@SuppressWarnings("deprecation")
	public Saving()
	{	
		objectByteConverter = new ObjectByteConverter();
		String path = Saving.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = path;
		try{
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch(Exception e) {System.out.println(e.getMessage());};
		
		saving_dir = new File(decodedPath + "..\\..\\" + dirName);
		
		if (!saving_dir.exists())//if this directory doesn't exist?
		{
			saving_dir.mkdir();//create directory
		}
		else//directory has already been made
		{}
		
		nullBufferedImage = open_GedImagePngFile(path + "..\\assets\\BasicImages\\", "nullImage" + ".png");
		
		File subFolders;
		String[] allDirectories = 
			{ 
					decodedPath + "..\\..\\" + dirName + playerFolderName,
					decodedPath + "..\\..\\" + dirName + serverFolderName,
					decodedPath + "..\\..\\" + dirName + usersModsFolderName,
			};
		
		for (int i = 0; i < allDirectories.length; i++)
		{
			testMakeDirectory(allDirectories[i]);
		}
		
		filePath = saving_dir.getAbsolutePath();
		playerFile = open_playerFile(playerFileName);
		
		if (!fileSuccess)
		{//create new file and world
			
			System.out.println("new");
//			worldFile = new ServerFile();
//			createNewWorld();
//			//save the three different types of file
//			save_file(openFileName);
//			save_file(openFileName);
			
			Calendar date1 = Calendar.getInstance();
			
			String rnd1 = "" + date1.get(Calendar.YEAR) + date1.get(Calendar.MONTH) + date1.get(Calendar.DATE);
			String rnd2 = "" + date1.get(Calendar.HOUR) + date1.get(Calendar.MINUTE) + date1.get(Calendar.SECOND);
			String rnd3 = "" + date1.get(Calendar.MILLISECOND);
			
			JOptionPane.showMessageDialog(null, "No Player file could be found, so a new Player file is being generated.\nClick OK to continue.");
			
			date1 = Calendar.getInstance();
			rnd3 += "" + date1.get(Calendar.MILLISECOND);
			
			String username = JOptionPane.showInputDialog("Please enter your username for this game");
			System.out.println(username);
			username = GameData.removeEscapeCharacters(username);
			System.out.println(username);
			
			date1 = Calendar.getInstance();
			String rnd4 = "" + date1.get(Calendar.MINUTE) + date1.get(Calendar.SECOND) + date1.get(Calendar.MILLISECOND);
			
//			System.out.println("rnd1: " + rnd1);
//			System.out.println("rnd2: " + rnd2);
//			System.out.println("rnd3: " + rnd3);
//			System.out.println("rnd4: " + rnd4);
//			System.out.println("username: " + username);
			
			String[] playerFileData = new String[] {username, rnd1, rnd2, rnd3, rnd4};//call window to fill out details here
			
			
			save_playerFile(playerFileName, new PlayerFile(playerFileData[0], playerFileData[1], playerFileData[2], 
					playerFileData[3], playerFileData[4]));
			
			playerFile = open_playerFile(playerFileName);
		}
		
		System.out.println("file");
	}
	
	public void updateWorldName(String wldName)
	{
		serversModsFolderName = "//" + wldName + " ServerMods";
	}

	public PlayerFile open_playerFile(String fileName)
	{
		PlayerFile retPF = new PlayerFile("n", "n", "n", "n", "n");

		String path = filePath + playerFolderName + "//" + fileName + ".PF";

		retPF = readFile(PlayerFile.class, new File(path));
		
		return retPF;
	}
	
	public void save_playerFile(String fileName, PlayerFile playerFile)
	{
		String path = filePath + playerFolderName;
		testMakeDirectory(path);
		path += "//" + fileName + ".PF";
		
		saveFile(playerFile, path);
	}
	
	
	
	public PlayerAccountsFile open_playerAccountsFile(String fileName)
	{
		String fileDetailName = "(Accounts)";
		
		PlayerAccountsFile retSFD = new PlayerAccountsFile();
		String path = filePath + serverFolderName + "//" + fileName + fileDetailName + ".SF";

		retSFD = readFile(PlayerAccountsFile.class, new File(path));
		
		return retSFD;
	}
	
	public void save_playerAccountsFile(String fileName, PlayerAccountsFile playerAccountsData)
	{
		String fileDetailName = "(Accounts)";
		
		String path = filePath + serverFolderName;
		testMakeDirectory(path);
		path += "//" + fileName + fileDetailName + ".SF";

		saveFile(playerAccountsData, path);
		
		System.out.println("Saved Server Account File");
	}
	
	
	public ServerFileInfo open_serverFileInfo(String fileName)
	{
		String fileDetailName = "(Info)";
		
		ServerFileInfo retSFD = new ServerFileInfo();

		String path = filePath + serverFolderName + "//" + fileName + fileDetailName + ".SF";

		retSFD = readFile(ServerFileInfo.class, new File(path));
		
		return retSFD;
	}
	
	public void save_serverFileInfo(String fileName, ServerFileInfo serverFileInfo)
	{
		String fileDetailName = "(Info)";

		String path = filePath + serverFolderName;
		testMakeDirectory(path);
		path += "//" + fileName + fileDetailName + ".SF";
		
		fileSuccess = saveFile(serverFileInfo, path);
		
		if (fileSuccess)
		{
			System.out.println("Saved Server File Info");
		}
	}
	
	
	//for opening/saving world files
	public Dimension open_serverDimensionFile(String fileName, int dimensionIndexNo)
	{
		String fileDetailName = dimensionIndexNo + "(Dimension)";
	
		Dimension retSFD = new Dimension(0, 0, -1);

		String path = filePath + serverFolderName + "//" + fileName + fileDetailName + ".SF";
		
		retSFD = readFile(Dimension.class, new File(path));
		
		return retSFD;
	}
	
	public void save_serverDimensionFile(String fileName, Dimension serverFileData, int dimensionIndexNo)
	{
		String fileDetailName = dimensionIndexNo + "(Dimension)";

		String path = filePath + serverFolderName;
		testMakeDirectory(path);
		path += "//" + fileName + fileDetailName + ".SF";
		
		fileSuccess = saveFile(serverFileData, path);

		if (fileSuccess) 
		{
			System.out.println("Saved Dimension " + dimensionIndexNo);
		}
	}
	
	public void save_mod(ModData modDataFile, Mod mod)
	{		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		modDataFile.setModDevDate(sdf.format(d));
		
		String[] allNames = new String[mod.getAllEntityData().length];
		System.out.println("len: " + allNames.length);
		for (int i = 0; i < allNames.length; i++)
		{
			allNames[i] = mod.getModName() + "_" + mod.getEntityData(i).getEntityName() + Extension_ENTITY;
			save_mod_GameEntityDataFile(mod.getModName(), allNames[i], mod.getEntityData(i));
		}
		
		modDataFile.setAllGameEntityFileNames(allNames);
		
		currentLoadedModFoldDirectory = filePath + usersModsFolderName + "//" + modDataFile.getModName() + "//";
		
		save_mod_ModDataFile(modDataFile);
	}

	
	public void save_mod_ModDataFile(ModData modDataFile)
	{	
		String path = filePath + usersModsFolderName + "//" + modDataFile.getModName();
		testMakeDirectory(path);
		path += "//" + modDataFile.getModName() + Extension_MODDATA;// + "_V-" + modDataFile.getModDevDate() + ".MODDATA";

		pathOfLastModDataFileSaved = path;
		
		saveFile(modDataFile, path);
	}
	
	public void save_mod_GameEntityDataFile(String folderName, String fileName, GameEntityData gameEntityData)
	{
		GameEntityDataFile gedFile = new GameEntityDataFile(
				gameEntityData.isAllDataSet()
				, gameEntityData.isEnabled()
				, gameEntityData.getEntityName()
				, gameEntityData.getEntityIndex()
				, BasicFunctions.getGedFileImagePaths(gameEntityData.getUploadedTextures())
				, gameEntityData.isEntityHasGUI()
				, gameEntityData.getEntityGUIObjects()
				, gameEntityData.getEntityPriority()
				, gameEntityData.getEntityMaxHealth()
				, gameEntityData.getMatterValue()
				, gameEntityData.getSpawnTypeID()
				, gameEntityData.getSpawnSize()
				, gameEntityData.getSpawnAbundance());
		
		
		String path = filePath + usersModsFolderName + "//" + folderName;// + modEntitiesFolderName;
		testMakeDirectory(path);
		path += "//" + fileName;

		fileSuccess = saveFile(gedFile, path);
	}
	
	public Mod open_modFile_manual(Loader loader)
	{
		Mod retMod = new Mod("", "", new String[0], new String[0]);
		ModData modData;

		FileDialog fileDialog = new FileDialog((Frame)null, "Choose a Mod to open");
		fileDialog.setDirectory(filePath + usersModsFolderName);
		fileDialog.setMode(FileDialog.LOAD);
		File file;
		String modFolderDir;
		String modFileName;
		//do {
		fileDialog.setVisible(true);
		modFolderDir = fileDialog.getDirectory();
		modFileName = fileDialog.getFile();
		file = new File(modFolderDir + modFileName);
		//} while (modFileName.equals(null) || file == null);
		fileDialog.dispose();
		
		if (modFolderDir != null && modFileName != null)// && testExtension(modFileName, Extension_MODDATA))
		{
			pathOfLastModDataFileLoaded = file.getPath();
			currentLoadedModFoldDirectory = modFolderDir;
			
			modData = readFile(ModData.class, file);
	
			retMod = new Mod(modData.getModName(), modData.getPublisherUserName(), modData.getAllCreators(), modData.getAllGameEntityFileNames());
			
			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
			{
				BasicFunctions.addNewGedToMod(retMod, open_gameEntityDataFile(retMod, modFolderDir, modData.getGameEntityFileName(i), loader, true));
			}
			System.out.println("Number of Entities loaded: " + retMod.getAllEntityData().length);
	
			return retMod;
		}
		return null;
	}
	
	public Mod open_modFile_automaticForServer(String modFileName, IntegratedModData integratedModData, List<SendObject> filesForClient)
	{
		System.out.println("Autoload Mod for Server");
		ModData modData;
	
		File file;
		String modFolderDir = filePath + serverFolderName + serversModsFolderName + "//" + modFileName + "//";	
		modFileName += Extension_MODDATA;//add extension
		
		file = new File(modFolderDir + modFileName);
		
		Mod retMod;
		
		if (testFileExists(file))
		{
			modData = readFile(ModData.class, file);
			
			filesForClient.add(new SendObject(serializeObject(readFile(ModData.class, file)), ModData.class, file.getName(), modData.getModName()));
			
			retMod = new Mod(modData.getModName(), modData.getPublisherUserName(), modData.getAllCreators(), modData.getAllGameEntityFileNames());
	
//			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
//			{
//				BasicFunctions.addNewGedToMod(mod
//						, open_gameEntityDataFile_forUpload(mod, modFolderDir, modData.getGameEntityFileName(i), filesForClient));
//			}
			
			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
			{
				open_gameEntityDataFile_forUpload(retMod, modFolderDir, modData.getGameEntityFileName(i), filesForClient);
			}
			
			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
			{
				BasicFunctions.addNewGedToMod(retMod
						, open_gameEntityDataFile(retMod, modFolderDir, modData.getGameEntityFileName(i), null, false));
			}
			
	//			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
	//			{
	//				BasicFunctions.addNewGedToMod(mod, open_gameEntityDataFile(mod, modFolderDir, modData.getGameEntityFileName(i), loader));
	//			}
			integratedModData.setLoadedModFromFile(true);
			System.out.println("Server Number of Entities loaded: " + retMod.getAllEntityData().length);
		}
		else
		{
			retMod = new Mod(modFileName, "", new String[0], new String[0]);
			System.out.println("Server couldn't find " + modFileName + " at: " + file.getPath());
		}
		
		return retMod;
	}
	
	public Mod open_modFile_automaticForClient(String modFileName, ClientIntegratedMod clientIntegratedMod, Loader loader)
	{
		System.out.println("Autoload Mod for Client");
		ModData modData;
	
		File file;
		String modFolderDir = filePath + clientFolderName + clientModsFolderName + "//" + modFileName + "//";	
		modFileName += ".MODDATA";//add extension
		
		Mod retMod;
		
		file = new File(modFolderDir + modFileName);
		
		if (testFileExists(file))
		{
			modData = readFile(ModData.class, file);
			
			retMod = new Mod(modData.getModName(), modData.getPublisherUserName(), modData.getAllCreators(), modData.getAllGameEntityFileNames());
	
			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
			{
				BasicFunctions.addNewGedToMod(retMod
						, open_gameEntityDataFile(retMod, modFolderDir, modData.getGameEntityFileName(i), loader, true));
			}
	
			clientIntegratedMod.setLoadedModFromFile(true);
			System.out.println("Client Number of Entities loaded: " + retMod.getAllEntityData().length);
		}
		else
		{
			//failed to load mod from file
			testMakeDirectory(modFolderDir);//make sure dir exists for next time
			System.out.println("Client couldn't find " + modFileName + " at: " + file.getPath());
			clientIntegratedMod.setAskServerForMod(true);
			System.out.println("Asking server for the " + modFileName + " Mod");
			retMod = new Mod(modFileName, "", new String[0], new String[0]);
		}
		
		return retMod;
	}
	
	public GameEntityData open_gameEntityDataFile(Mod mod, String modFolderDir, String GedFileName, Loader loader, boolean isLoader)
	{
		GameEntityDataFile gedFile;
		GameEntityData retGed = new GameEntityData();

		gedFile = readFile(GameEntityDataFile.class, new File(modFolderDir + GedFileName));
		
		retGed = BasicFunctions.openGedFileImages(modFolderDir, mod, gedFile, loader, isLoader);

		return retGed;
	}
	
	public boolean open_modFiles_forUpload(Loader loader, List<SendObject> allModFiles)
	{
		ModData modData = null;
		
		FileDialog fileDialog = new FileDialog((Frame)null, "Choose a Mod to upload");
		fileDialog.setDirectory(filePath + usersModsFolderName);
		fileDialog.setMode(FileDialog.LOAD);
		File file;
		String modFolderDir;
		String modFileName;
		//do {
		fileDialog.setVisible(true);
		modFolderDir = fileDialog.getDirectory();
		modFileName = fileDialog.getFile();
		file = new File(modFolderDir + modFileName);
		//} while (file == null || modFileName == null);
		fileDialog.dispose();
		
		if (modFileName != null && modFolderDir != null)
		{
			System.out.println("Loading Mod: " + modFolderDir + modFileName);
			
			modData = readFile(ModData.class, file);
			
			allModFiles.add(new SendObject(serializeObject(readFile(ModData.class, file)), ModData.class, modFileName, modData.getModName()));
			
			Mod retMod = new Mod(modData.getModName(), modData.getPublisherUserName(), modData.getAllCreators(), modData.getAllGameEntityFileNames());
			System.out.println("mod file loaded successfully");
			
			for (int i = 0; i < modData.getAllGameEntityFileNames().length; i++)
			{
				open_gameEntityDataFile_forUpload(retMod, modFolderDir, modData.getGameEntityFileName(i), allModFiles);
			}
			System.out.println("Number of Files loaded: " + allModFiles.size());
			
			return fileSuccess;
		}
		
		return false;
	}
	
	public GameEntityData open_gameEntityDataFile_forUpload(Mod mod, String modFolderDir, String GedFileName
			, List<SendObject> allModFiles)
	{
		GameEntityDataFile gedFile;
		GameEntityData retGed = new GameEntityData();
		
		gedFile = readFile(GameEntityDataFile.class, new File(modFolderDir + "//" + GedFileName));
		
		retGed = BasicFunctions.openGedFileImages(modFolderDir, mod, gedFile, null, false);

		//allModFiles.add(new SendObject(serializeObject(readFile(GameEntityDataFile.class, new File(modFolderDir + GedFileName))), GameEntityDataF//ile.class, GedFileName, mod.getModName()));
		allModFiles.add(new SendObject(serializeObject(readFile(GameEntityDataFile.class, new File(modFolderDir + GedFileName))), GameEntityDataFile.class, GedFileName, mod.getModName()));
		
		BasicFunctions.openGedFileImages_asObject(modFolderDir, mod, gedFile, this, allModFiles);

		System.out.println("Ged: " + GedFileName + " loaded successfully");
		
		return retGed;
	}
	
	public void save_mod_GedImagePngFile(String folderName, String fileName, BufferedImage img)
	{
		fileSuccess = false;
		
//		String path = filePath + usersModsFolderName + "//" + folderName;// + modEntitiesFolderName;
//		testMakeDirectory(path);
		try
		{
			//byte[] save_data = serialize_object(wldFile);
			if (!fileName.substring(fileName.length() - 4, fileName.length()).equals(".png"))
			{
				fileName += ".png";
			}
			String path = filePath + usersModsFolderName + "//" + folderName;// + modEntitiesFolderName;
			testMakeDirectory(path);
			path += "//" + fileName;
			
			ImageIO.write(img, "png", new File(path));
			
			fileSuccess = true;
			System.out.println("Saved image " + fileName + " to new location: " + path);
		}
		catch(FileNotFoundException e){e.printStackTrace();}
		catch(IOException e){e.printStackTrace();}
	}
	
	public BufferedImage open_GedImagePngFile(String imageFolderDir, String imgFileName)
	{
		BufferedImage retBi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		fileSuccess = false;
		try
		{
			if (!imgFileName.substring(imgFileName.length() - 4, imgFileName.length()).equals(".png"))
			{
				imgFileName += ".png";
			}
			System.out.println("Loading: " + imageFolderDir + imgFileName);
			File file;
			file = new File(imageFolderDir + imgFileName);
			retBi = ImageIO.read(file);
			
			fileSuccess = true;
			System.out.println("Image: " + imgFileName + " loaded successfully");
		}
		catch(FileNotFoundException e)
		{//create new file
			e.printStackTrace();
		}
		catch(IOException e){ e.printStackTrace(); }
		
		return retBi;
	}
	
	public byte[] open_GedImagePngFile_asByteArray(String imgFileDir)
	{
		byte[] ret = new byte[0];

		if (!imgFileDir.substring(imgFileDir.length() - 4, imgFileDir.length()).equals(".png"))
		{
			imgFileDir += ".png";
		}
		//ret = serializeObject(readFile(Object.class, new File(imgFileDir)));
		ret = readFile_asByteArray(new File(imgFileDir));

		return ret;
	}
	
	public void delete_GedImagePngFile(String imageFolderDir, String imgFileName)
	{
		fileSuccess = false;

		if (!imgFileName.substring(imgFileName.length() - 4, imgFileName.length()).equals(".png"))
		{
			imgFileName += ".png";
		}
		System.out.println("Loading: " + imageFolderDir + "//" + imgFileName);
		File file;
		String path = filePath + usersModsFolderName + "//" + imageFolderDir + "//" + imgFileName;
		file = new File(path);

		if (file != null)
		{
			if (file.delete())
			{
				fileSuccess = true;
				System.out.println("Image: " + path + " deleted successfully");
			}
			else
			{
				System.out.println("Failed to delete at: " + path);
			}
		}
	}
	
	public void deleteFile(String completePath)
	{
		fileSuccess = false;

		File file;
		file = new File(completePath);

		if (file != null)
		{
			if (file.delete())
			{
				fileSuccess = true;
				System.out.println("Image: " + completePath + " deleted successfully");
			}
			else
			{
				System.out.println("Failed to delete at: " + completePath);
			}
		}
	}
	
	public void deletePreviousModFile()
	{
		fileSuccess = false;

		File file;
		file = new File(pathOfLastModDataFileLoaded);

		if (file != null)
		{
			if (file.delete())
			{
				fileSuccess = true;
				System.out.println("Image: " + pathOfLastModDataFileLoaded + " deleted successfully");
			}
			else
			{
				System.out.println("Failed to delete at: " + pathOfLastModDataFileLoaded);
			}
		}
		
		pathOfLastModDataFileLoaded = pathOfLastModDataFileSaved;
	}
	
	public boolean save_mod_rawFileOnServer(SendObject sendObject)
	{
		String path = filePath + serverFolderName + serversModsFolderName + "//" + sendObject.modFileName_ref;
		testMakeDirectory(path);
		path += "//" + sendObject.fileName;
		System.out.println("Dir: " + path);
		//fileSuccess = saveFile(deserializeByteArray(sendObject.objType, sendObject.modFile), path);
		fileSuccess = saveFile_fromByteArray(sendObject.modFile, path);

		return fileSuccess;
	}
	
	public boolean save_mod_rawFileOnClient(SendObject sendObject)
	{
		String path = filePath + clientFolderName + clientModsFolderName + "//" + sendObject.modFileName_ref;
		testMakeDirectory(path);
		path += "//" + sendObject.fileName;
		System.out.println("Dir: " + path);
		//fileSuccess = saveFile(deserializeByteArray(sendObject.objType, sendObject.modFile), path);
		fileSuccess = saveFile_fromByteArray(sendObject.modFile, path);

		return fileSuccess;
	}

	public void testMakeDirectory(String dir)
	{
		System.out.println("path: " + dir);
		File subFolders = new File(dir);
		if (!subFolders.exists())
		{
			subFolders.mkdirs();//create directory
		}
	}
	
	
	private boolean testFileExists(File file)
	{
		if (file.exists())
		{
			fileSuccess = true;
			return true;
		}
		fileSuccess = false;
		return false;
	}
	
	private <T> boolean saveFile(T t, String path)
	{
		try {
			System.out.println("Try to save: " + path);
			FileOutputStream saver = new FileOutputStream(path);
			ObjectOutputStream oStream = new ObjectOutputStream(saver);
			oStream.writeObject(t);
			oStream.close();
			saver.close();
			System.out.println("Successfully saved: " + path);
			return true;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private <T> T readFile(Class<T> T, File file)
	{
		T bFile;
		System.out.println("Try to read: " + file.getPath());
		if (testFileExists(file))
		{
			try {
				//System.out.println("Try to read: " + file.getPath());
//				bFile = new byte[(int)file.length()];
				FileInputStream reader = new FileInputStream(file);
				ObjectInputStream iStream = new ObjectInputStream(reader);
				bFile = (T)iStream.readObject();
				iStream.close();
				reader.close();
				System.out.println("Successfully read: " + file.getPath());
				return bFile;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private boolean saveFile_fromByteArray(byte[] bFile , String path)
	{
		try {
			System.out.println("Try to save: " + path);
			FileOutputStream saver = new FileOutputStream(path);
//			ObjectOutputStream oStream = new ObjectOutputStream(saver);
			saver.write(bFile);
//			oStream.close();
			saver.close();
			System.out.println("Successfully saved: " + path);
			return true;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private byte[] readFile_asByteArray(File file)
	{
		byte[] bFile;
		if (testFileExists(file))
		{
			try {
				System.out.println("Try to read: " + file.getPath());
				bFile = new byte[(int)file.length()];
				FileInputStream reader = new FileInputStream(file);
//				ObjectInputStream iStream = new ObjectInputStream(reader);
				reader.read(bFile);
//				iStream.close();
				reader.close();
				System.out.println("Successfully read: " + file.getPath());
				return bFile;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Failed to read: " + file.getPath());
		}
		return new byte[0];
	}
	
	private <T> byte[] serializeObject(T obj)
	{
		byte[] ret = new byte[0];
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos  = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			ret = baos.toByteArray();
			oos.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	private <T> T deserializeByteArray(Class<?> T, byte[] arr)
	{
		T obj;
		if (fileSuccess)
		{
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(arr);
				ObjectInputStream ois  = new ObjectInputStream(bais);
				obj = (T)ois.readObject();
				ois.close();
				bais.close();
				return obj;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean testExtension(String fileName, String extensionName)
	{
		if (fileName.substring(fileName.length() - extensionName.length(), fileName.length()).equals(extensionName))
		{
			return true;
		}
		return false;
	}
	
	//getters and setters
	public BufferedImage getNullBufferedImage() {
		return nullBufferedImage;
	}
	
	public boolean isFileSuccess() {
		return fileSuccess;
	}

	public PlayerFile getPlayerFile() {
		return playerFile;
	}
	
	public String getCurrentLoadedModFoldDirectory() {
		return currentLoadedModFoldDirectory;
	}
}
