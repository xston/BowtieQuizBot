package bowtie.core;

import java.util.TimeZone;

import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;
import bowtie.db.Database;
import bowtie.util.Properties;
import bowtie.util.log.Log;

/**
 * @author &#8904
 *
 */
public class Main {
	public static Log log;
	private Bot bot;
	private Database database;
	
	public static void main(String[] args){
		log = new Log(TimeZone.getTimeZone("CET"));
		new Main();
	}
	
	public Main(){
		database = new Database();
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
	
	public Database getDatabase(){
		return database;
	}
}