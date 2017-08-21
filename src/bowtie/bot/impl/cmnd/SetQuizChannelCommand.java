package bowtie.bot.impl.cmnd;

import java.util.EnumSet;
import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.QuizPermissions;

/**
 * @author &#8904
 *
 */
public class SetQuizChannelCommand extends Command{
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public SetQuizChannelCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		IChannel newChannel = null;
		try{
			newChannel = event.getMessage().getChannelMentions().get(0);
		}catch(Exception e){
			bot.sendMessage("You have to mention the channel with `#channel name`.", event.getMessage().getChannel(), Colors.RED);
			return;
		}
		QuizGuild guild = (QuizGuild)event.getGuild();
		
		EnumSet<Permissions> perms = newChannel.getModifiedPermissions(bot.client.getOurUser());
		List<Permissions> missingPermissions = QuizPermissions.getMissingPermissions(perms, QuizPermissions.NEEDED_BOT_PERMISSIONS);
		
		if(!missingPermissions.isEmpty()){
			StringBuilder sb = new StringBuilder();
			for(Permissions missing : missingPermissions){
				sb.append("**`<"+missing.toString()+">`** ");
			}
			bot.sendMessage("The bot needs "+sb.toString()+"for the new quiz channel.", event.getMessage().getChannel(), Colors.RED);
		}else{
			guild.setQuizChannel(newChannel);
			bot.sendMessage("Set `"+newChannel.getName()+"` as the quiz channel.", event.getMessage().getChannel(), Colors.GREEN);
		}
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public void getHelp() {
	}
}