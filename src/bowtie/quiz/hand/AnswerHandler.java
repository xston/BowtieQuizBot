package bowtie.quiz.hand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.GuildObject;

/**
 * @author &#8904
 *
 */
public class AnswerHandler {
	private QuizGuild guild;
	
	public AnswerHandler(QuizGuild guild){
		this.guild = guild;
	}
	
	public GuildObject getGuildObject(){
		return guild;
	}
	
	public void handle(MessageReceivedEvent event){
		new Thread(new Runnable(){
			@Override
			public void run() {
				
			}
		}).start();
	}
}