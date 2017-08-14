package bowtie.evnt.impl;

import sx.blah.discord.handle.obj.IMessage;
import bowtie.bot.obj.GuildObject;
import bowtie.evnt.QuizEvent;

/**
 * @author &#8904
 *
 */
public class CommandEvent implements QuizEvent{
	private final GuildObject guild;
	private final IMessage message;
	
	public CommandEvent(GuildObject guild, IMessage message){
		this. guild = guild;
		this.message = message;
	}
	
	public CommandEvent(IMessage message){
		this.message = message;
		this.guild = null;
	}

	/**
	 * @return the guild
	 */
	public GuildObject getGuild() {
		return guild;
	}

	/**
	 * @return the message
	 */
	public IMessage getMessage() {
		return message;
	}
}