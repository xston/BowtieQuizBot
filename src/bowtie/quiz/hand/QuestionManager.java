package bowtie.quiz.hand;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import bowtie.bot.impl.QuizGuild;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.impl.QuestionImporter;
import bowtie.quiz.impl.QuizUser;
import bowtie.quiz.obj.Question;

/**
 * A calss which handles the {@link Question}s for a {@link QuizGuild}.
 * <p>
 * Povides methods to set the questions for a quiz and navigate through them.
 * </p>
 * 
 * @author &#8904
 */
public class QuestionManager {
	/** The list of currently imported {@link Question}s. */
	private List<Question> questions;
	/** The current {@link Question}. */
	private Question currentQuestion;
	/** The index of the {@link #currentQuestion} inside the {@link #questions} list. */
	private int currentQuestionIndex;
	/** The mode that determines how answers are being handled. */
	private int currentMode = QuizConstants.NORMAL_MODE;
	/** The {@link QuizGuild} for which this instance is managing {@link Question}s. */
	private final QuizGuild guild;
	/** The {@link QuestionImporter} which loads the {@link Question}s for this instance. */
	private final QuestionImporter questionImporter;
	
	/**
	 * Creates a new instance for the given {@link QuizGuild} and initializes 
	 * a {@link QuestionImporter}.
	 * 
	 * @param guild
	 */
	public QuestionManager(QuizGuild guild){
		this.guild = guild;
		this.questionImporter = new QuestionImporter(guild);
	}
	
	/**
	 * Stops the {@link #currentQuestion} by cancelinmg its timer and calling
	 * its {@link Question#sendAnswer()}.
	 */
	public void stopCurrentQuestion(){
		guild.setQuizActive(false);
		currentQuestion.stopTimer();
		currentQuestion.sendAnswer();
	}
	
	/**
	 * Sends the next {@link Question}. 
	 * <p>
	 * This method wont do anything if there are no more {@link Question}s after the current one
	 * or if no questions were imported.
	 * </p>
	 * 
	 * @param errorChannel The channel that is used for error messages if the {@link QuizGuild#quizChannel}
	 * has not been set before.
	 */
	public void nextQuestion(IChannel errorChannel){
		if(questions == null){
			return;
		}
		if(currentQuestion != null){
			currentQuestion.stopTimer();
			currentQuestion.reset();
		}
		for(QuizUser user : guild.getEnteredQuizUsers()){
			//resets the current question score and given answers for every entered user
			user.setCurrentQuestionScore(0);
			user.getGivenAnswers().clear();
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
	
	/**
	 * Jumps to the {@link Question} at the given index inside {@link #questions}.
	 * <p>
	 * This method wont do anything if the index is out of bounds
	 * or if no questions were imported.
	 * </p>
	 * 
	 * @param index The index of the desired question inside the {@link #questions} list.
	 * @param errorChannel The channel that is used for error messages if the {@link QuizGuild#quizChannel}
	 * has not been set before.
	 */
	public void selectQuestion(int index, IChannel errorChannel){
		if(questions == null){
			return;
		}
		if(currentQuestion != null){
			currentQuestion.stopTimer();
			currentQuestion.reset();
		}
		for(QuizUser user : guild.getEnteredQuizUsers()){
			//resets the current question score and given answers for every entered user
			user.setCurrentQuestionScore(0);
			user.getGivenAnswers().clear();
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
	 * Stops the {@link #currentQuestion}, sets the {@link #currentQuestionIndex} to 0
	 * and calls {@link Question#reset()} on every imported question.
	 */
	public void reset(){
		if(currentQuestion != null){
			currentQuestion.stopTimer();
		}
		currentQuestion = null;
		currentQuestionIndex = 0;
		if(questions != null){
			for(Question question : questions){
				question.reset();
			}
		}
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