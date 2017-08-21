package bowtie.bot.impl.cmnd;

import java.io.File;
import java.util.List;

import sx.blah.discord.handle.obj.IMessage.Attachment;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class PatchCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public PatchCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		List<Attachment> attachments = event.getMessage().getAttachments();
		if(!attachments.isEmpty()){
			QuizGuild guild = (QuizGuild) event.getGuild();
			QuizBot bot = (QuizBot)guild.getBot();
			if(!attachments.get(0).getFilename().toLowerCase().contains(".jar")){
				return;
			}
			String url = attachments.get(0).getUrl();
			bot.sendMessage("Patching..", event.getMessage().getChannel(), true);
			bot.getMain().kill(false);
			File jar = null;
			try{
				jar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				Process process = Runtime.getRuntime().exec("java -jar patcher.jar "+jar.getPath()+" "+url+" boot");
			}catch(Exception e){
				Main.log.print(e);
			}
			System.exit(0);
		}
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Patch Command \n"
				+ "<Creator> \n\n"
				+ "Replaces the bot file with the attached one and reboots the bot. \n\n\n"
				+ "Related Commands: \n"
				+ "- reboot"
				+ "```";
	}
}