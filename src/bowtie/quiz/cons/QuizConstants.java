package bowtie.quiz.cons;

import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public final class QuizConstants {
	/** Quiz mode during which only the first correct answer will win. */
	public static final int FIRST_MODE = 1;
	/** Quiz mode during which every correct answer wins. */
	public static final int NORMAL_MODE = 2;
	
	/** Returned by {@link Question#isCorrect(String)} if the given answer is wrong. */
	public static final int WRONG = -1;
	/** Returned by {@link Question#isCorrect(String)} if the given answer is a correct normal (non-bonus) answer. */
	public static final int CORRECT_NORMAL = 1;
	/** Returned by {@link Question#isCorrect(String)} if the given answer is a correct bonus answer. */
	public static final int CORRECT_BONUS = 2;
	
	/** The link to the question mark image which is displayed in the question message. */
	public static final String QUESTION_MARK_IMAGE = "https://cdn.discordapp.com/attachments/333386581832630273/348274046367956992/questionmark.png";
	public static final String QUIZ_BOT_ICON = "https://cdn.discordapp.com/attachments/333386581832630273/348279177041936395/bowtie_glasses_small.png";
}