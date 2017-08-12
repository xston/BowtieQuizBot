package bowtie.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import bowtie.util.cons.UtilConstants;

/**
 * A logging class which prints to either a given file or to the default log file
 * which is defined by {@link UtilConstants#DEFAULT_LOG_PATH}.
 * 
 * <h1>Example of usage:</h1>
 * <ul>
 * <pre>
 * Log logger = new Log("logFile.txt", "CET");
 * logger.print("Hello world");
 * </pre>
 * </ul>
 * Output:
 * <ul>
 * <pre>
 * Mon Aug 07 07:02:41 CEST 2017 Hello world
 * </pre>
 * </ul>
 * @author &#8904
 */
public class Log {
	/** A list which contains all currently active instances of this class. */
	private static ArrayList<Log> activeLoggers = new ArrayList<Log>();
	
	/** The {@link PrintWriter} object which is used to log to the desired file. */
	private PrintWriter writer;
	
	/** The file this Log instance is writing to. */
	private File logFile;
	
	/** Indicates wether this Log instance should also send its output to {@link System#out}. 
	 * True by default. */
	private boolean logToSystemOut = true;
	
	/**
	 * Creates a {@link Log} instance which uses the default timezone of the system and prints
	 * to the default log file which is defined by {@link UtilConstants#DEFAULT_LOG_PATH}.
	 */
	public Log(){
		this(UtilConstants.DEFAULT_LOG_PATH);
	}
	
	/**
	 * Creates a {@link Log} instance which uses the default timezone of the system and prints
	 * to the file with the given path. If the file does not exist it will be created.
	 * @param logPath The path to the desired log file.
	 */
	public Log(String logPath){
		this(new File(logPath));
	}
	
	/**
	 * Creates a {@link Log} instance which uses the default timezone of the system and prints
	 * to the given file. If the file does not exist it will be created.
	 * @param logFile The file to which this instance should print.
	 */
	public Log(File logFile){
		setLogFile(logFile);
	}
	
	/**
	 * Creates a {@link Log} instance which prints to the default log file which is 
	 * defined by {@link UtilConstants#DEFAULT_LOG_PATH}. This constructor will set the given timezone 
	 * as default for the Java VM. Note that this is affecting all {@link Log} instances.
	 * @param timeZone The timezone which should be set as default for the Java VM.
	 */
	public Log(TimeZone timeZone){
		this(UtilConstants.DEFAULT_LOG_PATH, timeZone);
	}
	
	/**
	 * Creates a {@link Log} instance which prints to the file with the given path. If the file does not exist it will be created. 
	 * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all {@link Log} instances.
	 * @param logPath The path to the desired log file.
	 * @param timeZone The timezone which should be set as default for the Java VM.
	 */
	public Log(String logPath, TimeZone timeZone){
		this(new File(logPath), timeZone);
	}
	
	/**
	 * Creates a {@link Log} instance which prints to the given file. If the file does not exist it will be created. 
	 * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all {@link Log} instances.
	 * @param logFile The file to which this instance should print.
	 * @param timeZone The timezone which should be set as default for the Java VM.
	 */
	public Log(File logFile, TimeZone timeZone){
		setLogFile(logFile);
		setTimeZone(timeZone);
		activeLoggers.add(this);
	}
	
	/**
	 * Creates a {@link Log} instance which prints to the file with the given path. If the file does not exist it will be created. 
	 * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all {@link Log} instances.
	 * @param logPath The path to the desired log file.
	 * @param timeZone The id of the timezone which should be set as default for the Java VM. If the id is not recognized the timezone will
	 * be set to GMT.
	 */
	public Log(String logPath, String timeZone){
		this(new File(logPath), timeZone);
	}
	
	/**
	 * Creates a {@link Log} instance which prints to the given file. If the file does not exist it will be created. 
	 * This constructor will set the given timezone as default for the Java VM. Note that this is affecting all {@link Log} instances.
	 * @param logFile The file to which this instance should print.
	 * @param timeZone The id of the timezone which should be set as default for the Java VM. If the id is not recognized the timezone will
	 * be set to GMT.
	 */
	public Log(File logFile, String timeZone){
		setLogFile(logFile);
		setTimeZone(timeZone);
		activeLoggers.add(this);
	}
	
	/**
	 * Closes this instances {@link #writer} and removes it from the {@link #activeLoggers} list.
	 */
	public void close(){
		writer.close();
		activeLoggers.remove(this);
	}
	
