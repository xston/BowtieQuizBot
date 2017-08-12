package bowtie.bot.obj;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


/**
 * @author &#8904
 *
 */
public abstract class Command{
	private List<String> validExpression;
	private final int permission;
	
	public Command(String[] validCommands, int permission){
		this.validExpression = new ArrayList<String>();
		for(String command : validCommands){
			this.validExpression.add(command);
		}
		this.permission = permission;
	}
	
	public Command(List<String> validCommands, int permission){
		this.validExpression = validCommands;
		this.permission = permission;
	}
	
	/**
	 * Checks if the given String is a valid command expression.
	 * 
	 * @param command
	 * @return
	 */
	public boolean isValidExpression(String command){
		if(validExpression.contains(command)){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given permission level is high enough to execute this command.
	 * 
	 * @param permission
	 * @return
	 */
	public boolean isValidPermission(int permission){
		if(permission >= this.permission){
			return true;
		}
		return false;
	}
	
	/**
	 * Defines the action that should be performed when this 
	 * command is called.
	 * 
	 * @param event The {@link MessageReceivedEvent} dispatched by the {@link bowtie.bot.hand.CommandHandler}.
	 */
	public abstract void execute(MessageReceivedEvent event);
}