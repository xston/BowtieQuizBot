package bowtie.bot.impl.cmnd;

import bowtie.bot.cons.Colors;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ThreadCountCommand extends Command{
	private Bot bot;

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ThreadCountCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		bot.sendMessage("Current thread count: `"+Thread.activeCount()+"`.", event.getMessage().getChannel(), Colors.PURPLE);
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public void getHelp() {
	}
}