	/**
	 * Closes the {@link #writer} of every active {@link Log} instance and clears the {@link #activeLoggers} list.
	 */
	public static void closeAll(){
		for(Log logger : activeLoggers){
			try{
				logger.getPrintWriter().close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		activeLoggers.clear();
	}
	
	/** Sets the {@link #logFile}. 
	 * @param path The path to the desired log file.
	 */
	public void setLogFile(String path){
		setLogFile(new File(path));
	}
	
	/** Sets the {@link #logFile}. 
	 * @param logFile The desired log file.
	 */
	public void setLogFile(File logFile){
		try{
			this.logFile = logFile;
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			writer = new PrintWriter(new BufferedWriter(new FileWriter(this.logFile, true)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current {@link #logFile}.
	 * @return The current {@link #logFile}.
	 */
	public File getLogFile(){
		return logFile;
	}
	
	/**
	 * Sets wether this instance should also print to {@link System#out}.
	 * @param b True if this insatnce should also print to {@link System#out}.
	 */
	public void logToSystemOut(boolean b){
		this.logToSystemOut = b;
	}
	
	/**
	 * Indicates wether this instance is also printing to {@link System#out}.
	 * @return True if this insatnce is also printing to {@link System#out}.
	 */
	public boolean isLogToSystemOut(){
		return this.logToSystemOut;
	}
	
	/**
	 * Sets the default timezone of the Java VM to the timezone with the given id. If the id is not
	 * recognized the timezone will be set to GMT. Note that this is affecting all {@link Log} instances.
	 * @param id The id of the desired timezone.
	 */
	public static void setTimeZone(String id){
		TimeZone.setDefault(TimeZone.getTimeZone(id));
	}
	
	/**
	 * Sets the default timezone of the Java VM to the given one. Note that this is affecting all {@link Log} instances.
	 * @param zone The desired timezone.
	 */
	public static void setTimeZone(TimeZone zone){
		TimeZone.setDefault(zone);
	}
	
	/**
	 * Gets the current default timezone of the Java VM.
	 * @return The currently set default timezone of the Java VM.
	 */
	public static TimeZone getTimeZone(){
		return TimeZone.getDefault();
	}
	
	/**
	 * Gets this instances {@link #writer}.
	 * @return This instances {@link #writer}.
	 */
	public Writer getPrintWriter(){
		return writer;
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param s
	 */
	public void print(String s){
		if(activeLoggers.contains(this)){
			if(logToSystemOut){
				System.out.println(new Date()+" "+s);
			}
			writer.println(new Date()+" "+s);
			writer.flush();
		}else{
			try{
				throw new ClosedLoggerException("Closed loggers can't print.");
			}catch(ClosedLoggerException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param b
	 */
	public void print(boolean b){
		print(Boolean.toString(b));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param i
	 */
	public void print(int i){
		print(Integer.toString(i));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param s
	 */
	public void print(short s){
		print(Short.toString(s));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param l
	 */
	public void print(long l){
		print(Long.toString(l));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param d
	 */
	public void print(double d){
		print(Double.toString(d));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param f
	 */
	public void print(float f){
		print(Float.toString(f));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param c
	 */
	public void print(char c){
		print(Character.toString(c));
	}
	
	/**
	 * Prints the given parameter with the current date to the {@link #logFile} and, if {@link #logToSystemOut} is true, to
	 * {@link System#out}.
	 * @param b
	 */
	public void print(byte b){
		print(Byte.toString(b));
	}
	
	/**
	 * Prints the String returned by the given object's {@link Object#toString()} method with the current date to the {@link #logFile} and, 
	 * if {@link #logToSystemOut} is true, to {@link System#out}.
	 * @param o
	 */
	public void print(Object o){
		print(o.toString());
	}
	
	/**
	 * Prints the message of the given throwable with the current date and the stacktrace to the {@link #logFile} and, 
	 * if {@link #logToSystemOut} is true, to {@link System#out}.
	 * @param t
	 */
	public void print(Throwable t){
		if(activeLoggers.contains(this)){
			if(logToSystemOut){
				t.printStackTrace();
			}
			writer.println(new Date()+" ERROR");
			t.printStackTrace(writer);
			writer.flush();
		}else{
			try{
				throw new ClosedLoggerException("Closed loggers can't print.");
			}catch(ClosedLoggerException e){
				e.printStackTrace();
			}
		}
	}
}