package bowtie.bot.hand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.impl.cmnd.ShutdownCommand;
import bowtie.bot.impl.cmnd.ThreadCountCommand;
import bowtie.bot.intf.CommandHandler;
import bowtie.bot.obj.Bot;
import bowtie.evnt.impl.CommandEvent;
import bowtie.util.QuizPermissions;

/**
 * A class which implements {@link IListener} and handles all {@link MessageReceivedEvent}s of the bot.
 * 
 * @author &#8904
 */
public class MessageHandler implements IListener<MessageReceivedEvent>{
	/** The {@link Bot} object this instance is handling messages for. */
	private Bot bot;
	
	/** The {@link CommandHandler} which handles commands received through
	 * 	private messages. */
	private CommandHandler privateHandler;
	
	/** Threadpool which provides threads when needed to handle events. */
	private ExecutorService executor;
	
	/**
	 * Creates a new instance for the given {@link Bot}.
	 * <p>
	 * This constructor will also create a {@link PrivateCommandHandler} and register 
	 * the standard commands.
	 * </p>
	 * @param bot The {@link Bot} instance.
	 */
	public MessageHandler(Bot bot){
		this.bot = bot;
		privateHandler = new PrivateCommandHandler();
		registerCommands();
		executor = Executors.newCachedThreadPool();
	}
	
	/**
	 * Registers the standard commands to the {@link #privateHandler}.
	 */
	private void registerCommands(){
		((PrivateCommandHandler)privateHandler).addCommand(new ShutdownCommand(new String[]{"off", "shutdown"}, 
				QuizPermissions.CREATOR, bot))
		
		.addCommand(new ThreadCountCommand(new String[]{"threads", "thread", "threadcount", "activethreads"}, 
				QuizPermissions.CREATOR, bot));
	}
	
	/**
	 * Gets this instance's {@link #bot}.
	 * 
	 * @return The bot.
	 */
	public Bot getBot(){
		return bot;
	}
	
	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(MessageReceivedEvent event){
		executor.submit(new Runnable(){
			@Override
			public void run(){
				IMessage message = event.getMessage();
				String text = message.getContent();
				
				if(!event.getChannel().isPrivate()){
					QuizGuild guildObject = (QuizGuild)bot.getGuildObjectByID(event.getGuild().getStringID());
					
					if(text.toLowerCase().startsWith(BotConstants.PREFIX)){
						//commands in guild channels
						guildObject.getCommandHandler().dispatch(new CommandEvent(guildObject, message));
					}else if(guildObject.getQuizChannel() != null 
							&& event.getChannel() == guildObject.getQuizChannel() 
							&& guildObject.isQuizActive()){
						//answers during a quiz
						RequestBuffer.request(() -> message.delete());
						if(guildObject.isEnteredQuizUser(message.getAuthor())){
							guildObject.getAnswerHandler().handle(event);
						}
					}
				}else if(text.toLowerCase().startsWith(BotConstants.PREFIX)){
					//commands in private chats
					privateHandler.dispatch(new CommandEvent(message));
				}else{
					//answers  during the quiz in a private chat
					QuizGuild guildObject = ((QuizBot)bot).getGuildForEnteredUser(message.getAuthor());
					if(guildObject != null && guildObject.isQuizActive()){
						guildObject.getAnswerHandler().handle(event);
					}
				}
			}
		});
	}
}