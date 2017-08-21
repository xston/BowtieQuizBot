package bowtie.bot.impl.cmnd;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ClearChatLogsCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ClearChatLogsCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		try {
			new PrintWriter("logs/chatLogs/"+event.getGuild().getStringID()+"_chat.txt").close();
			event.getMessage().addReaction(":white_check_mark:");
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
				+ "Clear Chatlogs Command \n"
				+ "<Master> \n\n"
				+ "This command will clear the chatlog file. This should be done before"
				+ "every quiz. \n\n\n"
				+ "Related Commands: \n"
				+ "- getchatlogs"
				+ "```";
	}
}