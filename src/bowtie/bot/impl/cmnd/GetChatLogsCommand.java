package bowtie.bot.impl.cmnd;

import java.io.File;
import java.io.FileNotFoundException;

import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class GetChatLogsCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public GetChatLogsCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		try {
			event.getMessage().getChannel().sendFile(new File("logs/chatLogs/"+event.getGuild().getStringID()+"_chat.txt"));
			new CommandCooldown(this, 10000);
		} catch (FileNotFoundException e) {
			Main.log.print(e);
		}
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Get Chatlogs Command \n"
				+ "<Master> \n\n"
				+ "This command will send you a chatlog file. During quizes the bot will log all "
				+ "given answers. \n\n\n"
				+ "Related Commands: \n"
				+ "- clearchatlogs"
				+ "```";
	}
}