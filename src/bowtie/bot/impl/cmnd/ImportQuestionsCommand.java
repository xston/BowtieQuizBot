package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.Colors;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class ImportQuestionsCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public ImportQuestionsCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event){
		new CommandCooldown(this, 5000).startTimer();
		IMessage message = event.getMessage();
		List<Attachment> at = message.getAttachments();
		int count = 0;
		if(!at.isEmpty()){
			count = ((QuizGuild)event.getGuild()).getQuestionManager().getQuestionImporter().downloadQuestionFile(at.get(0).getUrl());
			RequestBuffer.request(() -> message.delete());
		}else{
			count = ((QuizGuild)event.getGuild()).getQuestionManager().getQuestionImporter().importQuestions();
		}
		if(count == -1){
			bot.sendMessage("Unsupported file format. Only files with the '.btq' extension can be imported.", message.getChannel(), Colors.RED);
		}else{
			bot.sendMessage("Imported "+count+" questions.", message.getChannel(), count == 0 ? Colors.ORANGE : Colors.GREEN);
		}
	}
	
	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "```"
				+ "Import Questions Command \n"
				+ "<Master> \n\n"
				+ "This command will import the questions and prepare them "
				+ "for your quiz. \n\n"
				+ "You have to upload a valid Bowtie Quizfile (.btq) "
				+ "and write this command into the comment section that pops up. \n\n"
				+ "To create a Quizfile please take a look at the Quiztool, a "
				+ "downloadlink for that can be found when using the 'tool' command.\n\n\n"
				+ "Related Commands: \n"
				+ "- tool \n"
				+ "- select \n"
				+ "- next \n"
				+ "- stop \n"
				+ "- setchannel"
				+ "```";
	}
}