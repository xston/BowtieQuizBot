package bowtie.bot.impl.cmnd;

import bowtie.bot.Bot;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ShutdownCommand extends Command{
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ShutdownCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		bot.sendMessage(":skull:", event.getMessage().getChannel(), true);
		((QuizBot)bot).getMain().kill(true);
	}
}