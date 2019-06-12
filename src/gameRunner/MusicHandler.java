package gameRunner;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import serverFiles.DataPacket;

public class MusicHandler 
{
	private Clip currentClip;//the current audio clip which is being played
	private int NewClipID = 0;
	private int ClipID = -1;//current clip playing
	private int OldClipID = 0;//the ID of the last clip used
	private int musicChangeTimerMax = 10000;
	private int musicChangeTimer = 0;//to set some music to start with
	
	private Clip[] all_music;
	
	
	
	public MusicHandler() 
	{
		super();
		
		getMusic();
	}


	private void getMusic()
	{
		//set up variables to load the sound tracks in
		String[] music_locations = { "[SLOW] DarkMusic.wav" };//, "Autumn Lullaby.wav" };
		all_music = new Clip[music_locations.length];
		
		String basePath = "assets/music/";
		
		//get sound tracks
		for (int i = 0; i < music_locations.length; i++)
		{
			try
			{
				
				try{
					//File audioFile;
					AudioInputStream audioStream;
					AudioFormat format;
					
					audioStream = AudioSystem.getAudioInputStream( new File(basePath + music_locations[i]) );
					
					format = audioStream.getFormat();
					 
					DataLine.Info info = new DataLine.Info(Clip.class, format);
					
					all_music[i] = (Clip) AudioSystem.getLine(info);
				
					all_music[i].open(audioStream);//set music to start with the starting music
					
					System.out.println("Loaded Music: " + basePath + music_locations[i]);
				}
				catch (IOException e){System.out.println("Sound failed to load: " + e.getMessage());}
				catch (LineUnavailableException e){System.out.println("Sound failed to load: " + e.getMessage());}
				catch (UnsupportedAudioFileException e){System.out.println("Sound failed to load: " + e.getMessage());}
				
				//audioClip.close();
				//audioStream.close();
			}
			catch(Exception e)
			{
				System.out.println("Sound failed to load: " + e.getMessage());
			}
		}
	}
	
	public void runMusic(DataPacket dataPacket)
	{
		//change music
		if (musicChangeTimer < 0)
		{
			musicChangeTimer = musicChangeTimerMax;
			
			if (ClipID != -1)//if the game hasn't just been started?
			{
				all_music[ClipID].stop();//stop current music
			}//else set first track to be played
			
			
			while (NewClipID == ClipID || NewClipID == OldClipID)
			{
				NewClipID = 0;//GameData.world_backgroundMusic[dataPacket.cliWorldID][GameData.getRandomNumber(0, GameData.world_backgroundMusic[dataPacket.cliWorldID].length)];
			}
			
			System.out.println("music id: " + NewClipID);
			if (NewClipID >= 0 && NewClipID < all_music.length)
			{
				try {
					all_music[NewClipID].loop(Clip.LOOP_CONTINUOUSLY);
					ClipID = NewClipID;
					OldClipID = ClipID;
				} catch(NullPointerException e) { e.printStackTrace(); }
			}
			
		}
		else
		{
			musicChangeTimer--;
		}
	}
	
	
	//all getters and setters
	
	public Clip[] getAll_music() 
	{
		return all_music;
	}
	
	public Clip getAll_music(int index) 
	{
		return all_music[index];
	}
	
	public void stop()
	{
		if (ClipID > -1)
		{
			all_music[ClipID].stop();//stop current music
		}
	}
}
