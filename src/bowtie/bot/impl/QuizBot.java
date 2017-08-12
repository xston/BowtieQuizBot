package bowtie.bot.impl;


import java.util.List;

import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.Bot;
import bowtie.bot.hand.MessageHandler;
import bowtie.bot.hand.ReadyHandler;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;
import bowtie.util.Permissions;
import bowtie.util.Properties;

/**
 * @author &#8904
 *
 */
public class QuizBot extends Bot{
	private Main main;
	
	/**
	 * @param token
	 */
	public QuizBot(String token, Main main) {
		super(token);
		this.main = main;
		Permissions.setBot(this);
		login();
		loadCreators();
	}
	
	public void registerHandlers(){
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new MessageHandler(this));
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
		Main.log.print("Loaded "+getCreators().size()+" creators.");
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