package bowtie.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author &#8904
 *
 */
public class Log {
	private static PrintWriter chatWriter;
	private static PrintWriter systemWriter;
	
	public static void init(){
		try {
			File file = new File("chatLogs.txt");
			file.mkdir();
			chatWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File file = new File("systemLogs.txt");
			file.mkdir();
			systemWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void kill(){
		chatWriter.close();
		systemWriter.close();
	}
	
	public static void clearFile(String file){
		try{
			FileWriter fwOb = new FileWriter(file, false); 
	        PrintWriter pwOb = new PrintWriter(fwOb, false);
	        pwOb.flush();
	        pwOb.close();
	        fwOb.close();
		}catch(Exception e){
			
		}
	}
	
	public static void printChat(String text){
		chatWriter.println(text);
		chatWriter.flush();
	}
	
	public static void printSystem(String text){
		System.out.println(text);
		systemWriter.println(text);
		systemWriter.flush();
	}
	
	public static void printChat(boolean b){
		chatWriter.println(b);
		chatWriter.flush();
	}
	
	public static void printSystem(boolean b){
		System.out.println(b);
		systemWriter.println(b);
		systemWriter.flush();
	}
	
	public static void printChat(int i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(int i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(short i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(short i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(long i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(long i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(double i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(double i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(float i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(float i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(char i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(char i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(byte i){
		chatWriter.println(i);
		chatWriter.flush();
	}
	
	public static void printSystem(byte i){
		System.out.println(i);
		systemWriter.println(i);
		systemWriter.flush();
	}
	
	public static void printChat(Throwable e){
		e.printStackTrace();
		chatWriter.println(new Date()+" "+e.getMessage());
		e.printStackTrace(systemWriter);
		chatWriter.flush();
	}
	
	public static void printSystem(Throwable e){
		e.printStackTrace();
		systemWriter.println(new Date()+" "+e.getMessage());
		e.printStackTrace(systemWriter);
		systemWriter.flush();
	}
	
	public static void printChat(Object o){
		chatWriter.println(o.toString());
		chatWriter.flush();
	}
	
	public static void printSystem(Object o){
		System.out.println(o.toString());
		systemWriter.println(o.toString());
		systemWriter.flush();
	}
}