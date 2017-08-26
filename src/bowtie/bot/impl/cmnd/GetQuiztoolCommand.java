package bowtie.bot.impl.cmnd;

import java.io.File;
import java.io.FileNotFoundException;

import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.Properties;

/**
 * @author &#8904
 *
 */
public class GetQuiztoolCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public GetQuiztoolCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		new CommandCooldown(this, 10000).startTimer();
		try {
			String toolVersion = Properties.getValueOf("toolversion");
			event.getMessage().getChannel().sendFile(new File("Bowtie_Quiztool_v"+toolVersion+".jar"));
			Main.log.print("Quiztool was send on '"+event.getGuild().getGuild().getName()+"'.");
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
				+ "Get Quiztool Command \n"
				+ "<User> \n\n"
				+ "This will send the quiztool which is used to write questions for the bot. "
				+ "After you downloaded the jar-file you can run it by simply double clicking it. "
				+ "You need to have Java 1.8 or higher to be installed."
				+ "\n\n\n"
				+ "Related Commands: \n"
				+ "- import"
				+ "```";
	}
}