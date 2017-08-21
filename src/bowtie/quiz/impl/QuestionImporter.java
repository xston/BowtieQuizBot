package bowtie.quiz.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import bowtie.bot.impl.QuizGuild;
import bowtie.core.Main;
import bowtie.quiz.enu.QuestionType;
import bowtie.quiz.obj.Question;

/**
 * @author &#8904
 *
 */
public class QuestionImporter{
	private QuizGuild guild;
	
	public QuestionImporter(QuizGuild guild){
		this.guild = guild;
	}
	
	/**
	 * Attempts to download the file from the given url and import the contained questions.
	 * 
	 * @param url
	 * @return The amount of imported questions or -1 if the fileformat is invalid.
	 */
	public int downloadQuestionFile(String url){
		if(!url.toLowerCase().endsWith(".btq")){
			//invalid file format;
			return -1;
		}
		URL website;
		try{
			website = new URL(url);
			URLConnection conn = website.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			try (InputStream in = conn.getInputStream()){
			    Files.copy(in, new File("questions/"+guild.getStringID()+"_questions.btq").toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}catch (MalformedURLException e){
			Main.log.print(e);
		}catch (IOException e){
			Main.log.print(e);
		}
		return importQuestions();
	}
	
	/**
	 * Imports the questions contained in the default file.
	 * 
	 * @return The amount of imported questions.
	 */
	public int importQuestions(){
		int count = 0; //the number for each question
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("questions/"+guild.getStringID()+"_questions.btq")),"UTF-8"))){
			List<Question> questions = new ArrayList<Question>();
			String line = br.readLine();
		    QuestionType type = null; //the type of the question
		    List<Answer> rightAnswers = null; //correct answers
		    List<Answer> wrongAnswers = null; //wrong answers for multiplechoice
		    List<Answer> bonusAnswers = null; //bonus answers
			String questionText = null; //the actual question thats being asked
			String help = null; //a help text for the users
			String image = null; //an imagelink
			int time = 0; //the time that users have to answer
			int points = 0; //the points that users get for a correct answer
			int wrongPoints = 0; //the points that users lose for a wrong answer. only for multipleanswer questions
			int amountClosest = 0; //the amount of people that will get points during a closestanswer wuestion
			
		    while (line != null){
		        if(line.toLowerCase().startsWith("<freeanswer>")){
		        	type = QuestionType.Freeanswer;
		        }else if(line.toLowerCase().startsWith("<multiplechoice>")){
		        	type = QuestionType.Multiplechoice;
		        }else if(line.toLowerCase().startsWith("<multipleanswer>")){
		        	type = QuestionType.Multipleanswer;
		        }else if(line.toLowerCase().startsWith("<closestanswer>")){
		        	type = QuestionType.Closestanswer;
		        }else if(line.toLowerCase().startsWith("<q>")){
		        	questionText = line.replace("<q>", "").trim();
		        }else if(line.toLowerCase().startsWith("<right>")){
		        	rightAnswers = new ArrayList<Answer>();
		        	//multiple answers are divided by //
		        	String[] answers = line.replace("<right>", "").split("//");
		        	for(int i = 0; i < answers.length; i++){
		        		ArrayList<String> list = new ArrayList<String>();
		        		//multiple variations (spellings) of the same answer are divided by -o-
		        		String[] sameAnswers = answers[i].split("-o-");
		        		for(int j = 0; j < sameAnswers.length; j++){
		        			list.add(sameAnswers[j].trim());
		        		}
		        		rightAnswers.add(new Answer(list));
		        	}
		        }else if(line.toLowerCase().startsWith("<wrong>")){
		        	wrongAnswers = new ArrayList<Answer>();
		        	//multiple answers are divided by //
		        	String[] answers = line.replace("<wrong>", "").split("//");
		        	for(int i = 0; i < answers.length; i++){
		        		ArrayList<String> list = new ArrayList<String>();
		        		//multiple variations (spellings) of the same answer are divided by -o-
		        		String[] sameAnswers = answers[i].split("-o-");
		        		for(int j = 0; j < sameAnswers.length; j++){
		        			list.add(sameAnswers[j].trim());
		        		}
		        		wrongAnswers.add(new Answer(list));
		        	}
		        }else if(line.toLowerCase().startsWith("<bonus>")){
		        	bonusAnswers = new ArrayList<Answer>();
		        	//multiple answers are divided by //
		        	String[] answers = line.replace("<bonus>", "").split("//");
		        	for(int i = 0; i < answers.length; i++){
		        		ArrayList<String> list = new ArrayList<String>();
		        		//multiple variations (spellings) of the same answer are divided by -o-
		        		String[] sameAnswers = answers[i].split("-o-");
		        		for(int j = 0; j < sameAnswers.length; j++){
		        			list.add(sameAnswers[j].trim());
		        		}
		        		bonusAnswers.add(new Answer(list));
		        	}
		        }else if(line.toLowerCase().startsWith("<image>")){
		        	image = line.replace("<image>", "").trim();
		        }else if(line.toLowerCase().startsWith("<help>")){
		        	help = line.replace("<help>", "").trim();
		        }else if(line.toLowerCase().startsWith("<time>")){
		        	time = Integer.parseInt(line.replace("<time>", "").trim());
		        }else if(line.toLowerCase().startsWith("<points>")){
		        	points = Integer.parseInt(line.replace("<points>", "").trim());
		        }else if(line.toLowerCase().startsWith("<wrongpoints>")){
		        	wrongPoints = Integer.parseInt(line.replace("<wrongpoints>", "").trim());
		        }else if(line.toLowerCase().startsWith("<amountclosest>")){
		        	amountClosest = Integer.parseInt(line.toLowerCase().replace("<amountclosest>", "").trim());
		        }else if(line.toLowerCase().startsWith("<end>")){
		        	if(!type.equals(QuestionType.None) && rightAnswers != null && questionText != null){
		        		if(bonusAnswers != null){
			        		for(Answer answer : bonusAnswers){
			        			//setting the 'isBonus' value of the bonus answer in the right answer list to true
			        			if(rightAnswers.contains(answer)){
			        				rightAnswers.get(rightAnswers.indexOf(answer)).setIsBonusAnswer(true);;
			        			}
			        		}
		        		}
		        		count++;
		        		if(type == QuestionType.Closestanswer){
		        			questions.add(new ClosestAnswerQuestion(
		        					type,
		        					guild.getBot(),
		        					guild,
		        					questionText,
		        					rightAnswers,
		        					help,
		        					image,
		        					points,
		        					time,
		        					count,
		        					amountClosest));
		        		}else if(type == QuestionType.Freeanswer){
		        			questions.add(new FreeAnswerQuestion(
		        					type,
		        					guild.getBot(),
		        					guild,
		        					questionText,
		        					rightAnswers,
		        					help,
		        					image,
		        					points,
		        					time,
		        					count));
		        		}else if(type == QuestionType.Multipleanswer){
		        			questions.add(new MultipleAnswerQuestion(
		        					type,
		        					guild.getBot(),
		        					guild,
		        					questionText,
		        					rightAnswers,
		        					help,
		        					image,
		        					points,
		        					wrongPoints,
		        					time,
		        					count));
		        		}else if(type == QuestionType.Multiplechoice){
		        			questions.add(new MultipleChoiceQuestion(
		        					type,
		        					guild.getBot(),
		        					guild,
		        					questionText,
		        					rightAnswers,
		        					wrongAnswers,
		        					help,
		        					image,
		        					points,
		        					time,
		        					count));
		        		}
		        	}
		        	
		        	//resetting values for the next question
		        	type = QuestionType.None;
				    rightAnswers = null;
					wrongAnswers = null;
					questionText = null;
					help = null;
					time = 0;
					points = 0;
					wrongPoints = 0;
					amountClosest = 0;
					bonusAnswers = null;
					image = null;
		        }
		        line = br.readLine();
		    }
		    guild.getQuestionManager().setQuestions(questions);
		}catch(Exception e){
			Main.log.print(e);
		}
		return count;
	}
}