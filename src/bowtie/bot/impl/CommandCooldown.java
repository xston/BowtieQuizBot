package bowtie.bot.impl;

import java.util.Timer;
import java.util.TimerTask;

import bowtie.bot.obj.Command;

/**
 * Class which handles a cooldown for a single {@link Command}.
 * @author &#8904
 */
public class CommandCooldown {
	private Timer timer;
	private Command command;
	private long cooldown;
	
	/**
	 * Creates a new instance for the given command with the given cooldown.
	 * 
	 * @param command
	 * @param cooldown The wanted cooldown is milliseconds.
	 */
	public CommandCooldown(Command command, long cooldown){
		this.command = command;
		this.cooldown = cooldown;
	}
	
	/**
	 * Starts the timer for the set command.
	 * <p>
	 * This will set {@link Command#onCooldown} to true
	 * and change it back to false once the timer is finished.
	 * </p>
	 */
	public void startTimer(){
		timer = new Timer();
		command.setOnCooldown(true);
		timer.schedule(new TimerTask(){
			@Override
			public void run(){
				command.setOnCooldown(false);
				timer.cancel();
			}
		}, cooldown);
	}
}