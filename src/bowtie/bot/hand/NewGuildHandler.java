package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;

/**
 * @author &#8904
 *
 */
public class NewGuildHandler implements IListener<GuildCreateEvent>{
	private Bot bot;
	
	public NewGuildHandler(Bot bot){
		this.bot = bot;
	}

	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(GuildCreateEvent event){
		if(Bot.isReady){
			QuizGuild guild = new QuizGuild(event.getGuild(), bot);
			if(bot.addGuildObject(guild)){
				guild.saveMaster(event.getGuild().getOwner());
				bot.sendMessage("Bot was added  to '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")", 
						bot.client.getApplicationOwner().getOrCreatePMChannel(), Colors.GREEN);
			}
		}
	}
}