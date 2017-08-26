package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.Colors;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.Properties;

/**
 * @author &#8904
 *
 */
public class VersionCommand extends Command {
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public VersionCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		String versionInformation = "```"
									+ "Bot version: "+Properties.getValueOf("botversion")+" \n"
									+ "Tool version: "+Properties.getValueOf("toolversion")+" \n"
									+ "Patcher version: "+Properties.getValueOf("patcherversion")+" \n"
									+ "Rebooter version: "+Properties.getValueOf("rebooterversion")
									+ "```";
		bot.sendMessage(versionInformation, event.getMessage().getChannel(), Colors.PURPLE);
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Get Versions Command \n"
				+ "<User> \n\n"
				+ "This will send you information about the current versions of the bot, the "
				+ "quiztool, the patcher and the rebooter."
				+ "\n\n\n"
				+ "Related Commands: \n"
				+ "- import"
				+ "```";
	}
}