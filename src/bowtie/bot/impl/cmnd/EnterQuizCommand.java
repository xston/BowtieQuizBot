package bowtie.bot.impl.cmnd;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class EnterQuizCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public EnterQuizCommand(String[] validExpressions, int permission){
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		if(((QuizGuild)event.getGuild()).enterQuizUser(event.getMessage().getAuthor())){
			RequestBuffer.request(() -> event.getMessage().addReaction(":white_check_mark:"));
		}
	}
}