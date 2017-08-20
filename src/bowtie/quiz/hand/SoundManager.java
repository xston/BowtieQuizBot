package bowtie.quiz.hand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import sx.blah.discord.util.audio.AudioPlayer;
import bowtie.bot.impl.QuizGuild;
import bowtie.core.Main;

/**
 * @author &#8904
 *
 */
public class SoundManager{
	public static final int PING_SOUND = 0;
	public static final int GONG_SOUND = 1;
	
	private QuizGuild guild;
	private File pingSoundFile;
	private File gongSoundFile;
	private List<File> soundList;
	private AudioPlayer audioPlayer;
	
	public SoundManager(QuizGuild guild){
		this.guild = guild;
		loadSounds();
		setAudioPlayer();
	}
	
	public void setAudioPlayer(){
		audioPlayer = AudioPlayer.getAudioPlayerForGuild(guild.getGuild());
	}
	
	private void loadSounds(){
		soundList = new ArrayList<File>();
		pingSoundFile = new File("sounds/ping.mp3");
		gongSoundFile = new File("sounds/gong.mp3");
		soundList.add(pingSoundFile);
		soundList.add(gongSoundFile);
	}
	
	public void playSound(int sound){
		if(guild.getGuild().getConnectedVoiceChannel() != null){
			stopSound();
			try {
				audioPlayer.queue(AudioSystem.getAudioInputStream(soundList.get(sound)));
			} catch (IOException | UnsupportedAudioFileException e) {
				Main.log.print(e);
			}
		}
	}
	
	public void stopSound(){
		if(audioPlayer != null){
			audioPlayer.clear();
		}
	}
}