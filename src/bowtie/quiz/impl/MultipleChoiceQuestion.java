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
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class MultipleChoiceQuestion extends Question{
	private List<Answer> allAnswers;
	private boolean answersPrepared = false;
	
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
				getCorrectAnswers().add(new Answer(new String[]{indexToLetter(correctPosition)}));
			}
			answersPrepared = true;
		}
		
		for(int i = 0; i < allAnswers.size(); i++){
			builder.appendField(indexToLetter(i), allAnswers.get(i).get(), false);
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
			builder.withTitle("Users with the correct answer.");
			builder.withDescription("-------------------------------------------------------------");
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
		builder.withTitle("Users with the correct answer.");
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
		System.out.println("done");
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendFirstAnswer()
	 */
	@Override
	public void sendFirstAnswer(QuizUser user){
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
			default:
				return "I said 8, FFS.";
		}
	}

	/**
	 * @see bowtie.quiz.obj.Question#isCorrect(java.lang.String)
	 */
	@Override
	public int isCorrect(String answer){
		for(Answer correctAnswer : getCorrectAnswers()){
			if(correctAnswer.equals(answer)){
				if(correctAnswer.isBonusAnswer()){
					return QuizConstants.CORRECT_BONUS;
				}else{
					return QuizConstants.CORRECT_NORMAL;
				}
			}
		}
		return QuizConstants.WRONG;
	}
}