package bowtie.db;

import java.io.File;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bowtie.core.Main;


/**
 * @author &#8904
 *
 */
public class Database {
	public static final String DB = "jdbc:derby:./db;create=true;useUnicode=true&characterEncoding=utf8";
	
	public Database(){
		setDerbyHome();
		createDatabase();
		createTables();
	}
	
	/**
	 * Sets the home of Derby to the folder of this jar.
	 */
	private void setDerbyHome(){
		try{
			CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
			File jarFile;
			if (codeSource.getLocation() != null){
			    jarFile = new File(codeSource.getLocation().toURI());
			}else{
			    String path = getClass().getResource(getClass().getSimpleName() + ".class").getPath();
			    String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
			    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
			    jarFile = new File(jarFilePath);
			} 
			System.setProperty("derby.system.home", jarFile.getParentFile().getAbsolutePath());
			Main.log.print("Set derby home to "+jarFile.getParentFile().getAbsolutePath());    
		}catch(Exception e){
			Main.log.print(e);
		}
	}
	
	/**
	 * Creates the database if it does not exist yet.
	 */
	private void createDatabase(){
		try(Connection connection = DriverManager.getConnection(DB)){
			Main.log.print("Loaded database.");
		}catch (SQLException e){
			Main.log.print(e);
		}
	}
	
	/**
	 * Creates the tables if they don't exist yet.
	 */
	private void createTables(){
		try(Connection connection = DriverManager.getConnection(DB)){
			try(Statement statement = connection.createStatement()){
				statement.execute(
						 "CREATE TABLE masters ("
						+ "id INTEGER NOT NULL "
						+ "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
		        		+ "(START WITH 1, INCREMENT BY 1)"
		        		+ ", "
		        		+ "guildid VARCHAR(100),"
		        		+ "masterid VARCHAR(100)"
		        		+ ")");
				Main.log.print("Created Masters table.");
			}catch (SQLException e){
			}
			
			try(Statement statement = connection.createStatement()){
				statement.execute(
						 "CREATE TABLE statistics ("
						+ "id INTEGER NOT NULL "
						+ "PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
		        		+ "(START WITH 1, INCREMENT BY 1)"
		        		+ ", "
		        		+ "guildid VARCHAR(100),"
		        		+ "questions VARCHAR(100),"
		        		+ "answers VARCHAR(100)"
		        		+ ")");
				Main.log.print("Created Statistics table.");
			}catch (SQLException e){
			}
		}catch (SQLException e){
			Main.log.print(e);
		}
	}
	
	/**
	 * 
	 * @param id The id of the guild or -1 for the bot's total count.
	 * @return The number of asked questions on the guild (or total if id == -1) or -1 if the database does not have
	 * an entry for the given id.
	 */
	public int getAskedQuestions(String id){
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement("SELECT questions FROM statistics WHERE guildid = ?")){
			statement.setString(1, id);
			ResultSet results = statement.executeQuery();
			
			if(results.next()){
				return results.getInt("questions");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * This will update the entry of the guild with the given id.
	 * @param id
	 * @param questions
	 */
	public void setAskedQuestions(String id, int questions){
		String sql = null;
		if(getAskedQuestions(id) == -1){
			sql = "INSERT INTO statistics (questions, guildid) VALUES (?, ?)";
		}else{
			sql = "UPDATE statistics SET questions = ? WHERE guildid = ?";
		}
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, questions);
			statement.setString(2, id);
			statement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param id The id of the guild or -1 for the bot's total count.
	 * @return The number of received answers on the guild (or total if id == -1) or -1 if the database does not have
	 * an entry for the given id.
	 */
	public int getReceivedAnswers(String id){
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement("SELECT answers FROM statistics WHERE guildid = ?")){
			statement.setString(1, id);
			ResultSet results = statement.executeQuery();
			
			if(results.next()){
				return results.getInt("answers");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * This will update the entry of the guild with the given id.
	 * @param id
	 * @param answers
	 */
	public void setReceivedAnswers(String id, int answers){
		String sql = null;
		if(getAskedQuestions(id) == -1){
			sql = "INSERT INTO statistics (questions, guildid) VALUES (?, ?)";
		}else{
			sql = "UPDATE statistics SET questions = ? WHERE guildid = ?";
		}
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, answers);
			statement.setString(2, id);
			statement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of master id's for the given guild id.
	 * @param guildID
	 * @return
	 */
	public List<String> getMasterID(String guildID){
		List<String> masters = new ArrayList<String>();
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement("SELECT masterid FROM masters WHERE guildid = ?")){
			statement.setString(1, guildID);
			ResultSet results = statement.executeQuery();
			
			while(results.next()){
				masters.add(results.getString("masterid"));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return masters;
	}
	
	/**
	 * Adds a master for the given guild to the database.
	 * <p>
	 * <b>Note</b> that this does not check if the given master does already have an entry.
	 * </p>
	 * @param guildID
	 * @param masterID
	 */
	public void addMaster(String guildID, String masterID){
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement("INSERT INTO masters (guildid, masterid) VALUES (?, ?)")){
			statement.setString(1, guildID);
			statement.setString(2, masterID);
			statement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Remves all entries for the master on the given guild.
	 * @param guildID
	 * @param masterID
	 */
	public void removeMaster(String guildID, String masterID){
		try(Connection connection = DriverManager.getConnection(DB);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM masters WHERE guildid = ? AND masterid = ?")){
			statement.setString(1, guildID);
			statement.setString(2, masterID);
			statement.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
}