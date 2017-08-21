package bowtie.bot.impl.cmnd;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;
import bowtie.quiz.impl.QuizUser;

/**
 * @author &#8904
 *
 */
public class ScoreCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ScoreCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		new CommandCooldown(this, 5000).startTimer();
		QuizGuild guild = (QuizGuild)event.getGuild();
		guild.sortEnteredUsersAfterScores();
		List<QuizUser> users = guild.getEnteredQuizUsers();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setLenient(true);
		builder.withTitle("Current total scores:");
		builder.withDescription("------------------------------------------------------------------------------");
		builder.withColor(Colors.DEFAULT);
	    List<EmbedObject> embedObjects = new ArrayList<EmbedObject>();
		for(int i = 0; i < users.size(); i++){
			//adds up to 24 elements per embed
			if(builder.getFieldCount() < 24){
				builder.appendField(users.get(i).getName()+"#"+users.get(i).getDiscriminator(), 
						Integer.toString(users.get(i).getScore()), true);
			}else{
				//builds the full embed
				embedObjects.add(builder.build());
				//resets the builder and continues adding messages to the "new" embed
				builder.withTitle("");
				builder.withDescription("");
				builder.withColor(Colors.DEFAULT);
				builder.clearFields();
				builder.appendField(users.get(i).getName()+"#"+users.get(i).getDiscriminator(), 
						Integer.toString(users.get(i).getScore()), true);
			}
		}
		//adds empty embeds to avoid weird shifting of the elements
		while(builder.getFieldCount() % 3 != 0){
			builder.appendField("-", "-", true);
		}
		embedObjects.add(builder.build());
	    for(EmbedObject embed : embedObjects){
	    	RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(embed)).get();
	    }
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public void getHelp() {
	}
}