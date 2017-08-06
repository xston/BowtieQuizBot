package bowtie.core;

import java.util.Date;
import java.util.TimeZone;

import sx.blah.discord.util.DiscordException;
import bowtie.databse.DatabaseAccess;
import bowtie.discord.Bot;
import bowtie.util.Log;

/**
 * @author &#8904
 *
 */
public class Main {
	private DatabaseAccess database;
	private Bot bot;
	public String token;

	public static void main(String[] args){
		if(args.length > 0){
			new Main(args[0]);
		}else{
			new Main(null);
		}
	}
	
	public Main(String token){
		TimeZone.setDefault(TimeZone.getTimeZone("CET"));
		Log.clearFile("chatLogs.txt");
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			
		}
		Log.init();
		Log.printSystem(" ");
		database = new Database();
		database.setDbSystemDir();
		database.connect("jdbc:derby:/resource/db/bowtieDB;create=true");
		
		if(token != null){
			this.token = token;
		}else{
			this.token = database.getMisc("lastserver", "lastservertoken");
			if(this.token == null){
				this.token = "MzI1NjEzMzAzNjgwMzM1ODgz.DDQcaA.tKOGLzxuANVqHp8ttylZbx6TuZg";
				database.addMisc("test", "servertoken", this.token);
				Log.printSystem("No last server, logging into testing server.");
			}
		}
		bot = Bot.login(this.token, this);
	}
	
	public Database getDatabase(){
		return database;
	}
	
	public void kill(boolean exit){
		boolean offline = false;
		database.addMisc("lastserver", "lastservertoken", token);
		int attempts = 1;
		while(!offline){
			Log.printSystem(new Date()+" Trying to shut down. Attempt: "+attempts++);
			try{
				if(bot.getCurrentVoiceChannel() != null){
					bot.getCurrentVoiceChannel().leave();
					Log.printSystem(new Date()+" Left voice channel.");
				}
				bot.getVoice().kill();
				try {
					if(bot.getVoiceThread() != null){
						bot.getVoiceThread().join(1000);
					}
					Log.printSystem(new Date()+" Killed voice thread.");
				} catch (InterruptedException e) {
					Log.printSystem(e);
				}
				bot.client.logout();
				Log.printSystem(new Date()+" Logging out of discord.");
				if(bot.getManager() != null){
					bot.getManager().kill();
				}
				database.disconnect();
				offline = true;
			}catch(DiscordException e){
				Log.printSystem(e);
				try{
					Thread.sleep(200);
				}catch(Exception ex){
					Log.printSystem(ex);
				}
				if(attempts >= 5 && exit){
					Log.printSystem(new Date()+" Forced exit.");
					System.exit(0);
				}
			}
		}
		Log.printSystem(new Date()+" Offline");
		Log.kill();
		if(exit){
			System.exit(0);
		}
	}
}