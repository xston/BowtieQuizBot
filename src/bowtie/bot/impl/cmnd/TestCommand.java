package bowtie.bot.impl.cmnd;

import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class TestCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public TestCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		String text = event.getMessage().getContent().split(" ")[1].trim();
	}
}