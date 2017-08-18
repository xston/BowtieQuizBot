package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
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
		new CommandCooldown(this, 2000).startTimer();
		IMessage message = event.getMessage();
		List<Attachment> at = message.getAttachments();
		int count = 0;
		if(!at.isEmpty()){
			count = ((QuizGuild)event.getGuild()).getQuestionManager().getQuestionImporter().downloadQuestionFile(at.get(0).getUrl());
		}else{
			count = ((QuizGuild)event.getGuild()).getQuestionManager().getQuestionImporter().importQuestions();
		}
		if(count == -1){
			bot.sendMessage("Unsupported file format. Only files with the '.btq' extension can be imported.", message.getChannel(), Colors.RED);
		}else{
			bot.sendMessage("Imported "+count+" questions.", message.getChannel(), count == 0 ? Colors.ORANGE : Colors.GREEN);
		}
	}
}