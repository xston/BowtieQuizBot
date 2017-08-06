package bowtie.questions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import bowtie.discord.Bot;
import bowtie.util.Log;

/**
 * @author &#8904
 *
 */
public class Question {
	public String type;
	public ArrayList<ArrayList<String>> rightAnswers;
	public ArrayList<ArrayList<String>> wrongAnswers;
	private ArrayList<EmbedObject> answers;
	public String question;
	public String help;
	public String rightLetter;
	public ArrayList<ArrayList<String>> bonusAnswers;
	public int time;
	public int points;
	public int number;
	public int amountClosest;
	private int rightCount, wrongCount;
	public IMessage qMessage;
	public IMessage scMessage;
	public IMessage timeMessage;
	private IChannel channel;
	private Bot bot;
	private String image;
	private Timer timer;
	private boolean setup = false;
	
	
	public Question(String type, ArrayList<ArrayList<String>> rightAnswers, ArrayList<ArrayList<String>> wrongAnswers, 
			String question, String help, int time, int points, ArrayList<ArrayList<String>> bonusAnswer, int number, Bot bot, String image, int amountClosest){
		this.type = type;
		this.rightAnswers = rightAnswers;
		this.wrongAnswers = wrongAnswers;
		this.question = question;
		this.help = help;
		this.time = time;
		this.points = points;
		this.bonusAnswers = bonusAnswer;
		this.number = number;
		this.bot = bot;
		this.rightCount = rightAnswers.size();
		if(wrongAnswers != null){
			this.wrongCount = wrongAnswers.size();
		}
		this.image = image;
		this.answers = new ArrayList<EmbedObject>();
		this.amountClosest = amountClosest;
	}
	
	public IMessage sendQuestion(){
		bot.client.changePlayingText("Question "+number);
		Log.printChat(new Date()+"  QuizBot:   "+question);
		QuestionManager.active = true;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withAuthorName("Question "+number);
		if(type.equals("multipleanswer")){
			if(points > 1){
				builder.withTitle(time+" seconds. "+points+" points for each correct answer.");
			}else{
				builder.withTitle(time+" seconds. "+points+" point for each correct answer.");
			}
		}else if(type.equals("closestanswer")){
			builder.withTitle(time+" seconds. "+(points+amountClosest)+" points for the closest answer.");
		}else{
			builder.withTitle(time+" seconds. "+points+" points.");
		}
		builder.withDescription("**"+question+"**");
	    builder.withColor(133, 32, 201);
		if(type.equals("multiplechoice")){
			int rightIndex = new Random().nextInt(rightCount+wrongCount);
			int sizeWrong = wrongAnswers.size();
			int wrongIndex = 0;
			for(int i = 0; i < rightAnswers.size()+sizeWrong; i++){
				if(rightIndex == i){
					builder.appendField(indexToLetter(i), rightAnswers.get(0).get(0), false);
					rightLetter = indexToLetter(i).toLowerCase();
				}else{
					if(wrongIndex <  wrongAnswers.size()){
						builder.appendField(indexToLetter(i), wrongAnswers.get(wrongIndex).get(0), false);
						wrongIndex++;
					}
				}
			}
			if(!setup){
				ArrayList<String> letter = new ArrayList<String>();
				letter.add(rightLetter);
				rightAnswers.add(letter);
				setup = true;
			}else{
				rightAnswers.remove(rightAnswers.size()-1);
				ArrayList<String> letter = new ArrayList<String>();
				letter.add(rightLetter);
				rightAnswers.add(letter);
			}
		}
		if(image != null){
			builder.withImage(image);
		}
		if(help != null){
			builder.withFooterText(help);
		}
		IChannel channel = bot.getQuizChannel();
		if(channel == null){
			channel = bot.getCurrentChannel();
		}
		qMessage = channel.sendMessage(builder.build());
		timer();
		return qMessage;
	}
	
