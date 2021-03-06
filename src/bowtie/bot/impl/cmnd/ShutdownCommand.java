package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;
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
		bot.sendMessage(":skull:", event.getMessage().getChannel(), Colors.ORANGE, true);
		((QuizBot)bot).getMain().kill(true);
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Shutdown Command \n"
				+ "<Creator> \n\n"
				+ "This command will shut the bot down. \n\n\n"
				+ "Related Commands: \n"
				+ "- reboot \n"
				+ "- patch"
				+ "```";
	}
}