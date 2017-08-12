package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.Bot;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.intf.CommandHandler;

/**
 * @author &#8904
 *
 */
public class MessageHandler implements IListener<MessageReceivedEvent>{
	private Bot bot;
	private CommandHandler privateHandler;
	
	public MessageHandler(Bot bot){
		this.bot = bot;
		privateHandler = new PrivateCommandHandler();
	}
	
	public Bot getBot(){
		return bot;
	}
	
	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String text = message.getContent();
		
		if(!event.getChannel().isPrivate()){
			QuizGuild guildObject = (QuizGuild)bot.getGuildObjectByID(event.getGuild().getStringID());
			
			if(text.toLowerCase().startsWith(BotConstants.PREFIX)){
				guildObject.getCommandHandler().dispatch(event);
			}else if(guildObject.getQuizChannel() != null && event.getChannel() == guildObject.getQuizChannel() && guildObject.isQuizActive()){
				RequestBuffer.request(() -> message.delete());
				guildObject.getAnswerHandler().handle(event);
			}
		}else if(text.toLowerCase().startsWith(BotConstants.PREFIX)){
			privateHandler.dispatch(event);
		}
	}
}