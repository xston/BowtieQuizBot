package bowtie.bot.impl;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import bowtie.bot.Bot;
import bowtie.bot.hand.GuildCommandHandler;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.GuildObject;
import bowtie.quiz.hand.AnswerHandler;

/**
 * @author &#8904
 *
 */
public class QuizGuild extends GuildObject{
	private CommandHandler commandHandler;
	private AnswerHandler answerHandler;
	private Bot bot;
	private IChannel quizChannel;
	private boolean isQuizActive= false;

	/**
	 * @param guild
	 */
	public QuizGuild(IGuild guild, Bot bot){
		super(guild);
		this.bot = bot;
		commandHandler = new GuildCommandHandler(this);
		answerHandler = new AnswerHandler(this);
	}

	/**
	 * @return the answerHandler
	 */
	public AnswerHandler getAnswerHandler() {
		return answerHandler;
	}
	
	/**
	 * @return the commandHandler
	 */
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	/**
	 * @return the bot
	 */
	public Bot getBot() {
		return bot;
	}
	
	/**
	 * @return the quizChannel
	 */
	public IChannel getQuizChannel() {
		return quizChannel;
	}

	/**
	 * @param quizChannel the quizChannel to set
	 */
	public void setQuizChannel(IChannel quizChannel) {
		this.quizChannel = quizChannel;
	}
	
	public boolean isQuizActive(){
		return isQuizActive;
	}
	
	public void setQuizActive(boolean active){
		isQuizActive = active;
	}
}