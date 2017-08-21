package bowtie.quiz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.hand.SoundManager;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class ClosestAnswerQuestion extends Question{

	/**
	 * @param type
	 * @param bot
	 * @param questionText
	 * @param correctAnswers
	 * @param help
	 * @param image
	 * @param points
	 * @param time
	 * @param number
	 * @param closestWinners
	 */
	public ClosestAnswerQuestion(QuestionType type, Bot bot, QuizGuild guild,
			String questionText, List<Answer> correctAnswers, String help,
			String image, int points, int time, int number, int closestWinners) {
		super(type, bot, guild, questionText, correctAnswers, null,
				help, image, points, 0, time, number, closestWinners);
		
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendQuestion()
	 */
	@Override
	public boolean sendQuestion(IChannel errorChannel) {
		getGuild().getChatLog().print("Question "+getNumber());
		getGuild().getChatLog().print(getQuestionText());
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withAuthorName(getType().toString()+" Question "+getNumber());
		builder.withDescription("**"+getQuestionText()+"**");
		builder.withColor(Colors.PURPLE);
		builder.withAuthorIcon(QuizConstants.QUESTION_MARK_IMAGE);
		builder.withThumbnail(QuizConstants.QUIZ_BOT_ICON);
		builder.withTitle("`"+getTime()+" seconds. "+(getPoints()+getClosestWinners())+" max. points. "+getClosestWinners()+" winners.`");
		
		if(getImage() != null){
			builder.withImage(getImage());
		}
		if(getHelp() != null){
			builder.withFooterText(getHelp());
		}
		
		IChannel channel = getGuild().getQuizChannel();
		if(channel != null){
			if(channel.getModifiedPermissions(getBot().client.getOurUser()).contains(Permissions.SEND_MESSAGES)){
				RequestBuffer.request(() -> channel.sendMessage(builder.build())).get();
				getGuild().setQuizActive(true);
				setTimer();
				getGuild().addAskedQuestions(1);
				((QuizBot)getBot()).addAskedQuestions(1);
				return true;
		    }else{
		    	getGuild().getChatLog().print("ERROR");
		    	getGuild().getChatLog().print("		Channel: "+channel.getName());
		    	getGuild().getChatLog().print("		Missing SEND_MESSAGES permission.");
		    }
		}else{
			getBot().sendMessage("You have to set a quiz channel with `"+BotConstants.PREFIX+"setchannel` first.", errorChannel, Colors.RED);
		}
		return false;
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendWinnersMessage()
	 */
	@Override
	public void sendWinnersMessage() {
		//winners are announced in #sendAnswer() after the timer has ran out
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendAnswer()
	 */
	@Override
	public void sendAnswer() {
		getGuild().getSoundManager().playSound(SoundManager.GONG_SOUND);
		finalizeWinners();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withTitle("Time is up! The people with the closest answers are:");
		builder.withColor(Colors.DEFAULT);
	    List<EmbedObject> embedObjects = new ArrayList<EmbedObject>();
		for(int i = 0; i < getWinners().size(); i++){
			//adds up to 24 elements per embed
			if(builder.getFieldCount() < 24){
				builder.appendField(Integer.toString(i+1), getWinners().get(i).getName()+"#"+getWinners().get(i).getDiscriminator(), true);
			}else{
				//builds the full embed
				embedObjects.add(builder.build());
				//resets the builder and continues adding messages to the "new" embed
				builder.withTitle("");
				builder.withDescription("");
				builder.withColor(Colors.DEFAULT);
				builder.clearFields();
				builder.appendField(Integer.toString(i+1), getWinners().get(i).getName()+"#"+getWinners().get(i).getDiscriminator(), true);
			}
		}
		//adds empty embeds to avoid weird shifting of the elements
		while(builder.getFieldCount() % 3 != 0){
			builder.appendField("-", "-", true);
		}
		embedObjects.add(builder.build());
		IChannel channel = getGuild().getQuizChannel();
		for(EmbedObject embed : embedObjects){
	    	RequestBuffer.request(() -> channel.sendMessage(embed)).get();
	    }
		getGuild().getChatLog().print("Question "+getNumber()+" ended.");
	}
	
	private void sortClosestAnswers(){
		Collections.sort(getAnsweredUsers(), new Comparator<QuizUser>(){
			@Override
			public int compare(QuizUser o1, QuizUser o2) {
				return getGuild().formNumberString(o1.getClosestDiff()).compareTo(getGuild().formNumberString(o2.getClosestDiff()));
			}
	    });
	}
	
	private void finalizeWinners(){
		sortClosestAnswers();
		int bonusPoints = getClosestWinners();
		for(int i = 0; i < getClosestWinners(); i++){
			if(i < getAnsweredUsers().size()){
				getAnsweredUsers().get(i).addCurrentQuestionScore(getPoints()+bonusPoints);
				getAnsweredUsers().get(i).addScore(getPoints()+bonusPoints);
				addWinner(getAnsweredUsers().get(i));
				bonusPoints--;
			}
		}
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendFirstAnswer()
	 */
	@Override
	public void sendFirstAnswer(QuizUser user) {
		//no first mode for closestanswer questions
	}

	/**
	 * @see bowtie.quiz.obj.Question#updateWinnersMessage()
	 */
	@Override
	public void updateWinnersMessage() {
	}
}