package bowtie.bot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.Colors;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;

/**
 * Abstract class which provides basic connection and message methods for a Discord bot.
 * 
 * @author &#8904
 */
public abstract class Bot {
	/** The {@link IDiscordClient} instance used by this bot. */
	public IDiscordClient client;
	
	/** The list containing the registered {@link GuildObject}s. */
	private List<GuildObject> guilds;
	
	/** The list containing the registered users with creator permissions. */
	private List<IUser> creators;
	
	/** The application token. */
	private String token;
	
	/**
	 * Creates a new {@link Bot} instance and builds a {@link IDiscordClient} with the given token.
	 * 
	 * @param token The application token.
	 */
	public Bot(String token){
		this.token = token;
		buildClient(token);
		guilds = new ArrayList<GuildObject>();
		creators = new ArrayList<IUser>();
	}
	
	/**
	 * Creates a new {@link Bot} instance.
	 * <p>
	 * Call {@link #buildClient(String)} before calling Discord related methods.
	 * </p>
	 */
	public Bot(){
		guilds = new ArrayList<GuildObject>();
		creators = new ArrayList<IUser>();
	}
	
	/**
	 * Builds a {@link IDiscordClient} with the given token and assigns it to {@link #client}.
	 * <p>
	 * This called automatically if you use {@link #Bot(String)}.
	 * </p>
	 * 
	 * @param token The application token.
	 */
	public void buildClient(String token){
		this.token = token;
		ClientBuilder builder = new ClientBuilder()
									.withToken(token)
									.withRecommendedShardCount();
		try{
			client = builder.build();
		}catch (DiscordException e){ 
			Main.log.print(e);
		}
	}
	
	/**
	 * Logs the {@link #client} into Discord.
	 */
	public void login(){
		try{
			client.login();
		}catch(DiscordException e){
			Main.log.print(e);
		}
	}
	
	/**
	 * Logs the {@link #client} out of Discord.
	 */
	public void logout(){
		try{
			client.logout();
		}catch(DiscordException e){
			Main.log.print(e);
		}
	}
	
	/**
	 * Gets {@link #guilds} which contains all registered {@link GuildObject}s.
	 * 
	 * @return A {@link List} containing the registered {@link GuildObject}s.
	 */
	public List<GuildObject> getGuildObjects(){
		return guilds;
	}
	
	/**
	 * Sets the {@link #guilds}.
	 * @param guilds A {@link List} containing the {@link GuildObject}s.
	 */
	public void setGuildObjects(List<GuildObject> guilds){
		this.guilds = guilds;
	}
	
	/**
	 * Adds a single {@link GuildObject} to the {@link #guilds} list.
	 * 
	 * @param guild The {@link GuildObect} which should be added.
	 * @return true if the {@link GuildObject} was not yet contained in the {@link #guilds} list 
	 * and was added successfully.
	 */
	public boolean addGuildObject(GuildObject guild){
		if(!guilds.contains(guild)){
			return guilds.add(guild);
		}
		return false;
	}
	
	/**
	 * Remooves the given {@link GuildObject} from the {@link #guilds} list.
	 * 
	 * @param guild The {@link GuildObect} which should be removed.
	 * @return true if {@link #guilds} contained the given {@link GuildObject} and if it was
	 * successfully removed.
	 */
	public boolean removeGuildObject(GuildObject guild){
		return guilds.remove(guild);
	}
	
	/**
	 * Rermoves the {@link GuildObject} at the given position in the {@link #guilds} list.
	 * 
	 * @param index The index of the {@link GuildObject} which should be removed from {@link #guilds}.
	 * @return The removed {@link GuildObject}.
	 */
	public GuildObject removeGuildObject(int index){
		return guilds.remove(index);
	}
	
