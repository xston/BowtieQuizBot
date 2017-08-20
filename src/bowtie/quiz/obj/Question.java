package bowtie.quiz.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.impl.cmnd.StopQuestionCommand;
import bowtie.bot.obj.Bot;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.impl.Answer;
import bowtie.quiz.impl.QuizUser;

/**
 * @author &#8904
 *
 */
public abstract class Question{
	/** The {@link QuestionType} describing the kind of this question. */
	private final QuestionType type;
	/** The {@link Bot} instance that uses this question. */
	private final Bot bot;
	/** The {@link QuizGuild} for whichs quiz this question is used. */
	private final QuizGuild guild;
	/** The base amount of points for a correct answer. */
	private final int points;
	/** The points that a user will lose for a wrong answer during a {@link QuestionType#Multipleanswer} question. */
	private final int wrongPoints;
	/** The time for this question in seconds. */
	private final int time;
	/** The number that was assigned to this question. */
	private final int number;
	/** The amount of users that will receive points during a {@link QuestionType#Closestanswer} question. */
	private final int closestWinners;
	/** The answers that will be considered correct. */
	private List<Answer> correctAnswers;
	/** The wrong answer options during a {@link QuestionType#Multiplechoice} question. */
	private List<Answer> wrongAnswers;
	/** The actual question that is being asked. */
	private final String questionText;
	/** A help text which is displayed at the bottom of the question message. Should be used to 
	 * 	tell users how to answer this specific question, how many answers there are etc. */
	private final String help;
	/** An image link whichs content will be displayed in the question message. */
	private final String image;
	/** A {@link Timer} instance which keeps track of the remaining time for this question. */
	private Timer questionTimer;
	/** The messages displaying the users that scored points during this question. */
	private List<IMessage> winnersMessages;
	/** A {@link List} containing the users that scored points during this question. */
	private List<QuizUser> winners;
	/** A {@link Timer} instance which calls {@link #updateWinnersMessage()} every second. */
	private Timer messageUpdater;
	
	public Question(QuestionType type, Bot bot, QuizGuild guild, String questionText, List<Answer> correctAnswers, List<Answer> wrongAnswers,
			String help, String image, int points, int wrongPoints, int time, int number, int closestWinners){
		this.type = type;
		this.bot = bot;
		this.guild = guild;
		this.points = points;
		this.wrongPoints = wrongPoints;
		this.time = time;
		this.number = number;
		this.closestWinners = closestWinners;
		this.correctAnswers = correctAnswers;
		this.wrongAnswers = wrongAnswers;
		this.questionText = questionText;
		this.help = help;
		this.image = image;
		winners = new ArrayList<QuizUser>();
		winnersMessages = new ArrayList<IMessage>();
	}
	
	/**
	 * @return the winnersMessages
	 */
	public List<IMessage> getWinnersMessages() {
		return winnersMessages;
	}

	/**
	 * @param winnersMessages the winnersMessages to set
	 */
	public void setWinnersMessages(List<IMessage> winnersMessages) {
		this.winnersMessages = winnersMessages;
	}

	/**
	 * @return the winners
	 */
	public List<QuizUser> getWinners() {
		return winners;
	}

	/**
	 * @param winners the winners to set
	 */
	public void setWinners(List<QuizUser> winners) {
		this.winners = winners;
	}
	
	public boolean addWinner(QuizUser winner){
		if(!winners.contains(winner)){
			return winners.add(winner);
		}
		return false;
	}

	/**
	 * @return the guild
	 */
	public QuizGuild getGuild() {
		return guild;
	}

	/**
	 * @return the type
	 */
	public QuestionType getType() {
		return type;
	}

	/**
	 * @return the bot
	 */
	public Bot getBot() {
		return bot;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @return the wrongPoints
	 */
	public int getWrongPoints() {
		return wrongPoints;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the closestWinners
	 */
	public int getClosestWinners() {
		return closestWinners;
	}

	/**
	 * @return the correctAnswers
	 */
	public List<Answer> getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * @return the wrongAnswers
	 */
	public List<Answer> getWrongAnswers() {
		return wrongAnswers;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @return the help
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * Sets the {@link #questionTimer}.
	 * <p>
	 * After the timer has expired {@link #sendAnswer()} and 
	 * {@link #sendWinnersMessage()} will be called.
	 */
	public void setTimer(){
		questionTimer = new Timer();
		questionTimer.schedule(new TimerTask(){
			@Override
			public void run(){
				sendAnswer();
				guild.setQuizActive(false);
				stopMessageUpdater();
			    guild.saveAskedQuestions();
			    guild.saveReceivedAnswers();
			    ((QuizBot)bot).saveAskedQuestions();
			    ((QuizBot)bot).saveReceivedAnswers();
			    questionTimer.cancel();
			}
		}, time*1000);
	}
	
	/**
	 * Stops the {@link #questionTimer}.
	 */
	public void stopTimer(){
		questionTimer.cancel();
		guild.setQuizActive(false);
		stopMessageUpdater();
		guild.saveAskedQuestions();
	    guild.saveReceivedAnswers();
	    ((QuizBot)bot).saveAskedQuestions();
	    ((QuizBot)bot).saveReceivedAnswers();
	}
	
	/**
	 * Starts a {@link Timer} which calls {@link #updateWinnersMessage()} every second.
	 */
	public void startMessageUpdater(){
		messageUpdater = new Timer();
		messageUpdater.schedule(new TimerTask(){
			@Override
			public void run(){
				updateWinnersMessage();
			}
		}, 1000, 1000);
	}
	
	public void stopMessageUpdater(){
		if(messageUpdater != null){
			messageUpdater.cancel();
		}
	}
	
	/**
	 * Clears {@link #winners} and {@link #winnersMessages}.
	 */
	public void resetWinners(){
		winners.clear();
		winnersMessages.clear();
	}

	/**
	 * Sends the message containing the question.
	 * 
	 * @param errorChannel The channel that the command was used in. Will be used to send error 
	 * messages the {@link QuizGuild#quizChannel} was not set.
	 * @return true if the message was sent.
	 */
	public abstract boolean sendQuestion(IChannel errorChannel);
	
	/**
	 * Sends the {@link #winnersMessages} containing the users that got a correct answer for this question.
	 */
	public abstract void sendWinnersMessage();
	
	/**
	 * Updates the {@link #winnersMessages} with the {@link #winners} list.
	 */
	public abstract void updateWinnersMessage();
	
	/**
	 * Sends the answer message when the timer has run out or 
	 * a quiz master uses the {@link StopQuestionCommand}.
	 */
	public abstract void sendAnswer();
	
	/**
	 * Sends the answer message when a user got the correct answer during 
	 * {@link QuizConstants#FIRST_MODE}.
	 */
	public abstract void sendFirstAnswer(QuizUser user);
	
	/**
	 * Checks if the given answer is considered correct.
	 * 
	 * @param answer The answer of the user.
	 * @return 
	 * <ul>
	 * 		<li>{@link QuizConstants#WRONG} = -1</li>
	 * 		<li>{@link QuizConstants#CORRECT_NORMAL} = 1</li>
	 * 		<li>{@link QuizConstants#CORRECT_BONUS} = 2</li>
	 * </ul>
	 */
	public abstract int isCorrect(String answer);
}