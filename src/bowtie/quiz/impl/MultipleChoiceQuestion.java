package bowtie.quiz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class MultipleChoiceQuestion extends Question{
	private List<Answer> allAnswers;
	private boolean answersPrepared = false;
	private String correctLetter;
	
	/**
	 * @param type
	 * @param bot
	 * @param questionText
	 * @param correctAnswers
	 * @param wrongAnswers
	 * @param help
	 * @param image
	 * @param points
	 * @param time
	 * @param number
	 */
	public MultipleChoiceQuestion(QuestionType type, Bot bot, QuizGuild guild,
			String questionText, List<Answer> correctAnswers, List<Answer> wrongAnswers, String help,
			String image, int points, int time, int number){
		super(type, bot, guild, questionText, correctAnswers, wrongAnswers,
				help, image, points, 0, time, number, 0);
		
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendQuestion(IChannel)
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
		
		if(!answersPrepared){
			allAnswers = getWrongAnswers();
			if(allAnswers == null){
				allAnswers = getCorrectAnswers();
				//adds the correct letter to the correct answers
				getCorrectAnswers().add(new Answer(new String[]{indexToLetter(0)}));
			}else{
				//adds the correct answer at a random position
				int correctPosition = new Random().nextInt(allAnswers.size()+1);
				allAnswers.add(correctPosition, getCorrectAnswers().get(0));
				//adds the correct letter to the correct answers
				correctLetter = indexToLetter(correctPosition);
				getCorrectAnswers().add(new Answer(new String[]{correctLetter}));
			}
			answersPrepared = true;
		}
		
		for(int i = 0; i < allAnswers.size(); i++){
			if(i < 8){
				//only 8 answer options permitted
				builder.appendField(indexToLetter(i), allAnswers.get(i).get(), false);
			}
		}
		
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
	public void sendWinnersMessage(){
		//only if the mode isnt set to first, because duh
		if(getGuild().getQuestionManager().getCurrentMode() == QuizConstants.NORMAL_MODE){
			EmbedBuilder builder = new EmbedBuilder();
			builder.setLenient(true);
			builder.withTitle("Users with the correct answer:");
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
		builder.withTitle("Users with the correct answer:");
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
		builder.withTitle("Time is up! The correct answer was:");
		builder.withDescription(correctLetter+". "+getCorrectAnswers().get(0).get());
		builder.withColor(Colors.DEFAULT);
		RequestBuffer.request(() -> getGuild().getQuizChannel().sendMessage(builder.build())).get();
		getGuild().getChatLog().print("Question "+getNumber()+" ended.");
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendFirstAnswer(QuizUser)
	 */
	@Override
	public void sendFirstAnswer(QuizUser user){
		getGuild().getSoundManager().playSound(SoundManager.GONG_SOUND);
		EmbedBuilder builder = new EmbedBuilder();
		builder.withTitle(user.getName()+"#"+user.getDiscriminator()+" was first! The correct answer was:");
		builder.withDescription(correctLetter+". "+getCorrectAnswers().get(0).get());
		builder.withColor(Colors.DEFAULT);
		RequestBuffer.request(() -> getGuild().getQuizChannel().sendMessage(builder.build())).get();
		getGuild().getChatLog().print("Question "+getNumber()+" ended. "+user.getName()+"#"+user.getDiscriminator()+" was first.");
	}
	
	/**
	 * Converts the given index to a letter. 
	 * <p>
	 * The highest convertable index is 7.
	 * </p>
	 * <p>
	 * 0 -> A
	 * <br>
	 * 1 -> B
	 * <br>
	 * 2 -> C
	 * <br>
	 * ...
	 * <br>
	 * 7 -> H
	 * </p>
	 * 
	 * @param i The index.
	 * @return The letter.
	 */
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
			default:
				return "Options are limited to 8";
		}
	}
}