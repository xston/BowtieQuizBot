package bowtie.bot.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.hand.GuildCommandHandler;
import bowtie.bot.impl.cmnd.AddMasterCommand;
import bowtie.bot.impl.cmnd.DiscSpaceCommand;
import bowtie.bot.impl.cmnd.EnterQuizCommand;
import bowtie.bot.impl.cmnd.ImportQuestionsCommand;
import bowtie.bot.impl.cmnd.JoinVoiceCommand;
import bowtie.bot.impl.cmnd.LeaveQuizCommand;
import bowtie.bot.impl.cmnd.LeaveVoiceCommand;
import bowtie.bot.impl.cmnd.NextQuestionCommand;
import bowtie.bot.impl.cmnd.RemoveMasterCommand;
import bowtie.bot.impl.cmnd.SelectQuestionCommand;
import bowtie.bot.impl.cmnd.SetQuizChannelCommand;
import bowtie.bot.impl.cmnd.ShowMastersCommand;
import bowtie.bot.impl.cmnd.ShutdownCommand;
import bowtie.bot.impl.cmnd.StatisticCommand;
import bowtie.bot.impl.cmnd.StopQuestionCommand;
import bowtie.bot.impl.cmnd.TestCommand;
import bowtie.bot.impl.cmnd.ThreadCountCommand;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;
import bowtie.quiz.hand.AnswerHandler;
import bowtie.quiz.hand.QuestionManager;
import bowtie.quiz.hand.SoundManager;
import bowtie.quiz.impl.QuizUser;
import bowtie.util.QuizPermissions;
import bowtie.util.Region;
import bowtie.util.log.Log;

/**
 * @author &#8904
 *
 */
public class QuizGuild extends GuildObject{
	private CommandHandler commandHandler;
	private AnswerHandler answerHandler;
	private QuestionManager questionManager;
	private SoundManager soundManager;
	private Bot bot;
	private IChannel quizChannel;
	private boolean isQuizActive= true;
	private Log chatLog;
	/** A {@link List} that contains the {@link QuizUser}s which have enetred the quiz. */
	private List<QuizUser> enteredUsers;
	/** The number of questions that were asked in this guild. Just for statistics. */
	private int askedQuestions;
	/** The number of answers that were given in this guild. Just for statistics. */
	private int receivedAnswers;

	/**
	 * @param guild
	 */
	public QuizGuild(IGuild guild, Bot bot){
		super(guild);
		this.bot = bot;
		commandHandler = new GuildCommandHandler(this);
		answerHandler = new AnswerHandler(this);
		questionManager = new QuestionManager(this);
		soundManager = new SoundManager(this);
		createQuestionFile();
		loadAskedQuestions();
		loadReceivedAnswers();
		loadMasters();
		chatLog = new Log("logs/chatLogs/"+getStringID()+"_chat.txt", Region.getTimeZone(guild));
		chatLog.logToSystemOut(false);
		enteredUsers = new ArrayList<QuizUser>();
		registerCommands();
	}

