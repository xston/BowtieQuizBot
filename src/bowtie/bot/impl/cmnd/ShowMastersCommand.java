package bowtie.bot.impl.cmnd;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ShowMastersCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ShowMastersCommand(String[] validExpressions, int permission, Bot bot){
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		new CommandCooldown(this, 5000).startTimer();
		List<String> masterNames = new ArrayList<String>();
		List<IUser> masters = event.getGuild().getMasters();
		for(IUser user : masters){
			masterNames.add(user.getName()+"#"+user.getDiscriminator());
		}
		bot.sendListMessage("These people have master permissions on this server:", masterNames, event.getMessage().getChannel(), Colors.PURPLE);
	}
}