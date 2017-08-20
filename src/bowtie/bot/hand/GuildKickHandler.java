package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.GuildObject;

/**
 * @author &#8904
 *
 */
public class GuildKickHandler implements IListener<GuildLeaveEvent>{
	private Bot bot;
	
	public GuildKickHandler(Bot bot){
		this.bot = bot;
	}
	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(GuildLeaveEvent event){
		GuildObject guild = bot.getGuildObjectByID(event.getGuild().getStringID());
		bot.removeGuildObject(guild);
		((QuizBot)bot).getMain().getDatabase().deleteGuild(guild.getStringID());
		bot.sendMessage("Bot was kicked from '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")", 
				bot.client.getApplicationOwner().getOrCreatePMChannel(), Colors.RED);
	}
}