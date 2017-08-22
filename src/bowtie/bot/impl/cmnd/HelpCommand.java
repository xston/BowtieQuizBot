package bowtie.bot.impl.cmnd;

import java.util.List;

import sx.blah.discord.util.RequestBuffer;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.CommandCooldown;
import bowtie.bot.impl.QuizGuild;
import bowtie.bot.obj.Command;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class HelpCommand extends Command{

	/**
	 * @param validExpressions
	 * @param permission
	 */
	public HelpCommand(String[] validExpressions, int permission) {
		super(validExpressions, permission);
		
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		String type = event.getMessage().getContent().replace(BotConstants.PREFIX+"help", "").trim();
		String helpMessage = "";
		if(type.equals("")){
			helpMessage = getHelp();
		}else if(type.equals("quiz")){
			helpMessage = getQuizHelp();
		}else if(type.equals("questions")){
			helpMessage = getQuestionsHelp();
		}else if(type.equals("masters")){
			helpMessage = getMastersHelp();
		}else if(type.equals("commands")){
			helpMessage = getCommandsHelp();
		}else{
			QuizGuild guild = (QuizGuild)event.getGuild();
			List<Command> commands = guild.getCommandHandler().getCommands();
			for(Command command : commands){
				if(command.isValidExpression(type)){
					helpMessage = command.getHelp();
				}
			}
		}
		String message = helpMessage;
		if(message.equals("")){
			return;
		}
		new CommandCooldown(this, 3000).startTimer();
		RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(message));
	}

	/**
	 * @see bowtie.bot.obj.Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return "**```"
				+ "Help \n\n"
				+ "Use the keywords below to get more detailed information. \n"
				+ BotConstants.PREFIX+" is the prefix for every command. \n\n\n"
				+ BotConstants.PREFIX+"help quiz            |       Information about how to host a quiz. \n\n"
				+ BotConstants.PREFIX+"help questions       |       Information about the questiontypes. \n\n"
				+ BotConstants.PREFIX+"help masters         |       Information about the master system of the bot. \n\n"
				+ BotConstants.PREFIX+"help commands        |       Information about the commands. \n\n\n"
				+ "You can add this bot to your server with this link: \n\n"
				+ "https://discordapp.com/oauth2/authorize?client_id=345747519113920512&scope=bot&permissions=3402816"
				+ "\n\n\n"
				+ "If you have any kind of feedback feel free to join my Discord server and let me know \n\n"
				+ "https://discord.gg/vHuKbBw"
				+ "```**";
	}
	
	public String getQuizHelp(){
		return "**```"
				+ "Before you can start a quiz you need to write the questions. Since the bot "
				+ "needs the questions to be in a special format you should use the Quiztool. \n\n"
				+ "A downloadlink for the tool can be obtained by the '"+BotConstants.PREFIX+"quiztool' "
				+ "command. \n\n"
				+ "After you have downloaded the jar-file you can run it by simply double clicking it. "
				+ "For this you need java 1.8 or higher to be installed. \n\n"
				+ "Once you have created a Bowtie Quizfile (.btq) and filled it with all your questions "
				+ "you have to import it to the bot by using the '"+BotConstants.PREFIX+"import' command. \n\n"
				+ "The bot needs a dedicated quizchannel in which he will post the questions and answers. Use the "
				+ "'"+BotConstants.PREFIX+"setchannel #channel' command and change 'channel' to the name of the desired quizchannel. \n\n"
				+ "Now you are almost ready to start! \n"
				+ "Every user that wants to take part in the quiz has to use the '"+BotConstants.PREFIX+"enter' "
				+ "command. Once everyone has entered you can send the first question with the '"+BotConstants.PREFIX+"next' command. \n\n"
				+ "Entered users are now able to answer in the previously set quizchannel or in a private message to the bot. \n\n"
				+ "You can make the bot join you in a voice channel by using the '"+BotConstants.PREFIX+"joinme' command. If the bot "
				+ "is in a voice channel it will play 'pling' sounds whenever someone answered correctly and a 'gong' sound when the timer "
				+ "of the question has ran out. \n\n\n"
				+ "These are some more useful commands that you can use during the quiz: \n\n"
				+ "- chatlogs\n"
				+ "- select\n"
				+ "- stop\n"
				+ "- reset\n"
				+ "- scores\n"
				+ "- addpoints\n"
				+ "- joinme\n"
				+ "- leaveme \n"
				+ "- togglemode \n"
				+ "- tie \n"
				+ "- endtie \n\n"
				+ "Please read their respective help messages by typing '"+BotConstants.PREFIX+"help theCommandName'."
				+ "```**";
	}
	
	public String getQuestionsHelp(){
		return "**```"
				+ "There are 4 different question types. \n\n\n"
				+ "-Multiplechoice Question-\n\n"
				+ "A very standard multiple choice format with up to 8 answer options. "
				+ "You answer with the representing letter. Only your first answer "
				+ "will be counted. \n\n\n"
				+ "-Closestanswr Question- \n\n"
				+ "A number guessing format. You have to try to guess as close to the wanted number as possible. "
				+ "Only your first answer will be counted. The quizmaster specifies how many close "
				+ "answers will be rewarded with points. The closer you are, the more points you get. \n\n\n"
				+ "-Freeanswer Question-\n\n"
				+ "You have to answer with the correct answer and, if used, the bonus answer. You can guess "
				+ "as much as you want, wrong answers do not have any negative effect. Getting the bonusanswer "
				+ "will reward you with double the base points. "
				+ "The first person to get a correct answer will get 2 bonus points, the second person will get 1 bonus point. \n\n\n"
				+ "-Multipleanswer Question-\n\n"
				+ "Similar to the Freeanswer format. The quizmaster can set an unlimited amount of possible answers and bonus answers. "
				+ "Getting a bonusanswer will reward you with double the base points. "
				+ "You get points for every correct answer but be careful, wrong answers can make you lose points!"
				+ "```**";
	}
	
	public String getMastersHelp(){
		return "**```"
				+ "Master \n\n"
				+ "Masters are users that have power over the bot. They can use every command "
				+ "that is required to host a quiz. \n\n"
				+ "To give a user master permissions you have to use the '"+BotConstants.PREFIX+"master @user' command. \n\n"
				+ "To remove someones master permissions you have to use the '"+BotConstants.PREFIX+"nomaster @user' command. \n\n"
				+ "To show the current masters on this server you have to use the '"+BotConstants.PREFIX+"showmasters' command."
				+ "```**";
	}
	
	public String getCommandsHelp(){
		return "**```"
				+ "This is the bot's command list. \n\n"
				+ "To get more detailed information about a command please use '"+BotConstants.PREFIX+"help theCommandName'. \n\n\n"
				+ "- master           |     Gives master permissions.\n"
				+ "- nomaster         |     Removes the master permissions.\n"
				+ "- showmasters      |     Shows the masters.\n"
				+ "- import           |     Imports questions from the attached file.\n"
				+ "- next             |     Starts the next question.\n"
				+ "- select           |     Jumps to a specific question.\n"
				+ "- stop             |     Stops the current question.\n"
				+ "- scores           |     Shows the scores.\n"
				+ "- addpoints        |     Adds points to a user.\n"
				+ "- reset            |     Resets the scores.\n"
				+ "- enter            |     Makes you enter the quiz.\n"
				+ "- leave            |     Makes you leave the quiz.\n"
				+ "- setchannel       |     Sets the quizchannel.\n"
				+ "- joinme           |     Makes the bot join your voice channel.\n"
				+ "- leaveme          |     Makes the bot leave the voice channel.\n"
				+ "- quiztool         |     Sends the quiztool.\n"
				+ "- tie              |     Starts a tie between the mentioned users.\n"
				+ "- endtie           |     Ends the current tiebreaker.\n"
				+ "- togglemode       |     Switches between NORMAL and FIRST quizmode.\n"
				+ "- chatlogs         |     Sends the chatlog file.\n"
				+ "- clearchatlogs    |     Clears the chatlog file.\n"
				+ "```**";
	}
}