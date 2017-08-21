package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.quiz.impl.QuizUser;

/**
 * @author &#8904
 *
 */
public class AddPointsCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public AddPointsCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		List<IUser> mentions = event.getMessage().getMentions();
		QuizGuild guild = (QuizGuild) event.getGuild();
		String text = event.getMessage().getContent();
		for(int i = 0; i < mentions.size(); i++){
			text = text.replace(mentions.get(i).getStringID(), "");
		}
		text = text.replace("<", "").replace(">", "").replace("@", "").replace("!", "").trim();
		String[] parts = text.split(" ");
		String points = parts[parts.length-1];
		
		for(int i = 0; i < mentions.size(); i++){
			int scorePoints = Integer.parseInt(points);
			QuizUser user = guild.getEnteredQuizUser(mentions.get(i).getStringID());
			if(user == null){
				return;
			}
			user.addScore(scorePoints);
			RequestBuffer.request(() -> guild.getBot().sendMessage(user.getName()+"#"+user.getDiscriminator()+
					" got "+scorePoints+" points.", event.getMessage().getChannel(), Colors.GREEN));
		}
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Add Points Command \n"
				+ "<Master> \n\n"
				+ "This command will add the specified amount of points "
				+ "to the mentioned user. \n\n"
				+ "Examples: \n"
				+ BotConstants.PREFIX+"addpoints @User 10 \n"
				+ BotConstants.PREFIX+"addpoints @User1 @User2 -20 \n\n\n"
				+ "Related Commands: \n"
				+ "- score"
				+ "```";
	}
}