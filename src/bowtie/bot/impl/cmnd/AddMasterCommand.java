package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class AddMasterCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public AddMasterCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		new CommandCooldown(this, 2000).startTimer();
		List<IUser> mentions = event.getMessage().getMentions();
		if(mentions.isEmpty()){
			bot.sendMessage("You have to tag the user that should become a master.", event.getMessage().getChannel(), Colors.RED);
		}else{	
			if(((QuizGuild)event.getGuild()).saveMaster(mentions.get(0))){
				event.getMessage().addReaction(":white_check_mark:");
			}else{
				bot.sendMessage("That user is already a master on this server.", event.getMessage().getChannel(), Colors.RED);
			}
		}
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Add Master Command \n"
				+ "<Master> \n\n"
				+ "Gives the mentioned user master permissions for this bot. \n\n"
				+ "Users with master permissions can execute any user/master command, so "
				+ "be careful who you give this power to. \n\n\n"
				+ "Related Commands: \n"
				+ "- nomaster \n"
				+ "- showmasters"
				+ "```";
	}
}