	public IMessage sendScore(){
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		if(type.equals("multipleanswer")){
			builder.withTitle("People with correct answers:");
		}else if(type.equals("closestanswer")){
			builder.withTitle("People with the closest answer:");
		}else{
			builder.withTitle("People with the correct answer:");
		}
		builder.withDescription("-------------------------------------------------------------");
	    builder.withColor(28, 112, 209); //blue
	    
	    if(type.equals("closestanswer")){
	    	bot.getAnswerListener().mergeClosest();
	    }
	    
	    for(int i = 0; i < bot.getAnswerListener().users.size(); i++){
	    	builder.appendField(bot.getAnswerListener().users.get(i).user.getDisplayName(bot.getCurrentChannel().getGuild())
	    			+"#"+bot.getAnswerListener().users.get(i).user.getDiscriminator(), 
	 	    		"Total: "+bot.getAnswerListener().users.get(i).score, true);
	    }
	    
	    IChannel channel = bot.getQuizChannel();
		if(channel == null){
			channel = bot.getCurrentChannel();
		}
	    scMessage = channel.sendMessage(builder.build());
		return scMessage;
	}
	
	private void timer(){
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				sendAnswer();
				bot.getManager().playSound("gong.mp3");
			    QuestionManager.active = false;
			    QuestionManager.tie = false;
			    sendScore();
				timer.cancel();
			}
		}, time*1000);
	}
	
	public void sendAnswer(){
		bot.client.changePlayingText(bot.status);
		answers.clear();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		if(type.equals("multipleanswer")){
			builder.withTitle("Time is up! The correct answers were:");
		}else{
			builder.withTitle("Time is up! The correct answer was:");
		}
		builder.withColor(28, 209, 82); 
		channel = bot.getQuizChannel();
		if(channel == null){
			channel = bot.getCurrentChannel();
		}
	    if(type.equals("multiplechoice")){
			builder.withDescription("**"+rightLetter.toUpperCase()+".  "+rightAnswers.get(0).get(0)+"**");
		}else if(type.equals("multipleanswer")){
			int amount = 0;
			for(int i = 0; i < rightAnswers.size(); i++){
				if(amount < 24){
					builder.appendField(Integer.toString(i+1), rightAnswers.get(i).get(0), true);
					amount++;
				}else{
					answers.add(builder.build());
					builder.withTitle("");
					builder.clearFields();
					builder.appendField(Integer.toString(i+1), rightAnswers.get(i).get(0), true);
					amount = 1;
				}
			}
		}else{
			builder.withDescription("**"+rightAnswers.get(0).get(0)+"**");
		}
	    answers.add(builder.build());
	    for(int i = 0; i < answers.size(); i++){
	    	EmbedObject currentObject = answers.get(i);
	    	RequestBuffer.request(() -> channel.sendMessage(currentObject));
	    	try{
	    		Thread.sleep(100);
	    	}catch(Exception e){
	    		Log.printSystem(e);
	    	}
	    }
	    if(type.equals("multiplechoice")){
	    	Log.printChat(new Date()+"  QuizBot:   Question "+number+" over. The correct letter was "+rightLetter+".");
	    }else{
	    	Log.printChat(new Date()+"  QuizBot:   Question "+number+" over.");
	    }
	}
	
	public void stopTimer(){
		timer.cancel();
	}
	
	public void sendFirstAnswer(IUser user){
		bot.client.changePlayingText(bot.status);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withTitle(user.getDisplayName(bot.getQuizChannel().getGuild())+"#"+user.getDiscriminator()+" was first!");
		builder.withColor(28, 209, 82); //green
		if(type.equals("multiplechoice")){
			builder.appendField("The correct answer was:", "**"+rightLetter.toUpperCase()+".  "+rightAnswers.get(0).get(0)+"**", true);
		}else if(type.equals("freeanswer")){
			builder.appendField("The correct answer was:", "**"+rightAnswers.get(0).get(0)+"**", true);
		}
		bot.getQuizChannel().sendMessage(builder.build());
		if(type.equals("multiplechoice")){
			Log.printChat(new Date()+"  QuizBot:   Question "+number+" over. The correct letter was "+rightLetter+".");
	    }else{
	    	Log.printChat(new Date()+"  QuizBot:   Question "+number+" over.");
	    }
	}
	
	public String indexToLetter(int i){
		switch(i){
			case 0:
				return "A";
			case 1:
				return "B";
			case 2:
				return "C";
			case 3:
				return "D";
			case 4:
				return "E";
			case 5:
				return "F";
			case 6:
				return "G";
			case 7:
				return "H";
			case 8:
				return "I";
			default:
				return "NULL";
		}
	}
	
	@Override
	public String toString(){
		return new String("Type: "+type+" || Question: "+question);
	}
}