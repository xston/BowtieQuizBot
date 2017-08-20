package bowtie.bot.impl.cmnd;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.quiz.impl.QuizUser;

/**
 * @author &#8904
 *
 */
public class EnterQuizCommand extends Command{
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public EnterQuizCommand(String[] validExpressions, int permission, Bot bot){
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		//checks if the user is entered on any guild
		QuizGuild currentlyEnteredGuild = ((QuizBot) bot).getGuildForEnteredUser(event.getMessage().getAuthor());
		if(currentlyEnteredGuild == null){
			QuizUser user = new QuizUser(event.getMessage().getAuthor());
			user.setEnteredGuild(((QuizGuild)event.getGuild()));
			if(((QuizGuild)event.getGuild()).enterQuizUser(user)){
				RequestBuffer.request(() -> event.getMessage().addReaction(":white_check_mark:"));
			}
		}else if(!currentlyEnteredGuild.equals(event.getGuild())){
			RequestBuffer.request(() -> bot.sendMessage("You are already registered for a quiz on another server. Use '"+BotConstants.PREFIX+"leave' to exit "
					+ "that quiz. Retry this command after you have done so.", event.getMessage().getChannel(), Colors.RED));
		}
	}
}