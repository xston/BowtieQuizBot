package bowtie.quiz.hand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.impl.QuizGuild;
import bowtie.quiz.cons.QuizConstants;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.impl.Answer;
import bowtie.quiz.impl.ClosestAnswerQuestion;
import bowtie.quiz.impl.FreeAnswerQuestion;
import bowtie.quiz.impl.MultipleAnswerQuestion;
import bowtie.quiz.impl.MultipleChoiceQuestion;
import bowtie.quiz.impl.QuizUser;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class AnswerHandler {
	private QuizGuild guild;
	private ExecutorService executor;
	
	public AnswerHandler(QuizGuild guild){
		this.guild = guild;
		executor = Executors.newCachedThreadPool();
	}
	
	public QuizGuild getGuild(){
		return guild;
	}
	
	public void handle(MessageReceivedEvent event){
		executor.execute(new Runnable(){
			@Override
			public void run(){
				if(guild.isTieActive() && !guild.getTieUsers().contains(event.getMessage().getAuthor())){
					//if the user is not part of the current tiebreaker
					return;
				}
				//logs the answer
				IUser user = event.getAuthor();
				guild.getChatLog().print(user.getName()+"#"+user.getDiscriminator()+":  "+event.getMessage().getContent());
				
				Question question = guild.getQuestionManager().getCurrentQuestion();
				
				if(question.getType() == QuestionType.Multiplechoice){
					handleMultiplechoice((MultipleChoiceQuestion)question, event.getMessage());
				}else if(question.getType() == QuestionType.Multipleanswer){
					handleMultipleanswer((MultipleAnswerQuestion)question, event.getMessage());
				}else if(question.getType() == QuestionType.Closestanswer){
					handleClosestanswer((ClosestAnswerQuestion)question, event.getMessage());
				}else if(question.getType() == QuestionType.Freeanswer){
					handleFreeanswer((FreeAnswerQuestion)question, event.getMessage());
				}
			}
		});
	}
	
	private void handleMultiplechoice(MultipleChoiceQuestion question, IMessage message){
		QuizUser user = guild.getEnteredQuizUser(message.getAuthor().getStringID());
		if(user == null){
			//user has not entered the quiz beforehand
			return;
		}
		if(!question.addAnsweredUser(user)){
			//user has already given his answer and is therefore already in the list
			return;
		}
		guild.addReceivedAnswers(1);
		((QuizBot)guild.getBot()).addReceivedAnswers(1);
		if(question.isCorrect(message.getContent().trim()) == QuizConstants.CORRECT_NORMAL){
			question.addWinner(user);
			user.addCurrentQuestionScore(question.getPoints());
			user.addScore(question.getPoints());
			guild.getSoundManager().playSound(SoundManager.PING_SOUND);
			
			if(guild.getQuestionManager().getCurrentMode() == QuizConstants.FIRST_MODE){
				question.stopTimer();
				question.sendFirstAnswer(user);
			}
		}
	}
	
	private void handleFreeanswer(FreeAnswerQuestion question, IMessage message){
		QuizUser user = guild.getEnteredQuizUser(message.getAuthor().getStringID());
		if(user == null){
			//user has not entered the quiz beforehand
			return;
		}
		if(question.getAnsweredUsers().contains(user)){
			//user has already given all correct answers
			return;
		}
		guild.addReceivedAnswers(1);
		((QuizBot)guild.getBot()).addReceivedAnswers(1);
		
		if(question.isCorrect(message.getContent()) == QuizConstants.WRONG){
			return;
		}
		
		Answer answer = question.getAnswerForString(message.getContent().trim());
		if(!user.addGivenAnswer(answer)){
			//if the user has given this answer before
			return;
		}
		user.addCurrentQuestionScore(answer.isBonusAnswer() ? question.getPoints()*2 : question.getPoints());
		user.addScore(answer.isBonusAnswer() ? question.getPoints()*2 : question.getPoints());
		question.addWinner(user);
		guild.getSoundManager().playSound(SoundManager.PING_SOUND);
		
		if(guild.getQuestionManager().getCurrentMode() == QuizConstants.FIRST_MODE){
			question.stopTimer();
			question.sendFirstAnswer(user);
			return;
		}
		
		if(question.getCorrectAnswers().size() == user.getGivenAnswers().size()){
			//adds the user to the list with the people who got every corrrect answer
			question.addAnsweredUser(user);
		}
	}
	
	private void handleMultipleanswer(MultipleAnswerQuestion question, IMessage message){
		QuizUser user = guild.getEnteredQuizUser(message.getAuthor().getStringID());
		if(user == null){
			//user has not entered the quiz beforehand
			return;
		}
		if(question.getAnsweredUsers().contains(user)){
			//user has already given all correct answers
			return;
		}
		guild.addReceivedAnswers(1);
		((QuizBot)guild.getBot()).addReceivedAnswers(1);
		
		if(question.isCorrect(message.getContent()) == QuizConstants.WRONG){
			//user loses points for wrong answer
			user.addCurrentQuestionScore(question.getWrongPoints()*(-1));
			user.addScore(question.getWrongPoints()*(-1));
			return;
		}
		
		Answer answer = question.getAnswerForString(message.getContent().trim());
		if(!user.addGivenAnswer(answer)){
			//if the user has given this answer before
			return;
		}
		user.addCurrentQuestionScore(answer.isBonusAnswer() ? question.getPoints()*2 : question.getPoints());
		user.addScore(answer.isBonusAnswer() ? question.getPoints()*2 : question.getPoints());
		question.addWinner(user);
		guild.getSoundManager().playSound(SoundManager.PING_SOUND);
		
		if(guild.getQuestionManager().getCurrentMode() == QuizConstants.FIRST_MODE){
			question.stopTimer();
			question.sendFirstAnswer(user);
			return;
		}
		
		if(question.getCorrectAnswers().size() == user.getGivenAnswers().size()){
			//adds the user to the list with the people who got every corrrect answer
			question.addAnsweredUser(user);
		}
	}
	
	private void handleClosestanswer(ClosestAnswerQuestion question, IMessage message){
		QuizUser user = guild.getEnteredQuizUser(message.getAuthor().getStringID());
		if(user == null){
			//user has not entered the quiz beforehand
			return;
		}
		if(question.getAnsweredUsers().contains(user)){
			//user has already given his answer and is therefore already in the list
			return;
		}
		int answer;
		try{
			answer = Integer.parseInt(message.getContent().trim());
		}catch(NumberFormatException e){
			return;
		}
		int correct = Integer.parseInt(question.getCorrectAnswers().get(0).getVariations().get(0));
		int diff = correct - answer;
		if(diff < 0){
			diff *= -1;
		}
		user.setClosestDiff(diff);
		question.addAnsweredUser(user);
		guild.addReceivedAnswers(1);
		((QuizBot)guild.getBot()).addReceivedAnswers(1);
	}
}