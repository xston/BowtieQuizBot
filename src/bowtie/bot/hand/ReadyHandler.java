package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;

/**
 * Listens for a {@link ReadyEvent} of the bot.
 * 
 * @author &#8904
 */
public class ReadyHandler implements IListener<ReadyEvent>{
	/** The {@link Bot} object whichs {@link ReadyEvent} should be listened for. */
	private Bot bot;
	
	/**
	 * Creates a new instance for the given {@link Bot}.
	 * 
	 * @param bot The bot for whichs {@link ReadyEvent} should be listened.
	 */
	public ReadyHandler(Bot bot){
		this.bot = bot;
	}
	
	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(ReadyEvent event) {
		((QuizBot)bot).createGuildObjects();
		((QuizBot)bot).loadCreators();
		Bot.isReady = true;
	}
}