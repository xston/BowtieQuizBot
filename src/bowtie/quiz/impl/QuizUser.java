package bowtie.quiz.impl;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.impl.QuizGuild;

/**
 * @author &#8904
 *
 */
public class QuizUser {
	private IUser user;
	private int score;
	private QuizGuild enteredGuild;
	
	public QuizUser(IUser user){
		this.user = user;
	}
	
	public String getStringID(){
		return user.getStringID();
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