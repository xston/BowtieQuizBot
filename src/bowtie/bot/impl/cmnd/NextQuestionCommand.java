package bowtie.bot.impl.cmnd;

import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class NextQuestionCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public NextQuestionCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		QuizGuild guild = (QuizGuild)event.getGuild();
		guild.getQuestionManager().nextQuestion(event.getMessage().getChannel());
		new CommandCooldown(this, 3000).startTimer();
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Next Question Command \n"
				+ "<Master> \n\n"
				+ "This command will start the next question. It will then the question message and "
				+ "start the timer. \n\n\n"
				+ "Related Commands: \n"
				+ "- select \n"
				+ "- import \n"
				+ "- reset \n"
				+ "- stop \n"
				+ "- setchannel"
				+ "```";
	}
}