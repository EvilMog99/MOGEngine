package toolbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import entities.Entity;
import gameRunner.MouseListener;
import guiOperations.GuiEvents;
import guiOperations.GuiObject;
import guiOperations.GuiPanel;
import guis.GuiRenderer;
import guis.GuiTexture;
import modComponents.EntityGUIObject;
import modComponents.GameEntity;
import modComponents.GameEntityData;
import modComponents.GameEntityDataFile;
import modComponents.Mod;
import modComponents.UploadedTexture;
import renderEngine.Loader;
import serverFiles.SendObject;
import shaders.StaticShader;

public class BasicFunctions 
{
	static public void addNewGedToMod(Mod mod, GameEntityData ged)
	{
		mod.setAllEntityData( addToGedArray( ged, mod.getAllEntityData() ) );
		ged.setEntityIndex(mod.getAllEntityData().length - 1);
	}
	
	static public GameEntityData[] addToGedArray (GameEntityData singleGed, GameEntityData[] arrGed)
	{
		List<GameEntityData> list = new ArrayList<GameEntityData>();
		
		for (int i = 0; i < arrGed.length; i++)
		{
			list.add(arrGed[i]);
		}
		list.add(singleGed);
		
		//arrGed = (GameEntityData[])(list.toArray()); 
		arrGed = new GameEntityData[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arrGed[i] = list.get(i);
		}
		
		return arrGed;
	}
	
	static public void addToStringArray (String singleStr, String[] arrStr)
	{
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < arrStr.length; i++)
		{
			list.add(arrStr[i]);
		}
		list.add(singleStr);
		
