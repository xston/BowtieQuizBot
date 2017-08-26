package bowtie.bot.intf;

import java.util.List;

import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public interface CommandHandler {
	
	/**
	 * Gets a list of the registered {@link Command}s.
	 * @return The list.
	 */
	public List<Command> getCommands();
	
	/**
	 * Dispatches the given event to the correct {@link Command} by calling its
	 * {@link Command#execute(CommandEvent)}
	 * 
	 * @param event The {@link CommandEvent} which should be dispatched.
	 */
	public void dispatch(CommandEvent event);
}