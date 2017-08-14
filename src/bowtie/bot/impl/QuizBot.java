package bowtie.bot.impl;


import java.util.List;
import java.util.concurrent.Executors;

import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.Bot;
import bowtie.bot.hand.MessageHandler;
import bowtie.bot.hand.ReadyHandler;
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
	
	/**
	 * Creates a new bot and logs it in with the given token.
	 * 
	 * @param token The application token which is used to log into Discord.
	 * @param main The {@link Main} instance.
	 */
	public QuizBot(String token, Main main) {
		super(token);
		this.main = main;
		QuizPermissions.setBot(this);
		registerHandlers();
		login();
	}
	
	/**
	 * Registers the handlers to the {@link Bot#client}s {@link EventDispatcher}.
	 */
	private void registerHandlers(){
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(Executors.newCachedThreadPool(), new MessageHandler(this));
		dispatcher.registerListener(new ReadyHandler(this));
	}
	
	public void createGuildObjects(){
		List<IGuild> connectedGuilds = client.getGuilds();
		for(IGuild guild : connectedGuilds){
			addGuildObject(new QuizGuild(guild, this));
		}
	}
	
	public void loadCreators(){
		String[] ids = Properties.getValueOf("creators").split(",");
		for(String id : ids){
			IUser creator = client.fetchUser(Long.parseLong(id.trim()));
			if(creator != null){
				addCreator(creator);
			}
		}
		Main.log.print(getCreators().size() > 1 ? "Registered "+getCreators().size()+" creators." : "Registered "+getCreators().size()+" creator.");
	}
	
	public Main getMain(){
		return main;
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
}