	private void registerCommands(){
		((GuildCommandHandler)commandHandler).addCommand(new ShutdownCommand(new String[]{"off", "offline", "shutdown"},
				QuizPermissions.CREATOR, bot))
				
		.addCommand(new TestCommand(new String[]{"test"}, 
				QuizPermissions.CREATOR))
				
		.addCommand(new DiscSpaceCommand(new String[]{"size", "space", "disc", "discspace"}, 
				QuizPermissions.CREATOR, bot))
				
		.addCommand(new ThreadCountCommand(new String[]{"threads", "thread", "threadcount", "activethreads"}, 
				QuizPermissions.CREATOR, bot))
				
		.addCommand(new StatisticCommand(new String[]{"stats", "statistics", "stat", "numbers"}, 
				QuizPermissions.CREATOR, bot))
				
		.addCommand(new SetQuizChannelCommand(new String[]{"setchannel", "quizchannel", "setquiz", "setquizchannel"},
				QuizPermissions.MASTER, bot))
				
		.addCommand(new ImportQuestionsCommand(new String[]{"load", "import", "loadquestions", "importquestions", "setquestions"}, 
				QuizPermissions.MASTER, bot))
				
		.addCommand(new AddMasterCommand(new String[]{"addmaster", "master", "newmaster"}, 
				QuizPermissions.MASTER, bot))
				
		.addCommand(new RemoveMasterCommand(new String[]{"nomaster", "removemaster", "deletemaster"}, 
				QuizPermissions.MASTER, bot))
				
		.addCommand(new ShowMastersCommand(new String[]{"masters", "showmasters", "getmasters"}, 
				QuizPermissions.MASTER, bot))
				
		.addCommand(new JoinVoiceCommand(new String[]{"join", "joinme", "joinvoice"}, 
				QuizPermissions.MASTER, bot))
				
		.addCommand(new LeaveVoiceCommand(new String[]{"exit", "leavevoice", "exitvoice", "leaveme"}, 
				QuizPermissions.MASTER))
				
		.addCommand(new NextQuestionCommand(new String[]{"next", "continue", "nextquestion"}, 
				QuizPermissions.MASTER))
				
		.addCommand(new SelectQuestionCommand(new String[]{"select", "choose", "selectquestion", "choosequestion"}, 
				QuizPermissions.MASTER))
				
		.addCommand(new StopQuestionCommand(new String[]{"stop", "stopquestion", "cancel", "cancelquestion"}, 
				QuizPermissions.MASTER))
				
		.addCommand(new EnterQuizCommand(new String[]{"enter", "enterquiz", "joinquiz"}, 
				QuizPermissions.USER, bot))
				
		.addCommand(new LeaveQuizCommand(new String[]{"leave", "leavequiz", "exitquiz"}, 
				QuizPermissions.USER, bot));
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
	 * Loads the master ID's from the database and adds the users to {@link GuildObject#masters}.
	 */
	private void loadMasters(){
		List<String> masterID = ((QuizBot) bot).getMain().getDatabase().getMasterID(getStringID());
		for(String id : masterID){
			IUser user = getGuild().getUserByID(Long.parseLong(id));
			if(user != null){
				addMaster(user);
			}else{
				//removes the database entry if the bot can't find the user on this guild
				((QuizBot) bot).getMain().getDatabase().removeMaster(getStringID(), id);
			}
		}
	}
	
	/**
	 * Adds a user to the {@link GuildObject#masters} list and saves the id in the database.
	 * @param user
	 */
	public boolean saveMaster(IUser user){
		if(addMaster(user)){
			((QuizBot) bot).getMain().getDatabase().addMaster(getStringID(), user.getStringID());
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a master from the {@link GuildObject#masters} list and the database.
	 * @param user
	 */
	public boolean deleteMaster(IUser user){
		if(removeMaster(user)){
			((QuizBot) bot).getMain().getDatabase().removeMaster(getStringID(), user.getStringID());
			return true;
		}
		return false;
	}
	
	/**
	 * Enters a user for a quiz.
	 * @param user
	 * @return true if the user was not yet added.
	 */
	public boolean enterQuizUser(QuizUser user){
		if(!isEnteredQuizUser(user)){
			return enteredUsers.add(user);
		}
		return false;
	}
	
	/**
	 * Enters a user for a quiz.
	 * @param user
	 * @return true if the user was not yet added.
	 */
	public boolean enterQuizUser(IUser user){
		return enterQuizUser(new QuizUser(user));
	}
	
	/**
	 * Removes the given user from the quiz.
	 * @param user
	 * @return	true if the user was removed.
	 */
	public boolean removeQuizUser(QuizUser user){
		return enteredUsers.remove(user);
	}
	
	/**
	 * Removes the given user from the quiz.
	 * @param user
	 * @return	true if the user was removed.
	 */
	public boolean removeQuizUser(IUser user){
		return enteredUsers.remove(user);
	}
	
	/**
	 * Checks if the given user is entered for the quiz.
	 * @param user
	 * @return 
	 */
	public boolean isEnteredQuizUser(QuizUser user){
		return enteredUsers.contains(user);
	}
	
	/**
	 * Checks if the given user is entered for the quiz.
	 * @param user
	 * @return 
	 */
	public boolean isEnteredQuizUser(IUser user){
		return enteredUsers.contains(user);
	}
	
	/**
	 * Gets the user with the given id if he is entered for the quiz. Null if not.
	 * @param id
	 * @return
	 */
	public QuizUser getEnteredQuizUser(String id){
		for(QuizUser user : enteredUsers){
			if(user.getStringID().equals(id)){
				return user;
			}
		}
		return null;
	}
	
	/**
	 * @return the chatLog
	 */
	public Log getChatLog() {
		return chatLog;
	}

	/**
	 * @return the questionManager
	 */
	public QuestionManager getQuestionManager() {
		return questionManager;
	}

	/**
	 * @return the soundManager
	 */
	public SoundManager getSoundManager() {
		return soundManager;
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
	
	public void loadAskedQuestions(){
		askedQuestions = ((QuizBot) bot).getMain().getDatabase().getAskedQuestions(getStringID());
		if(askedQuestions == -1){
			//if its the first time and the database does not have an entry for the guild yet
			askedQuestions = 0;
			((QuizBot) bot).getMain().getDatabase().setAskedQuestions(getStringID(), 0);
		}
	}
	
	public void saveAskedQuestions(){
		((QuizBot) bot).getMain().getDatabase().setAskedQuestions(getStringID(), askedQuestions);
	}

	/**
	 * @return the askedQuestions
	 */
	public int getAskedQuestions() {
		return askedQuestions;
	}

	/**
	 * @param askedQuestions the askedQuestions to set
	 */
	public void setAskedQuestions(int askedQuestions) {
		this.askedQuestions = askedQuestions;
	}
	
	public void addAskedQuestions(int questions){
		askedQuestions += questions;
	}

	public void loadReceivedAnswers(){
		receivedAnswers = ((QuizBot) bot).getMain().getDatabase().getReceivedAnswers(getStringID());
		if(receivedAnswers == -1){
			//if its the first time and the database does not have an entry for the guild yet
			receivedAnswers = 0;
			((QuizBot) bot).getMain().getDatabase().setReceivedAnswers(getStringID(), 0);
		}
	}
	
	public void saveReceivedAnswers(){
		((QuizBot) bot).getMain().getDatabase().setReceivedAnswers(getStringID(), receivedAnswers);
	}
	
	/**
	 * @return the receivedAnswers
	 */
	public int getReceivedAnswers() {
		return receivedAnswers;
	}

	/**
	 * @param receivedAnswers the receivedAnswers to set
	 */
	public void setReceivedAnswers(int receivedAnswers) {
		this.receivedAnswers = receivedAnswers;
	}
	
	public void addReceivedAnswers(int answers){
		receivedAnswers += answers;
	}
}