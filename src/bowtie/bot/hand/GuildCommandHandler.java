package bowtie.bot.hand;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Command;
import bowtie.util.Permissions;

/**
 * Handles commands by dispatching {@link MessageReceivedEvent}s to the correct {@link bowtie.bot.obj.Command}s.
 * 
 * @author &#8904
 */
public class GuildCommandHandler implements CommandHandler{
	private List<Command> commands;
	private QuizGuild guild;
	
	public GuildCommandHandler(QuizGuild guild){
		this.guild = guild;
		commands = new ArrayList<Command>();
	}
	
	public void addCommand(Command command){
		commands.add(command);
	}
	
	public QuizGuild getGuildObject(){
		return guild;
	}
	
	/**
	 * @see bowtie.bot.intf.CommandHandler#dispatch(sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent)
	 */
	@Override
	public void dispatch(MessageReceivedEvent event){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String command = event.getMessage().getContent().split(" ")[0].replace(BotConstants.PREFIX, "");
				for(Command commandObject : commands){
					if(commandObject.isValidExpression(command) && commandObject.isValidPermission(Permissions.getPermissionLevel(event.getAuthor(), guild))){
						commandObject.execute(event);
						return;
					}
				}
			}
		}).start();	
	}
}