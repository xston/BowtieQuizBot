package bowtie.quiz.impl;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.quiz.enu.QuestionType;
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
		return false;
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendWinnersMessage()
	 */
	@Override
	public void sendWinnersMessage() {
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendAnswer()
	 */
	@Override
	public void sendAnswer() {
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendFirstAnswer()
	 */
	@Override
	public void sendFirstAnswer(QuizUser user) {
	}

	/**
	 * @see bowtie.quiz.obj.Question#isCorrect(java.lang.String)
	 */
	@Override
	public int isCorrect(String answer) {
		return 0;
	}

	/**
	 * @see bowtie.quiz.obj.Question#updateWinnersMessage()
	 */
	@Override
	public void updateWinnersMessage() {
	}
}