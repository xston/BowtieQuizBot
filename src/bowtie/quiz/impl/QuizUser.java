package bowtie.quiz.impl;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.impl.QuizGuild;

/**
 * @author &#8904
 *
 */
public class QuizUser{
	/** The {@link IUser} this instance is representing. */
	private IUser user;
	/** The total score of this user. */
	private int score;
	/** The score this user reached during the current question. */
	private int currentQuestionScore;
	/** The guild that the user entered a quiz on. */
	private QuizGuild enteredGuild;
	
	public QuizUser(IUser user){
		this.user = user;
	}
	
	public String getStringID(){
		return user.getStringID();
	}
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	
	public String getName(){
		return user.getName();
	}
	
	public String getDiscriminator(){
		return user.getDiscriminator();
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}

	/**
	 * @return the currentQuestionScore
	 */
	public int getCurrentQuestionScore() {
		return currentQuestionScore;
	}

	/**
	 * @param currentQuestionScore the currentQuestionScore to set
	 */
	public void setCurrentQuestionScore(int currentQuestionScore) {
		this.currentQuestionScore = currentQuestionScore;
	}
	
	public void addCurrentQuestionScore(int currentQuestionScore) {
		this.currentQuestionScore += currentQuestionScore;
	}

	/**
	 * @return the enteredGuild
	 */
	public QuizGuild getEnteredGuild() {
		return enteredGuild;
	}

	/**
	 * @param enteredGuild the enteredGuild to set
	 */
	public void setEnteredGuild(QuizGuild enteredGuild) {
		this.enteredGuild = enteredGuild;
	}

	/**
	 * @return the user
	 */
	public IUser getUser() {
		return user;
	}
	
	public void reset(){
		score = 0;
		currentQuestionScore = 0;
		enteredGuild = null;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof IUser && ((IUser)o).getStringID().equals(this.getStringID())){
			return true;
		}
		if(o instanceof QuizUser && ((QuizUser)o).getStringID().equals(this.getStringID())){
			return true;
		}
		return false;
	}
}