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

public class Music 
{
	public Clip getMusic()
	{
		//set up variables to load the sound tracks in
		String music_location = "assets/music/Autumn Lullaby.wav";
		//get sound tracks
		try
		{
			try{
				//File audioFile;
				AudioInputStream audioStream;
				AudioFormat format;
				
				//audioStream = AudioSystem.getAudioInputStream( getClass().getResource(music_location) );
				File f = new File(music_location);
				audioStream = AudioSystem.getAudioInputStream(f);
				format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				Clip current_music;
				current_music = (Clip) AudioSystem.getLine(info);
				current_music.open(audioStream);//set music to start with the starting music
				
				audioStream.close();
				
				current_music.loop(Clip.LOOP_CONTINUOUSLY);
				return current_music;
			}
			catch (IOException e){e.printStackTrace();}
			catch (LineUnavailableException e){e.printStackTrace();}
			catch (UnsupportedAudioFileException e){e.printStackTrace();}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