	/**
	 * Searches the {@link #guilds} list for a {@link GuildObject} with the given ID and 
	 * returns it.
	 * 
	 * @param guildID The String ID of the {@link GuildObject} that should be returned.
	 * @return The {@link GuildObject} with the given ID or null if no element in {@link #guilds}
	 * has the given ID.
	 */
	public GuildObject getGuildObjectByID(String guildID){
		for(GuildObject guild : guilds){
			if(guild.getGuild().getStringID().equals(guildID)){
				return guild;
			}
		}
		return null;
	}
	
	/**
	 * Gets {@link #creators} which contains all registered creators.
	 * 
	 * @return {@link #creators}.
	 */
	public List<IUser> getCreators(){
		return creators;
	}
	
	/**
	 * Checks if the given user is a registered creator.
	 * 
	 * @param user
	 * @return true if the user is a creator.
	 */
	public boolean isCreator(IUser user){
		return creators.contains(user);
	} 
	
	/**
	 * Adds the given user to the {@link #creators} list which enables him 
	 * to use every command.
	 * 
	 * @param user
	 * @return true if the user was not yet a creator and was successfully added to
	 * {@link #creators}.
	 */
	public boolean addCreator(IUser user){
		if(!creators.contains(user)){
			return creators.add(user);
		}
		return false;
	}
	
	/**
	 * Removes the given user from {@link #creators} if he was contained.
	 * 
	 * @param user
	 * @return 
	 */
	public boolean removeCreator(IUser user){
		return creators.remove(user);
	}
	
	/**
	 * Removes the {@link IUser} at the given position from {@link #creators}.
	 * 
	 * @param index
	 * @return
	 */
	public IUser removeCreator(int index){
		return creators.remove(index);
	}
	
