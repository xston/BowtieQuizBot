package bowtie.bot.hand;

import java.util.List;

import bowtie.bot.obj.Command;
import bowtie.bot.obj.GuildObject;

/**
 * @author &#8904
 *
 */
public class CommandCooldownHandler {
	private List<Command> commandOnCooldown;
	private GuildObject guild;
	
	public CommandCooldownHandler(GuildObject guild){
		this.guild = guild;
	}
	
	
}