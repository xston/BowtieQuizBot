package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class StatisticCommand extends Command{
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public StatisticCommand(String[] validExpressions, int permission, Bot bot){
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		String statistics = "```"
				+ "Active guilds: "+((QuizBot)bot).getGuildCount()+"\n\n"
				+ "Users: "+((QuizBot)bot).getTotalUserCount()+"\n"
				+ "Masters: "+((QuizBot)bot).getTotalMasterCount()+"\n"
				+ "Voice connections: "+bot.client.getConnectedVoiceChannels().size()+"\n"
				+ "Asked questions: "+((QuizBot)bot).getAskedQuestions()+"\n"
				+ "Received answers: "+((QuizBot)bot).getReceivedAnswers()+"\n"
				+ "```";
		bot.sendMessage(statistics, event.getMessage().getChannel(), Colors.PURPLE);
	}
}