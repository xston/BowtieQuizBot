package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.GuildObject;

/**
 * @author &#8904
 *
 */
public class MasterLeaveHandler implements IListener<UserLeaveEvent>{
	private Bot bot;
	
	public MasterLeaveHandler(Bot bot){
		this. bot = bot;
	}

	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(UserLeaveEvent event){
		IUser user = event.getUser();
		IGuild guild = event.getGuild();
		GuildObject guildObject = bot.getGuildObjectByID(guild.getStringID());
		if(guildObject.isMaster(user)){
			((QuizGuild)guildObject).deleteMaster(user);
		}
	}
}