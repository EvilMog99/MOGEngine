package gameRunner;

import java.io.Serializable;
import java.util.Random;

public class Block implements Serializable
{
//	private int blkID = 0;
//	private int blkTextureNo = 0;
//	private int blkAnimationNo = 0;
//	private int blkFrameNo = 0;
//	
//	private float blkBreakTimer = 0f;
//
//
//	private Random rnd;
//	
//	public Block(Random rnd)
//	{
//		this.rnd = rnd;
//	}
//	
//	
//	
//	//all getters and setters
//	
//	public int getBlkTextureNo() {
//		return blkTextureNo;
//	}
//
//	public void setBlkTextureNo(int blkTextureNo) {
//		this.blkTextureNo = blkTextureNo;
//	}
//
//	public int getBlkAnimationNo() {
//		return blkAnimationNo;
//	}
//
//	public void setBlkAnimationNo(int blkAnimationNo) {
//		this.blkAnimationNo = blkAnimationNo;
//	}
//
//	public int getBlkFrameNo() {
//		return blkFrameNo;
//	}
//
//	public void setBlkFrameNo(int blkFrameNo) {
//		this.blkFrameNo = blkFrameNo;
//	}
//
//	public int getBlkID() {
//		return blkID;
//	}
//	
//	public float getBlkBreakTimer() {
//		return blkBreakTimer;
//	}
//
//	public void setBlkBreakTimer(float blkBreakTimer) {
//		this.blkBreakTimer = blkBreakTimer;
//	}
//
//	public void addToBlkBreakTimer(float add) {
//		
//		if (this.blkBreakTimer + add > GameData.getBlockBreakTime(blkID))
//		{
//			this.blkBreakTimer = GameData.getBlockBreakTime(blkID);
//		}
//		else
//		{
//			this.blkBreakTimer += add;
//		}
//	}
//	
//	public void removeFromBlkBreakTimer(float minus) {
//		
//		if (this.blkBreakTimer - minus < 0)
//		{
//			this.blkBreakTimer = 0;
//		}
//		else
//		{
//			this.blkBreakTimer -= minus;
//		}
//	}
//	
//	
//	public void setBlkID(int blkID, Dimension wld) {
//		
//		
//		/*if (blkID > -1 && GameData.allBlockData[blkID][2] != 0)//if the old block was important to process
//		{
//			switch (GameData.allBlockData[blkID][2])
//			{
//			case 3://fast
//				wld.allFastActiveBlocks.remove(this);
//				break;
//			
//			case 2://medium
//				wld.allMediumActiveBlocks.remove(this);
//				break;
//				
//			default://slow
//				wld.allSlowActiveBlocks.remove(this);
//				break;
//			}
//		}*/
//		
//		this.blkID = blkID;
//		
//		blkBreakTimer = GameData.getBlockBreakTime(blkID);
//		
//		blkFrameNo = 0;
//		blkAnimationNo = 0;
//		
//		if (blkID > -1)
//		{
//			blkTextureNo = rnd.nextInt(GameData.allBlockData[blkID][0]);
//		}
//		else
//		{
//			blkTextureNo = 0;
//		}
//		
//		if (blkID > -1 && GameData.allBlockData[blkID][2] != 0)//if this block is important to process
//		{
//			switch (GameData.allBlockData[blkID][2])
//			{
//			case 3://fast
//				//wld.allFastActiveBlocks.add(this);
//				break;
//			
//			case 2://medium
//				//wld.allMediumActiveBlocks.add(this);
//				break;
//				
//			default://slow
//				//wld.allSlowActiveBlocks.add(this);
//				break;
//			}
//		}
//		
//	}
}
