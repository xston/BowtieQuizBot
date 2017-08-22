package bowtie.bot.impl.cmnd;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class EndTieCommand extends Command {

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public EndTieCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);

	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		QuizGuild guild = (QuizGuild)event.getGuild();
		guild.getTieUsers().clear();;
		guild.setTieActive(false);
		RequestBuffer.request(() -> event.getMessage().addReaction(":white_check_mark:"));
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "End Tiebreaker Command \n"
				+ "<Master> \n\n"
				+ "Ends the current tiebreaker."
				+ "\n\n\n"
				+ "Related Commands: \n"
				+ "- tie"
				+ "```";
	}

}
