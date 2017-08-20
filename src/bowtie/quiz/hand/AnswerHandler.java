package bowtie.quiz.hand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import bowtie.bot.impl.QuizGuild;

/**
 * @author &#8904
 *
 */
public class AnswerHandler {
	private QuizGuild guild;
	private ExecutorService executor;
	
	public AnswerHandler(QuizGuild guild){
		this.guild = guild;
		executor = Executors.newCachedThreadPool();
	}
	
	public QuizGuild getGuild(){
		return guild;
	}
	
	public void handle(MessageReceivedEvent event){
		executor.execute(new Runnable(){
			@Override
			public void run(){
				
			}
		});
	}
}