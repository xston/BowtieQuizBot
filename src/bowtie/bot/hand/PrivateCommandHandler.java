package bowtie.bot.hand;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Command;
import bowtie.util.Permissions;

/**
 * @author &#8904
 *
 */
public class PrivateCommandHandler implements CommandHandler{
	private List<Command> commands;
	
	public PrivateCommandHandler(){
		commands = new ArrayList<Command>();
	}
	
	public void addCommand(Command command){
		commands.add(command);
	}
	
	/**
	 * @see bowtie.bot.intf.CommandHandler#dispatch(sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent)
	 */
	@Override
	public void dispatch(MessageReceivedEvent event) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				String command = event.getMessage().getContent().split(" ")[0].replace(BotConstants.PREFIX, "");
				for(Command commandObject : commands){
					if(commandObject.isValidExpression(command) && commandObject.isValidPermission(Permissions.getPermissionLevel(event.getAuthor()))){
						commandObject.execute(event);
						return;
					}
				}
			}
		}).start();
	}

}