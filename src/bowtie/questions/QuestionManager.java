package bowtie.questions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.sampled.UnsupportedAudioFileException;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;
import bowtie.discord.Bot;
import bowtie.util.Log;

/**
 * @author &#8904
 *
 */
public class QuestionManager {
	private IGuild guild;
	private ArrayList<Question> questions;
	private int currentIndex;
	private Bot bot;
	private Question currentQuestion;
	public static boolean active = false;
	public static boolean tie = false;
	private String mode;
	public final static String FIRST = "first";
	public final static String ALL = "all";
	public AudioPlayer player;
	
	public QuestionManager(Bot bot){
		this.bot = bot;
		this.mode = "all";
	}
	
	public String getMode(){
		return mode;
	}
	
	public void setMode(String mode){
		this.mode = mode;
	}
	
	public void setQuestions(ArrayList<Question> questions){
		setPlayer();
		this.questions = questions;
		reset();
	}
	
	public void setPlayer(){
		player = AudioPlayer.getAudioPlayerForGuild(bot.client.getGuilds().get(0));
		player.setVolume(0.2f);
	}
	
	public void playSound(String path){
		if(bot.getCurrentVoiceChannel() != null){
			if(player == null){
				setPlayer();
			}
			try {
				stopSound();
				player.queue(new File("sounds/"+path));
				player.setLoop(false);
			} catch (IOException e) {
				Log.printSystem(e);
			} catch (UnsupportedAudioFileException e) {
				Log.printSystem(e);
			}
		}
	}
	
	public void playSound(String path, boolean loop){
		if(bot.getCurrentVoiceChannel() != null){
			if(player == null){
				setPlayer();
			}
			try {
				stopSound();
				player.queue(new File("sounds/"+path));
				player.setLoop(loop);
			} catch (IOException e) {
				Log.printSystem(e);
			} catch (UnsupportedAudioFileException e) {
				Log.printSystem(e);
			}
		}
	}
	
	public void stopSound(){
		if(player != null){
			player.clear();
		}
	}
	
	public void kill(){
		if(player != null){
			player.clean();
			player.clear();
		}
		Log.printSystem(new Date()+" Manager killed.");
	}
	
	public void reset(){
		currentIndex = 0;
	}
	
	public Question getCurrentQuestion(){
		return currentQuestion;
	}
	
	public void display(int index){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		if(questions.size() > index){
			bot.getAnswerListener().reset();
			currentQuestion = questions.get(index);
			currentQuestion.sendQuestion();
			currentIndex = index + 1;
		}
	}
	
	public void displayNext(){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		if(questions.size() > currentIndex){
			bot.getAnswerListener().reset();
			currentQuestion = questions.get(currentIndex);
			currentQuestion.sendQuestion();
			currentIndex++;
		}
	}
	
	public void stopCurrent(){
		active = false;
		currentQuestion.stopTimer();
		currentQuestion.sendAnswer();
		currentQuestion.sendScore();
	}
	
	public int isBonusAnswer(String answer){
		ArrayList<ArrayList<String>> bonus = currentQuestion.bonusAnswers;
		for(int i = 0; i < bonus.size(); i++){
			for(int j = 0; j < bonus.get(i).size(); j++){
				System.out.println(bonus.get(i).get(j).toLowerCase());
				if(bonus.get(i).get(j).toLowerCase().equals(answer.toLowerCase())){
					return i;
				}
			}
		}
		return -1;
	}
	
	public int isCorrect(String answer){
		ArrayList<ArrayList<String>> right = currentQuestion.rightAnswers;
		for(int i = 0; i < right.size(); i++){
			for(int j = 0; j < right.get(i).size(); j++){
				if(right.get(i).get(j).toLowerCase().equals(answer.toLowerCase())){
					return i;
				}
			}
		}
		return -1;
	}
}