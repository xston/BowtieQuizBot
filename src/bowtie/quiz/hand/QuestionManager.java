package bowtie.quiz.hand;

import java.util.List;

import bowtie.bot.impl.QuizGuild;
import bowtie.quiz.impl.QuestionImporter;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class QuestionManager {
	private List<Question> questions;
	private QuizGuild guild;
	private QuestionImporter questionImporter;
	
	public QuestionManager(QuizGuild guild){
		this.guild = guild;
		this.questionImporter = new QuestionImporter(guild);
	}
	
	/**
	 * @return the questionImporter
	 */
	public QuestionImporter getQuestionImporter() {
		return questionImporter;
	}

	public void setQuestions(List<Question> questions){
		this.questions = questions;
	}
}