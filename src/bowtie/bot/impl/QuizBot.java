package bowtie.bot.impl;


import java.util.List;
import java.util.concurrent.Executors;

import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.hand.MasterLeaveHandler;
import bowtie.bot.hand.MessageHandler;
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
		String[] ids = Properties.getValueOf("creators").split(",");
		for(String id : ids){
			IUser creator = client.fetchUser(Long.parseLong(id.trim()));
			if(creator != null){
				addCreator(creator);
			}
		}
		Main.log.print(getCreators().size() > 1 ? "Registered "+getCreators().size()+" creators." : "Registered "+getCreators().size()+" creator.");
		return getCreators().size();
	}
	
	public Main getMain(){
		return main;
	}
	
	public int getGuildCount(){
		return client.getGuilds().size();
	}
	
	public int getTotalUserCount(){
		return client.getUsers().size();
	}
	
	public int getTotalQuestionCount(){
		return askedQuestions;
	}
	
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
	
	public QuizGuild getGuildForEnteredUser(IUser user){
		
		return null;
	}
	
	public void loadAskedQuestions(){
		askedQuestions = main.getDatabase().getAskedQuestions("-1");
		if(askedQuestions == -1){
			//if its the first time and the database does not have an entry for the bot yet
			askedQuestions = 0;
			main.getDatabase().setAskedQuestions("-1", 0);
		}
	}
	
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
	
	public void addAskedQuestions(int askedQuestions) {
		this.askedQuestions += askedQuestions;
	}
	
	public void loadReceivedAnswers(){
		receivedAnswers = main.getDatabase().getReceivedAnswers("-1");
		if(receivedAnswers == -1){
			//if its the first time and the database does not have an entry for the bot yet
			receivedAnswers = 0;
			main.getDatabase().setReceivedAnswers("-1", 0);
		}
	}
	
	public void saveReceivedAnswers(){
		main.getDatabase().setAskedQuestions("-1", receivedAnswers);
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
	
	public void addReceivedAnswers(int receivedAnswers) {
		this.receivedAnswers += receivedAnswers;
	}
}