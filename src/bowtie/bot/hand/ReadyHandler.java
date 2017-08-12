package bowtie.bot.hand;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import bowtie.bot.Bot;
import bowtie.bot.impl.QuizBot;

/**
 * @author &#8904
 *
 */
public class ReadyHandler implements IListener<ReadyEvent>{
	private Bot bot;
	
	public ReadyHandler(Bot bot){
		this.bot = bot;
	}
	
	/**
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(ReadyEvent event) {
		((QuizBot)bot).createGuildObjects();
	}
}