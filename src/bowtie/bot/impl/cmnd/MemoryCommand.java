package bowtie.bot.impl.cmnd;

import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class MemoryCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public MemoryCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		String memory = format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		bot.sendMessage("`Memory used: "+memory+"`", event.getMessage().getChannel());
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public void getHelp() {
	}
	
	private String format(long ram){
		String[] units = {"b", "kb", "mb", "gb"};
		float actSize = (float)ram;
		String unit = units[0];
		for(int i = 0; i < 4; i++){
			if(actSize >= 1000){
				actSize /= 1000;
				unit = units[i+1];
			}
		}
		return String.format("%.2f", actSize)+" "+unit;
	}
}