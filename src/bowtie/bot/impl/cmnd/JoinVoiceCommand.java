package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class JoinVoiceCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public JoinVoiceCommand(String[] validExpressions, int permission, Bot bot){
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		try{
			IUser user = event.getMessage().getAuthor();
			List<IVoiceChannel> channels = event.getGuild().getGuild().getVoiceChannels();
			IVoiceChannel wantedChannel = null;
			for(IVoiceChannel voiceChannel : channels){
				if(voiceChannel.getConnectedUsers().contains(user)){
					wantedChannel = voiceChannel;
				}
			}
			if(wantedChannel != null){
				final IVoiceChannel connectChannel = wantedChannel;
				RequestBuffer.request(() -> connectChannel.join());
				new CommandCooldown(this, 10000).startTimer();
			}else{
				bot.sendMessage("You have to be in a voicechannel.", event.getMessage().getChannel(), Colors.RED);
				new CommandCooldown(this, 2000).startTimer();
			}
		}catch(MissingPermissionsException e){
			bot.sendMessage("Please make sure that the bot has permissions to connect to a voicechannel.", event.getMessage().getChannel(), Colors.RED);
			new CommandCooldown(this, 2000).startTimer();
		}
	}
}