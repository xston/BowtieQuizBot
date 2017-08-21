package bowtie.bot.impl.cmnd;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * Resets the quiz for the guild. Clears all user lists and rests all scores.
 * 
 * @author &#8904
 */
public class ResetCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ResetCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		((QuizGuild)event.getGuild()).reset();
		RequestBuffer.request(() -> event.getMessage().addReaction(":white_check_mark:"));
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Reset Quiz Command \n"
				+ "<Master> \n\n"
				+ "This command will reset the quiz. All entered users will leave the quiz and lose "
				+ "all their points. \n\n\n"
				+ "Related Commands: \n"
				+ "- select \n"
				+ "- import \n"
				+ "- next \n"
				+ "- stop \n"
				+ "- setchannel \n"
				+ "- addpoints"
				+ "```";
	}
}