package bowtie.bot.intf;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * @author &#8904
 *
 */
public interface CommandHandler {
	/**
	 * Dispatches the given event to the correct {@link bowtie.bot.obj.Command} by calling its
	 * {@link bowtie.bot.obj.Command#execute(MessageReceivedEvent)}
	 * 
	 * @param event The {@link MessageReceivedEvent} which should be dispatched.
	 */
	public void dispatch(MessageReceivedEvent event);
}