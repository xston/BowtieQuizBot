package bowtie.bot.impl.cmnd;

import java.io.File;

import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class RebootCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public RebootCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		QuizGuild guild = (QuizGuild) event.getGuild();
		QuizBot bot = (QuizBot)guild.getBot();
		bot.sendMessage("Rebooting..", event.getMessage().getChannel(), true);
		bot.getMain().kill(false);
		File jar = null;
		try{
			jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Process process = Runtime.getRuntime().exec("java -jar rebooter.jar "+jar.getPath()+" 5");
		}catch(Exception e){
			Main.log.print(e);
		}
		System.exit(0);
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Reboot Command \n"
				+ "<Creator> \n\n"
				+ "Reboots the bot. \n\n\n"
				+ "Related Commands: \n"
				+ "- patch"
				+ "```";
	}
}