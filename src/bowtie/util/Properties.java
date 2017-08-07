package bowtie.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import bowtie.core.Main;
import bowtie.util.cons.UtilConstants;

/**
 * A utility class to get and set property values.
 * 
 * <h1>Property file format:</h1>
 * <p>
 * In order to be able to read values from property files with this class the file
 * has to be formatted correctly.
 * </p>
 * <ul>
 * <pre>
 * fieldname1: value1
 * fieldname2: value2
 * </pre>
 * </ul>
 * <h2>Example of an usage:</h2>
 * <ul>
 * <pre>
 * String value = Properties.getValueOf("fieldname1", "properties.txt");
 * 
 * Properties.setValueOf("fieldname2", "value3", "properties.txt");
 * </pre>
 * </ul>
 * @author &#8904
 */
public final class Properties {
	/**
	 * Gets the value of the given field inside the given file or null if the field does not 
	 * exist in the file.
	 * @param field The name of the field whichs value you want to get.
	 * @param file The file which should be searched for the field.
	 * @return The String value of the given field or null if the field does not exist in the given file.
	 * @see #getValueOf(String)
	 * @see #getValueOf(String, String)
	 */
	public static String getValueOf(String field, File file){
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"))){
			 String line;
			 while((line = br.readLine()) != null){
				 if(line.startsWith(field+":")){
					 return line.replace(field+":", "").trim();
				 }
			 }
		}catch(Exception e){
			Main.log.print(e);
		}
		return null;
	}
	
	/**
	 * Gets the value of the given field inside the file at the given location or null if the field does not 
	 * exist in the file.
	 * @param field The name of the field whichs value you want to get.
	 * @param path The path to a file which should be searched for the field.
	 * @return The String value of the given field or null if the field does not exist in the file at the given location.
	 * @see #getValueOf(String, File)
	 * @see #getValueOf(String)
	 */
	public static String getValueOf(String field, String path){
		return getValueOf(field, new File(path));
	}
	
	/**
	 * Gets the value of the given field inside the default property file whichs path is defined by {@link UtilConstants#DEFAULT_PROPERTY_PATH}.
	 * @param field The name of the field whichs value you want to get.
	 * @return The String value of the given field or null if the field does not exist in the default property file.
	 * @see #getValueOf(String, File)
	 * @see #getValueOf(String, String)
	 */
	public static String getValueOf(String field){
		return getValueOf(field, new File(UtilConstants.DEFAULT_PROPERTY_PATH));
	}
	
	/**
	 * Updates the value of the given field with the given value inside the given file. If the field does not exist 
	 * it will be added with the given value.
	 * @param field The name of the field whichs value should be updated or which should be added.
	 * @param value The new value for the given field.
	 * @param file The file which should be updated.
	 * @see #setValueOf(String, String)
	 * @see #setValueOf(String, String, String)
	 */
	public static void setValueOf(String field, String value, File file){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"))){
			 String line;
			 boolean exists = false;
			 while((line = br.readLine()) != null){
				 if(line.startsWith(field+":")){
					 sb.append(field+": "+value+System.lineSeparator());
					 exists = true;
				 }else{
					 sb.append(line+System.lineSeparator());
				 }
			 }
			 if(!exists){
				 sb.append(field+": "+value+System.lineSeparator());
			 }
		}catch(Exception e){
			Main.log.print(e);
		}
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
			writer.print(sb.toString());
		}catch(Exception e){
			Main.log.print(e);
		}
	}
	
	/**
	 * Updates the value of the given field with the given value inside the file at the given location. If the field does not exist 
	 * it will be added with the given value.
	 * @param field The name of the field whichs value should be updated or which should be added.
	 * @param value The new value for the given field.
	 * @param path The path to the file which should be updated.
	 * @see #setValueOf(String, String)
	 * @see #setValueOf(String, String, File)
	 */
	public static void setValueOf(String field, String value, String path){
		setValueOf(field, value, new File(path));
	}
	
	/**
	 * Updates the value of the given field with the given value inside the default property file whichs 
	 * path is defined by {@link UtilConstants#DEFAULT_PROPERTY_PATH}.
	 * @param field The name of the field whichs value should be updated or which should be added.
	 * @param value The new value for the given field.
	 * @see #setValueOf(String, String, File)
	 * @see #setValueOf(String, String, String)
	 */
	public static void setValueOf(String field, String value){
		setValueOf(field, value, new File(UtilConstants.DEFAULT_PROPERTY_PATH));
	}
}