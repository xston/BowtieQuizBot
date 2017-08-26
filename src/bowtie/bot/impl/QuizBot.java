package bowtie.bot.impl;


import java.util.List;
import java.util.concurrent.Executors;

import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.hand.GuildKickHandler;
import bowtie.bot.hand.MasterLeaveHandler;
import bowtie.bot.hand.MessageHandler;
import bowtie.bot.hand.NewGuildHandler;
import bowtie.bot.hand.ReadyHandler;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;
import bowtie.util.Properties;
import bowtie.util.QuizPermissions;

/**
 * A {@link Bot} implementation which provides more quiz specific methods.
 * 
 * @author &#8904
 */
public class QuizBot extends Bot{
	/** The {@link Main} instance of this bot. */
	private Main main;
	
	private int askedQuestions;
	private int receivedAnswers;
	
	/**
	 * Creates a new bot and logs it in with the given token.
	 * 
	 * @param token The application token which is used to log into Discord.
	 * @param main The {@link Main} instance.
	 */
	public QuizBot(String token, Main main){
		super(token);
		this.main = main;
		QuizPermissions.setBot(this);
		loadAskedQuestions();
		loadReceivedAnswers();
		registerHandlers();
		login();
	}
	
	/**
	 * Registers the handlers to the {@link Bot#client}s {@link EventDispatcher}.
	 */
	private void registerHandlers(){
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(Executors.newCachedThreadPool(), new MessageHandler(this));
		dispatcher.registerListener(Executors.newCachedThreadPool(), new MasterLeaveHandler(this));
		dispatcher.registerListener(Executors.newCachedThreadPool(), new NewGuildHandler(this));
		dispatcher.registerListener(Executors.newCachedThreadPool(), new GuildKickHandler(this));
		dispatcher.registerListener(new ReadyHandler(this));
	}
	
	/**
	 * Creates a {@link GuildObject} for each connected guild.
	 */
	public void createGuildObjects(){
		List<IGuild> connectedGuilds = client.getGuilds();
		for(IGuild guild : connectedGuilds){
			addGuildObject(new QuizGuild(guild, this));
		}
	}
	
	/**
	 * Loads the ID's from the 'creators' field in the property file.
	 * <p>
	 * ID's loaded by this are treated as creators and are able to use
	 * any command on any guild.
	 * </p>
	 */
	public int loadCreators(){
		String[] ids = Properties.getValueOf("creators").split(" ");
		for(String id : ids){
			IUser creator = client.fetchUser(Long.parseLong(id.trim()));
			if(creator != null){
				addCreator(creator);
			}
		}
		Main.log.print(getCreators().size() > 1 ? "Registered "+getCreators().size()+" creators." : "Registered "+getCreators().size()+" creator.");
		return getCreators().size();
	}
	
	/**
	 * Gets the {@link Main} instance of this bot.
	 * @return
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Gets the number of guilds the {@link Bot#client} is connected to.
	 * @return The number of guilds.
	 */
	public int getGuildCount(){
		return client.getGuilds().size();
	}
	
	/**
	 * Gets the number of users that are visible to the bot.
	 * @return
	 */
	public int getTotalUserCount(){
		return client.getUsers().size();
	}
	
	/**
	 * Gets the total number of masters registered to the {@link GuildOObject}s.
	 * @return
	 */
	public int getTotalMasterCount(){
		List<GuildObject> guilds = getGuildObjects();
		int count = 0;
		for(GuildObject guild : guilds){
			count += guild.getMasters().size();
		}
		return count;
	}
	
	/**
	 * Getss the total number of questions this bot has asked.
	 * @return
	 */
	public int getTotalQuestionCount(){
		return askedQuestions;
	}
	
	/**
	 * Getss the total number of answers this bot has received.
	 * @return
	 */
	public int getTotalAnswersCount(){
		return receivedAnswers;
	}
	
	/**
	 * Checks if the given user is a master on any of the registered guilds.
	 * 
	 * @param user
	 * @return
	 */
	public boolean isMaster(IUser user){
		for(GuildObject guild : getGuildObjects()){
			if(guild.isMaster(user)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the {@link QuizGuild} the given user is entered on. 
	 * 
	 * @param user
	 * @return The guild or null if the user is not entered on any guild.
	 */
	public QuizGuild getGuildForEnteredUser(IUser user){
		for(GuildObject guild : getGuildObjects()){
			QuizGuild quizGuild = (QuizGuild)guild;
			if(quizGuild.isEnteredQuizUser(user)){
				return quizGuild;
			}
		}
		return null;
	}
	
	/**
	 * Loads the total number of asked questions from the dataabse.
	 */
	public void loadAskedQuestions(){
		askedQuestions = main.getDatabase().getAskedQuestions("-1");
		if(askedQuestions == -1){
			//if its the first time and the database does not have an entry for the bot yet
			askedQuestions = 0;
			main.getDatabase().setAskedQuestions("-1", 0);
		}
	}
	
	/**
	 * Saves the total number of asked questions to the database.
	 */
	public void saveAskedQuestions(){
		main.getDatabase().setAskedQuestions("-1", askedQuestions);
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
	
	/**
	 * Adds the given number to the total count of asked questions.
	 * @param askedQuestions
	 */
	public void addAskedQuestions(int askedQuestions) {
		this.askedQuestions += askedQuestions;
	}
	
	/**
	 * Loads the total number of received answers from the dataabse.
	 */
	public void loadReceivedAnswers(){
		receivedAnswers = main.getDatabase().getReceivedAnswers("-1");
		if(receivedAnswers == -1){
			//if its the first time and the database does not have an entry for the bot yet
			receivedAnswers = 0;
			main.getDatabase().setReceivedAnswers("-1", 0);
		}
	}
	
	/**
	 * Saves the total number of received answers to the database.
	 */
	public void saveReceivedAnswers(){
		main.getDatabase().setReceivedAnswers("-1", receivedAnswers);
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
	
	/**
	 * Adds the given number to the total count of received answers.
	 * @param receivedAnswers
	 */
	public void addReceivedAnswers(int receivedAnswers) {
		this.receivedAnswers += receivedAnswers;
	}
}