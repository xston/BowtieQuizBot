package bowtie.bot.impl;

import java.util.Timer;
import java.util.TimerTask;

import bowtie.bot.obj.Command;

/**
 * @author &#8904
 *
 */
public class CommandCooldown {
	private Timer timer;
	private Command command;
	private long cooldown;
	
	public CommandCooldown(Command command, long cooldown){
		this.command = command;
		this.cooldown = cooldown;
	}
	
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