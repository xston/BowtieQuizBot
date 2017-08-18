package bowtie.quiz.impl;

import java.util.List;

import bowtie.bot.obj.Bot;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class MultipleChoiceQuestion extends Question{

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
	public MultipleChoiceQuestion(QuestionType type, Bot bot,
			String questionText, List<Answer> correctAnswers, List<Answer> wrongAnswers, String help,
			String image, int points, int time, int number){
		super(type, bot, questionText, correctAnswers, wrongAnswers,
				help, image, points, 0, time, number, 0);
		
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendQuestion()
	 */
	@Override
	public void sendQuestion() {
	}

	/**
	 * @see bowtie.quiz.obj.Question#sendWinners()
	 */
	@Override
	public void sendWinners() {
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
	public void sendFirstAnswer() {
	}

	/**
	 * @see bowtie.quiz.obj.Question#isCorrect(java.lang.String)
	 */
	@Override
	public int isCorrect(String answer) {
		return 0;
	}
}