	/**
	 * Gets the application token the bot is using to log into Discord with.
	 * @return The application token.
	 */
	public String getToken(){
		return token;
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param color The color of the embeded message.
	 * @param icon The icon that will be displayed in the foootnote.
	 * @param fast true if the message should be sent directly, false if it should be added to 
	 * the {@link RequestBuffer}.
	 */
	public void sendMessage(String message, IChannel channel, Color color, String icon, boolean fast){
		EmbedBuilder builder = new EmbedBuilder();
		builder.withDesc(message);
	    builder.withColor(color);
	    if(icon != null){
	    	builder.withFooterIcon(icon);
	    }
	    if(fast){
	    	channel.sendMessage(builder.build());
	    }else{
	    	RequestBuffer.request(() -> channel.sendMessage(builder.build()));
	    }
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param color The color of the embeded message.
	 * @param fast true if the message should be sent directly, false if it should be added to 
	 * the {@link RequestBuffer}.
	 */
	public void sendMessage(String message, IChannel channel, Color color, boolean fast){
		sendMessage(message, channel, color, null, fast);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 */
	public void sendMessage(String message, IChannel channel){
		sendMessage(message, channel, false);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param icon The icon that will be displayed in the foootnote.
	 */
	public void sendMessage(String message, IChannel channel, String icon){
		sendMessage(message, channel, Colors.DEFAULT, icon, false);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param color The color of the embeded message.
	 */
	public void sendMessage(String message, IChannel channel, Color color){
		sendMessage(message, channel, color, false);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param color The color of the embeded message.
	 * @param icon The icon that will be displayed in the foootnote.
	 */
	public void sendMessage(String message, IChannel channel, Color color, String icon){
		sendMessage(message, channel, color, icon, false);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param fast true if the message should be sent directly, false if it should be added to 
	 * the {@link RequestBuffer}.
	 */
	public void sendMessage(String message, IChannel channel, boolean fast){
		sendMessage(message, channel, Colors.DEFAULT, fast);
	}
	
	/**
	 * Sends an embedded message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param icon The icon that will be displayed in the foootnote.
	 * @param fast true if the message should be sent directly, false if it should be added to 
	 * the {@link RequestBuffer}.
	 */
	public void sendMessage(String message, IChannel channel, String icon, boolean fast){
		sendMessage(message, channel, Colors.DEFAULT, icon, fast);
	}
	
	/**
	 * Sends an embedded message with the elements from the list to the given channel. If the list contains more than 24 
	 * elements this method will create another embedded message.
	 * 
	 * @param title The text displayed on top of the first message.
	 * @param messages All the messages that should be listed.
	 * @param channel The channel to which the messages should be sent. Can be a private channel.
	 * @param color The color of the embeded messages.
	 * @param icon The icon that will be displayed in the foootnotes.
	 */
	public void sendListMessage(String title, List<String> messages, IChannel channel, Color color, String icon){
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withTitle(title);
		if(icon != null){
			builder.withFooterIcon(icon);
		}
	    builder.withColor(color);
	    List<EmbedObject> embedObjects = new ArrayList<EmbedObject>();
		for(int i = 0; i < messages.size(); i++){
			//adds up to 24 elements per embed
			if(builder.getFieldCount() < 24){
				builder.appendField(Integer.toString(i+1), messages.get(i), true);
			}else{
				//builds the full embed
				embedObjects.add(builder.build());
				//resets the builder and continues adding messages to the "new" embed
				builder.withTitle("");
				if(icon != null){
					builder.withFooterIcon(icon);
				}
			    builder.withColor(color);
				builder.clearFields();
				builder.appendField(Integer.toString(i+1), messages.get(i), true);
			}
		}
		//adds empty embeds to avoid weird shifting of the elements
		while(builder.getFieldCount() % 3 != 0){
			builder.appendField("", "", true);
		}
		//builds the last embed
		embedObjects.add(builder.build());
	    for(EmbedObject embed : embedObjects){
	    	RequestBuffer.request(() -> channel.sendMessage(embed));
	    	try{
	    		//short ddelay to ensure that messages are mostly sent in the corretc order
	    		Thread.sleep(100);
	    	}catch(Exception e){
	    	}
	    }
	}
	
	/**
	 * Sends an embedded message with the elements from the list to the given channel. If the list contains more than 24 
	 * elements this method will create another embedded message.
	 * 
	 * @param title The text displayed on top of the first message.
	 * @param messages All the messages that should be listed.
	 * @param channel The channel to which the messages should be sent. Can be a private channel.
	 * @param icon The icon that will be displayed in the foootnotes.
	 */
	public void sendListMessage(String title, List<String> messages, IChannel channel, String icon){
		sendListMessage(title, messages, channel, Colors.DEFAULT, icon);
	}
	
	/**
	 * Sends an embedded message with the elements from the list to the given channel. If the list contains more than 24 
	 * elements this method will create another embedded message.
	 * 
	 * @param title The text displayed on top of the first message.
	 * @param messages All the messages that should be listed.
	 * @param channel The channel to which the messages should be sent. Can be a private channel.
	 * @param color The color of the embeded messages.
	 */
	public void sendListMessage(String title, List<String> messages, IChannel channel, Color color){
		sendListMessage(title, messages, channel, color, null);
	}
	
	/**
	 * Sends an embedded message with the elements from the list. If the list contains more than 24 elements this method will 
	 * create another embedded message.
	 * 
	 * @param title The text displayed on top of the first message.
	 * @param messages All the messages that should be listed.
	 * @param channel The channel to which the messages should be sent. Can be a private channel.
	 */
	public void sendListMessage(String title, List<String> messages, IChannel channel){
		sendListMessage(title, messages, channel, Colors.DEFAULT, null);
	}
	
	/**
	 * Sends a standard text message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 * @param fast true if the message should be sent directly, false if it should be added to 
	 * the {@link RequestBuffer}.
	 */
	public void sendPlainMessage(String message, IChannel channel, boolean fast){
		if(fast){
			channel.sendMessage(message);
		}else{
			RequestBuffer.request(() -> channel.sendMessage(message));
		}
	}
	
	/**
	 * Sends a standard text message to the given channel.
	 * 
	 * @param message The text of the message.
	 * @param channel The channel to which the message should be sent. Can be a private channel.
	 */
	public void sendPlainMessage(String message, IChannel channel){
		sendPlainMessage(message, channel, false);
	}
}