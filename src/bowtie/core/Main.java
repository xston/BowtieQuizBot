package bowtie.core;

import java.util.TimeZone;

import bowtie.bot.Bot;
import bowtie.bot.impl.QuizBot;
import bowtie.util.Properties;
import bowtie.util.log.Log;

/**
 * @author &#8904
 *
 */
public class Main {
	public static Log log;
	private Bot bot;
	
	public static void main(String[] args){
		log = new Log(TimeZone.getTimeZone("CET"));
		new Main();
	}
	
	public Main(){
		bot = new QuizBot(Properties.getValueOf("token"), this);
	}
	
	public void kill(boolean exit){
		bot.logout();
		log.print("Offline");
		Log.closeAll();
		if(exit){
			System.exit(0);
		}
	}
}