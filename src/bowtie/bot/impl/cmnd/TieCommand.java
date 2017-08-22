package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class TieCommand extends Command {

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public TieCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);

	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		List<IUser> users = event.getMessage().getMentions();
		if(users.isEmpty()){
			return;
		}
		QuizGuild guild = (QuizGuild)event.getGuild();
		guild.setTieUsers(users);
		guild.setTieActive(true);
		String msg = "Tiebreaker between";
		for(int i = 0; i < users.size(); i++){
			msg += " "+users.get(i);
		}
		msg += ".";
		guild.getBot().sendMessage(msg, event.getMessage().getChannel());
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Tiebreaker Command \n"
				+ "<Master> \n\n"
				+ "Starts a tiebreaker between the mentioned users. During a "
				+ "tiebreaker only the tied users can answer questions. \n\n"
				+ "Example: \n\n"
				+ BotConstants.PREFIX+"tie @User1 @User2 @User3"
				+ "\n\n\n"
				+ "Related Commands: \n"
				+ "- endtie"
				+ "```";
	}

}
