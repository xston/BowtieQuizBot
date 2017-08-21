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
 * Superclass for every question.
 * 
 * @author &#8904
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
	/** A {@link List} containing the users that have given an answer durin the question. Only relevant for {@link QuestionType#Closestanswer}
	 *  and {@link QuestionType#Multiplecoice} because only one final answer is permitted. */
	private List<QuizUser> answeredUsers;
	/** A {@link Timer} instance which calls {@link #updateWinnersMessage()} in certain intervalls. */
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
		answeredUsers = new ArrayList<QuizUser>();
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
	 * @return the answeredUsers
	 */
	public List<QuizUser> getAnsweredUsers() {
		return answeredUsers;
	}

	/**
	 * @param answeredUsers the answeredUsers to set
	 */
	public void setAnsweredUsers(List<QuizUser> answeredUsers) {
		this.answeredUsers = answeredUsers;
	}
	
	/**
	 * Adds the given {@link QuizUser} to {@link #answeredUsers} if it is 
	 * not contained yet.
	 * 
	 * @param answeredUser
	 * @return true if successfully added, false if contained or unsuccessfull.
	 */
	public boolean addAnsweredUser(QuizUser answeredUser){
		if(!answeredUsers.contains(answeredUser)){
			return answeredUsers.add(answeredUser);
		}
		return false;
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
	
	/**
	 * Adds the given {@link QuizUser} to {@link #winners} if it is 
	 * not contained yet.
	 * 
	 * @param winner
	 * @return true if successfully added, false if contained or unsuccessfull.
	 */
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
	 * Starts a {@link Timer} which calls {@link #updateWinnersMessage()} in certain intervalls.
	 */
	public void startMessageUpdater(){
		messageUpdater = new Timer();
		messageUpdater.schedule(new TimerTask(){
			@Override
			public void run(){
				updateWinnersMessage();
			}
		}, 1000, 1500);
	}
	
	public void stopMessageUpdater(){
		if(messageUpdater != null){
			messageUpdater.cancel();
		}
	}
	
	/**
	 * Clears {@link #winners}, {@link #winnersMessages} and {@link #answeredUsers}.
	 */
	public void reset(){
		winners.clear();
		winnersMessages.clear();
		answeredUsers.clear();
	}
	
	/**
	 * Returns the element in {@link #correctAnswers} which has the given String
	 * as one of its {@link Answer#variations}.
	 * 
	 * @param variation
	 * @return The {@link Answer} or null if none of the elements in {@link #correctAnswers}
	 * contains the given String.
	 */
	public Answer getAnswerForString(String variation){
		for(Answer answer : correctAnswers){
			if(answer.equals(variation)){
				return answer;
			}
		}
		return null;
	}
	
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

	/**
	 * Sends the message containing the question.
	 * 
	 * @param errorChannel The channel that the command was used in. Will be used to send error 
	 * messages if the {@link QuizGuild#quizChannel} was not set.
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
}