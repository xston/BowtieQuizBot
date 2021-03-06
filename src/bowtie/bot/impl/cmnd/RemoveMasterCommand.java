package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class RemoveMasterCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public RemoveMasterCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		List<IUser> mentions = event.getMessage().getMentions();
		if(mentions.isEmpty()){
			bot.sendMessage("You have to tag the user that should lose the master permissions.", event.getMessage().getChannel(), Colors.RED);
		}else{	
			if(!((QuizGuild)event.getGuild()).isMaster(mentions.get(0))){
				bot.sendMessage("That user is not a master on this server.", event.getMessage().getChannel(), Colors.RED);
			}else if(event.getGuild().getMasters().size() == 1){
				bot.sendMessage("That is my last master. I can't function without a master.", event.getMessage().getChannel(), Colors.RED);
			}else if(((QuizGuild)event.getGuild()).deleteMaster(mentions.get(0))){
				event.getMessage().addReaction(":white_check_mark:");
				Main.log.print("Removed master permissions of "+mentions.get(0).getName()+"#"+mentions.get(0).getDiscriminator()+" on "
						+ "'"+event.getGuild().getGuild().getName()+"'.");
			}
		}
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Remove Master Command \n"
				+ "<Master> \n\n"
				+ "Takes the master permissions of the mentioned user away. \n\n"
				+ "The bot needs at least one master on the server. \n\n\n"
				+ "Related Commands: \n"
				+ "- master \n"
				+ "- showmasters"
				+ "```";
	}
}