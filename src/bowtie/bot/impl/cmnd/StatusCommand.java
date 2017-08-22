package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.BotConstants;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class StatusCommand extends Command {
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public StatusCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		String status = event.getMessage().getContent().replace(BotConstants.PREFIX+"status", "").trim();
		bot.client.changePlayingText(status);
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Status Command\n"
				+ "<Creator>\n\n"
				+ "Sets the 'playing game' text of the bot."
				+ "```";
	}

}
