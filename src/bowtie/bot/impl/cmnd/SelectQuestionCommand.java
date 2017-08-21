package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class SelectQuestionCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public SelectQuestionCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		String text = event.getMessage().getContent();
		int index = 0;
		try{
			index = Integer.parseInt(text.split(" ")[1].trim());
		}catch(NumberFormatException e){
			return;
		}
		QuizGuild guild = (QuizGuild)event.getGuild();
		guild.getQuestionManager().selectQuestion(index-1, event.getMessage().getChannel());
		new CommandCooldown(this, 3000).startTimer();
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Select Question Command \n"
				+ "<Master> \n\n"
				+ "You have to write a number behind the command, for example '"+BotConstants.PREFIX+"select 5'. \n\n"
				+ "This command will jump the question with that number and start it. The first question in your file "
				+ "is 1 the second question is 2 and so on. \n\n\n"
				+ "Related Commands: \n"
				+ "- import \n"
				+ "- next \n"
				+ "- stop \n"
				+ "- setchannel \n"
				+ "- points"
				+ "```";
	}
}