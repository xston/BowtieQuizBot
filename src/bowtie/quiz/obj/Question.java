package bowtie.quiz.obj;

import java.util.List;

import bowtie.bot.obj.Bot;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.impl.Answer;

/**
 * @author &#8904
 *
 */
public abstract class Question{
	private final QuestionType type;
	private final Bot bot;
	private final int points;
	private final int wrongPoints;
	private final int time;
	private final int number;
	private final int closestWinners;
	private List<Answer> correctAnswers;
	private List<Answer> wrongAnswers;
	private final String questionText;
	private final String help;
	private final String image;
	
	public Question(QuestionType type, Bot bot, String questionText, List<Answer> correctAnswers, List<Answer> wrongAnswers,
			String help, String image, int points, int wrongPoints, int time, int number, int closestWinners){
		this.type = type;
		this.bot = bot;
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

	public abstract void sendQuestion();
	
	public abstract void sendWinners();
	
	public abstract void sendAnswer();
	
	public abstract void sendFirstAnswer();
	
	public abstract int isCorrect(String answer);
}