		arrStr = (String[])(list.toArray()); 
	}
	
	static public void addToTextureArray (UploadedTexture[][][] singleUt, UploadedTexture[][][][] arrUt)
	{
		List<UploadedTexture[][][]> list = new ArrayList<UploadedTexture[][][]>();
		for (int i = 0; i < arrUt.length; i++)
		{
			list.add(arrUt[i]);
		}
		list.add(singleUt);
		arrUt = new UploadedTexture[list.size()][][][];
		for (int i = 0; i < list.size(); i++)
		{
			arrUt[i] = list.get(i);
		}
	}
	
	static public void addToVersionArray (UploadedTexture[][] singleUt, UploadedTexture[][][] arrUt)
	{
		List<UploadedTexture[][]> list = new ArrayList<UploadedTexture[][]>();
		for (int i = 0; i < arrUt.length; i++)
		{
			list.add(arrUt[i]);
		}
		list.add(singleUt);
		arrUt = new UploadedTexture[list.size()][][];
		for (int i = 0; i < list.size(); i++)
		{
			arrUt[i] = list.get(i);
		}
	}
	
	static public void addToAnimationArray (UploadedTexture[] singleUt, UploadedTexture[][][][] arrUt, int tx, int vr)
	{
		List<UploadedTexture[]> list = new ArrayList<UploadedTexture[]>();
		for (int i = 0; i < arrUt[tx][vr].length; i++)
		{
			list.add(arrUt[tx][vr][i]);
		}
		list.add(singleUt);
		arrUt[tx][vr] = new UploadedTexture[list.size()][];
		for (int i = 0; i < list.size(); i++)
		{
			arrUt[tx][vr][i] = list.get(i);
		}
	}
	
	static public void addToFrameArray (UploadedTexture singleUt, UploadedTexture[][][][] arrUt, int tx, int vr, int an)
	{
		List<UploadedTexture> list = new ArrayList<UploadedTexture>();
		for (int i = 0; i < arrUt[tx][vr][an].length; i++)
		{
			list.add(arrUt[tx][vr][an][i]);
		}
		list.add(singleUt);
		arrUt[tx][vr][an] = new UploadedTexture[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arrUt[tx][vr][an][i] = list.get(i);
		}
	}
	
	static public void removeAnimationFromArray (UploadedTexture[][][][] arrUt, int tx, int vr)
	{
		if (arrUt[tx][vr].length > 1)
		{
			List<UploadedTexture[]> list = new ArrayList<UploadedTexture[]>();
			for (int i = 0; i < arrUt[tx][vr].length; i++)
			{
				list.add(arrUt[tx][vr][i]);
			}
			list.remove(list.size() - 1);
			arrUt[tx][vr] = new UploadedTexture[list.size()][];
			for (int i = 0; i < list.size(); i++)
			{
				arrUt[tx][vr][i] = list.get(i);
			}
		}
	}
	
	static public void removeFrameFromArray (UploadedTexture[][][][] arrUt, int tx, int vr, int an)
	{
		if (arrUt[tx][vr][an].length > 1)
		{
			List<UploadedTexture> list = new ArrayList<UploadedTexture>();
			for (int i = 0; i < arrUt[tx][vr][an].length; i++)
			{
				list.add(arrUt[tx][vr][an][i]);
			}
			list.remove(list.size() - 1);
			arrUt[tx][vr][an] = new UploadedTexture[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				arrUt[tx][vr][an][i] = list.get(i);
			}
		}
	}
	
	
	
	//turn one object into another
	static public GameEntityData openGedFileImages(String path, Mod mod, GameEntityDataFile gedFile, Loader loader, boolean isLoader)
	{
		GameEntityData ged;
		
		ged = new GameEntityData(gedFile.getEntityName(), gedFile.getEntityIndex()
				, gedFile.getEntityPriority(), gedFile.getEntityMaxHealth(), gedFile.getMatterValue()
				, gedFile.getSpawnTypeID(), gedFile.getSpawnSize(), gedFile.getSpawnAbundance());
		ged.setEnabled(gedFile.isEnabled());
		
		ged.setEntityGUIObjects(gedFile.getEntityGUIObjects());
		
		List<List<List<List<String>>>> imgNames = gedFile.getAllImageNames();
		
		UploadedTexture[][][][] allUploadedTextures = new UploadedTexture[imgNames.size()][][][];
		
		String dir;
		
		for (int i = 0; i < allUploadedTextures.length; i++)
		{
			allUploadedTextures[i] = new UploadedTexture[imgNames.get(i).size()][][];
			for (int j = 0; j < allUploadedTextures[i].length; j++)
			{
				allUploadedTextures[i][j] = new UploadedTexture[imgNames.get(i).get(j).size()][];
				for (int k = 0; k < allUploadedTextures[i][j].length; k++)
				{
					allUploadedTextures[i][j][k] = new UploadedTexture[imgNames.get(i).get(j).get(k).size()];
					for (int l = 0; l < allUploadedTextures[i][j][k].length; l++)
					{
						dir = path + JFrameIO.getImageName(mod, ged, i, j, k, l) + ".png";
						System.out.println("Opening Dir: " + dir);
						if (isLoader)
						{
							allUploadedTextures[i][j][k][l] = new UploadedTexture(loader.loadTexture_completeDir(dir)
									, loader.loadTextureID_completeDir(dir));
						}
					}
				}	
			}	
		}	
		
		ged.setUploadedTextures(allUploadedTextures);
		
		
		
		return ged;
	}
	
	static public List<List<List<List<String>>>> getGedFileImagePaths(UploadedTexture[][][][] allUploadedTextures)
	{
		List<List<List<List<String>>>> allImageNames = new ArrayList<List<List<List<String>>>>();
		
		for (int i = 0; i < allUploadedTextures.length; i++)
		{
			allImageNames.add(new ArrayList<List<List<String>>>());
			
			for (int j = 0; j < allUploadedTextures[i].length; j++)
			{
				allImageNames.get(i).add(new ArrayList<List<String>>());
				for (int k = 0; k < allUploadedTextures[i][j].length; k++)
				{
					allImageNames.get(i).get(j).add(new ArrayList<String>());
					for (int l = 0; l < allUploadedTextures[i][j][k].length; l++)
					{
						allImageNames.get(i).get(j).get(k).add(allUploadedTextures[i][j][k][l].getFileName());
						
//						dir = "";
//						allUploadedTextures[i][j][k][l] = new UploadedTexture(loader.loadTexture_completeDir(dir)
//								, loader.loadTextureID_completeDir(dir));
					}
				}	
			}	
		}	
		
		return allImageNames;
	}
	
	
	static public void openGedFileImages_asObject(String path, Mod mod, GameEntityDataFile gedFile, Saving saver
			, List<SendObject> allModFiles)
	{
		GameEntityData ged;
		
		ged = new GameEntityData(gedFile.getEntityName(), gedFile.getEntityIndex()
				, gedFile.getEntityPriority(), gedFile.getEntityMaxHealth(), gedFile.getMatterValue()
				, gedFile.getSpawnTypeID(), gedFile.getSpawnSize(), gedFile.getSpawnAbundance());
		ged.setEnabled(gedFile.isEnabled());
		
		ged.setEntityGUIObjects(gedFile.getEntityGUIObjects());
		
		List<List<List<List<String>>>> imgNames = gedFile.getAllImageNames();

		String dir;
		
		for (int i = 0; i < imgNames.size(); i++)
		{
			for (int j = 0; j < imgNames.get(i).size(); j++)
			{
				for (int k = 0; k < imgNames.get(i).get(j).size(); k++)
				{
					for (int l = 0; l < imgNames.get(i).get(j).get(k).size(); l++)
					{
						dir = path + JFrameIO.getImageName(mod, ged, i, j, k, l) + ".png";
						System.out.println("Opening Dir: " + dir);
						
						allModFiles.add(new SendObject(saver.open_GedImagePngFile_asByteArray(dir), Object.class, JFrameIO.getImageName(mod, ged, i, j, k, l) + ".png", mod.getModName()));
						
//						allUploadedTextures[i][j][k][l] = new UploadedTexture(loader.loadTexture_completeDir(dir)
//								, loader.loadTextureID_completeDir(dir));
					}
				}	
			}	
		}
	}
	
	static public void addToIntArray (int singleInt, int[] arrInt)
	{
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < arrInt.length; i++)
		{
			list.add(arrInt[i]);
		}
		list.add(singleInt);
		arrInt = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arrInt[i] = list.get(i);
		}
	}
	
	static public void removeIntFromArray (int[] arrInt)
	{
		if (arrInt.length > 1)
		{
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < arrInt.length; i++)
			{
				list.add(arrInt[i]);
			}
			list.remove(list.size() - 1);
			arrInt = new int[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				arrInt[i] = list.get(i);
			}
		}
	}
	
	static public void addToEntityGUIObjectArray (EntityGUIObject singleEGO, GameEntityData ged)
	{
		EntityGUIObject[] arrEGO = ged.getEntityGUIObjects();
		List<EntityGUIObject> list = new ArrayList<EntityGUIObject>();
		for (int i = 0; i < arrEGO.length; i++)
		{
			list.add(arrEGO[i]);
		}
		list.add(singleEGO);
		arrEGO = new EntityGUIObject[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arrEGO[i] = list.get(i);
		}
		ged.setEntityGUIObjects(arrEGO);
	}
	
	static public void removeEntityGUIObjectFromArray (EntityGUIObject[] arrEGO)
	{
		if (arrEGO.length > 1)
		{
			List<EntityGUIObject> list = new ArrayList<EntityGUIObject>();
			for (int i = 0; i < arrEGO.length; i++)
			{
				list.add(arrEGO[i]);
			}
			list.remove(list.size() - 1);
			arrEGO = new EntityGUIObject[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				arrEGO[i] = list.get(i);
			}
		}
	}
	
	static public void setEGO_fromGuiTexture(GuiTexture guiTexture, EntityGUIObject eGO)
	{
		eGO.setElementID(guiTexture.getAdditionalNumber(0));
	}
	
	
	public static void testGuiLayers_click(GuiPanel panel, GuiEvents guiEvents, MouseListener mouse, float parentPositionX, float parentPositionY)
	{
		for (int i = 0; i < panel.panels.size(); i++)
		{
			if (panel.panels.get(i).visible)
			{
				guiEvents.testClick(panel.panels.get(i).guis, mouse
						, parentPositionX + panel.panels.get(i).position.x
						, parentPositionY + panel.panels.get(i).position.y);
				
				testGuiLayers_click(panel.panels.get(i), guiEvents, mouse
						, parentPositionX + panel.panels.get(i).position.x
						, parentPositionY + panel.panels.get(i).position.y);
			}
		}
	}
	
	public static void drawGuiLayers(GuiPanel panel, GuiRenderer guiRenderer, StaticShader shader, UI ui, float parentPositionX, float parentPositionY
			, Entity blankEntity, Texture tempTexture)
	{
		if (panel.visible)
		{
			guiRenderer.render(panel.guis, parentPositionX, parentPositionY);
			
			ui.prepareDrawing(blankEntity, shader, tempTexture);
			for (int k = 0; k < panel.guis.size(); k++)
			{
				if (panel.guis.get(k).isGuiObjectSet() && panel.guis.get(k).isVisible())
				{
					switch (panel.guis.get(k).getGuiObject().getTextPositionType())
					{
					case 0://centred
						ui.drawCentredString_toGui(panel.guis.get(k)
								, panel.guis.get(k).getGuiObject().getText()
								, panel.guis.get(k).getGuiObject().getTextColor()
								, panel.guis.get(k).getGuiObject().getTextSize()
								, 0, 0); 
						break;
						
					case 1://left
						ui.drawLeftString_toGui(panel.guis.get(k)
								, panel.guis.get(k).getGuiObject().getText()
								, panel.guis.get(k).getGuiObject().getTextColor()
								, panel.guis.get(k).getGuiObject().getTextSize()
								, parentPositionX, parentPositionY); 
						break;
						
					case 2://centred and up - for characters and players
						ui.drawCentredString_toGui(panel.guis.get(k)
								, panel.guis.get(k).getGuiObject().getText()
								, panel.guis.get(k).getGuiObject().getTextColor()
								, panel.guis.get(k).getGuiObject().getTextSize()
								, 0, -25f); 
						break;
						
					case 3://centred and up further - for items in inv
						ui.drawCentredString_toGui(panel.guis.get(k)
								, panel.guis.get(k).getGuiObject().getText()
								, panel.guis.get(k).getGuiObject().getTextColor()
								, panel.guis.get(k).getGuiObject().getTextSize()
								, 0, -32f); 
						break;
						
					case 4://down and right - for item amount in inv
						ui.drawCentredString_toGui(panel.guis.get(k)
								, panel.guis.get(k).getGuiObject().getText()
								, panel.guis.get(k).getGuiObject().getTextColor()
								, panel.guis.get(k).getGuiObject().getTextSize()
								, 25f, 25f); 
						break;
						
						default:
							break;
					}
				}
			}
			
			for (int i = 0; i < panel.panels.size(); i++)
			{
				drawGuiLayers(panel.panels.get(i), guiRenderer, shader, ui
						, parentPositionX + panel.panels.get(i).position.x, parentPositionY + panel.panels.get(i).position.y
						, blankEntity, tempTexture);
			}
		}
	}
	
}
