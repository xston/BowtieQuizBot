package bowtie.quiz.impl;

import java.util.ArrayList;
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
public class FreeAnswerQuestion extends Question{

	/**
	 * @param type
	 * @param bot
	 * @param questionText
	 * @param correctAnswers
	 * @param wrongAnswers
	 * @param bonusAnswers
	 * @param help
	 * @param image
	 * @param points
	 * @param time
	 * @param number
	 * @param closestWinners
	 */
	public FreeAnswerQuestion(QuestionType type, Bot bot, QuizGuild guild, String questionText,
			List<Answer> correctAnswers, String help, 
			String image, int points, int time, int number) {
		super(type, bot, guild, questionText, correctAnswers, null,
				help, image, points, 0, time, number, 0);
		
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
		builder.withTitle("`"+getTime()+" seconds. "+getPoints()+" points.`");
		
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
		//only if the mode isnt set to first, because duh
		if(getGuild().getQuestionManager().getCurrentMode() == QuizConstants.NORMAL_MODE){
			EmbedBuilder builder = new EmbedBuilder();
			builder.setLenient(true);
			builder.withTitle("Users with correct answers:");
			builder.withDescription("------------------------------------------------------------------------------");
			builder.withColor(Colors.GREEN);
			IChannel channel = getGuild().getQuizChannel();
			RequestBuffer.request(() -> getWinnersMessages().add(channel.sendMessage(builder.build()))).get();
			startMessageUpdater();
		}
	}
	
	/**
	 * @see bowtie.quiz.obj.Question#updateWinnersMessage()
	 */
	@Override
	public void updateWinnersMessage() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withTitle("Users with correct answers:");
		builder.withDescription("------------------------------------------------------------------------------");
		builder.withColor(Colors.GREEN);
	    List<EmbedObject> embedObjects = new ArrayList<EmbedObject>();
		for(int i = 0; i < getWinners().size(); i++){
			//adds up to 24 elements per embed
			if(builder.getFieldCount() < 24){
				builder.appendField(getWinners().get(i).getName()+"#"+getWinners().get(i).getDiscriminator(), 
						Integer.toString(getWinners().get(i).getCurrentQuestionScore()), true);
			}else{
				//builds the full embed
				embedObjects.add(builder.build());
				//resets the builder and continues adding messages to the "new" embed
				builder.withTitle("");
				builder.withDescription("");
				builder.withColor(Colors.GREEN);
				builder.clearFields();
				builder.appendField(getWinners().get(i).getName()+"#"+getWinners().get(i).getDiscriminator(), 
						Integer.toString(getWinners().get(i).getCurrentQuestionScore()), true);
			}
		}
		embedObjects.add(builder.build());
		IChannel channel = getGuild().getQuizChannel();
	    for(int i = 0; i < embedObjects.size(); i++){
	    	EmbedObject embed = embedObjects.get(i);
	    	//picks an existing message and edits it or sends a new one
	    	if(i < getWinnersMessages().size()){
	    		int messageIndex = i;
	    		RequestBuffer.request(() -> getWinnersMessages().get(messageIndex).edit(embed));
	    	}else{
	    		RequestBuffer.request(() -> getWinnersMessages().add(channel.sendMessage(embed))).get();
	    	}
	    }
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendAnswer()
	 */
	@Override
	public void sendAnswer() {
		getGuild().getSoundManager().playSound(SoundManager.GONG_SOUND);
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle(getCorrectAnswers().size() > 1 ? "Time is up! The correct answers were:" : "Time is up! The correct answer was:");
		builder.withColor(Colors.DEFAULT);
		for(int i = 0; i < getCorrectAnswers().size(); i++){
			builder.appendField(Integer.toString(i+1), getCorrectAnswers().get(i).get(), false);
		}
		RequestBuffer.request(() -> getGuild().getQuizChannel().sendMessage(builder.build())).get();
		getGuild().getChatLog().print("Question "+getNumber()+" ended.");
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendFirstAnswer(QuizUser)
	 */
	@Override
	public void sendFirstAnswer(QuizUser user) {
		getGuild().getSoundManager().playSound(SoundManager.GONG_SOUND);
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle(getCorrectAnswers().size() > 1 ? user.getName()+"#"+user.getDiscriminator()+" was first! The correct answers were:" : 
			user.getName()+"#"+user.getDiscriminator()+" was first! The correct answer was:");
		builder.withColor(Colors.DEFAULT);
		for(int i = 0; i < getCorrectAnswers().size(); i++){
			builder.appendField(Integer.toString(i+1), getCorrectAnswers().get(i).get(), false);
		}
		RequestBuffer.request(() -> getGuild().getQuizChannel().sendMessage(builder.build())).get();
		getGuild().getChatLog().print("Question "+getNumber()+" ended. "+user.getName()+"#"+user.getDiscriminator()+" was first.");
	}
}