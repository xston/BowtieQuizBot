package bowtie.bot.hand;

import java.util.ArrayList;
import java.util.List;

import bowtie.bot.cons.BotConstants;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.QuizPermissions;

/**
 * Handles commands by dispatching {@link CommandEvent}s to the correct {@link Command}s.
 * 
 * @author &#8904
 */
public class PrivateCommandHandler implements CommandHandler{
	/** A {@link List} which contains all {@link Command}s this instance is handling. */
	private List<Command> commands;
	
	/**
	 * Creates a new instance that will handle {@link Command}s dispatched by the bot's 
	 * {@link MessageHandler}.
	 */
	public PrivateCommandHandler(){
		commands = new ArrayList<Command>();
	}
	
	/**
	 * Adds a {@link Command} to this instances {@link #commands} list.
	 * 
	 * @param command The {@link Command} that should be added.
	 * @return This {@link PrivateCommandHandler} instance.
	 */
	public PrivateCommandHandler addCommand(Command command){
		commands.add(command);
		return this;
	}
	
	@Override
	public List<Command> getCommands(){
		return commands;
	}
	
	/**
	 * @see bowtie.bot.intf.CommandHandler#dispatch(bowtie.evnt.QuizEvent)
	 */
	@Override
	public void dispatch(CommandEvent event) {
		String command = event.getMessage().getContent().split(" ")[0].replace(BotConstants.PREFIX, "");
		for(Command commandObject : commands){
			if(commandObject.isValidExpression(command) 
					&& commandObject.isValidPermission(QuizPermissions.getPermissionLevel(event.getMessage().getAuthor()))
					&& !commandObject.isOnCooldown()){
				commandObject.execute(event);
				return;
			}
		}
	}
}