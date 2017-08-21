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
public class ClearSystemLogsCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ClearSystemLogsCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		try {
			new PrintWriter("logs/systemLogs.txt").close();
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
				+ "Clear Systemlogs Command \n"
				+ "<Creator> \n\n"
				+ "This command will clear the systemlog file. \n\n\n"
				+ "Related Commands: \n"
				+ "- getsystemlogs"
				+ "```";
	}
}