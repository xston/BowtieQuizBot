package bowtie.bot.impl.cmnd;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * Leaves the currently entered quiz.
 * @author &#8904
 */
public class LeaveQuizCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public LeaveQuizCommand(String[] validExpressions, int permission, Bot bot){
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		QuizGuild guildToLeave = ((QuizBot) bot).getGuildForEnteredUser(event.getMessage().getAuthor());
		if(guildToLeave != null && guildToLeave.removeQuizUser(event.getMessage().getAuthor())){
			RequestBuffer.request(() -> event.getMessage().addReaction(":white_check_mark:"));
		}
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Leave Quiz Command \n"
				+ "<User> \n\n"
				+ "Makes you leave the quiz you are currently signed up for. This command can be used "
				+ "on any server, not just the one where you signed up. \n\n\n"
				+ "Related Commands: \n"
				+ "- enter \n"
				+ "- reset"
				+ "```";
	}
}