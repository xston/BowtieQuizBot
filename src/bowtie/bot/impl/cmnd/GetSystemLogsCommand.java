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
public class GetSystemLogsCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public GetSystemLogsCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		try {
			event.getMessage().getChannel().sendFile(new File("logs/systemLogs.txt"));
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
				+ "Get Systemlogs Command \n"
				+ "<Creator> \n\n"
				+ "This command will send you a file containing the system logs. \n\n\n"
				+ "Related Commands: \n"
				+ "- clearsystemlogs"
				+ "```";
	}
}