package bowtie.bot.impl.cmnd;

import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class StopQuestionCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public StopQuestionCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		((QuizGuild)event.getGuild()).getQuestionManager().stopCurrentQuestion();
	}
}