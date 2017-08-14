package bowtie.bot.impl;

import java.io.File;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import bowtie.bot.Bot;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.hand.GuildCommandHandler;
import bowtie.bot.impl.cmnd.SetQuizChannelCommand;
import bowtie.bot.impl.cmnd.ShutdownCommand;
import bowtie.bot.impl.cmnd.ThreadCountCommand;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;
import bowtie.quiz.hand.AnswerHandler;
import bowtie.util.Region;
import bowtie.util.log.Log;

/**
 * @author &#8904
 *
 */
public class QuizGuild extends GuildObject{
	private CommandHandler commandHandler;
	private AnswerHandler answerHandler;
	private Bot bot;
	private IChannel quizChannel;
	private boolean isQuizActive= true;
	private Log chatLog;

	/**
	 * @param guild
	 */
	public QuizGuild(IGuild guild, Bot bot){
		super(guild);
		this.bot = bot;
		commandHandler = new GuildCommandHandler(this);
		answerHandler = new AnswerHandler(this);
		createQuestionFile();
		chatLog = new Log("logs/chatLogs/"+getStringID()+"_chat.txt", Region.getTimeZone(guild));
		chatLog.logToSystemOut(false);
		registerCommands();
	}
	
	private void registerCommands(){
		((GuildCommandHandler)commandHandler).addCommand(new ShutdownCommand(new String[]{"off", "shutdown"}, BotConstants.CREATOR_PERMISSION, bot))
		.addCommand(new SetQuizChannelCommand(new String[]{"setchannel", "quizchannel", "setquiz", "setquizchannel"}, BotConstants.MASTER_PERMISSION, bot))
		.addCommand(new ThreadCountCommand(new String[]{"threads", "thread", "threadcount", "activethreads"}, BotConstants.CREATOR_PERMISSION, bot));
	}
	
	private void createQuestionFile(){
		try{
			File file = new File("questions/"+getStringID()+"_questions.btq");
			file.getParentFile().mkdirs();
			file.createNewFile();
		}catch(Exception e){
			Main.log.print(e);
		}
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