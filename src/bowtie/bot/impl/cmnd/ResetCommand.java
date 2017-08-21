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
	public void getHelp() {
	}
}