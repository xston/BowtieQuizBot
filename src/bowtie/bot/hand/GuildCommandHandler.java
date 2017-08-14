package bowtie.bot.hand;

import java.util.ArrayList;
import java.util.List;

import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.QuizPermissions;

/**
 * Handles commands by dispatching {@link CommandEvent}s to the correct {@link bowtie.bot.obj.Command}s.
 * 
 * @author &#8904
 */
public class GuildCommandHandler implements CommandHandler{
	/** A {@link List} which contains all {@link Command}s this instance is handling. */
	private List<Command> commands;
	
	/** The {@link QuizGuild} object this instance is handling commands for. */
	private QuizGuild guild;
	
	/**
	 * Creates a new instance that will handle {@link Command}s dispatched by the bot's 
	 * {@link MessageHandler} for the given {@link QuizGuild}.
	 * 
	 * @param guild The guild for which this instance should handle {@link Command}s for.
	 */
	public GuildCommandHandler(QuizGuild guild){
		this.guild = guild;
		commands = new ArrayList<Command>();
	}
	
	/**
	 * Adds a {@link Command} to this instances {@link #commands} list.
	 * 
	 * @param command The {@link Command} that should be added.
	 * @return This {@link GuildCommandHandler} instance.
	 */
	public GuildCommandHandler addCommand(Command command){
		commands.add(command);
		return this;
	}
	
	/**
	 * Gets the {@link QuizGuild} object this instance is handling {@link Command}s for.
	 * 
	 * @return The guild.
	 */
	public QuizGuild getGuildObject(){
		return guild;
	}

	/**
	 * @see bowtie.bot.intf.CommandHandler#dispatch(bowtie.evnt.QuizEvent)
	 */
	@Override
	public void dispatch(CommandEvent event){
		String command = event.getMessage().getContent().split(" ")[0].replace(BotConstants.PREFIX, "");
		for(Command commandObject : commands){
			if(commandObject.isValidExpression(command) 
					&& commandObject.isValidPermission(QuizPermissions.getPermissionLevel(event.getMessage().getAuthor(), guild))){
				commandObject.execute(event);
				return;
			}
		}
	}
}