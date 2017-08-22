package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.hand.QuestionManager;

/**
 * @author &#8904
 *
 */
public class ToggleModeCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ToggleModeCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		QuizGuild guild = (QuizGuild)event.getGuild();
		QuestionManager manager = guild.getQuestionManager();
		
		if(manager.getCurrentMode() == QuizConstants.NORMAL_MODE){
			manager.setCurrentMode(QuizConstants.FIRST_MODE);
			guild.getBot().sendMessage("Set quizmode to FIRST. Only the first correct answer gets points.", 
					event.getMessage().getChannel(), Colors.GREEN);
		}else{
			manager.setCurrentMode(QuizConstants.NORMAL_MODE);
			guild.getBot().sendMessage("Set quizmode to NORMAL. All correct answer get points.", 
					event.getMessage().getChannel(), Colors.GREEN);
		}
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Toggle Quizmode Command \n"
				+ "<Master> \n\n"
				+ "This will toggle the current quizmode between NORMAL and FIRST. \n\n\n"
				+ "During NORMAL mode every answer is considered until the question timer "
				+ "has ran out.\n\n"
				+ "During FIRST mode only the first correct answer will get points. This has no effect on "
				+ "closestanswer questions."
				+ "\n\n\n"
				+ "Related Commands: \n"
				+ "- addpoints \n"
				+ "- tie \n"
				+ "- stoptie"
				+ "```";
	}
}