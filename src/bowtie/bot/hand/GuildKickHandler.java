package bowtie.bot.hand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.GuildObject;
import bowtie.core.Main;

/**
 * Handles {@link GuildLeaveEvent}s for the bot.
 * @author &#8904
 */
public class GuildKickHandler implements IListener<GuildLeaveEvent>{
	private Bot bot;
	
	/**
	 * Creates a new instance for the given bot.
	 * @param bot
	 */
	public GuildKickHandler(Bot bot){
		this.bot = bot;
	}
	/**
	 * Removes the representing {@link GuildObject} from the {@link Bot#guilds} list,
	 * clears every entry about this guild from the database and deletes the questions and chatlog files.
	 * 
	 * @see sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.Event)
	 */
	@Override
	public void handle(GuildLeaveEvent event){
		GuildObject guild = bot.getGuildObjectByID(event.getGuild().getStringID());
		bot.removeGuildObject(guild);
		((QuizBot)bot).getMain().getDatabase().deleteGuild(guild.getStringID());
		try{
			Files.delete(new File("questions/"+guild.getStringID()+"_questions.btq").toPath());
		}catch (IOException e){
			Main.log.print(e);
			Main.log.print("Corpse file: questions/"+guild.getStringID()+"_questions.btq");
		}
		try{
			Files.delete(new File("logs/chatLogs/"+guild.getStringID()+"_chat.txt").toPath());
		}catch (IOException e){
			Main.log.print(e);
			Main.log.print("Corpse file: logs/chatLogs/"+guild.getStringID()+"_chat.txt");
		}
		bot.sendMessage("Bot was kicked from '"+event.getGuild().getName()+"' ("+event.getGuild().getStringID()+")", 
				bot.client.getApplicationOwner().getOrCreatePMChannel(), Colors.RED);
	}
}