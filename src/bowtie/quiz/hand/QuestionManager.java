package bowtie.quiz.hand;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import bowtie.bot.impl.QuizGuild;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.impl.QuestionImporter;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class QuestionManager {
	private List<Question> questions;
	private Question currentQuestion;
	private int currentQuestionIndex;
	private int currentMode = QuizConstants.NORMAL_MODE;
	private final QuizGuild guild;
	private final QuestionImporter questionImporter;
	
	public QuestionManager(QuizGuild guild){
		this.guild = guild;
		this.questionImporter = new QuestionImporter(guild);
	}
	
	public void stopCurrentQuestion(){
		guild.setQuizActive(false);
		currentQuestion.stopTimer();
		currentQuestion.sendAnswer();
	}
	
	public void nextQuestion(IChannel errorChannel){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		if(questions.size() > currentQuestionIndex){
			Question question = questions.get(currentQuestionIndex);
			if(question.sendQuestion(errorChannel)){
				currentQuestion = question;
				currentQuestion.sendWinnersMessage();
				currentQuestionIndex++;
			}
		}
	}
	
	public void selectQuestion(int index, IChannel errorChannel){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		if(questions.size() > index){
			Question question = questions.get(index);
			if(question.sendQuestion(errorChannel)){
				currentQuestion = question;
				currentQuestion.sendWinnersMessage();
				currentQuestionIndex = index + 1;
			}
		}
	}
	
	/**
	 * @return the questionImporter
	 */
	public QuestionImporter getQuestionImporter() {
		return questionImporter;
	}
	
	/**
	 * @return the questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		currentQuestionIndex = 0;
		this.questions = questions;
	}

	/**
	 * @return the currentQuestion
	 */
	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	/**
	 * @param currentQuestion the currentQuestion to set
	 */
	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	/**
	 * @return the currentQuestionIndex
	 */
	public int getCurrentQuestionIndex() {
		return currentQuestionIndex;
	}

	/**
	 * @param currentQuestionIndex the currentQuestionIndex to set
	 */
	public void setCurrentQuestionIndex(int currentQuestionIndex) {
		this.currentQuestionIndex = currentQuestionIndex;
	}

	/**
	 * @return the currentMode
	 */
	public int getCurrentMode() {
		return currentMode;
	}

	/**
	 * @param currentMode the currentMode to set
	 */
	public void setCurrentMode(int currentMode) {
		this.currentMode = currentMode;
	}

	/**
	 * @return the guild
	 */
	public QuizGuild getGuild() {
		return guild;
	}
}