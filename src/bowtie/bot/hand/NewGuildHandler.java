package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;

/**
 * Handles {@link GuildCreateEvent}s which is received when the bot is added to a new
 * guild.
 * @author &#8904
 */
public class NewGuildHandler implements IListener<GuildCreateEvent>{
	private Bot bot;
	
	/**
	 * Creates a new instance for the given bot.
	 * @param bot
	 */
	public NewGuildHandler(Bot bot){
		this.bot = bot;
	}

	/**
	 * Creates the {@link GuildObject} and registeres the guild owner as the first master.
	 * 
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(GuildCreateEvent event){
		if(Bot.isReady){ //to make sure its a new guild. else this happens after every boot
			QuizGuild guild = new QuizGuild(event.getGuild(), bot);
			if(bot.addGuildObject(guild)){
				guild.saveMaster(event.getGuild().getOwner());
				bot.sendMessage("Bot was added  to '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")", 
						bot.client.getApplicationOwner().getOrCreatePMChannel(), Colors.GREEN);
			}
		}
	}
}