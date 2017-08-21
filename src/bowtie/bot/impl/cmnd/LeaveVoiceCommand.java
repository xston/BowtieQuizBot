package bowtie.bot.impl.cmnd;

import sx.blah.discord.handle.obj.IVoiceChannel;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class LeaveVoiceCommand extends Command{
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public LeaveVoiceCommand(String[] validExpressions, int permission){
		super(validExpressions, permission);
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		IVoiceChannel voiceChannel = event.getGuild().getGuild().getConnectedVoiceChannel();
		if(voiceChannel != null){
			voiceChannel.leave();
		}
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Leave Voicechannel Command \n"
				+ "<Master> \n\n"
				+ "This command will make the bot leave the voicechannel it is currently in. \n\n\n"
				+ "Related Commands: \n"
				+ "- joinme"
				+ "```";
	}
}